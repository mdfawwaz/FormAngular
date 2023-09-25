package com.prodapt.learningspring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prodapt.learningspring.entity.FavPost;
import com.prodapt.learningspring.entity.Post;
import com.prodapt.learningspring.entity.User;
import com.prodapt.learningspring.repository.FavPostRepository;

import jakarta.annotation.PostConstruct;

@Service
public class FavPostMuteService {

	private final FavPostRepository favPostRepository;

 
	
	public List<FavPost> favpostList;


	@PostConstruct
	public void init() {
		
		favpostList = new ArrayList<>();
	}

	@Autowired
	public FavPostMuteService(FavPostRepository favPostRepository) {
		this.favPostRepository = favPostRepository;
	}

	public List<FavPost> findAllFavPostsByUser(User user) {
		return favPostRepository.findAllByUser(user);
	}

	public boolean isFavPost(User user, Post post) {
		return favPostRepository.existsByUserAndPost(user, post);
	}

//	public Favpost addFavPost(User user, Post post) {
//		if (!isFavPost(user, post)) {
//			Favpost favpost = new Favpost(user, post);
//			return favPostRepository.save(favpost);
//		}
//		return null; // Return null to indicate that the post is already a favorite.
//	}

	public void removeFavPost(User user, Post post) {
		FavPost favpost = favPostRepository.findByUserAndPost(user, post);
		if (favpost != null) {
			favPostRepository.delete(favpost);
		}
	}
}