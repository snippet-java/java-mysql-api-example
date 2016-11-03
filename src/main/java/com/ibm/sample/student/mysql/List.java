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

@WebServlet("/mysql/list")
public class List extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	JsonObject output = new JsonObject();
    	Connection conn = new MySQLConnectionService().getConnection();
    	try {
    		
	    	String query = "SELECT * FROM student";
	    	QueryRunner queryRunner = new QueryRunner();
	    	java.util.List<Map<String, Object>>listOfMaps = queryRunner.query(conn, query, new MapListHandler());
	    	String result = new Gson().toJson(listOfMaps);
	    	JsonArray resultSet = new JsonParser().parse(result).getAsJsonArray();
	    	output.add("data", resultSet);

			conn.close();
    		
    	} catch(SQLException ex) {
    		output.addProperty("err", ex.getMessage());
    	}
    	
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(output);
    }

}
