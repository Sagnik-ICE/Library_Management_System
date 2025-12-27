package com.library.management.infrastructure.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * TransactionManager - Manages database transactions
 * Provides transaction boundary management
 * 
 * @author Library Management System
 * @version 2.0
 */
public class TransactionManager {
    private static final Logger logger = LoggerFactory.getLogger(TransactionManager.class);
    private final ConnectionManager connectionManager;

    public TransactionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Execute operation within a transaction
     */
    public <T> T executeInTransaction(TransactionalOperation<T> operation) throws SQLException {
        Connection conn = null;
        boolean originalAutoCommit = true;
        
        try {
            conn = connectionManager.getConnection();
            originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            
            logger.debug("Transaction started");
            
            T result = operation.execute(conn);
            
            conn.commit();
            logger.debug("Transaction committed successfully");
            
            return result;
            
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    logger.warn("Transaction rolled back due to: {}", e.getMessage());
                } catch (SQLException rollbackEx) {
                    logger.error("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new SQLException("Transaction failed", e);
            
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(originalAutoCommit);
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Error closing connection", e);
                }
            }
        }
    }

    /**
     * Functional interface for transactional operations
     */
    @FunctionalInterface
    public interface TransactionalOperation<T> {
        T execute(Connection connection) throws SQLException;
    }
}
