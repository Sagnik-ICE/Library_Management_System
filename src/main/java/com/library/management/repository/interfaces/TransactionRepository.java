package com.library.management.repository.interfaces;

import com.library.management.domain.entity.TransactionEntity;
import com.library.management.domain.enums.TransactionStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * TransactionRepository - Data access interface for Transaction operations
 * 
 * @author Library Management System
 * @version 2.0
 */
public interface TransactionRepository {
    
    /**
     * Save a new transaction
     * @param transaction Transaction entity to save
     * @return Saved transaction entity with generated ID
     */
    TransactionEntity save(TransactionEntity transaction);
    
    /**
     * Find transaction by ID
     * @param id Database ID
     * @return Optional containing transaction if found
     */
    Optional<TransactionEntity> findById(Long id);
    
    /**
     * Find all transactions
     * @return List of all transactions
     */
    List<TransactionEntity> findAll();
    
    /**
     * Find transactions by book ID
     * @param bookId Book database ID
     * @return List of transactions for the book
     */
    List<TransactionEntity> findByBookId(Long bookId);
    
    /**
     * Find transactions by member ID
     * @param memberId Member database ID
     * @return List of transactions for the member
     */
    List<TransactionEntity> findByMemberId(Long memberId);
    
    /**
     * Find transactions by status
     * @param status Transaction status
     * @return List of transactions with status
     */
    List<TransactionEntity> findByStatus(TransactionStatus status);
    
    /**
     * Find currently issued transactions
     * @return List of issued transactions
     */
    List<TransactionEntity> findIssuedTransactions();
    
    /**
     * Find overdue transactions
     * @return List of overdue transactions
     */
    List<TransactionEntity> findOverdueTransactions();
    
    /**
     * Find transactions issued between dates
     * @param startDate Start date
     * @param endDate End date
     * @return List of transactions in date range
     */
    List<TransactionEntity> findByIssueDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find active transaction for book and member
     * @param bookId Book database ID
     * @param memberId Member database ID
     * @return Optional containing active transaction if found
     */
    Optional<TransactionEntity> findActiveTransaction(Long bookId, Long memberId);
    
    /**
     * Update transaction
     * @param transaction Transaction entity with updated fields
     * @return Updated transaction entity
     */
    TransactionEntity update(TransactionEntity transaction);
    
    /**
     * Check if book is currently issued to member
     * @param bookId Book database ID
     * @param memberId Member database ID
     * @return true if book is issued to member
     */
    boolean isBookIssuedToMember(Long bookId, Long memberId);
    
    /**
     * Get count of issued books for member
     * @param memberId Member database ID
     * @return Number of currently issued books
     */
    int countIssuedBooksForMember(Long memberId);
    
    /**
     * Get total fine amount for member
     * @param memberId Member database ID
     * @return Total unpaid fines
     */
    double getTotalFineForMember(Long memberId);
    
    /**
     * Delete transaction by ID
     * @param id Database ID
     * @return true if deleted successfully
     */
    boolean deleteById(Long id);
    
    /**
     * Get total count of transactions
     * @return Total number of transactions
     */
    long count();
    
    /**
     * Get count of issued transactions
     * @return Number of currently issued books
     */
    long countIssued();
    
    /**
     * Get count of overdue transactions
     * @return Number of overdue books
     */
    long countOverdue();
}
