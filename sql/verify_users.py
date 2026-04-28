import pymysql

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

cursor.execute("SELECT user_id, user_name, nick_name, password FROM content_user WHERE user_name = '212121'")
row = cursor.fetchone()
if row:
    print(f"User found: ID={row[0]}, username={row[1]}, nickname={row[2]}")
    print(f"Password hash: {row[3][:30]}...")
else:
    print("User 212121 NOT found!")

cursor.execute("SELECT user_id, user_name, nick_name, password FROM content_user ORDER BY user_id")
print("\n=== All C-end users ===")
for r in cursor.fetchall():
    print(f"  username: {r[1]:12s}  nickname: {r[2]:6s}  password: {r[3][:20]}...")

cursor.close()
conn.close()
