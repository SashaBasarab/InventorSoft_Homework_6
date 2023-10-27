package org.example;

import org.example.dao.BookDao;
import org.example.dao.BookDaoImpl;
import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.entity.Book;
import org.example.entity.User;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class App
{

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main( String[] args )
    {
        String createBooksTable = "create table books(\n" +
                "                       id long auto_increment primary key,\n" +
                "                       name varchar(255),\n" +
                "                       author varchar(255)\n" +
                ");";
        String createUsersTable = "create table users(\n" +
                "                       id long auto_increment primary key,\n" +
                "                       username varchar(255),\n" +
                "                       email varchar(255),\n" +
                "                       password varchar(255)\n" +
                ");";
        String jdbcUrl = "jdbc:h2:mem:default";
        try (Connection connection = DriverManager.getConnection(jdbcUrl)){
            Statement statement = connection.createStatement();
            statement.execute(createUsersTable);
            statement.execute(createBooksTable);

            System.out.println("Connected successfully!");

            JdbcDataSource h2DataSource = new JdbcDataSource();
            h2DataSource.setUrl(jdbcUrl);
            UserDao userDao = new UserDaoImpl(h2DataSource);

            User user = new User("testName", "testEmail", "testPassword");
            userDao.save(user);
            System.out.println(userDao.getAll());
            User user1 = new User(1L,"testName2", "testEmail2", "testPassword2");
            userDao.update(user1);
            System.out.println(userDao.findById(1L));
            userDao.delete(user1);
            System.out.println(userDao.getAll());

            BookDao bookDao = new BookDaoImpl(h2DataSource);
            Book book = new Book("Harry Potter", "Rowling");
            bookDao.save(book);
            System.out.println(bookDao.getAll());
            Book book1 = new Book(1L, "Harry Portter 2", "Rowling");
            bookDao.update(book1);
            System.out.println(bookDao.findById(1L));
            bookDao.delete(book1);
            System.out.println(bookDao.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
