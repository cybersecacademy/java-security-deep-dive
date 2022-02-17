package edu.otib.lab_xss.controller;

import edu.otib.lab_xss.entity.Post;
import edu.otib.lab_xss.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostController {
    @Autowired
    private IPostService postService;

    @GetMapping("/")
    public String postsList(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "posts_list";
    }

    @PostMapping("/")
    public String addPost(@RequestParam String author, @RequestParam String text, Model model) {
        Post post = new Post();
        post.setAuthor(author);
        post.setText(text);
        postService.insertPost(post);
        return "redirect:";
    }
}
