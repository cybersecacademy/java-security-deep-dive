package edu.otib.lab_xss.service;

import edu.otib.lab_xss.entity.Post;

import java.util.List;

public interface IPostService {
    List<Post> getAllPosts();
    void insertPost(Post post);
}
