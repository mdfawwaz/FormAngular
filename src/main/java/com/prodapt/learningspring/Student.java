package com.prodapt.learningspring;

public class Student {
    private int rank;
    private String name;
    private int marks;

    public Student() {
    }

    public Student(int rank, String name, int marks) {
        this.rank = rank;
        this.name = name;
        this.marks = marks;
    }

    // Getters and setters

   

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }
}

