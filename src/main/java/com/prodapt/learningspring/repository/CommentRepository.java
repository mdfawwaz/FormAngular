
package com.prodapt.learningspring.repository;

 

 

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;

 

import com.prodapt.learningspring.entity.Comment;

 

import java.util.List;

 

public interface CommentRepository extends CrudRepository<Comment, Integer> {

    @Query(value = "select * from comment c where post_id = ?1", nativeQuery= true)

    List<Comment> findAllByPostId(Integer postId);

}