import pymysql
import re

conn = pymysql.connect(
    host='localhost',
    port=3306,
    user='root',
    password='118829',
    database='ry-cloud',
    charset='utf8mb4',
    autocommit=False
)

cursor = conn.cursor()

sql_file = r'd:\Bishe\RuoYi-Cloud\sql\test_data.sql'
with open(sql_file, 'r', encoding='utf-8') as f:
    sql_content = f.read()

sql_content = re.sub(r'^--.*$', '', sql_content, flags=re.MULTILINE)
sql_content = re.sub(r'^SET\s+.*$', '', sql_content, flags=re.MULTILINE)

statements = []
current = ''
for line in sql_content.split('\n'):
    stripped = line.strip()
    if not stripped:
        continue
    current += line + '\n'
    if stripped.endswith(';'):
        statements.append(current.strip())
        current = ''

success = 0
errors = 0
for stmt in statements:
    try:
        cursor.execute(stmt)
        conn.commit()
        success += 1
    except Exception as e:
        errors += 1
        if errors <= 5:
            print(f"ERROR: {str(e)[:100]}")
            print(f"SQL: {stmt[:80]}...")

cursor.close()
conn.close()

print(f"\nDone! Success: {success}, Errors: {errors}")
