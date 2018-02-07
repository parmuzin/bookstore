package com.bookstore.service;

import com.bookstore.model.Book;

import java.util.List;

public interface BookService {

    List<Book> findAllBooks();

    Book findBookById(Long bookId);

    Book updateBook(Book book);

    void deleteBook(Book book);

    Book saveBook(Book book);
}
