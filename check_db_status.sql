USE library_db;
SELECT 'CHECKING USERS TABLE:' as Info;
SELECT id, username, LEFT(password, 30) as password_preview, role, status FROM users;
