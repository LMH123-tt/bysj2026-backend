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

print("=== C端用户 (content_user) ===")
cursor.execute("SELECT user_id, user_name, nick_name, status FROM content_user")
for row in cursor.fetchall():
    print(f"  ID={row[0]}, username={row[1]}, nickname={row[2]}, status={row[3]}")

print("\n=== B端管理员 (sys_user) ===")
cursor.execute("SELECT user_id, user_name, nick_name, status FROM sys_user WHERE del_flag='0' LIMIT 10")
for row in cursor.fetchall():
    print(f"  ID={row[0]}, username={row[1]}, nickname={row[2]}, status={row[3]}")

print("\n=== 内容封面图状态 ===")
cursor.execute("SELECT content_id, title, content_type, cover_image, source_url FROM content_info WHERE status='0' ORDER BY content_id")
for row in cursor.fetchall():
    type_map = {'1': '文章', '2': '视频', '3': '图片', '4': '音频'}
    has_cover = "有" if row[3] and row[3].strip() else "无"
    has_source = "有" if row[4] and row[4].strip() else "无"
    print(f"  ID={row[0]}, type={type_map.get(row[2],'?')}, cover={has_cover}, source={has_source}, title={row[1][:25]}")

cursor.close()
conn.close()
