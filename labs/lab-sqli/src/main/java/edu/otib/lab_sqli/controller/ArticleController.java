package edu.otib.lab_sqli.controller;

import edu.otib.lab_sqli.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ArticleController {
    @Autowired
    private IArticleService articleService;

    @GetMapping("/")
    public String articles_list(
            Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return "articles_list";
    }

    @GetMapping("/{id}")
    public String article_view(
            @PathVariable String id,
            Model model
    ) {
        model.addAttribute("article", articleService.getArticleById(id));
        return "article_view";
    }
}
