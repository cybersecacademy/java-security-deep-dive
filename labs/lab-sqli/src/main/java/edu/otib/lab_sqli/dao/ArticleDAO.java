package edu.otib.lab_sqli.dao;

import edu.otib.lab_sqli.entity.Article;
import edu.otib.lab_sqli.entity.ArticleRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class ArticleDAO implements IArticleDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    final static String SQL_GET_ALL_ARTICLES = "SELECT id, title, text FROM article";
    final static String SQL_GET_ARTICLE_BY_ID = "SELECT id, title, text FROM article WHERE id = ";

    @Override
    public List<Article> getAllAtricles() {
        RowMapper<Article> rowMapper = new ArticleRowMapper();
        return jdbcTemplate.query(SQL_GET_ALL_ARTICLES, rowMapper);
    }

    @Override
    public Article getArticleById(String id) {
        RowMapper<Article> rowMapper = new ArticleRowMapper();
        Article article = jdbcTemplate.queryForObject(SQL_GET_ARTICLE_BY_ID + id, rowMapper);
        return article;
    }
}
