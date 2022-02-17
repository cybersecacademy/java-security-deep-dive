package edu.otib.lab_xss.service;

import edu.otib.lab_xss.dao.IPostDAO;
import edu.otib.lab_xss.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService implements IPostService {

    @Autowired
    private IPostDAO postDAO;

    @Override
    public List<Post> getAllPosts() {
        return postDAO.getAllPosts();
    }

    @Override
    public void insertPost(Post post) {
        postDAO.insertPost(post);
    }
}
