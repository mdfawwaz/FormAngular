//package com.prodapt.learningspring.model;
//
//
//import java.sql.SQLException; 
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.stereotype.Component;
//
//import com.prodapt.learningspring.model.Students;
//
//@Component
//public class Classroom {
//  private List<Student> students;
//  private static int idCounter = 1;
//  DBConnect db = new DBConnect();
//
//  public Classroom() {
//    students = new ArrayList<>();
//  }
//  
//  public List<Student> getStudents() {
//	  students= db.loadData();
//	  rank();
//    return Collections.unmodifiableList(students);
//  }
//
//  private void rank() {
//    Collections.sort(students, (s1, s2) -> -Integer.compare(s1.getScore(), s2.getScore()));
//    for (int i = 0; i < students.size(); i++)
//      students.get(i).setRank(i + 1);
//    for (int i = 1; i < students.size(); i++) {
//      if (students.get(i).getScore() == students.get(i-1).getScore())
//        students.get(i).setRank(students.get(i-1).getRank());
//    }
//  }
//  
//  public int updateID() {
//		int maxId = 0;
//    
//    for (Student st : students) {
//        int studentId = st.getId();
//        if (studentId > maxId) {
//            maxId = studentId;
//        }
//    }
//    System.out.println(maxId+1);
//    return maxId+1;
//}
//
//
//  public void add(Student student) {
//    student.setId(updateID());
//    students.add(student);
//    rank();
//    try {
//		db.insertData("INSERT INTO classroom (id, name, score) VALUES "
//		        + "('"+student.getId()+"', '"+student.getName()+"', "+student.getScore()+")");
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//  }
//  
//  public void remove(int idx) {
//	  int id = students.get(idx).getId();
//	  System.out.println(id);
//    students.remove(idx);
//    rank();
//    try {
//        String deleteQuery = "DELETE FROM classroom WHERE id = " + (id);
//
//        db.deleteData(deleteQuery);
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//  }
//  
//  public void replace(int id, Student current) {
//	  try {
//		db.updateData(id, current.getName(), current.getScore());
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//    for (int i = 0; i < students.size(); i++) {
//      if (students.get(i).getId() == id) {
//        students.get(i).setName(current.getName());
//        students.get(i).setScore(current.getScore());
//      }
//    }
//    rank();
//  }
//  
//  public Optional<Student> getById(int id) {
//    for (int i = 0; i < students.size(); i++) {
//      if (students.get(i).getId() == id)
//        return Optional.of(students.get(i));
//    }
//    return Optional.empty();
//  }
//
//
//}
