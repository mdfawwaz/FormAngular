package com.prodapt.learningspring.model;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Component
@Data
public class Student {
  private int id;
  private String name;
  @Min(value = 0) private int score;
  private int rank;  
  
  @Override
  public String toString() {
	  return "id-" + this.id + ", name- " + this.name + ", score- " + this.score;
	 
  }
}
