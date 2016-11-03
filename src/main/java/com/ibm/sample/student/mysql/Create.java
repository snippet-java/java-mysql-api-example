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

@WebServlet("/mysql/create")
public class Create extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	JsonObject output = new JsonObject();
    	Connection conn = new MySQLConnectionService().getConnection();
    	try {
    		JsonObject studentJson = new JsonObject();
    		studentJson.addProperty("firstname", "joe");
    		studentJson.addProperty("lastname", "doe");
    		studentJson.addProperty("student_id", generateStudentId());
    		
	    	String query = "INSERT INTO student (firstname, lastname, student_id)"
	    			+ " values (?, ?, ?)";
	    	PreparedStatement stat = conn.prepareStatement(query);
	    	stat.setString(1, studentJson.get("firstname").getAsString());
	    	stat.setString(2, studentJson.get("lastname").getAsString());
	    	stat.setString(3, studentJson.get("student_id").getAsString());
	    	
    		stat.execute();
    		
			output.add("doc", studentJson);
			output.addProperty("status", "success created");

			stat.close();
			conn.close();
			
    	} catch(SQLException ex) {
    		output.addProperty("err", ex.getMessage());
    	}
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(output);
    }
    
    private String generateStudentId() {
    	return "ID#" + new Double(Math.floor(Math.random()*10000)).intValue();
    }

    private static final long serialVersionUID = 1L;
}
