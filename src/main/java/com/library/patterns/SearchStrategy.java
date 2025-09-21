package com.library.patterns;

import com.library.models.Book;
import java.util.List;

/**
 * Strategy pattern for book search functionality.
 * Allows different search strategies to be used interchangeably.
 */
public interface SearchStrategy {
    List<Book> search(List<Book> books, String searchTerm);
}
