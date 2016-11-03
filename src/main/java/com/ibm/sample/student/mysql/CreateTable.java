package com.ibm.sample.student.mysql;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

@WebServlet("/mysql/createtable")
public class CreateTable extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	JsonObject output = new JsonObject();
    	String tablename = "student";
    	Connection conn = new MySQLConnectionService().getConnection();
    	try {
	    	String query = "CREATE TABLE " + tablename + "("
					+ "id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY, "
					+ "firstname VARCHAR(30) NOT NULL, "
					+ "lastname VARCHAR(30) NOT NULL, "
					+ "student_id VARCHAR(50))" ;
	    	PreparedStatement stat = conn.prepareStatement(query);
    		stat.execute();
    		output.addProperty("result", "Success created table" + tablename);

    		stat.close();
    		conn.close();
    	} catch(SQLException ex) {
    		output.addProperty("err", ex.getMessage());
    	}
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(output);
    }

}
