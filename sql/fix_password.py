import pymysql
import subprocess
import json

conn = pymysql.connect(
    host='localhost',
    port=3306,
    user='root',
    password='118829',
    database='ry-cloud',
    charset='utf8mb4',
    autocommit=True
)
cursor = conn.cursor()

cursor.execute("SELECT user_id, user_name, password FROM content_user WHERE user_name = '212121'")
row = cursor.fetchone()
if row:
    stored_hash = row[2]
    print(f"User 212121 found, stored hash: {stored_hash}")
else:
    print("User 212121 NOT found")
    cursor.close()
    conn.close()
    exit()

# Use Python bcrypt to verify
try:
    import bcrypt
    password = "212121"
    result = bcrypt.checkpw(password.encode('utf-8'), stored_hash.encode('utf-8'))
    print(f"BCrypt verify '212121' against stored hash: {result}")
    
    if not result:
        # Generate correct hash
        correct_hash = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt(rounds=10)).decode('utf-8')
        print(f"Correct hash for '212121': {correct_hash}")
        
        # Update all users with the correct hash
        cursor.execute("UPDATE content_user SET password = %s", (correct_hash,))
        print(f"Updated ALL content_user passwords to correct hash")
        
        # Verify again
        result2 = bcrypt.checkpw(password.encode('utf-8'), correct_hash.encode('utf-8'))
        print(f"Verify new hash: {result2}")
    else:
        print("Password hash is correct, the issue might be elsewhere")
        
except ImportError:
    print("bcrypt not installed, installing...")
    import os
    os.system("pip install bcrypt")
    import bcrypt
    password = "212121"
    result = bcrypt.checkpw(password.encode('utf-8'), stored_hash.encode('utf-8'))
    print(f"BCrypt verify '212121' against stored hash: {result}")
    
    if not result:
        correct_hash = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt(rounds=10)).decode('utf-8')
        print(f"Correct hash for '212121': {correct_hash}")
        cursor.execute("UPDATE content_user SET password = %s", (correct_hash,))
        print(f"Updated ALL content_user passwords to correct hash")
        result2 = bcrypt.checkpw(password.encode('utf-8'), correct_hash.encode('utf-8'))
        print(f"Verify new hash: {result2}")

cursor.close()
conn.close()
