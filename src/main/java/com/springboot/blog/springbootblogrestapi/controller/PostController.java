package com.springboot.blog.springbootblogrestapi.controller;

import com.springboot.blog.springbootblogrestapi.payload.PostDto;
import com.springboot.blog.springbootblogrestapi.payload.PostResponse;
import com.springboot.blog.springbootblogrestapi.service.PostService;
import com.springboot.blog.springbootblogrestapi.utils.AppContants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //create blog post

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    //get all posts
    @GetMapping("/api/posts/v1")
    public PostResponse getAllPosts(@RequestParam(value = "pageNo", defaultValue = AppContants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = AppContants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = AppContants.DEFAULT_SORT_BY, required = false) String sortBy,
                                    @RequestParam(value = "sortDir", defaultValue = AppContants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    //get a particular post
    @GetMapping("/api/posts/v1/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    //update a particular post
    @PutMapping("/api/posts/v1/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable("id") Long id) {
        PostDto postResponse = postService.updatePost(postDto, id);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    //deleting a post
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/posts/v1/{id}")
    public ResponseEntity<String> deletePost(@Valid @PathVariable("id") Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(String.format("The post with id %s is deleted", id), HttpStatus.OK);
    }
}
