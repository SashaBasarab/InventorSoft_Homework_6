package org.example.dao;

import org.example.entity.User;
import org.example.exception.DaoOperationException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{
    private DataSource dataSource;
    private static final String insertQuery = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
    private static final String selectByIdQuery = "SELECT * FROM users WHERE id = ?";
    private static final String selectAllQuery = "SELECT * FROM users";
    private static final String updateQuery = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
    private static final String deleteQuery = "DELETE FROM users WHERE id = ?";

    public UserDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(User user) {
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage(), e);
        }
    }

    @Override
    public User findById(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(selectByIdQuery);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String username = resultSet.getString("username");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            return new User(id, username, email, password);
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot find User by id = " + id, e);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectAllQuery);
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                users.add(new User(id, username, email, password));
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot find any user", e);
        }
        return users;
    }

    @Override
    public void update(User user) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setLong(4, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot update user", e);
        }
    }

    @Override
    public void delete(User user) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot delete user", e);
        }
    }
}
