package com.springboot.blog.springbootblogrestapi.controller;

import com.springboot.blog.springbootblogrestapi.payload.CommentDto;
import com.springboot.blog.springbootblogrestapi.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/api/v1/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable("postId") long postId,
                                                    @Valid @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.createComment(postId,commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/posts/{postId}/comments")
    public List<CommentDto> getCommentByPostId(@PathVariable("postId") Long postId){
        return commentService.getCommentByPostId(postId);
    }

    //get comment by id

    @GetMapping("/api/v1/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("postId") long postId,
                                                     @PathVariable("commentId") long commentId) {
     CommentDto comment = commentService.getCommentById(postId,commentId);
     return new ResponseEntity<>(comment, HttpStatus.OK);

    }

    @PutMapping("/api/v1/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("postId") Long postId,
                                                    @PathVariable("commentId") Long commentId,
                                                    @Valid @RequestBody CommentDto commentDto){
        CommentDto updatedComment = commentService.updateComment(postId,commentId,commentDto);
        return new ResponseEntity<>(updatedComment,HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("postId") Long postId,
                                                @PathVariable("commentId") Long commentId){
        commentService.deleteComment(postId,commentId);
        return new ResponseEntity<>(String.format("The comment with id %s is deleted", commentId),HttpStatus.OK);
    }
}
