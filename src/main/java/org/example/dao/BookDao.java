package org.example.dao;

import org.example.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao{
    void save(Book book);

    Book findById(Long id);

    List<Book> getAll();

    void update(Book book);

    void delete(Book book);
}
