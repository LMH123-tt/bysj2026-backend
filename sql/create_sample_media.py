import struct
import os

upload_dir = r'D:\ruoyi\uploadPath\2026\04\22'
os.makedirs(upload_dir, exist_ok=True)

def create_minimal_mp4(filepath, duration_sec=5):
    with open(filepath, 'wb') as f:
        ftyp = b'\x00\x00\x00\x18ftypmp42\x00\x00\x00\x00mp42isom'
        f.write(ftyp)
        moov_size = 108
        moov = struct.pack('>I', moov_size) + b'moov'
        mvhd = struct.pack('>I', 108) + b'mvhd'
        mvhd += b'\x00' * 100
        moov += mvhd
        f.write(moov)

def create_minimal_mp3(filepath, duration_sec=5):
    with open(filepath, 'wb') as f:
        header = b'\xff\xfb\x90\x00'
        frame_size = 417
        for _ in range(duration_sec * 38):
            f.write(header)
            f.write(b'\x00' * (frame_size - 4))

def create_sample_image(filepath, width=640, height=360):
    header = f'P6\n{width} {height}\n255\n'.encode()
    pixels = bytes([128] * (width * height * 3))
    with open(filepath, 'wb') as f:
        f.write(header + pixels)

create_minimal_mp4(os.path.join(upload_dir, 'sample_tech_001.mp4'))
create_minimal_mp4(os.path.join(upload_dir, 'sample_game_002.mp4'))
create_minimal_mp4(os.path.join(upload_dir, 'sample_concert_003.mp4'))
create_minimal_mp4(os.path.join(upload_dir, 'sample_food_004.mp4'))
create_minimal_mp4(os.path.join(upload_dir, 'sample_space_005.mp4'))
create_minimal_mp4(os.path.join(upload_dir, 'sample_chengdu_006.mp4'))

create_minimal_mp3(os.path.join(upload_dir, 'sample_guzheng_001.mp3'))
create_minimal_mp3(os.path.join(upload_dir, 'sample_piano_002.mp3'))

create_sample_image(os.path.join(upload_dir, 'cover_tech_001.ppm'))
create_sample_image(os.path.join(upload_dir, 'cover_entertainment_002.ppm'))

print("Sample media files created successfully!")
for f in os.listdir(upload_dir):
    size = os.path.getsize(os.path.join(upload_dir, f))
    print(f"  {f}: {size} bytes")
