package edu.otib.lab_xss.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostRowMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet row, int rowNum) throws SQLException {
        Post post = new Post();
        post.setId(row.getInt("id"));
        post.setAuthor(row.getString("author"));
        post.setText(row.getString("text"));
        post.setDateCreated(row.getDate("created_at"));
        return post;
    }
}
