package com.library.management.domain.entity;

import com.library.management.domain.enums.TransactionStatus;
import com.library.management.domain.vo.Money;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Transaction Entity - Represents a book issue/return transaction
 * 
 * @author Library Management System
 * @version 2.0
 */
public class TransactionEntity {
    private Long id;
    private Long bookId;
    private Long memberId;
    private LocalDate issueDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private TransactionStatus status;
    private Money fineAmount;
    private boolean finePaid;
    private String notes;
    private String issuedBy;
    private String returnedBy;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    private TransactionEntity() {}

    // Business methods
    public boolean isOverdue() {
        if (actualReturnDate != null) {
            return actualReturnDate.isAfter(expectedReturnDate);
        }
        return LocalDate.now().isAfter(expectedReturnDate);
    }

    public long getDaysOverdue() {
        if (!isOverdue()) return 0;
        
        LocalDate referenceDate = actualReturnDate != null ? actualReturnDate : LocalDate.now();
        return ChronoUnit.DAYS.between(expectedReturnDate, referenceDate);
    }

    public Money calculateFine(Money finePerDay) {
        long daysOverdue = getDaysOverdue();
        if (daysOverdue <= 0) {
            return Money.ZERO;
        }
        return finePerDay.multiply(daysOverdue);
    }

    public boolean isReturned() {
        return status == TransactionStatus.RETURNED;
    }

    public boolean isIssued() {
        return status == TransactionStatus.ISSUED;
    }

    public void markAsReturned(String returnedBy) {
        this.actualReturnDate = LocalDate.now();
        this.status = TransactionStatus.RETURNED;
        this.returnedBy = returnedBy;
        this.updatedAt = LocalDate.now();
    }

    public void payFine() {
        this.finePaid = true;
        this.updatedAt = LocalDate.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public Money getFineAmount() {
        return fineAmount;
    }

    public boolean isFinePaid() {
        return finePaid;
    }

    public String getNotes() {
        return notes;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public String getReturnedBy() {
        return returnedBy;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public void setFineAmount(Money fineAmount) {
        this.fineAmount = fineAmount;
    }

    public void setFinePaid(boolean finePaid) {
        this.finePaid = finePaid;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public void setReturnedBy(String returnedBy) {
        this.returnedBy = returnedBy;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionEntity that = (TransactionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TransactionEntity{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", memberId=" + memberId +
                ", status=" + status +
                ", issueDate=" + issueDate +
                '}';
    }

    // Builder
    public static class Builder {
        private final TransactionEntity transaction = new TransactionEntity();

        public Builder id(Long id) {
            transaction.id = id;
            return this;
        }

        public Builder bookId(Long bookId) {
            transaction.bookId = bookId;
            return this;
        }

        public Builder memberId(Long memberId) {
            transaction.memberId = memberId;
            return this;
        }

        public Builder issueDate(LocalDate issueDate) {
            transaction.issueDate = issueDate;
            return this;
        }

        public Builder expectedReturnDate(LocalDate expectedReturnDate) {
            transaction.expectedReturnDate = expectedReturnDate;
            return this;
        }

        public Builder actualReturnDate(LocalDate actualReturnDate) {
            transaction.actualReturnDate = actualReturnDate;
            return this;
        }

        public Builder status(TransactionStatus status) {
            transaction.status = status;
            return this;
        }

        public Builder fineAmount(Money fineAmount) {
            transaction.fineAmount = fineAmount;
            return this;
        }

        public Builder finePaid(boolean finePaid) {
            transaction.finePaid = finePaid;
            return this;
        }

        public Builder notes(String notes) {
            transaction.notes = notes;
            return this;
        }

        public Builder issuedBy(String issuedBy) {
            transaction.issuedBy = issuedBy;
            return this;
        }

        public Builder returnedBy(String returnedBy) {
            transaction.returnedBy = returnedBy;
            return this;
        }

        public Builder createdAt(LocalDate createdAt) {
            transaction.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDate updatedAt) {
            transaction.updatedAt = updatedAt;
            return this;
        }

        public TransactionEntity build() {
            if (transaction.bookId == null) {
                throw new IllegalStateException("Book ID is required");
            }
            if (transaction.memberId == null) {
                throw new IllegalStateException("Member ID is required");
            }
            if (transaction.issueDate == null) {
                transaction.issueDate = LocalDate.now();
            }
            if (transaction.status == null) {
                transaction.status = TransactionStatus.ISSUED;
            }
            if (transaction.fineAmount == null) {
                transaction.fineAmount = Money.ZERO;
            }
            return transaction;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
