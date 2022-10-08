package com.springboot.blog.springbootblogrestapi.repository;

import com.springboot.blog.springbootblogrestapi.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

//no need to provide repository annonation since simpleJPArepo already does that, same with transactional

public interface PostRepository extends JpaRepository<Post, Long> {
}
