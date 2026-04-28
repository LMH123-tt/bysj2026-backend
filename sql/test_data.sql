-- ============================================================
-- 内容分发平台 - 完整测试数据脚本
-- 涵盖：分类、用户、内容、评论、点赞、收藏、浏览历史、好友、私信
-- 使用前请确保所有表已创建，建议先清空旧数据再执行
-- ============================================================

SET NAMES utf8mb4;
SET CHARACTER_SET_CLIENT = utf8mb4;
SET CHARACTER_SET_CONNECTION = utf8mb4;
SET CHARACTER_SET_RESULTS = utf8mb4;

-- 1. 清空旧数据（按外键依赖顺序）
DELETE FROM user_message;
DELETE FROM user_friend;
DELETE FROM content_view_history;
DELETE FROM content_like;
DELETE FROM user_favorite;
DELETE FROM content_comment;
DELETE FROM content_info;
DELETE FROM content_user;
DELETE FROM content_category;

-- 重置自增ID
ALTER TABLE content_category AUTO_INCREMENT = 1;
ALTER TABLE content_user AUTO_INCREMENT = 1;
ALTER TABLE content_info AUTO_INCREMENT = 1;
ALTER TABLE content_comment AUTO_INCREMENT = 1;
ALTER TABLE user_favorite AUTO_INCREMENT = 1;
ALTER TABLE content_view_history AUTO_INCREMENT = 1;
ALTER TABLE content_like AUTO_INCREMENT = 1;
ALTER TABLE user_friend AUTO_INCREMENT = 1;
ALTER TABLE user_message AUTO_INCREMENT = 1;


-- ============================================================
-- 2. 分类数据（8个分类）
-- ============================================================
INSERT INTO content_category (category_name, parent_id, order_num, status, icon, description, content_count, create_by, create_time) VALUES
('科技', 0, 1, '0', 'tech', '科技前沿、数码产品、人工智能', 0, 'admin', NOW()),
('娱乐', 0, 2, '0', 'entertainment', '影视综艺、明星八卦、游戏动漫', 0, 'admin', NOW()),
('体育', 0, 3, '0', 'sports', '足球篮球、赛事资讯、健身运动', 0, 'admin', NOW()),
('资讯', 0, 4, '0', 'news', '时事热点、社会新闻、国际动态', 0, 'admin', NOW()),
('教育', 0, 5, '0', 'education', '学习方法、考试考证、知识科普', 0, 'admin', NOW()),
('生活', 0, 6, '0', 'life', '美食旅行、家居装饰、宠物养护', 0, 'admin', NOW()),
('音乐', 0, 7, '0', 'music', '流行音乐、古典乐、乐器演奏', 0, 'admin', NOW()),
('美食', 0, 8, '0', 'food', '菜谱分享、餐厅推荐、烘焙甜品', 0, 'admin', NOW());


-- ============================================================
-- 3. C端用户数据（10个用户，密码统一为 212121）
-- BCrypt加密后的 212121 密文
-- ============================================================
-- 密码 212121 的BCrypt密文：$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2
INSERT INTO content_user (user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, pwd_update_date, create_by, create_time) VALUES
('zhangsan',   '张三',     '01', 'zhangsan@test.com',   '13800000001', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', NOW(), 'register', NOW()),
('lisi',       '李四',     '01', 'lisi@test.com',       '13800000002', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', NOW(), 'register', NOW()),
('wangwu',     '王五',     '01', 'wangwu@test.com',     '13800000003', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', NOW(), 'register', NOW()),
('zhaoliu',    '赵六',     '01', 'zhaoliu@test.com',    '13800000004', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', NOW(), 'register', NOW()),
('sunqi',      '孙七',     '01', 'sunqi@test.com',      '13800000005', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', NOW(), 'register', NOW()),
('zhouba',     '周八',     '01', 'zhouba@test.com',     '13800000006', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', NOW(), 'register', NOW()),
('wujiu',      '吴九',     '01', 'wujiu@test.com',      '13800000007', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', NOW(), 'register', NOW()),
('zhengshi',   '郑十',     '01', 'zhengshi@test.com',   '13800000008', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', NOW(), 'register', NOW()),
('testuser',   '测试用户',  '01', 'testuser@test.com',   '13800000009', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', NOW(), 'register', NOW()),
('demouser',   '演示账号',  '01', 'demouser@test.com',   '13800000010', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', NOW(), 'register', NOW());


-- ============================================================
-- 4. 内容数据（50条，覆盖8个分类和4种类型）
-- 用户ID: 1-10, 分类ID: 1-8
-- status: 0=已发布, 1=待审核, 2=已驳回
-- ============================================================
INSERT INTO content_info (title, summary, content, category_id, author, cover_image, content_type, status, view_count, like_count, comment_count, share_count, is_recommended, is_top, publish_time, tags, source_url, create_by, create_time) VALUES
-- 科技类 (category_id=1)
('GPT-5发布：AI能力再上新台阶', 'OpenAI最新发布的GPT-5模型在多项基准测试中刷新纪录，推理能力较前代提升显著', '<h2>GPT-5核心升级</h2><p>OpenAI于本周正式发布了GPT-5模型，该模型在数学推理、代码生成和多模态理解等维度均取得了突破性进展。据官方数据显示，GPT-5在MMLU基准上的得分达到92.3%，较GPT-4提升了近8个百分点。</p><p>在代码生成方面，GPT-5在HumanEval测试中的通过率从GPT-4的67%提升至89%，这意味着它能够更准确地理解和实现复杂的编程需求。</p>', 1, '张三', '', '1', '0', 15280, 892, 156, 234, '1', '1', DATE_SUB(NOW(), INTERVAL 2 HOUR), 'AI,GPT,人工智能', '', 'zhangsan', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
('量子计算突破：1000量子比特芯片问世', 'IBM宣布成功研制1000量子比特处理器，标志着量子计算进入新纪元', '<h2>量子计算新里程碑</h2><p>IBM Research近日公布了其最新的量子处理器Condor，该处理器拥有超过1000个量子比特，是目前世界上最大的量子芯片。</p><p>这一突破意味着量子计算正从实验室走向实际应用阶段，未来有望在药物研发、金融建模和密码学等领域发挥重要作用。</p>', 1, '李四', '', '1', '0', 8960, 456, 78, 123, '1', '0', DATE_SUB(NOW(), INTERVAL 5 HOUR), '量子计算,IBM,芯片', '', 'lisi', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
('苹果Vision Pro 2正式发布', '第二代空间计算设备更轻更强大，售价下调至2499美元', '<h2>Vision Pro 2全面升级</h2><p>苹果公司在WWDC上发布了第二代Vision Pro头显设备。新款设备重量减轻了30%，电池续航提升至4小时，搭载M4 Ultra芯片，图形处理能力较前代提升3倍。</p><p>最令人惊喜的是价格下调至2499美元，较初代降低了1000美元，有望推动空间计算设备的普及。</p>', 1, '王五', '', '2', '0', 12340, 678, 112, 189, '0', '0', DATE_SUB(NOW(), INTERVAL 8 HOUR), '苹果,VR,空间计算', '', 'wangwu', DATE_SUB(NOW(), INTERVAL 9 HOUR)),
('国产操作系统深度V24发布', '深度操作系统发布全新V24版本，全面适配国产CPU和信创生态', '<p>深度操作系统V24版本带来了全新的DDE桌面环境，支持龙芯、飞腾、鲲鹏等国产CPU平台，在系统安全性和应用兼容性方面均有大幅提升。</p>', 1, '赵六', '', '1', '0', 5670, 345, 67, 89, '0', '0', DATE_SUB(NOW(), INTERVAL 12 HOUR), '操作系统,国产,信创', '', 'zhaoliu', DATE_SUB(NOW(), INTERVAL 13 HOUR)),
('5G-A商用加速：万兆时代即将到来', '三大运营商启动5G-A商用部署，下行速率突破10Gbps', '<p>中国移动、中国电信、中国联通三大运营商联合宣布启动5G-Advanced（5G-A）商用部署。5G-A在下行速率上可达到10Gbps，是当前5G的10倍，将支撑裸眼3D、全息通信等新应用场景。</p>', 1, '孙七', '', '1', '0', 4230, 213, 45, 56, '0', '0', DATE_SUB(NOW(), INTERVAL 1 DAY), '5G,通信,万兆', '', 'sunqi', DATE_SUB(NOW(), INTERVAL 1 DAY)),
('自动驾驶L4级路测全面开放', '北京、上海、深圳等城市全面开放L4级自动驾驶路测', '<p>交通运输部宣布在北京、上海、深圳、广州等15个城市全面开放L4级自动驾驶道路测试。百度Apollo、小马智行等多家企业已获得测试牌照。</p>', 1, '张三', '', '1', '0', 7890, 423, 89, 112, '0', '0', DATE_SUB(NOW(), INTERVAL 2 DAY), '自动驾驶,L4,智能交通', '', 'zhangsan', DATE_SUB(NOW(), INTERVAL 2 DAY)),
('Rust语言2026年度调查报告', 'Rust连续8年成为最受开发者喜爱的编程语言', '<p>Stack Overflow 2026年度开发者调查结果显示，Rust语言连续第8年被评为最受开发者喜爱的编程语言，喜爱度达到87.2%。Rust在系统编程、WebAssembly和嵌入式开发领域的应用持续增长。</p>', 1, '周八', '', '1', '0', 3450, 267, 56, 78, '0', '0', DATE_SUB(NOW(), INTERVAL 3 DAY), 'Rust,编程语言,开发', '', 'zhouba', DATE_SUB(NOW(), INTERVAL 3 DAY)),

-- 娱乐类 (category_id=2)
('2026暑期档票房突破200亿', '国产电影强势崛起，暑期档总票房创历史新高', '<h2>暑期档电影市场火爆</h2><p>2026年暑期档电影总票房突破200亿元大关，创下历史新纪录。其中，国产电影占比超过75%，《长安三万里2》《流浪地球3》等影片表现亮眼。</p><p>业内人士分析，国产电影在制作水平和叙事能力上的持续进步，是票房增长的核心驱动力。</p>', 2, '李四', '', '1', '0', 18900, 1023, 234, 456, '1', '0', DATE_SUB(NOW(), INTERVAL 1 HOUR), '电影,票房,暑期档', '', 'lisi', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
('《黑神话：悟空》DLC正式上线', '游戏科学发布首个大型DLC"再世西游"，新增30小时剧情内容', '<p>游戏科学正式发布了《黑神话：悟空》的首个大型DLC"再世西游"，新增超过30小时的主线剧情、5个全新Boss和2个隐藏结局。Steam首发同时在线人数突破150万。</p>', 2, '王五', '', '2', '0', 21300, 1567, 345, 678, '1', '1', DATE_SUB(NOW(), INTERVAL 3 HOUR), '游戏,黑神话,DLC', '', 'wangwu', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
('周杰伦2026巡回演唱会官宣', '嘉年华世界巡回演唱会中国站公布8城行程', '<p>周杰伦嘉年华世界巡回演唱会2026中国站正式官宣，将在北京、上海、广州、成都、武汉、南京、杭州、深圳8座城市举办。门票将于下月开售。</p>', 2, '赵六', '', '1', '0', 9870, 567, 123, 234, '0', '0', DATE_SUB(NOW(), INTERVAL 6 HOUR), '周杰伦,演唱会,音乐', '', 'zhaoliu', DATE_SUB(NOW(), INTERVAL 7 HOUR)),
('动漫《鬼灭之刃》最终季开播', '柱训练篇动画正式开播，全球同步放送', '<p>ufotable制作的《鬼灭之刃》最终季"柱训练篇"正式开播，首集在全球范围内引发热议。动画制作水准再创新高，战斗场面尤为精彩。</p>', 2, '孙七', '', '2', '0', 7650, 432, 98, 156, '0', '0', DATE_SUB(NOW(), INTERVAL 10 HOUR), '动漫,鬼灭之刃,日漫', '', 'sunqi', DATE_SUB(NOW(), INTERVAL 11 HOUR)),
('综艺节目《歌手2026》首播破纪录', '全新赛制引发全民热议，首播收视率破3', '<p>湖南卫视《歌手2026》首播收视率突破3%，创下该系列节目首播收视纪录。本季引入"国际对抗"赛制，来自8个国家的歌手同台竞技。</p>', 2, '周八', '', '1', '0', 5430, 321, 76, 98, '0', '0', DATE_SUB(NOW(), INTERVAL 1 DAY), '综艺,歌手,音乐', '', 'zhouba', DATE_SUB(NOW(), INTERVAL 1 DAY)),

-- 体育类 (category_id=3)
('2026世界杯亚洲区预选赛', '国足客场2-1力克日本，晋级前景光明', '<h2>国足创造历史</h2><p>在2026世界杯亚洲区预选赛第三阶段比赛中，中国男足客场2-1击败日本队，这是国足近20年来首次在正式比赛中战胜日本。武磊梅开二度成为全场最佳球员。</p><p>此役过后，国足在小组积分榜上升至第二位，直接晋级世界杯的希望大增。</p>', 3, '张三', '', '1', '0', 25600, 1890, 456, 789, '1', '1', DATE_SUB(NOW(), INTERVAL 30 MINUTE), '世界杯,国足,足球', '', 'zhangsan', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
('NBA总决赛：凯尔特人卫冕成功', '凯尔特人4-2击败湖人，蝉联NBA总冠军', '<p>波士顿凯尔特人在NBA总决赛第六场比赛中以112-98击败洛杉矶湖人，以总比分4-2成功卫冕。塔图姆当选总决赛MVP，场均贡献32.5分8.7篮板。</p>', 3, '李四', '', '1', '0', 13400, 678, 145, 234, '0', '0', DATE_SUB(NOW(), INTERVAL 4 HOUR), 'NBA,凯尔特人,篮球', '', 'lisi', DATE_SUB(NOW(), INTERVAL 5 HOUR)),
('巴黎奥运会中国代表团总结', '中国代表团斩获40金27银24铜，金牌数创境外参赛纪录', '<p>巴黎奥运会圆满落幕，中国代表团共获得40枚金牌、27枚银牌和24枚铜牌，金牌数创下境外参赛历史最佳战绩。跳水、乒乓球、举重等优势项目继续保持统治力。</p>', 3, '王五', '', '1', '0', 8900, 456, 98, 167, '0', '0', DATE_SUB(NOW(), INTERVAL 2 DAY), '奥运会,中国,金牌', '', 'wangwu', DATE_SUB(NOW(), INTERVAL 2 DAY)),
('马拉松热潮：全国跑者突破千万', '中国田径协会数据显示，全国马拉松完赛人数突破1000万', '<p>据中国田径协会最新统计，2025年全国马拉松赛事完赛人数首次突破1000万大关，较上一年增长35%。跑步已成为最受欢迎的全民健身运动之一。</p>', 3, '赵六', '', '1', '0', 3210, 189, 34, 56, '0', '0', DATE_SUB(NOW(), INTERVAL 4 DAY), '马拉松,跑步,健身', '', 'zhaoliu', DATE_SUB(NOW(), INTERVAL 4 DAY)),

-- 资讯类 (category_id=4)
('2026年两会：数字经济成焦点', '政府工作报告提出数字经济占GDP比重目标提升至55%', '<h2>数字经济新目标</h2><p>2026年全国两会上，政府工作报告明确提出到2030年数字经济占GDP比重提升至55%的目标。报告强调要加快新型基础设施建设，推动人工智能、大数据、云计算等技术与实体经济深度融合。</p>', 4, '张三', '', '1', '0', 11200, 567, 123, 189, '1', '0', DATE_SUB(NOW(), INTERVAL 1 HOUR), '两会,数字经济,政策', '', 'zhangsan', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
('全国碳排放交易市场扩容', '钢铁、水泥等高耗能行业纳入碳交易体系', '<p>生态环境部宣布，全国碳排放权交易市场将迎来重大扩容，钢铁、水泥、化工等高耗能行业将分批纳入碳交易体系。预计新增覆盖企业超过2000家，碳排放覆盖量将占全国总排放量的70%以上。</p>', 4, '李四', '', '1', '0', 4560, 234, 56, 78, '0', '0', DATE_SUB(NOW(), INTERVAL 7 HOUR), '碳交易,环保,政策', '', 'lisi', DATE_SUB(NOW(), INTERVAL 8 HOUR)),
('教育部发布新课标改革方案', '中小学课程全面融入AI素养和数字公民教育', '<p>教育部正式发布《义务教育课程方案和课程标准（2026年版）》，首次将人工智能素养和数字公民教育纳入中小学必修课程，计划从2027年秋季学期开始实施。</p>', 4, '王五', '', '1', '0', 6780, 345, 89, 112, '0', '0', DATE_SUB(NOW(), INTERVAL 1 DAY), '教育,课标,AI素养', '', 'wangwu', DATE_SUB(NOW(), INTERVAL 1 DAY)),
('全球人口突破85亿', '联合国发布《世界人口展望2026》修订版', '<p>联合国人口基金发布最新修订版《世界人口展望》，全球人口已突破85亿。报告预测，到2050年全球人口将达到97亿，印度将继续保持人口第一大国地位。</p>', 4, '赵六', '', '1', '0', 2340, 123, 23, 45, '0', '0', DATE_SUB(NOW(), INTERVAL 5 DAY), '人口,联合国,全球', '', 'zhaoliu', DATE_SUB(NOW(), INTERVAL 5 DAY)),

-- 教育类 (category_id=5)
('2026考研国家线公布', '多学科分数线较去年下降，扩招政策效果显现', '<h2>考研国家线变化</h2><p>教育部公布了2026年全国硕士研究生招生考试考生进入复试的初试成绩基本要求。受研究生扩招政策影响，多个学科的分数线较去年有所下降，其中工学降幅最大，达到15分。</p><p>专家建议考生在关注分数线的同时，也要重视复试准备，尤其是专业面试环节。</p>', 5, '张三', '', '1', '0', 9800, 456, 234, 89, '1', '0', DATE_SUB(NOW(), INTERVAL 3 HOUR), '考研,国家线,研究生', '', 'zhangsan', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
('ChatGPT在教育领域的应用指南', '如何合理使用AI工具辅助学习，避免学术不端', '<p>随着ChatGPT等AI工具的普及，如何在教育中合理使用成为热议话题。本文从学术写作、编程学习、语言翻译三个场景出发，探讨AI辅助学习的正确姿势，并分析学术不端的风险边界。</p>', 5, '李四', '', '1', '0', 5670, 345, 78, 123, '0', '0', DATE_SUB(NOW(), INTERVAL 9 HOUR), 'AI,教育,ChatGPT', '', 'lisi', DATE_SUB(NOW(), INTERVAL 10 HOUR)),
('高考改革新方案解读', '八省市启动新高考3+1+2模式改革', '<p>教育部宣布，河南、四川等8个省市将从2027年起实施新高考"3+1+2"模式。改革后，考生可在物理和历史中选择1门首选科目，在化学、生物、政治、地理中选择2门再选科目。</p>', 5, '王五', '', '1', '0', 7890, 423, 156, 134, '0', '0', DATE_SUB(NOW(), INTERVAL 2 DAY), '高考,改革,教育', '', 'wangwu', DATE_SUB(NOW(), INTERVAL 2 DAY)),
('在线教育平台用户突破5亿', '知识付费市场持续增长，职业教育成新增长点', '<p>中国互联网络信息中心数据显示，截至2026年6月，中国在线教育用户规模突破5亿。其中，职业教育和技能培训成为增长最快的细分领域，同比增长42%。</p>', 5, '赵六', '', '1', '0', 3450, 178, 45, 67, '0', '0', DATE_SUB(NOW(), INTERVAL 6 DAY), '在线教育,知识付费,职业培训', '', 'zhaoliu', DATE_SUB(NOW(), INTERVAL 6 DAY)),

-- 生活类 (category_id=6)
('极简生活：30天断舍离挑战', '从物品到人际关系，重新审视生活中真正重要的东西', '<h2>30天断舍离计划</h2><p>极简生活不是一味地扔东西，而是通过减少不必要的物质占有，把时间和精力留给真正重要的事物。本文分享一个30天断舍离挑战计划，每天完成一个小任务，逐步建立清爽有序的生活方式。</p>', 6, '孙七', '', '1', '0', 4560, 289, 67, 98, '0', '0', DATE_SUB(NOW(), INTERVAL 5 HOUR), '极简,断舍离,生活方式', '', 'sunqi', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
('2026年最值得去的10个旅行目的地', '从冰岛极光到新西兰星空，年度旅行清单推荐', '<p>Lonely Planet发布了2026年最佳旅行目的地榜单，冰岛、新西兰、日本京都、葡萄牙里斯本等10个目的地入选。本文详细介绍每个目的地的最佳旅行时间、必去景点和预算参考。</p>', 6, '周八', '', '1', '0', 6780, 456, 89, 178, '0', '0', DATE_SUB(NOW(), INTERVAL 1 DAY), '旅行,目的地,攻略', '', 'zhouba', DATE_SUB(NOW(), INTERVAL 1 DAY)),
('宠物经济爆发：养宠家庭突破1亿', '猫经济领跑，宠物医疗和智能设备成新风口', '<p>中国宠物行业白皮书显示，2026年中国养宠家庭数量突破1亿户，宠物市场规模达到3500亿元。其中，猫相关消费占比首次超过狗，宠物智能设备市场增速最快。</p>', 6, '吴九', '', '1', '0', 3210, 198, 45, 67, '0', '0', DATE_SUB(NOW(), INTERVAL 3 DAY), '宠物,猫,消费', '', 'wujiu', DATE_SUB(NOW(), INTERVAL 3 DAY)),
('智能家居入门指南', '从智能音箱到全屋智能，手把手教你打造智慧家庭', '<p>智能家居产品越来越丰富，但很多消费者不知道从何入手。本文从预算、需求和生态兼容性三个维度，为不同需求的家庭提供智能家居搭建方案，涵盖入门级（2000元内）到旗舰级（2万元以上）的配置推荐。</p>', 6, '郑十', '', '1', '0', 2340, 156, 34, 45, '0', '0', DATE_SUB(NOW(), INTERVAL 7 DAY), '智能家居,物联网,装修', '', 'zhengshi', DATE_SUB(NOW(), INTERVAL 7 DAY)),

-- 音乐类 (category_id=7)
('2026格莱美颁奖典礼回顾', 'Taylor Swift创纪录获得年度专辑四连冠', '<h2>格莱美之夜</h2><p>第68届格莱美颁奖典礼在洛杉矶举行，Taylor Swift凭借新专辑再次获得年度专辑奖，创造了格莱美历史上首位四连冠的纪录。此外，中国音乐人首次获得格莱美提名，标志着华语音乐的国际化进程。</p>', 7, '周八', '', '1', '0', 8900, 567, 123, 234, '1', '0', DATE_SUB(NOW(), INTERVAL 4 HOUR), '格莱美,音乐,Taylor Swift', '', 'zhouba', DATE_SUB(NOW(), INTERVAL 5 HOUR)),
('AI作曲工具盘点：音乐创作新范式', '从Suno到Udio，AI音乐生成工具如何改变创作方式', '<p>AI音乐生成工具在过去一年取得了长足进步。Suno v4、Udio 2.0等工具已经能够生成结构完整、旋律优美的音乐作品。本文对比了5款主流AI作曲工具的功能特点和适用场景。</p>', 7, '吴九', '', '1', '0', 5670, 345, 78, 123, '0', '0', DATE_SUB(NOW(), INTERVAL 11 HOUR), 'AI,音乐,作曲', '', 'wujiu', DATE_SUB(NOW(), INTERVAL 12 HOUR)),
('吉他入门：零基础30天学会弹唱', '从持琴姿势到和弦转换，系统学习吉他弹唱', '<p>本文为零基础吉他爱好者设计了一个30天学习计划，从最基础的持琴姿势、右手拨弦开始，逐步掌握C、G、Am、Em等常用和弦，最终能够独立完成一首完整的弹唱曲目。</p>', 7, '郑十', '', '4', '0', 3450, 234, 56, 78, '0', '0', DATE_SUB(NOW(), INTERVAL 2 DAY), '吉他,入门,教学', '', 'zhengshi', DATE_SUB(NOW(), INTERVAL 2 DAY)),
('华语乐坛2026年中盘点', '新生代歌手崛起，音乐风格多元化趋势明显', '<p>2026年上半年华语乐坛呈现出多元化发展趋势，说唱、电子、民谣等风格百花齐放。多位95后歌手凭借原创作品获得广泛关注，华语音乐原创力持续提升。</p>', 7, '张三', '', '1', '0', 4560, 267, 67, 89, '0', '0', DATE_SUB(NOW(), INTERVAL 5 DAY), '华语音乐,歌手,原创', '', 'zhangsan', DATE_SUB(NOW(), INTERVAL 5 DAY)),

-- 美食类 (category_id=8)
('家常菜100道：从入门到精通', '详细图文教程，新手也能做出美味家常菜', '<h2>家常菜合集</h2><p>本文精选100道经典家常菜，从最简单的番茄炒蛋到有一定难度的红烧肉，每道菜都配有详细的步骤图和烹饪技巧。无论你是厨房新手还是有一定基础的烹饪爱好者，都能从中找到适合自己的菜谱。</p>', 8, '赵六', '', '1', '0', 7650, 456, 98, 156, '0', '0', DATE_SUB(NOW(), INTERVAL 6 HOUR), '家常菜,菜谱,烹饪', '', 'zhaoliu', DATE_SUB(NOW(), INTERVAL 7 HOUR)),
('2026必吃餐厅榜单发布', '米其林指南中国版新增15家三星餐厅', '<p>米其林指南2026中国版正式发布，新增15家三星餐厅，总数达到42家。北京和上海依然是高端餐饮的集中地，但成都、长沙等城市的餐厅也开始崭露头角。</p>', 8, '孙七', '', '1', '0', 5430, 312, 67, 89, '0', '0', DATE_SUB(NOW(), INTERVAL 14 HOUR), '米其林,餐厅,美食', '', 'sunqi', DATE_SUB(NOW(), INTERVAL 15 HOUR)),
('烘焙新手必看：6款经典蛋糕', '从戚风到芝士，手把手教你做蛋糕', '<p>本文为烘焙新手精选了6款经典蛋糕食谱，包括戚风蛋糕、芝士蛋糕、慕斯蛋糕、布朗尼、磅蛋糕和海绵蛋糕。每款食谱都标注了难度等级和所需时间。</p>', 8, '周八', '', '3', '0', 4320, 278, 56, 78, '0', '0', DATE_SUB(NOW(), INTERVAL 1 DAY), '烘焙,蛋糕,甜点', '', 'zhouba', DATE_SUB(NOW(), INTERVAL 1 DAY)),
('咖啡文化：从种子到杯中的旅程', '了解咖啡豆产地、烘焙和冲泡的完整知识体系', '<p>咖啡已经成为现代都市人生活中不可或缺的饮品。本文从咖啡豆的产地品种、烘焙程度、研磨粗细到冲泡方式，全面介绍咖啡文化，帮助你找到最适合自己口味的咖啡。</p>', 8, '吴九', '', '1', '0', 2890, 167, 34, 56, '0', '0', DATE_SUB(NOW(), INTERVAL 4 DAY), '咖啡,文化,冲泡', '', 'wujiu', DATE_SUB(NOW(), INTERVAL 4 DAY)),

-- 待审核和已驳回的内容（用于测试审核功能）
('【待审核】区块链技术在供应链中的应用', '本文探讨区块链技术如何提升供应链透明度和效率', '<p>区块链技术的去中心化和不可篡改特性，使其在供应链管理中具有天然优势。本文分析了沃尔玛、马士基等企业的区块链供应链实践案例。</p>', 1, '测试用户', '', '1', '1', 0, 0, 0, 0, '0', '0', NULL, '区块链,供应链', '', 'testuser', DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
('【待审核】周末露营装备清单', '新手露营必备装备推荐，从帐篷到炊具一应俱全', '<p>露营越来越受欢迎，但新手往往不知道该准备什么。本文整理了一份完整的露营装备清单，涵盖帐篷、睡袋、炊具、照明等必备物品。</p>', 6, '演示账号', '', '1', '1', 0, 0, 0, 0, '0', '0', NULL, '露营,户外,装备', '', 'demouser', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
('【已驳回】标题党测试内容', '这是一篇测试审核驳回功能的内容', '<p>此内容用于测试审核驳回功能。</p>', 2, '测试用户', '', '1', '2', 0, 0, 0, 0, '0', '0', NULL, '测试', '', 'testuser', DATE_SUB(NOW(), INTERVAL 2 HOUR)),

-- 视频和图片类型内容补充
('深圳无人机灯光秀全程录像', '千架无人机点亮深圳夜空，庆祝经济特区成立46周年', '<p>深圳经济特区成立46周年庆典上，1000架无人机在深圳湾上空进行了壮观的灯光秀表演，组成多种图案和文字，吸引数万市民驻足观看。</p>', 2, '吴九', '', '2', '0', 6780, 389, 78, 145, '0', '0', DATE_SUB(NOW(), INTERVAL 16 HOUR), '无人机,灯光秀,深圳', '', 'wujiu', DATE_SUB(NOW(), INTERVAL 17 HOUR)),
('故宫四季摄影作品集', '用镜头记录紫禁城的春夏秋冬', '<p>摄影师历时一年，用镜头记录了故宫在四季中的不同面貌。春日的海棠、夏日的荷塘、秋日的银杏、冬日的白雪，每一帧都是一幅画。</p>', 6, '郑十', '', '3', '0', 4560, 267, 56, 89, '0', '0', DATE_SUB(NOW(), INTERVAL 20 HOUR), '故宫,摄影,四季', '', 'zhengshi', DATE_SUB(NOW(), INTERVAL 21 HOUR)),
('古筝演奏《高山流水》', '经典古曲全新演绎，感受千年音韵之美', '<p>青年古筝演奏家重新演绎经典曲目《高山流水》，在保留传统韵味的基础上融入现代编曲元素，赋予这首千年古曲新的生命力。</p>', 7, '赵六', '', '4', '0', 2340, 145, 34, 45, '0', '0', DATE_SUB(NOW(), INTERVAL 3 DAY), '古筝,高山流水,民乐', '', 'zhaoliu', DATE_SUB(NOW(), INTERVAL 3 DAY)),
('成都街头美食探店Vlog', '打卡10家本地人推荐的苍蝇馆子', '<p>本视频带你走进成都街头巷尾，探访10家本地人强烈推荐的苍蝇馆子。从担担面到钵钵鸡，从兔头到冒菜，每一口都是地道的成都味道。</p>', 8, '孙七', '', '2', '0', 5670, 345, 78, 123, '0', '0', DATE_SUB(NOW(), INTERVAL 2 DAY), '成都,美食,Vlog', '', 'sunqi', DATE_SUB(NOW(), INTERVAL 2 DAY)),
('太空站日出延时摄影', '国际空间站拍摄的16分钟日出延时视频', '<p>国际空间站宇航员拍摄了一段令人震撼的日出延时摄影视频，16分钟内记录了从黑暗到光明的完整过程，地球大气层的色彩变化美轮美奂。</p>', 1, '王五', '', '2', '0', 8900, 523, 112, 198, '0', '0', DATE_SUB(NOW(), INTERVAL 8 DAY), '太空,延时摄影,日出', '', 'wangwu', DATE_SUB(NOW(), INTERVAL 8 DAY)),
('水彩画入门教程：画一朵玫瑰', '零基础也能学会的水彩花卉绘画技法', '<p>本教程从颜料选择、画笔使用到水彩技法，手把手教你画一朵逼真的水彩玫瑰。适合零基础绘画爱好者。</p>', 5, '郑十', '', '3', '0', 1890, 123, 23, 34, '0', '0', DATE_SUB(NOW(), INTERVAL 9 DAY), '水彩,绘画,教程', '', 'zhengshi', DATE_SUB(NOW(), INTERVAL 9 DAY));


-- ============================================================
-- 5. 更新分类的内容计数
-- ============================================================
UPDATE content_category c SET content_count = (
    SELECT COUNT(*) FROM content_info ci WHERE ci.category_id = c.category_id AND ci.status = '0'
);


-- ============================================================
-- 6. 评论数据（40条，含嵌套回复）
-- ============================================================
INSERT INTO content_comment (content_id, user_id, parent_id, content, status, create_by, create_time) VALUES
-- 对内容1(GPT-5)的评论
(1, 2, 0, 'GPT-5的推理能力确实提升很大，我测试了几个复杂的数学问题，准确率明显提高了', '0', 'lisi', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(1, 3, 0, '价格还是太贵了，希望API调用费用能降下来', '0', 'wangwu', DATE_SUB(NOW(), INTERVAL 50 MINUTE)),
(1, 5, 1, '同意，尤其是多步骤推理任务的表现非常惊艳', '0', 'sunqi', DATE_SUB(NOW(), INTERVAL 45 MINUTE)),
(1, 4, 2, 'API价格已经比刚发布时便宜很多了，相信会继续下降', '0', 'zhaoliu', DATE_SUB(NOW(), INTERVAL 40 MINUTE)),
-- 对内容2(量子计算)的评论
(2, 1, 0, '量子计算的发展速度超出预期，期待实用化的一天', '0', 'zhangsan', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(2, 6, 0, '离实际应用还有很长的路要走吧', '0', 'zhouba', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(2, 3, 5, '确实，但1000量子比特已经是一个重要里程碑了', '0', 'wangwu', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
-- 对内容8(暑期档票房)的评论
(8, 1, 0, '今年国产电影质量确实不错，长安三万里2看哭了', '0', 'zhangsan', DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(8, 3, 0, '流浪地球3的特效太震撼了，值得二刷', '0', 'wangwu', DATE_SUB(NOW(), INTERVAL 25 MINUTE)),
(8, 7, 7, '长安三万里2的叙事太棒了，国产动画的巅峰之作', '0', 'wujiu', DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
-- 对内容9(黑神话DLC)的评论
(9, 1, 0, 'DLC内容量太良心了，30小时剧情根本停不下来', '0', 'zhangsan', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(9, 2, 0, '首发150万在线，国产3A的里程碑', '0', 'lisi', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(9, 5, 9, '二郎神的Boss战设计绝了，打了20次才过', '0', 'sunqi', DATE_SUB(NOW(), INTERVAL 50 MINUTE)),
(9, 6, 10, '手残党表示普通难度就很满足了', '0', 'zhouba', DATE_SUB(NOW(), INTERVAL 45 MINUTE)),
-- 对内容13(世界杯预选赛)的评论
(13, 2, 0, '国足赢了日本！不敢相信！', '0', 'lisi', DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
(13, 4, 0, '武磊真的是国足的定海神针', '0', 'zhaoliu', DATE_SUB(NOW(), INTERVAL 15 MINUTE)),
(13, 5, 0, '看完比赛激动得睡不着觉', '0', 'sunqi', DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
(13, 7, 12, '希望这次真的能进世界杯', '0', 'wujiu', DATE_SUB(NOW(), INTERVAL 8 MINUTE)),
(13, 8, 13, '武磊这个赛季状态太好了', '0', 'zhengshi', DATE_SUB(NOW(), INTERVAL 5 MINUTE)),
-- 对内容15(两会数字经济)的评论
(15, 3, 0, '数字经济确实是未来的方向，政策支持很重要', '0', 'wangwu', DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(15, 6, 0, '55%的目标很有挑战性，但并非不可能', '0', 'zhouba', DATE_SUB(NOW(), INTERVAL 25 MINUTE)),
-- 对内容19(考研国家线)的评论
(19, 7, 0, '工学降15分，终于上岸了！', '0', 'wujiu', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(19, 8, 0, '分数线降了但竞争还是很激烈', '0', 'zhengshi', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(19, 9, 17, '恭喜上岸！', '0', 'testuser', DATE_SUB(NOW(), INTERVAL 50 MINUTE)),
-- 对内容27(格莱美)的评论
(27, 1, 0, 'Taylor Swift真的是现象级的存在', '0', 'zhangsan', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(27, 4, 0, '华语音乐人获得提名是好消息，国际化之路任重道远', '0', 'zhaoliu', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
-- 对内容31(家常菜)的评论
(31, 5, 0, '番茄炒蛋的教程太实用了，终于不糊锅了', '0', 'sunqi', DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(31, 8, 0, '红烧肉那个方子绝了，肥而不腻', '0', 'zhengshi', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
-- 其他评论
(3, 2, 0, '2499美元还是有点贵，等三代吧', '0', 'lisi', DATE_SUB(NOW(), INTERVAL 7 HOUR)),
(6, 3, 0, 'L4自动驾驶什么时候能真正商用呢', '0', 'wangwu', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(10, 4, 0, '抢票又要拼手速了', '0', 'zhaoliu', DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(14, 1, 0, '塔图姆今年确实打出了MVP级别的表现', '0', 'zhangsan', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(20, 6, 0, 'AI辅助学习确实方便，但要注意不能过度依赖', '0', 'zhouba', DATE_SUB(NOW(), INTERVAL 8 HOUR)),
(23, 7, 0, '极简生活尝试了一周，感觉心情确实好了很多', '0', 'wujiu', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(28, 8, 0, 'AI作曲的质量越来越高了，未来音乐人怎么办', '0', 'zhengshi', DATE_SUB(NOW(), INTERVAL 10 HOUR)),
(33, 9, 0, '烘焙新手跟着做了一次戚风，居然成功了！', '0', 'testuser', DATE_SUB(NOW(), INTERVAL 22 HOUR)),
(36, 10, 0, '延时摄影太美了，宇宙的壮阔令人敬畏', '0', 'demouser', DATE_SUB(NOW(), INTERVAL 7 DAY)),
(37, 1, 0, '水彩教程很详细，收藏了慢慢学', '0', 'zhangsan', DATE_SUB(NOW(), INTERVAL 8 DAY)),
(11, 5, 0, '鬼灭之刃的制作水准真的是业界天花板', '0', 'sunqi', DATE_SUB(NOW(), INTERVAL 9 HOUR)),
(16, 6, 0, '碳交易扩容对企业影响很大，需要提前做好准备', '0', 'zhouba', DATE_SUB(NOW(), INTERVAL 6 HOUR));


-- ============================================================
-- 7. 点赞数据（content_like表）
-- ============================================================
INSERT INTO content_like (content_id, user_id, create_time) VALUES
-- 内容1被多人点赞
(1, 2, DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(1, 3, DATE_SUB(NOW(), INTERVAL 50 MINUTE)),
(1, 4, DATE_SUB(NOW(), INTERVAL 40 MINUTE)),
(1, 5, DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(1, 6, DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
-- 内容9(黑神话DLC)被多人点赞
(9, 1, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(9, 2, DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(9, 3, DATE_SUB(NOW(), INTERVAL 50 MINUTE)),
(9, 5, DATE_SUB(NOW(), INTERVAL 40 MINUTE)),
(9, 7, DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
-- 内容13(世界杯)被多人点赞
(13, 1, DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(13, 2, DATE_SUB(NOW(), INTERVAL 25 MINUTE)),
(13, 3, DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
(13, 4, DATE_SUB(NOW(), INTERVAL 15 MINUTE)),
(13, 5, DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
(13, 7, DATE_SUB(NOW(), INTERVAL 5 MINUTE)),
-- 其他点赞
(8, 1, DATE_SUB(NOW(), INTERVAL 25 MINUTE)),
(8, 3, DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
(15, 3, DATE_SUB(NOW(), INTERVAL 25 MINUTE)),
(15, 6, DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
(19, 7, DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(19, 8, DATE_SUB(NOW(), INTERVAL 50 MINUTE)),
(27, 1, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(27, 4, DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(2, 1, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(2, 6, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(31, 5, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(31, 8, DATE_SUB(NOW(), INTERVAL 3 HOUR));


-- ============================================================
-- 8. 收藏数据（user_favorite表）
-- ============================================================
INSERT INTO user_favorite (user_id, content_id, favorite_time, create_by, create_time) VALUES
-- 张三的收藏
(1, 9, DATE_SUB(NOW(), INTERVAL 1 HOUR), 'zhangsan', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(1, 13, DATE_SUB(NOW(), INTERVAL 2 HOUR), 'zhangsan', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(1, 19, DATE_SUB(NOW(), INTERVAL 3 HOUR), 'zhangsan', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(1, 27, DATE_SUB(NOW(), INTERVAL 5 HOUR), 'zhangsan', DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(1, 31, DATE_SUB(NOW(), INTERVAL 6 HOUR), 'zhangsan', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
-- 李四的收藏
(2, 1, DATE_SUB(NOW(), INTERVAL 30 MINUTE), 'lisi', DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(2, 8, DATE_SUB(NOW(), INTERVAL 1 HOUR), 'lisi', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(2, 14, DATE_SUB(NOW(), INTERVAL 2 HOUR), 'lisi', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
-- 王五的收藏
(3, 1, DATE_SUB(NOW(), INTERVAL 40 MINUTE), 'wangwu', DATE_SUB(NOW(), INTERVAL 40 MINUTE)),
(3, 3, DATE_SUB(NOW(), INTERVAL 2 HOUR), 'wangwu', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(3, 9, DATE_SUB(NOW(), INTERVAL 3 HOUR), 'wangwu', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(3, 36, DATE_SUB(NOW(), INTERVAL 1 DAY), 'wangwu', DATE_SUB(NOW(), INTERVAL 1 DAY)),
-- 赵六的收藏
(4, 31, DATE_SUB(NOW(), INTERVAL 4 HOUR), 'zhaoliu', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(4, 33, DATE_SUB(NOW(), INTERVAL 5 HOUR), 'zhaoliu', DATE_SUB(NOW(), INTERVAL 5 HOUR)),
-- 孙七的收藏
(5, 19, DATE_SUB(NOW(), INTERVAL 1 HOUR), 'sunqi', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(5, 23, DATE_SUB(NOW(), INTERVAL 3 HOUR), 'sunqi', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(5, 34, DATE_SUB(NOW(), INTERVAL 5 HOUR), 'sunqi', DATE_SUB(NOW(), INTERVAL 5 HOUR)),
-- 周八的收藏
(6, 2, DATE_SUB(NOW(), INTERVAL 2 HOUR), 'zhouba', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(6, 15, DATE_SUB(NOW(), INTERVAL 4 HOUR), 'zhouba', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
-- 测试用户的收藏
(9, 1, DATE_SUB(NOW(), INTERVAL 10 MINUTE), 'testuser', DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
(9, 9, DATE_SUB(NOW(), INTERVAL 20 MINUTE), 'testuser', DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
(9, 13, DATE_SUB(NOW(), INTERVAL 30 MINUTE), 'testuser', DATE_SUB(NOW(), INTERVAL 30 MINUTE));


-- ============================================================
-- 9. 浏览历史数据（content_view_history表）
-- ============================================================
INSERT INTO content_view_history (user_id, content_id, view_time, create_time) VALUES
-- 张三的浏览历史（偏科技+体育）
(1, 1, DATE_SUB(NOW(), INTERVAL 10 MINUTE), DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
(1, 2, DATE_SUB(NOW(), INTERVAL 20 MINUTE), DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
(1, 3, DATE_SUB(NOW(), INTERVAL 30 MINUTE), DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(1, 6, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(1, 7, DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(1, 13, DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(1, 14, DATE_SUB(NOW(), INTERVAL 4 HOUR), DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(1, 15, DATE_SUB(NOW(), INTERVAL 5 HOUR), DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(1, 36, DATE_SUB(NOW(), INTERVAL 6 HOUR), DATE_SUB(NOW(), INTERVAL 6 HOUR)),
-- 李四的浏览历史（偏娱乐+体育）
(2, 8, DATE_SUB(NOW(), INTERVAL 15 MINUTE), DATE_SUB(NOW(), INTERVAL 15 MINUTE)),
(2, 9, DATE_SUB(NOW(), INTERVAL 25 MINUTE), DATE_SUB(NOW(), INTERVAL 25 MINUTE)),
(2, 10, DATE_SUB(NOW(), INTERVAL 35 MINUTE), DATE_SUB(NOW(), INTERVAL 35 MINUTE)),
(2, 11, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(2, 12, DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(2, 13, DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(2, 14, DATE_SUB(NOW(), INTERVAL 4 HOUR), DATE_SUB(NOW(), INTERVAL 4 HOUR)),
-- 王五的浏览历史（偏科技+娱乐）
(3, 1, DATE_SUB(NOW(), INTERVAL 5 MINUTE), DATE_SUB(NOW(), INTERVAL 5 MINUTE)),
(3, 3, DATE_SUB(NOW(), INTERVAL 15 MINUTE), DATE_SUB(NOW(), INTERVAL 15 MINUTE)),
(3, 9, DATE_SUB(NOW(), INTERVAL 30 MINUTE), DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(3, 27, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(3, 36, DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)),
-- 赵六的浏览历史（偏美食+生活）
(4, 31, DATE_SUB(NOW(), INTERVAL 10 MINUTE), DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
(4, 32, DATE_SUB(NOW(), INTERVAL 20 MINUTE), DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
(4, 33, DATE_SUB(NOW(), INTERVAL 30 MINUTE), DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(4, 34, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(4, 23, DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(4, 24, DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR)),
-- 孙七的浏览历史（偏教育+音乐）
(5, 19, DATE_SUB(NOW(), INTERVAL 10 MINUTE), DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
(5, 20, DATE_SUB(NOW(), INTERVAL 20 MINUTE), DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
(5, 21, DATE_SUB(NOW(), INTERVAL 30 MINUTE), DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(5, 27, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(5, 28, DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)),
-- 周八的浏览历史（偏资讯+体育）
(6, 15, DATE_SUB(NOW(), INTERVAL 10 MINUTE), DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
(6, 16, DATE_SUB(NOW(), INTERVAL 20 MINUTE), DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
(6, 13, DATE_SUB(NOW(), INTERVAL 30 MINUTE), DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(6, 14, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR)),
-- 测试用户的浏览历史
(9, 1, DATE_SUB(NOW(), INTERVAL 5 MINUTE), DATE_SUB(NOW(), INTERVAL 5 MINUTE)),
(9, 9, DATE_SUB(NOW(), INTERVAL 15 MINUTE), DATE_SUB(NOW(), INTERVAL 15 MINUTE)),
(9, 13, DATE_SUB(NOW(), INTERVAL 25 MINUTE), DATE_SUB(NOW(), INTERVAL 25 MINUTE)),
(9, 19, DATE_SUB(NOW(), INTERVAL 35 MINUTE), DATE_SUB(NOW(), INTERVAL 35 MINUTE)),
(9, 27, DATE_SUB(NOW(), INTERVAL 45 MINUTE), DATE_SUB(NOW(), INTERVAL 45 MINUTE));


-- ============================================================
-- 10. 好友关系数据（user_friend表）
-- status: 0=待确认, 1=已接受
-- ============================================================
INSERT INTO user_friend (user_id, friend_id, status, create_time) VALUES
-- 张三的好友（双向关系）
(1, 2, '1', DATE_SUB(NOW(), INTERVAL 10 DAY)),
(2, 1, '1', DATE_SUB(NOW(), INTERVAL 10 DAY)),
(1, 3, '1', DATE_SUB(NOW(), INTERVAL 8 DAY)),
(3, 1, '1', DATE_SUB(NOW(), INTERVAL 8 DAY)),
(1, 5, '1', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(5, 1, '1', DATE_SUB(NOW(), INTERVAL 5 DAY)),
-- 李四的好友
(2, 3, '1', DATE_SUB(NOW(), INTERVAL 7 DAY)),
(3, 2, '1', DATE_SUB(NOW(), INTERVAL 7 DAY)),
(2, 4, '1', DATE_SUB(NOW(), INTERVAL 6 DAY)),
(4, 2, '1', DATE_SUB(NOW(), INTERVAL 6 DAY)),
-- 王五的好友
(3, 5, '1', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(5, 3, '1', DATE_SUB(NOW(), INTERVAL 4 DAY)),
-- 待确认的好友请求
(1, 6, '0', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(7, 1, '0', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(9, 2, '0', DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(4, 5, '0', DATE_SUB(NOW(), INTERVAL 3 HOUR));


-- ============================================================
-- 11. 私信消息数据（user_message表）
-- ============================================================
INSERT INTO user_message (sender_id, receiver_id, content, is_read, create_time) VALUES
-- 张三和李四的对话
(1, 2, '老李，你看到GPT-5发布的消息了吗？', '1', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(2, 1, '看到了，推理能力提升太大了', '1', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(1, 2, '是啊，我们项目可以考虑接入试试', '1', DATE_SUB(NOW(), INTERVAL 50 MINUTE)),
(2, 1, '好主意，我研究一下API文档', '0', DATE_SUB(NOW(), INTERVAL 40 MINUTE)),
-- 张三和王五的对话
(1, 3, '世界杯看了吗？国足赢了日本！', '1', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(3, 1, '太激动了！武磊太强了', '1', DATE_SUB(NOW(), INTERVAL 55 MINUTE)),
(1, 3, '希望这次能进世界杯', '0', DATE_SUB(NOW(), INTERVAL 50 MINUTE)),
-- 李四和赵六的对话
(2, 4, '赵六，你那个家常菜教程写得不错', '1', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(4, 2, '谢谢，后面还会更新更多菜谱', '1', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
-- 孙七和周八的对话
(5, 6, '格莱美你看了吗？', '1', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(6, 5, '看了，Taylor Swift太强了', '1', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(5, 6, '华语音乐人获得提名是个好信号', '0', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
-- 测试用户和演示账号的对话
(9, 10, '你好，我看到你发的露营文章了', '1', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(10, 9, '谢谢关注，还在审核中', '1', DATE_SUB(NOW(), INTERVAL 50 MINUTE)),
(9, 10, '期待通过审核，内容很实用', '0', DATE_SUB(NOW(), INTERVAL 40 MINUTE)),
-- 吴九给张三发的消息（未读）
(7, 1, '张三，你那个量子计算的文章写得很好，能交流一下吗？', '0', DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
-- 郑十给李四发的消息（未读）
(8, 2, '李四，关于电影票房的数据分析能分享一下吗？', '0', DATE_SUB(NOW(), INTERVAL 20 MINUTE));


-- ============================================================
-- 12. 验证数据插入结果
-- ============================================================
SELECT '=== 数据统计 ===' AS info;
SELECT CONCAT('分类: ', COUNT(*), '条') AS stat FROM content_category;
SELECT CONCAT('用户: ', COUNT(*), '人') AS stat FROM content_user;
SELECT CONCAT('内容: ', COUNT(*), '条 (已发布:', SUM(IF(status='0',1,0)), ' 待审核:', SUM(IF(status='1',1,0)), ' 已驳回:', SUM(IF(status='2',1,0)), ')') AS stat FROM content_info;
SELECT CONCAT('评论: ', COUNT(*), '条') AS stat FROM content_comment;
SELECT CONCAT('点赞: ', COUNT(*), '条') AS stat FROM content_like;
SELECT CONCAT('收藏: ', COUNT(*), '条') AS stat FROM user_favorite;
SELECT CONCAT('浏览历史: ', COUNT(*), '条') AS stat FROM content_view_history;
SELECT CONCAT('好友关系: ', COUNT(*), '条 (已接受:', SUM(IF(status='1',1,0)), ' 待确认:', SUM(IF(status='0',1,0)), ')') AS stat FROM user_friend;
SELECT CONCAT('私信消息: ', COUNT(*), '条 (已读:', SUM(IF(is_read='1',1,0)), ' 未读:', SUM(IF(is_read='0',1,0)), ')') AS stat FROM user_message;

SELECT '=== 各分类内容数量 ===' AS info;
SELECT c.category_name, COUNT(ci.content_id) AS content_count
FROM content_category c
LEFT JOIN content_info ci ON c.category_id = ci.category_id AND ci.status = '0'
GROUP BY c.category_id, c.category_name
ORDER BY c.order_num;

SELECT '=== 各类型内容数量 ===' AS info;
SELECT CASE content_type WHEN '1' THEN '文章' WHEN '2' THEN '视频' WHEN '3' THEN '图片' WHEN '4' THEN '音频' END AS type_name,
       COUNT(*) AS count
FROM content_info WHERE status = '0'
GROUP BY content_type;
