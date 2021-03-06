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

@WebServlet("/mysql/update")
public class Update extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	JsonObject output = new JsonObject();
    	
    	String id = request.getParameter("id");
    	String firstname = request.getParameter("firstname");
    	String lastname = request.getParameter("lastname");
    	
    	if(id == null || id.isEmpty()) {
    		output.addProperty("err", "Please specify valid ID");
    	}
    	else if(firstname == null || firstname.isEmpty()) {
    		output.addProperty("err", "Please specify valid firstname");
    	}
    	else if(lastname == null || lastname.isEmpty()) {
    		output.addProperty("err", "Please specify valid lastname");
    	}
    	else {
	    	Connection conn = new MySQLConnectionService().getConnection();
	    	try {
	    		
		    	String query = "UPDATE student SET firstname = ?, lastname = ? WHERE id = ?";
		    	PreparedStatement stat = conn.prepareStatement(query);
		    	stat.setString(1, firstname);
		    	stat.setString(2, lastname);
		    	stat.setInt(3, Integer.parseInt(id));
		    	
	    		int statusCode = stat.executeUpdate();
	    		
	    		if(statusCode == 0)
	    			output.addProperty("status", "No data found. Update operation cannot be done");
	    		else
	    			output.addProperty("status", "success updated");
				
				stat.close();
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
