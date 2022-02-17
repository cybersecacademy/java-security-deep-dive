package edu.otib.lab_sqli.entity;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticleRowMapper implements RowMapper<Article> {
    @Override
    public Article mapRow(ResultSet row, int rowNum) throws SQLException {
        Article article = new Article();
        article.setId(row.getInt("id"));
        article.setTitle(row.getString("title"));
        article.setText(row.getString("text"));
        return article;
    }
}
