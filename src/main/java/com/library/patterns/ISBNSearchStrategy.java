package com.library.patterns;

import com.library.models.Book;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Concrete strategy for searching books by ISBN.
 */
public class ISBNSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String searchTerm) {
        return books.stream()
                .filter(book -> book.getIsbn().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }
}
