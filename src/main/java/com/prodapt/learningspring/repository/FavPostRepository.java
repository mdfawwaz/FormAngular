package com.prodapt.learningspring.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.prodapt.learningspring.entity.FavPost;
import com.prodapt.learningspring.entity.Post;
import com.prodapt.learningspring.entity.User;

public interface FavPostRepository extends CrudRepository<FavPost, Integer> {

    List<FavPost> findAllByUser(User user);
    boolean existsByUserAndPost(User user, Post post);
    
    FavPost findByUserAndPost(User user, Post post);
    
    void deleteByUserAndPost(User user, Post post);
    
}