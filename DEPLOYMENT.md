# 🚀 Deployment Guide

## Production Deployment Checklist

### Pre-Deployment

- [ ] Update `application.properties` with production database credentials
- [ ] Change default admin password
- [ ] Review and adjust connection pool settings
- [ ] Configure logging levels (INFO for production)
- [ ] Test database connectivity
- [ ] Backup existing database
- [ ] Review security settings

### Database Setup

```sql
-- 1. Create production database
CREATE DATABASE library_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. Create database user with limited privileges
CREATE USER 'library_user'@'localhost' IDENTIFIED BY 'STRONG_PASSWORD_HERE';
GRANT SELECT, INSERT, UPDATE, DELETE ON library_db.* TO 'library_user'@'localhost';
FLUSH PRIVILEGES;

-- 3. Import schema
USE library_db;
SOURCE library_db.sql;

-- 4. Create admin user with BCrypt hashed password
-- Note: Password will be hashed on first login migration
INSERT INTO users (username, password, role, status) 
VALUES ('admin', 'CHANGE_THIS_PASSWORD', 'admin', 'active');
```

### Application Configuration

#### 1. Update application.properties

```properties
# Production Database
db.url=jdbc:mysql://localhost:3306/library_db?useSSL=true&requireSSL=true
db.username=library_user
db.password=STRONG_PASSWORD_HERE

# Connection Pool - Production Settings
hikari.maximum.pool.size=20
hikari.minimum.idle=10

# Logging - Production
logging.level=INFO
```

#### 2. Secure Sensitive Files

```bash
# Set proper permissions
chmod 600 src/application.properties
chmod 700 logs/
```

### Build for Production

```bash
# Clean and build
mvn clean package -DskipTests

# Verify JAR creation
ls -lh target/library-management-system-2.0.0.jar

# Test the JAR
java -jar target/library-management-system-2.0.0.jar
```

### Deployment Options

#### Option 1: Standalone JAR

```bash
# Copy to deployment directory
cp target/library-management-system-2.0.0.jar /opt/library-management/
cp src/application.properties /opt/library-management/config/

# Create startup script
cat > /opt/library-management/start.sh << 'EOF'
#!/bin/bash
cd /opt/library-management
java -Xms512m -Xmx1024m \
     -Dconfig.location=config/application.properties \
     -jar library-management-system-2.0.0.jar
EOF

chmod +x /opt/library-management/start.sh

# Run application
./start.sh
```

#### Option 2: Windows Service

Create `library-management.bat`:

```batch
@echo off
cd C:\LibraryManagement
start javaw -Xms512m -Xmx1024m ^
     -jar library-management-system-2.0.0.jar
```

Create Windows scheduled task to run on startup.

#### Option 3: Linux Systemd Service

Create `/etc/systemd/system/library-management.service`:

```ini
[Unit]
Description=Library Management System
After=network.target mysql.service

[Service]
Type=simple
User=library
WorkingDirectory=/opt/library-management
ExecStart=/usr/bin/java -Xms512m -Xmx1024m -jar library-management-system-2.0.0.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Enable and start:

```bash
sudo systemctl enable library-management
sudo systemctl start library-management
sudo systemctl status library-management
```

### Monitoring

#### Check Logs

```bash
# Real-time log monitoring
tail -f logs/library-management.log

# Search for errors
grep ERROR logs/library-management.log

# Check connection pool stats
grep "HikariCP" logs/library-management.log
```

#### Database Monitoring

```sql
-- Check active connections
SHOW PROCESSLIST;

-- Check table sizes
SELECT 
    table_name AS 'Table',
    ROUND(((data_length + index_length) / 1024 / 1024), 2) AS 'Size (MB)'
FROM information_schema.TABLES
WHERE table_schema = 'library_db'
ORDER BY (data_length + index_length) DESC;

-- Check slow queries
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 2;
```

### Backup Strategy

#### Database Backup

```bash
# Daily backup script
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
mysqldump -u root -p library_db > backup_$DATE.sql
gzip backup_$DATE.sql

# Keep only last 30 days
find /backups -name "backup_*.sql.gz" -mtime +30 -delete
```

#### Application Backup

```bash
# Backup images and configurations
tar -czf library_backup_$(date +%Y%m%d).tar.gz \
    Images/ \
    "book images/" \
    src/application.properties \
    logs/
```

### Performance Tuning

#### MySQL Optimization

```sql
-- Add indexes for frequent queries
CREATE INDEX idx_book_title ON books(title);
CREATE INDEX idx_member_email ON members(email);
CREATE INDEX idx_issued_books_dates ON issued_books(issue_date, return_date);

-- Analyze tables
ANALYZE TABLE books, members, issued_books, returned_books, fines, users;
```

#### JVM Tuning

```bash
# For production with 4GB RAM
java -Xms1024m -Xmx2048m \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar library-management-system-2.0.0.jar
```

### Security Hardening

1. **Database**
   - Use SSL/TLS for database connections
   - Limit database user privileges
   - Regular password rotation

2. **Application**
   - Store `application.properties` outside JAR
   - Encrypt sensitive configuration
   - Regular security updates

3. **System**
   - Firewall rules (MySQL port 3306)
   - Regular OS security patches
   - User access controls

### Troubleshooting

#### Issue: Cannot connect to database

```bash
# Check MySQL is running
sudo systemctl status mysql

# Test connection
mysql -h localhost -u library_user -p library_db

# Check firewall
sudo ufw status
```

#### Issue: Out of memory

```bash
# Increase JVM heap size
java -Xms1024m -Xmx2048m -jar library-management-system-2.0.0.jar

# Monitor memory
jstat -gc <PID> 1000
```

#### Issue: Too many database connections

```bash
# Check HikariCP settings in application.properties
hikari.maximum.pool.size=20

# Check MySQL max connections
SHOW VARIABLES LIKE 'max_connections';
```

### Health Checks

Create a health check script:

```bash
#!/bin/bash
# health_check.sh

# Check if process is running
pgrep -f "library-management-system" > /dev/null
if [ $? -ne 0 ]; then
    echo "Application is not running!"
    exit 1
fi

# Check database connectivity
mysql -u library_user -p'PASSWORD' -e "USE library_db;" 2>/dev/null
if [ $? -ne 0 ]; then
    echo "Database connection failed!"
    exit 1
fi

echo "All systems operational"
exit 0
```

### Rollback Plan

```bash
# 1. Stop application
systemctl stop library-management

# 2. Restore database backup
mysql -u root -p library_db < backup_YYYYMMDD.sql

# 3. Restore previous JAR version
cp library-management-system-1.0.0.jar.backup \
   library-management-system-2.0.0.jar

# 4. Restart application
systemctl start library-management
```

### Post-Deployment

- [ ] Verify login functionality
- [ ] Test critical features (book issue/return)
- [ ] Check log files for errors
- [ ] Monitor database connections
- [ ] Verify backup schedule
- [ ] Update documentation
- [ ] Train users on new features

---

## Support

For deployment issues:
- Check logs in `logs/library-management.log`
- Review this deployment guide
- Contact: support@example.com
