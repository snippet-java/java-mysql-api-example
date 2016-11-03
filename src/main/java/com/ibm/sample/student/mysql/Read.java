package com.ibm.sample.student.mysql;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/mysql/read")
public class Read extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	JsonObject output = new JsonObject();
		String id = request.getParameter("id");

		if(id == null || id.isEmpty()) {
			output.addProperty("err", "Please specify valid Doc ID");
		}
		else {
	    	Connection conn = new MySQLConnectionService().getConnection();
	    	try {
	    		
		    	String query = "SELECT * FROM student WHERE id = " + id;
		    	QueryRunner queryRunner = new QueryRunner();
		    	java.util.List<Map<String, Object>>listOfMaps = queryRunner.query(conn, query, new MapListHandler());
		    	String result = new Gson().toJson(listOfMaps);
		    	JsonArray resultSet = new JsonParser().parse(result).getAsJsonArray();
		    	output.add("data", resultSet);

				conn.close();
		    	
	    	} catch(SQLException ex) {
	    		output.addProperty("err", ex.getMessage());
	    	}
		}
    	
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(output);
    }

}
