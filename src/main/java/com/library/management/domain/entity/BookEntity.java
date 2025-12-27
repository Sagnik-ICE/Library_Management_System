package com.library.management.domain.entity;

import com.library.management.domain.vo.ISBN;
import com.library.management.domain.vo.Money;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Book Entity - Domain model representing a book in the library
 * 
 * @author Library Management System
 * @version 2.0
 */
public class BookEntity {
    private Long id;
    private String uniqueBookId;
    private ISBN isbn;
    private String title;
    private String author;
    private String genre;
    private String publisher;
    private Money price;
    private LocalDate dateReceived;
    private Integer quantity;
    private Integer availableQuantity;
    private String description;
    private String coverImagePath;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String createdBy;
    private String updatedBy;

    // Private constructor for builder
    private BookEntity() {}

    // Business methods
    public boolean isAvailable() {
        return availableQuantity != null && availableQuantity > 0;
    }

    public void reduceAvailableQuantity(int count) {
        if (availableQuantity < count) {
            throw new IllegalStateException("Not enough copies available");
        }
        this.availableQuantity -= count;
    }

    public void increaseAvailableQuantity(int count) {
        this.availableQuantity += count;
    }

    public boolean hasISBN() {
        return isbn != null;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUniqueBookId() {
        return uniqueBookId;
    }

    public ISBN getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public Money getPrice() {
        return price;
    }

    public LocalDate getDateReceived() {
        return dateReceived;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUniqueBookId(String uniqueBookId) {
        this.uniqueBookId = uniqueBookId;
    }

    public void setIsbn(ISBN isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public void setDateReceived(LocalDate dateReceived) {
        this.dateReceived = dateReceived;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity that = (BookEntity) o;
        return Objects.equals(id, that.id) && 
               Objects.equals(uniqueBookId, that.uniqueBookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uniqueBookId);
    }

    @Override
    public String toString() {
        return "BookEntity{" +
                "id=" + id +
                ", uniqueBookId='" + uniqueBookId + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", availableQuantity=" + availableQuantity +
                '}';
    }

    // Builder pattern
    public static class Builder {
        private final BookEntity book = new BookEntity();

        public Builder id(Long id) {
            book.id = id;
            return this;
        }

        public Builder uniqueBookId(String uniqueBookId) {
            book.uniqueBookId = uniqueBookId;
            return this;
        }

        public Builder isbn(ISBN isbn) {
            book.isbn = isbn;
            return this;
        }

        public Builder title(String title) {
            book.title = title;
            return this;
        }

        public Builder author(String author) {
            book.author = author;
            return this;
        }

        public Builder genre(String genre) {
            book.genre = genre;
            return this;
        }

        public Builder publisher(String publisher) {
            book.publisher = publisher;
            return this;
        }

        public Builder price(Money price) {
            book.price = price;
            return this;
        }

        public Builder dateReceived(LocalDate dateReceived) {
            book.dateReceived = dateReceived;
            return this;
        }

        public Builder quantity(Integer quantity) {
            book.quantity = quantity;
            book.availableQuantity = quantity; // Initially all are available
            return this;
        }

        public Builder availableQuantity(Integer availableQuantity) {
            book.availableQuantity = availableQuantity;
            return this;
        }

        public Builder description(String description) {
            book.description = description;
            return this;
        }

        public Builder coverImagePath(String coverImagePath) {
            book.coverImagePath = coverImagePath;
            return this;
        }

        public Builder createdAt(LocalDate createdAt) {
            book.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDate updatedAt) {
            book.updatedAt = updatedAt;
            return this;
        }

        public Builder createdBy(String createdBy) {
            book.createdBy = createdBy;
            return this;
        }

        public Builder updatedBy(String updatedBy) {
            book.updatedBy = updatedBy;
            return this;
        }

        public BookEntity build() {
            // Validate required fields
            if (book.uniqueBookId == null || book.uniqueBookId.trim().isEmpty()) {
                throw new IllegalStateException("Unique Book ID is required");
            }
            if (book.title == null || book.title.trim().isEmpty()) {
                throw new IllegalStateException("Title is required");
            }
            if (book.author == null || book.author.trim().isEmpty()) {
                throw new IllegalStateException("Author is required");
            }
            return book;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
