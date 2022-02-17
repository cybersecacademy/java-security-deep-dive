package edu.otib.lab_sqli.dao;

import edu.otib.lab_sqli.entity.Article;

import java.util.List;

public interface IArticleDAO {
    List<Article> getAllAtricles();
    Article getArticleById(String id);
}
