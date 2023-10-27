package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Book {
    private Long id;
    private String name;
    private String author;

    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }
}
