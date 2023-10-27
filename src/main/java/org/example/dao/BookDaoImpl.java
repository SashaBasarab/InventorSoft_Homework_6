package org.example.dao;

import org.example.entity.Book;
import org.example.exception.DaoOperationException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao{
    private DataSource dataSource;
    private static final String insertQuery = "INSERT INTO books (name, author) VALUES (?, ?)";
    private static final String selectByIdQuery = "SELECT * FROM books WHERE id = ?";
    private static final String selectAllQuery = "SELECT * FROM books";
    private static final String updateQuery = "UPDATE books SET name = ?, author = ? WHERE id = ?";
    private static final String deleteQuery = "DELETE FROM books WHERE id = ?";

    public BookDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Book book) {
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage(), e);
        }
    }

    @Override
    public Book findById(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(selectByIdQuery);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String name = resultSet.getString("name");
            String author = resultSet.getString("author");
            return new Book(id, name, author);
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot find book by id = " + id, e);
        }
    }

    @Override
    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectAllQuery);
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                books.add(new Book(id, name, author));
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot find any book", e);
        }
        return books;
    }

    @Override
    public void update(Book book) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setLong(3, book.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot update book", e);
        }
    }

    @Override
    public void delete(Book book) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setLong(1, book.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot delete book", e);
        }
    }
}
