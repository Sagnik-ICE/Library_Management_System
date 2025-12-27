package com.library.management.repository.interfaces;

import com.library.management.domain.entity.BookEntity;

import java.util.List;
import java.util.Optional;

/**
 * BookRepository - Data access interface for Book operations
 * Following Repository pattern for loose coupling
 * 
 * @author Library Management System
 * @version 2.0
 */
public interface BookRepository {
    
    /**
     * Save a new book or update existing book
     * @param book Book entity to save
     * @return Saved book entity with generated ID
     */
    BookEntity save(BookEntity book);
    
    /**
     * Find book by database ID
     * @param id Database ID
     * @return Optional containing book if found
     */
    Optional<BookEntity> findById(Long id);
    
    /**
     * Find book by unique book ID
     * @param uniqueBookId Unique identifier
     * @return Optional containing book if found
     */
    Optional<BookEntity> findByUniqueBookId(String uniqueBookId);
    
    /**
     * Find all books
     * @return List of all books
     */
    List<BookEntity> findAll();
    
    /**
     * Find books by title (partial match)
     * @param title Book title to search
     * @return List of matching books
     */
    List<BookEntity> findByTitle(String title);
    
    /**
     * Find books by author (partial match)
     * @param author Author name to search
     * @return List of matching books
     */
    List<BookEntity> findByAuthor(String author);
    
    /**
     * Find books by genre
     * @param genre Genre to filter
     * @return List of books in genre
     */
    List<BookEntity> findByGenre(String genre);
    
    /**
     * Find all available books (quantity > 0)
     * @return List of available books
     */
    List<BookEntity> findAvailableBooks();
    
    /**
     * Update book details
     * @param book Book entity with updated fields
     * @return Updated book entity
     */
    BookEntity update(BookEntity book);
    
    /**
     * Delete book by ID
     * @param id Database ID
     * @return true if deleted successfully
     */
    boolean deleteById(Long id);
    
    /**
     * Check if book with unique ID exists
     * @param uniqueBookId Unique book identifier
     * @return true if exists
     */
    boolean existsByUniqueBookId(String uniqueBookId);
    
    /**
     * Update book quantity
     * @param bookId Database ID
     * @param quantityChange Amount to add/subtract
     * @return true if updated successfully
     */
    boolean updateQuantity(Long bookId, int quantityChange);
    
    /**
     * Get total count of books
     * @return Total number of books
     */
    long count();
    
    /**
     * Get count of available books
     * @return Number of available books
     */
    long countAvailable();
}
