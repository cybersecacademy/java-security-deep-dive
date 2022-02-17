package edu.otib.lab_xss.dao;

import edu.otib.lab_xss.entity.Post;
import edu.otib.lab_xss.entity.PostRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class PostDAO implements IPostDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    final private String SQL_GET_ALL_POSTS = "SELECT * FROM post ORDER BY created_at DESC";
    final private String SQL_NEW_POST = "INSERT INTO post (author, text, created_at) VALUES (?, ?, NOW())";

    @Override
    public List<Post> getAllPosts() {
        RowMapper<Post> rowMapper = new PostRowMapper();
        return jdbcTemplate.query(SQL_GET_ALL_POSTS, rowMapper);
    }

    @Override
    public void insertPost(Post post) {
        jdbcTemplate.update(SQL_NEW_POST, new Object[] { post.getAuthor(), post.getText() });
    }
}
