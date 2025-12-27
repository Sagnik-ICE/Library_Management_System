package com.library.management.repository.impl;

import com.library.management.domain.entity.BookEntity;
import com.library.management.domain.vo.ISBN;
import com.library.management.domain.vo.Money;
import com.library.management.infrastructure.database.ConnectionManager;
import com.library.management.repository.interfaces.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * BookRepositoryImpl - MySQL implementation of BookRepository
 * Uses prepared statements for security and performance
 * 
 * @author Library Management System
 * @version 2.0
 */
public class BookRepositoryImpl implements BookRepository {
    private static final Logger logger = LoggerFactory.getLogger(BookRepositoryImpl.class);
    
    private final ConnectionManager connectionManager;

    public BookRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public BookEntity save(BookEntity book) {
        String sql = "INSERT INTO books (unique_book_id, isbn, title, author, genre, publisher, price, " +
                    "date_received, quantity, available_quantity, description, cover_image_path, " +
                    "created_at, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            setBookParameters(pstmt, book);
            pstmt.setDate(13, Date.valueOf(LocalDate.now()));
            pstmt.setString(14, book.getCreatedBy());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getLong(1));
                    logger.info("Book saved successfully with ID: {}", book.getId());
                    return book;
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error saving book: {}", book, e);
            throw new RuntimeException("Failed to save book", e);
        }
    }

    @Override
    public Optional<BookEntity> findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding book by ID: {}", id, e);
        }
        
        return Optional.empty();
    }

    @Override
    public Optional<BookEntity> findByUniqueBookId(String uniqueBookId) {
        String sql = "SELECT * FROM books WHERE unique_book_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, uniqueBookId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding book by unique ID: {}", uniqueBookId, e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<BookEntity> findAll() {
        String sql = "SELECT * FROM books ORDER BY title";
        List<BookEntity> books = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                books.add(mapResultSetToEntity(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding all books", e);
        }
        
        return books;
    }

    @Override
    public List<BookEntity> findByTitle(String title) {
        String sql = "SELECT * FROM books WHERE title LIKE ? ORDER BY title";
        List<BookEntity> books = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + title + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding books by title: {}", title, e);
        }
        
        return books;
    }

    @Override
    public List<BookEntity> findByAuthor(String author) {
        String sql = "SELECT * FROM books WHERE author LIKE ? ORDER BY title";
        List<BookEntity> books = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + author + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding books by author: {}", author, e);
        }
        
        return books;
    }

    @Override
    public List<BookEntity> findByGenre(String genre) {
        String sql = "SELECT * FROM books WHERE genre = ? ORDER BY title";
        List<BookEntity> books = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, genre);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding books by genre: {}", genre, e);
        }
        
        return books;
    }

    @Override
    public List<BookEntity> findAvailableBooks() {
        String sql = "SELECT * FROM books WHERE available_quantity > 0 ORDER BY title";
        List<BookEntity> books = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                books.add(mapResultSetToEntity(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding available books", e);
        }
        
        return books;
    }

    @Override
    public BookEntity update(BookEntity book) {
        String sql = "UPDATE books SET unique_book_id = ?, isbn = ?, title = ?, author = ?, genre = ?, " +
                    "publisher = ?, price = ?, date_received = ?, quantity = ?, available_quantity = ?, " +
                    "description = ?, cover_image_path = ?, updated_at = ?, updated_by = ? WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setBookParameters(pstmt, book);
            pstmt.setDate(13, Date.valueOf(LocalDate.now()));
            pstmt.setString(14, book.getUpdatedBy());
            pstmt.setLong(15, book.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating book failed, no rows affected.");
            }
            
            logger.info("Book updated successfully: {}", book.getId());
            return book;
            
        } catch (SQLException e) {
            logger.error("Error updating book: {}", book, e);
            throw new RuntimeException("Failed to update book", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("Book deleted successfully: {}", id);
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Error deleting book: {}", id, e);
        }
        
        return false;
    }

    @Override
    public boolean existsByUniqueBookId(String uniqueBookId) {
        String sql = "SELECT COUNT(*) FROM books WHERE unique_book_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, uniqueBookId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error checking book existence: {}", uniqueBookId, e);
        }
        
        return false;
    }

    @Override
    public boolean updateQuantity(Long bookId, int quantityChange) {
        String sql = "UPDATE books SET available_quantity = available_quantity + ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, quantityChange);
            pstmt.setDate(2, Date.valueOf(LocalDate.now()));
            pstmt.setLong(3, bookId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("Book quantity updated: {} by {}", bookId, quantityChange);
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Error updating book quantity: {}", bookId, e);
        }
        
        return false;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM books";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error counting books", e);
        }
        
        return 0;
    }

    @Override
    public long countAvailable() {
        String sql = "SELECT COUNT(*) FROM books WHERE available_quantity > 0";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error counting available books", e);
        }
        
        return 0;
    }

    // Helper methods
    private void setBookParameters(PreparedStatement pstmt, BookEntity book) throws SQLException {
        pstmt.setString(1, book.getUniqueBookId());
        pstmt.setString(2, book.getIsbn() != null ? book.getIsbn().getValue() : null);
        pstmt.setString(3, book.getTitle());
        pstmt.setString(4, book.getAuthor());
        pstmt.setString(5, book.getGenre());
        pstmt.setString(6, book.getPublisher());
        pstmt.setDouble(7, book.getPrice() != null ? book.getPrice().toDouble() : 0.0);
        pstmt.setDate(8, book.getDateReceived() != null ? Date.valueOf(book.getDateReceived()) : null);
        pstmt.setInt(9, book.getQuantity() != null ? book.getQuantity() : 0);
        pstmt.setInt(10, book.getAvailableQuantity() != null ? book.getAvailableQuantity() : 0);
        pstmt.setString(11, book.getDescription());
        pstmt.setString(12, book.getCoverImagePath());
    }

    private BookEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        String isbnValue = rs.getString("isbn");
        double priceValue = rs.getDouble("price");
        Date dateReceived = rs.getDate("date_received");
        Date createdAt = rs.getDate("created_at");
        Date updatedAt = rs.getDate("updated_at");
        
        return BookEntity.builder()
                .id(rs.getLong("id"))
                .uniqueBookId(rs.getString("unique_book_id"))
                .isbn(isbnValue != null ? ISBN.ofUnchecked(isbnValue) : null)
                .title(rs.getString("title"))
                .author(rs.getString("author"))
                .genre(rs.getString("genre"))
                .publisher(rs.getString("publisher"))
                .price(Money.of(priceValue))
                .dateReceived(dateReceived != null ? dateReceived.toLocalDate() : null)
                .quantity(rs.getInt("quantity"))
                .availableQuantity(rs.getInt("available_quantity"))
                .description(rs.getString("description"))
                .coverImagePath(rs.getString("cover_image_path"))
                .createdAt(createdAt != null ? createdAt.toLocalDate() : null)
                .updatedAt(updatedAt != null ? updatedAt.toLocalDate() : null)
                .createdBy(rs.getString("created_by"))
                .updatedBy(rs.getString("updated_by"))
                .build();
    }
}
