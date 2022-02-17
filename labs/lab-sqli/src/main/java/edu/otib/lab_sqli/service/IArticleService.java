package edu.otib.lab_sqli.service;

import edu.otib.lab_sqli.entity.Article;

import java.util.List;

public interface IArticleService {
    List<Article> getAllArticles();
    Article getArticleById(String id);
}
