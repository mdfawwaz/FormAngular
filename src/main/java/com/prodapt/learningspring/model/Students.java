package com.prodapt.learningspring.model;

public class Students implements Comparable<Students>{
	int score;
	String name;
	int rank;
	int id;
	
	public Students(String name, int score) {
		this.score = score;
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public int getRank() {
		return this.rank;
	}
	
	public int getId() {
		return this.id;
	}

	public void updateData(String name, int score) {
		this.score = score;
		this.name = name;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "id- "+this.id +"rank- " + this.rank + ",name-"+ this.name + ", score - " + this.score;
	}
	
	@Override
	public int compareTo(Students other) {
        if(this.score < other.score)
            return 1;
        if(this.score > other.score)
            return -1;
        return this.name.compareTo(other.name);
    }

}

