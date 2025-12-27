package com.library.management.infrastructure.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ConnectionManager - Thread-safe singleton for database connection management
 * Uses HikariCP for connection pooling
 * 
 * @author Library Management System
 * @version 2.0
 */
public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    private static volatile ConnectionManager instance;
    private final HikariDataSource dataSource;

    private ConnectionManager() {
        HikariConfig config = new HikariConfig();
        Properties props = loadProperties();
        
        config.setJdbcUrl(props.getProperty("db.url", "jdbc:mysql://localhost:3306/library_db"));
        config.setUsername(props.getProperty("db.username", "root"));
        config.setPassword(props.getProperty("db.password", ""));
        config.setDriverClassName(props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver"));
        
        // Connection pool settings
        config.setPoolName("LibraryConnectionPool");
        config.setMaximumPoolSize(Integer.parseInt(props.getProperty("hikari.maximum.pool.size", "10")));
        config.setMinimumIdle(Integer.parseInt(props.getProperty("hikari.minimum.idle", "5")));
        config.setConnectionTimeout(Long.parseLong(props.getProperty("hikari.connection.timeout", "30000")));
        config.setIdleTimeout(Long.parseLong(props.getProperty("hikari.idle.timeout", "600000")));
        config.setMaxLifetime(Long.parseLong(props.getProperty("hikari.max.lifetime", "1800000")));
        
        // Performance optimization
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");
        
        this.dataSource = new HikariDataSource(config);
        logger.info("ConnectionManager initialized with pool size: {}", config.getMaximumPoolSize());
    }

    public static ConnectionManager getInstance() {
        if (instance == null) {
            synchronized (ConnectionManager.class) {
                if (instance == null) {
                    instance = new ConnectionManager();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        try {
            Connection conn = dataSource.getConnection();
            logger.debug("Connection obtained. Active: {}, Idle: {}", 
                dataSource.getHikariPoolMXBean().getActiveConnections(),
                dataSource.getHikariPoolMXBean().getIdleConnections());
            return conn;
        } catch (SQLException e) {
            logger.error("Failed to obtain connection", e);
            throw e;
        }
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("ConnectionManager closed");
        }
    }

    public boolean isHealthy() {
        try (Connection conn = getConnection()) {
            return conn.isValid(5);
        } catch (SQLException e) {
            logger.error("Health check failed", e);
            return false;
        }
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                props.load(input);
                logger.info("Properties loaded successfully");
            } else {
                logger.warn("application.properties not found, using defaults");
            }
        } catch (IOException e) {
            logger.error("Error loading properties", e);
        }
        return props;
    }

    // Metrics
    public int getActiveConnections() {
        return dataSource.getHikariPoolMXBean().getActiveConnections();
    }

    public int getIdleConnections() {
        return dataSource.getHikariPoolMXBean().getIdleConnections();
    }

    public int getTotalConnections() {
        return dataSource.getHikariPoolMXBean().getTotalConnections();
    }
}
