# Database Schema Documentation

## Database Name
`library_db`

## Tables Overview

### 1. books
Stores the library's book inventory.

| Column | Type | Constraint | Description |
|--------|------|-----------|-------------|
| id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique book identifier |
| title | VARCHAR(100) | | Book title |
| author | VARCHAR(100) | | Author name |
| quantity | INT | | Available quantity |

**Indexes**: PRIMARY KEY on id

---

### 2. members
Stores information about library members.

| Column | Type | Constraint | Description |
|--------|------|-----------|-------------|
| id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique member identifier |
| name | VARCHAR(100) | NOT NULL | Member full name |
| email | VARCHAR(100) | UNIQUE, NOT NULL | Email address (must be unique) |
| contact | VARCHAR(20) | | Phone number |
| status | ENUM('active', 'inactive') | DEFAULT 'active' | Account status |

**Indexes**: PRIMARY KEY on id, UNIQUE on email

**Notes**: Members can be deactivated if they have no issued books and no pending fines.

---

### 3. users
Stores user login credentials and role information.

| Column | Type | Constraint | Description |
|--------|------|-----------|-------------|
| id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique user identifier |
| username | VARCHAR(50) | UNIQUE, NOT NULL | Login username |
| password | VARCHAR(100) | NOT NULL | Login password |
| role | ENUM('admin', 'user') | NOT NULL | User role |
| status | ENUM('active', 'inactive') | DEFAULT 'active' | Account status |
| member_id | INT | FOREIGN KEY | Link to members table (optional) |

**Indexes**: PRIMARY KEY on id, UNIQUE on username
**Foreign Keys**: member_id → members(id)

---

### 4. issued_books
Tracks which books are currently issued to members.

| Column | Type | Constraint | Description |
|--------|------|-----------|-------------|
| issue_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique issue identifier |
| book_id | INT | NOT NULL, FOREIGN KEY | Reference to books(id) |
| member_id | INT | NOT NULL, FOREIGN KEY | Reference to members(id) |
| issue_date | DATE | NOT NULL | Date when book was issued |
| return_date | DATE | NULLABLE | Date when book was returned (NULL = not returned) |

**Indexes**: PRIMARY KEY on issue_id
**Foreign Keys**: 
- book_id → books(id)
- member_id → members(id)

**Business Rules**:
- return_date is NULL for active issues
- return_date is filled when book is returned
- Can query for overdue books: WHERE return_date IS NULL AND DATEDIFF(CURDATE(), issue_date) > 14

---

### 5. returned_books
Maintains a complete history of all returned books and calculated fines.

| Column | Type | Constraint | Description |
|--------|------|-----------|-------------|
| id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique record identifier |
| book_id | INT | FOREIGN KEY | Reference to books(id) |
| member_id | INT | FOREIGN KEY | Reference to members(id) |
| issue_date | DATE | | Original issue date |
| return_date | DATE | | Actual return date |
| fine | DECIMAL(10,2) | | Calculated fine amount |

**Indexes**: PRIMARY KEY on id
**Foreign Keys**:
- book_id → books(id)
- member_id → members(id)

**Purpose**: Provides audit trail and historical data for reporting.

---

### 6. fines
Tracks outstanding fines for members.

| Column | Type | Constraint | Description |
|--------|------|-----------|-------------|
| id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique fine record |
| member_id | INT | FOREIGN KEY | Reference to members(id) |
| book_id | INT | FOREIGN KEY | Reference to books(id) |
| amount | DECIMAL(10,2) | | Fine amount in currency |
| paid | BOOLEAN | DEFAULT FALSE | Payment status |
| fine_date | DATE | | Date fine was recorded |

**Indexes**: PRIMARY KEY on id
**Foreign Keys**:
- member_id → members(id)
- book_id → books(id)

**Notes**: 
- Fine rate: ₹10 per day after 14-day grace period
- fine_date typically matches return_date
- Can be marked as paid when payment is received

---

## Entity Relationship Diagram (ERD)

```
┌─────────────┐
│    books    │
├─────────────┤
│ id (PK)     │───────────┐
│ title       │           │
│ author      │           │ 1:M
│ quantity    │           │
└─────────────┘           │
                          │
┌─────────────┐           │      ┌──────────────────┐
│   members   │           │      │ issued_books     │
├─────────────┤           │      ├──────────────────┤
│ id (PK)     │────────────────────│ issue_id (PK)    │
│ name        │     1:M   │      │ book_id (FK)─────┘
│ email       │           │      │ member_id (FK)───┐
│ contact     │           │      │ issue_date       │
│ status      │           │      │ return_date      │
└─────────────┘           │      └──────────────────┘
      │                   │
      │ 1:M               │
      │                   │
      ├──────────────────────────┐
      │                          │
      │                ┌─────────────────────┐
      │                │  returned_books     │
      │                ├─────────────────────┤
      │────────────────│ id (PK)             │
      │                │ book_id (FK)────────┘
      │ 1:M            │ member_id (FK)──────┐
      │                │ issue_date          │
      │                │ return_date         │
      │                │ fine                │
      │                └─────────────────────┘
      │
      ├──────────────────┐
      │                  │
      │ 1:M        ┌──────────┐
      │            │  fines   │
      │            ├──────────┤
      └────────────│ id (PK)  │
                   │ member_id│
                   │ book_id  │
                   │ amount   │
                   │ paid     │
                   │ fine_date│
                   └──────────┘
```

---

## Sample Queries

### Find overdue books (> 14 days)
```sql
SELECT i.issue_id, i.book_id, i.member_id, i.issue_date,
       DATEDIFF(CURDATE(), i.issue_date) as days_issued
FROM issued_books i
WHERE return_date IS NULL 
  AND DATEDIFF(CURDATE(), i.issue_date) > 14
ORDER BY days_issued DESC;
```

### Calculate fines for a member
```sql
SELECT member_id, SUM(amount) as total_fine
FROM fines
WHERE paid = FALSE
GROUP BY member_id
ORDER BY total_fine DESC;
```

### Get member issue history
```sql
SELECT m.name, b.title, i.issue_date, i.return_date,
       DATEDIFF(i.return_date, i.issue_date) as days_kept
FROM issued_books i
JOIN members m ON i.member_id = m.id
JOIN books b ON i.book_id = b.id
WHERE m.id = ?
ORDER BY i.issue_date DESC;
```

### Get available books
```sql
SELECT id, title, author, quantity
FROM books
WHERE quantity > 0
ORDER BY title ASC;
```

### Get active members
```sql
SELECT id, name, email, contact
FROM members
WHERE status = 'active'
ORDER BY name ASC;
```

---

## Constraints & Validations

### Foreign Keys
- All foreign keys enforce referential integrity
- CASCADE DELETE is not enabled (manual cleanup required)

### Unique Constraints
- Email must be unique per member
- Username must be unique per user

### Enums
- **members.status**: 'active' or 'inactive'
- **users.role**: 'admin' or 'user'
- **users.status**: 'active' or 'inactive'

### Business Rules
1. Members cannot be deactivated if they have:
   - Unreturned books (return_date IS NULL)
   - Unpaid fines (paid = FALSE)

2. Books cannot be issued if:
   - Member is inactive
   - Book quantity is 0
   - Member already has same book issued

3. Fine Calculation:
   - Base period: 14 days
   - Fine rate: ₹10/day for days beyond period
   - Applied only on return

---

## Backup & Recovery

### Backup the database
```bash
mysqldump -u root -p library_db > backup_library_db.sql
```

### Restore the database
```bash
mysql -u root -p library_db < backup_library_db.sql
```

---

## Database Maintenance

### Check database size
```sql
SELECT 
    table_name,
    ROUND(((data_length + index_length) / 1024 / 1024), 2) as size_mb
FROM information_schema.TABLES 
WHERE table_schema = 'library_db'
ORDER BY (data_length + index_length) DESC;
```

### Optimize tables
```sql
OPTIMIZE TABLE books;
OPTIMIZE TABLE members;
OPTIMIZE TABLE users;
OPTIMIZE TABLE issued_books;
OPTIMIZE TABLE returned_books;
OPTIMIZE TABLE fines;
```

### Check for orphaned records
```sql
-- Orphaned issued_books
SELECT * FROM issued_books 
WHERE book_id NOT IN (SELECT id FROM books) 
   OR member_id NOT IN (SELECT id FROM members);
```
