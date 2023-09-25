
package com.prodapt.learningspring.repository;

 

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.prodapt.learningspring.entity.Post;
import com.prodapt.learningspring.entity.User;

 

public interface PostRepository extends CrudRepository<Post, Integer>{
    List<Post> findAllByAuthorId(Integer authorId);
 

}