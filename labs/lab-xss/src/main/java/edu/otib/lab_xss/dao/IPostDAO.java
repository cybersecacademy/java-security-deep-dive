package edu.otib.lab_xss.dao;

import edu.otib.lab_xss.entity.Post;

import java.util.List;

public interface IPostDAO {
    List<Post> getAllPosts();
    void insertPost(Post post);
}
