//package com.prodapt.learningspring.model;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//public class StudentDBConnection {
//	
//	private PreparedStatement insert = null;
//	private Connection cnx = null;
//	private ResultSet rs;
//	
//	public StudentDBConnection() {
//		try {
//			this.cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/ClassroomDB", "bibhu04", "bibhu04");
//			System.out.println("connected");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void insertData(String updateQuery) throws SQLException {
//		PreparedStatement show = cnx.prepareStatement(updateQuery);
//		show.executeUpdate();
//		System.out.println("data added");
//	}
//	
//	public void deleteData(String deleteQuery) throws SQLException {
//	    PreparedStatement statement = cnx.prepareStatement(deleteQuery);
//	    int rowsAffected = statement.executeUpdate();
//	    statement.close();
//	}
//	
//	public void updateData(int id, String newName, int newScore) throws SQLException {
//	    String updateQuery = "UPDATE classroom SET name = ?, score = ? WHERE id = ?";
//	    
//	    try (PreparedStatement statement = cnx.prepareStatement(updateQuery)) {
//	        statement.setString(1, newName);
//	        statement.setInt(2, newScore);
//	        statement.setInt(3, id);
//	        
//	        statement.executeUpdate();
//	    }
//	}
//
//	
//	public ArrayList<Student> loadData(){
//		ArrayList<Student> students = new ArrayList<>();
//		
//		try {
//	        String selectQuery = "SELECT * FROM classroom";
//	        PreparedStatement statement = cnx.prepareStatement(selectQuery);
//	        ResultSet rs = statement.executeQuery();
//	        
//	        while (rs.next()) {
//	            int id = rs.getInt("id");
//	            String name = rs.getString("name");
//	            int score = rs.getInt("score");
//	            
//	            Student student = new Student();
//	            student.setId(id);
//	            student.setName(name);
//	            student.setScore(score);
//	            students.add(student);
//	        }
//	        
//	        rs.close();
//	        statement.close();
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//		
//		return students;
//	}
//
//}
