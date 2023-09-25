package com.prodapt.learningspring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class FavoritePosts {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "user_id",referencedColumnName = "id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "post_id",referencedColumnName = "id")
	private Post post;
}
