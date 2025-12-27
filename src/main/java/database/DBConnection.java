package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConfigManager;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Database Connection Manager using HikariCP
 * Provides connection pooling for improved performance and resource management
 * Singleton pattern ensures single datasource instance
 */
public class DBConnection {
    private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);
    private static volatile HikariDataSource dataSource;
    private static final Object lock = new Object();

    private DBConnection() {
        // Private constructor to prevent instantiation
    }

    /**
     * Initializes HikariCP DataSource with configuration
     */
    private static void initializeDataSource() {
        if (dataSource == null) {
            synchronized (lock) {
                if (dataSource == null) {
                    try {
                        ConfigManager config = ConfigManager.getInstance();
                        
                        HikariConfig hikariConfig = new HikariConfig();
                        hikariConfig.setJdbcUrl(config.getDatabaseUrl());
                        hikariConfig.setUsername(config.getDatabaseUsername());
                        hikariConfig.setPassword(config.getDatabasePassword());
                        hikariConfig.setDriverClassName(config.getDatabaseDriver());
                        
                        // Connection pool settings
                        hikariConfig.setPoolName(config.getProperty("hikari.pool.name", "LibraryHikariPool"));
                        hikariConfig.setMaximumPoolSize(config.getHikariMaxPoolSize());
                        hikariConfig.setMinimumIdle(config.getHikariMinIdle());
                        hikariConfig.setConnectionTimeout(config.getHikariConnectionTimeout());
                        hikariConfig.setIdleTimeout(config.getIntProperty("hikari.idle.timeout", 600000));
                        hikariConfig.setMaxLifetime(config.getIntProperty("hikari.max.lifetime", 1800000));
                        
                        // Performance optimization
                        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
                        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
                        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
                        hikariConfig.addDataSourceProperty("useLocalSessionState", "true");
                        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", "true");
                        hikariConfig.addDataSourceProperty("cacheResultSetMetadata", "true");
                        hikariConfig.addDataSourceProperty("cacheServerConfiguration", "true");
                        hikariConfig.addDataSourceProperty("elideSetAutoCommits", "true");
                        hikariConfig.addDataSourceProperty("maintainTimeStats", "false");
                        
                        dataSource = new HikariDataSource(hikariConfig);
                        logger.info("HikariCP DataSource initialized successfully");
                        logger.info("Max Pool Size: {}, Min Idle: {}", 
                            hikariConfig.getMaximumPoolSize(), 
                            hikariConfig.getMinimumIdle());
                    } catch (Exception e) {
                        logger.error("Failed to initialize HikariCP DataSource", e);
                        throw new RuntimeException("Database initialization failed", e);
                    }
                }
            }
        }
    }

    /**
     * Gets a connection from the pool
     * 
     * @return Connection from the pool
     * @throws SQLException if connection cannot be obtained
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            initializeDataSource();
        }
        
        try {
            Connection connection = dataSource.getConnection();
            logger.debug("Connection obtained from pool. Active connections: {}", 
                dataSource.getHikariPoolMXBean().getActiveConnections());
            return connection;
        } catch (SQLException e) {
            logger.error("Failed to get connection from pool", e);
            throw e;
        }
    }

    /**
     * Closes the connection (returns it to the pool)
     * 
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close(); // Returns connection to pool
                logger.debug("Connection returned to pool");
            } catch (SQLException e) {
                logger.error("Error closing connection", e);
            }
        }
    }

    /**
     * Shuts down the datasource and closes all connections
     * Should be called when application is shutting down
     */
    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("HikariCP DataSource shut down successfully");
        }
    }

    /**
     * Gets pool statistics for monitoring
     * 
     * @return Pool statistics string
     */
    public static String getPoolStats() {
        if (dataSource != null) {
            return String.format("Active: %d, Idle: %d, Total: %d, Threads Awaiting: %d",
                dataSource.getHikariPoolMXBean().getActiveConnections(),
                dataSource.getHikariPoolMXBean().getIdleConnections(),
                dataSource.getHikariPoolMXBean().getTotalConnections(),
                dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
        return "DataSource not initialized";
    }

    /**
     * Tests database connectivity
     * 
     * @return true if connection is successful
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            boolean isValid = conn.isValid(5);
            logger.info("Database connection test: {}", isValid ? "SUCCESS" : "FAILED");
            return isValid;
        } catch (SQLException e) {
            logger.error("Database connection test failed", e);
            return false;
        }
    }
}