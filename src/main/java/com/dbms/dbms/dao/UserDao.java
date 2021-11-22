package com.dbms.dbms.dao;

import com.dbms.dbms.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createUser(User user) {
        String sql = "INSERT INTO users (email,password,role) VALUES (?,?,?)";

        try {
            int res = jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getRole());
            logger.debug("Created user " + user.getEmail() + ". Result: " + res);
            assert (res == 1);
            return res;
        } catch (DuplicateKeyException e) {
            return 0;
        }
    }

    public List<User> selectAllUsers() {
        String sql = "" +
                "SELECT *" +
                "FROM users";

        return jdbcTemplate.query(sql, mapUserFomDb());
    }

    public int setUserInit(String email) {
        String sql = "update users set init=true where email=?";
        int rows = jdbcTemplate.update(sql, email);
        logger.info("Setting " + email + " as init: Rows affected: " + rows);
        return rows;
    }

    public User findUserByEmail(String email) {
        String sql = "" +
                "SELECT *" +
                "FROM users WHERE email=?";

        return jdbcTemplate.queryForObject(sql, mapUserFomDb(), email);
    }

    public User findUserById(int id) {
        String sql = "" +
                "SELECT *" +
                "FROM users WHERE id=?";

        return jdbcTemplate.queryForObject(sql, mapUserFomDb(), id);
    }

    public int setUserEnabled(int id, Boolean value) {
        String sql = "update users set enabled=? where id=?";
        return jdbcTemplate.update(sql, value, id);
    }

    public void deleteUser(int id) {
        String sql = "delete from users where id=?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<User> mapUserFomDb() {
        return (resultSet, i) -> {
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            int id = resultSet.getInt("id");
            String role = resultSet.getString("role").toUpperCase();
            boolean enabled = resultSet.getBoolean("enabled");
            boolean init = resultSet.getBoolean("init");

            User user = new User();

            user.setEmail(email);
            user.setPassword(password);
            user.setId(id);
            user.setRole(role);
            user.setInit(init);
            user.setEnabled(enabled);

            return user;
        };
    }
}
