import pymysql
import bcrypt

conn = pymysql.connect(
    host='localhost', port=3306, user='root', password='118829',
    database='ry-cloud', charset='utf8mb4', autocommit=True
)
cursor = conn.cursor()

# Python bcrypt generates $2b$ but Java BCryptPasswordEncoder uses $2a$
# They are compatible - just need to replace $2b$ with $2a$
cursor.execute("SELECT user_id, user_name, password FROM content_user WHERE user_name = '212121'")
row = cursor.fetchone()
current_hash = row[2]
print(f"Current hash: {current_hash}")

# Replace $2b$ with $2a$ for Java compatibility
if current_hash.startswith('$2b$'):
    java_compatible_hash = current_hash.replace('$2b$', '$2a$', 1)
    cursor.execute("UPDATE content_user SET password = %s", (java_compatible_hash,))
    print(f"Updated to Java-compatible hash: {java_compatible_hash}")
    
    # Verify both work with Python bcrypt
    result_2a = bcrypt.checkpw("212121".encode('utf-8'), java_compatible_hash.encode('utf-8'))
    result_2b = bcrypt.checkpw("212121".encode('utf-8'), current_hash.encode('utf-8'))
    print(f"Verify $2a$ hash: {result_2a}")
    print(f"Verify $2b$ hash: {result_2b}")
else:
    print(f"Hash already starts with $2a$, no change needed")

# Final verification - check all users
cursor.execute("SELECT user_name, LEFT(password, 7) as prefix FROM content_user")
print("\n=== All users password prefix ===")
for r in cursor.fetchall():
    print(f"  {r[0]}: {r[1]}")

cursor.close()
conn.close()
print("\nDone! All passwords should now work with '212121'")
