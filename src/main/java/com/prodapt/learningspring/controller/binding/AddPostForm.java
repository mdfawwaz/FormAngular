package com.prodapt.learningspring.controller.binding;

 

import lombok.Data;

 

@Data

public class AddPostForm {

  public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

private String content;

  private int userId;



}




















