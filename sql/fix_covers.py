import pymysql
from PIL import Image, ImageDraw, ImageFont
import os
import random

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

# 1. 添加原来的 212121 账号
cursor.execute("SELECT user_id FROM content_user WHERE user_name = '212121'")
existing = cursor.fetchone()
if not existing:
    cursor.execute("""
        INSERT INTO content_user (user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, pwd_update_date, create_by, create_time)
        VALUES ('212121', '212121', '01', '', '', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', NOW(), 'register', NOW())
    """)
    print("Added user: 212121 / 212121")
else:
    print("User 212121 already exists")

# 2. 生成封面图
upload_dir = r'D:\ruoyi\uploadPath\2026\04\22'
covers_dir = os.path.join(upload_dir, 'covers')
os.makedirs(covers_dir, exist_ok=True)

category_colors = {
    1: [(41, 128, 185), (52, 152, 219)],
    2: [(192, 57, 43), (231, 76, 60)],
    3: [(39, 174, 96), (46, 204, 113)],
    4: [(211, 84, 0), (243, 156, 18)],
    5: [(142, 68, 173), (155, 89, 182)],
    6: [(22, 160, 133), (26, 188, 156)],
    7: [(241, 196, 15), (243, 156, 18)],
    8: [(230, 126, 34), (211, 84, 0)],
}

type_labels = {'1': '文章', '2': '视频', '3': '图片', '4': '音频'}

def create_cover(filepath, title, category_id, content_type):
    width, height = 640, 360
    img = Image.new('RGB', (width, height))
    draw = ImageDraw.Draw(img)

    colors = category_colors.get(category_id, [(100, 100, 100), (150, 150, 150)])
    for y in range(height):
        ratio = y / height
        r = int(colors[0][0] * (1 - ratio) + colors[1][0] * ratio)
        g = int(colors[0][1] * (1 - ratio) + colors[1][1] * ratio)
        b = int(colors[0][2] * (1 - ratio) + colors[1][2] * ratio)
        draw.line([(0, y), (width, y)], fill=(r, g, b))

    for i in range(5):
        x = random.randint(0, width)
        y = random.randint(0, height)
        r = random.randint(30, 80)
        alpha_color = (
            min(255, colors[1][0] + 40),
            min(255, colors[1][1] + 40),
            min(255, colors[1][2] + 40)
        )
        draw.ellipse([x-r, y-r, x+r, y+r], fill=alpha_color)

    type_label = type_labels.get(content_type, '')
    badge_w, badge_h = 60, 28
    draw.rectangle([20, 20, 20+badge_w, 20+badge_h], fill=(255, 255, 255, 180))
    try:
        font_badge = ImageFont.truetype("msyh.ttc", 16)
        font_title = ImageFont.truetype("msyh.ttc", 28)
        font_sub = ImageFont.truetype("msyh.ttc", 18)
    except:
        try:
            font_badge = ImageFont.truetype("C:/Windows/Fonts/msyh.ttc", 16)
            font_title = ImageFont.truetype("C:/Windows/Fonts/msyh.ttc", 28)
            font_sub = ImageFont.truetype("C:/Windows/Fonts/msyh.ttc", 18)
        except:
            font_badge = ImageFont.load_default()
            font_title = ImageFont.load_default()
            font_sub = ImageFont.load_default()

    bbox = draw.textbbox((0, 0), type_label, font=font_badge)
    tw = bbox[2] - bbox[0]
    draw.text((20 + (badge_w - tw) // 2, 22), type_label, fill=colors[0], font=font_badge)

    display_title = title[:15] + '...' if len(title) > 15 else title
    margin = 30
    max_text_width = width - 2 * margin
    draw.text((margin, height // 2 - 20), display_title, fill=(255, 255, 255), font=font_title)

    draw.rectangle([0, height-4, width, height], fill=(255, 255, 255))

    img.save(filepath, 'JPEG', quality=85)

# 3. 查询所有已发布内容
cursor.execute("""
    SELECT content_id, title, category_id, content_type, cover_image
    FROM content_info WHERE status = '0' ORDER BY content_id
""")
contents = cursor.fetchall()

base_url = "http://127.0.0.1:9300/statics/2026/04/22/covers"
updated = 0

for content_id, title, category_id, content_type, cover_image in contents:
    if cover_image and cover_image.strip():
        print(f"  ID={content_id}: already has cover, skipping")
        continue

    filename = f"cover_{content_id}.jpg"
    filepath = os.path.join(covers_dir, filename)
    create_cover(filepath, title, category_id or 1, content_type)

    cover_url = f"{base_url}/{filename}"
    cursor.execute(
        "UPDATE content_info SET cover_image = %s WHERE content_id = %s",
        (cover_url, content_id)
    )
    updated += 1
    print(f"  ID={content_id}: cover created -> {filename}")

print(f"\nTotal covers created: {updated}")

# 4. 验证
cursor.execute("""
    SELECT content_id, LEFT(title, 20), content_type,
           CASE WHEN cover_image IS NOT NULL AND cover_image != '' THEN 'YES' ELSE 'NO' END as has_cover
    FROM content_info WHERE status = '0' ORDER BY content_id
""")
print("\n=== Cover status ===")
for row in cursor.fetchall():
    type_map = {'1': 'article', '2': 'video', '3': 'image', '4': 'audio'}
    print(f"  ID={row[0]} type={type_map.get(row[2],'?')} cover={row[3]} title={row[1]}")

cursor.close()
conn.close()
print("\nDone!")
