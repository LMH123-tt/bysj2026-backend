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

base_url = "http://127.0.0.1:9300/statics/2026/04/22"

video_updates = [
    (3, f"{base_url}/sample_tech_001.mp4", f"{base_url}/cover_tech_001.ppm"),
    (9, f"{base_url}/sample_game_002.mp4", f"{base_url}/cover_entertainment_002.ppm"),
    (34, f"{base_url}/sample_concert_003.mp4", ""),
    (35, f"{base_url}/sample_food_004.mp4", ""),
    (36, f"{base_url}/sample_space_005.mp4", ""),
    (37, f"{base_url}/sample_chengdu_006.mp4", ""),
]

audio_updates = [
    (29, f"{base_url}/sample_guzheng_001.mp3", ""),
    (28, f"{base_url}/sample_piano_002.mp3", ""),
]

for content_id, source_url, cover_image in video_updates:
    if cover_image:
        cursor.execute(
            "UPDATE content_info SET source_url=%s, cover_image=%s WHERE content_id=%s",
            (source_url, cover_image, content_id)
        )
    else:
        cursor.execute(
            "UPDATE content_info SET source_url=%s WHERE content_id=%s",
            (source_url, content_id)
        )
    print(f"Updated content_id={content_id}: video source_url={source_url}")

for content_id, source_url, cover_image in audio_updates:
    cursor.execute(
        "UPDATE content_info SET source_url=%s WHERE content_id=%s",
        (source_url, content_id)
    )
    print(f"Updated content_id={content_id}: audio source_url={source_url}")

cursor.execute("SELECT content_id, title, content_type, cover_image, source_url FROM content_info WHERE content_type IN ('2','3','4')")
rows = cursor.fetchall()
print("\n=== Media content verification ===")
for row in rows:
    type_map = {'2': 'video', '3': 'image', '4': 'audio'}
    print(f"  ID={row[0]}, type={type_map.get(row[2], row[2])}, title={row[3][:20] if row[3] else 'N/A'}..., source={row[4][:50] if row[4] else 'N/A'}...")

cursor.close()
conn.close()
print("\nDone!")
