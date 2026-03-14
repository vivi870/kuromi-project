/*
 Navicat Premium Data Transfer

 Source Server         : MySQL80
 Source Server Type    : MySQL
 Source Server Version : 80041
 Source Host           : localhost:3306
 Source Schema         : kuromi

 Target Server Type    : MySQL
 Target Server Version : 80041
 File Encoding         : 65001

 Date: 14/03/2026 16:25:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'BCrypt加密密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '管理员' COMMENT '显示名称',
  `role` tinyint NULL DEFAULT 1 COMMENT '1=普通管理员 2=超级管理员',
  `status` tinyint NULL DEFAULT 1 COMMENT '1=正常 0=禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '$2a$10$8kiqjyHN2TcxRpXZKfg/2OYpoIQ1VSHRpm0P3I3RU7p6l6zhnrAt2', '超级管理员', 2, 1, '2026-03-11 16:10:04', '2026-03-11 16:56:47');

-- ----------------------------
-- Table structure for hd
-- ----------------------------
DROP TABLE IF EXISTS `hd`;
CREATE TABLE `hd`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动标题',
  `time_range` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动时间范围',
  `img_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动图片路径',
  `img_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '活动详情多张图片，逗号分隔',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '活动信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hd
-- ----------------------------
INSERT INTO `hd` VALUES (2, '新品 | 三丽鸥白昼之月盲盒摆件', '2025.11.20 至 2025.11.25', 'hd2.jpg', 'hd2.jpg,hd2-1.jpg,hd2-2.jpg');
INSERT INTO `hd` VALUES (3, '【库洛米20周年限定系列】即将上新', '2025.11.23 至 2025.11.25', 'hd3.jpg', 'hd3.jpg,hd3-1.jpg,hd3-2.jpg,hd3-3.jpg,hd3-4.jpg,hg3-5.jpg');
INSERT INTO `hd` VALUES (4, '美乐蒂&库洛米主题派对限时高萌来袭！', '2025.10.30 至 2026.03.15', 'hd4.jpg', NULL);
INSERT INTO `hd` VALUES (5, '官宣！三丽鸥家族甜酷派对华南首展来厦门啦！', '2025.12.31 至 2026.01.31', 'hd5.jpg', NULL);
INSERT INTO `hd` VALUES (6, 'DQ x 美乐蒂库洛米梦幻限定联动来了', '2025.11.30 至 2025.12.15', 'hd6.jpg', NULL);
INSERT INTO `hd` VALUES (7, '美乐蒂&库洛米周年主题快闪即将启幕！', '2025.09.21 至 2025.10.31', 'hd7.jpg', NULL);
INSERT INTO `hd` VALUES (8, '香港最新打卡点！和库洛米宇宙能量大冒险', '2025.07.18 至 2025.09.01', 'hd8.jpg', NULL);
INSERT INTO `hd` VALUES (9, '周边可爱到犯规！DQ联名美乐蒂&库洛米限定', '2025.11.15 至 2025.12.20', 'hd9.jpg', NULL);
INSERT INTO `hd` VALUES (10, '库洛米周年主题快闪即将启幕！', '2025.09.21 至 2025.10.31', 'hd10.jpg', NULL);
INSERT INTO `hd` VALUES (11, '香港最新打卡点！和库洛米宇宙能量大冒险', '2025.07.18 至 2025.09.01', 'hd11.jpg', NULL);
INSERT INTO `hd` VALUES (12, 'DQ联名美乐蒂&库洛米限定', '2025.11.15 至 2025.12.20', 'hd12.jpg', NULL);
INSERT INTO `hd` VALUES (13, '可爱到犯规！DQ联名美乐蒂&库洛米限定', '2025.11.15 至 2025.12.20', 'hd13.jpg', NULL);
INSERT INTO `hd` VALUES (14, '库洛米', '2026.03.11-2026.03.13', '76760886-7c75-4e21-b0e5-a2fa3e1afcdb.jpg', '082a8781-c47d-4fa8-ac22-bca90d3d0685.jpg,34ce21cc-2bca-4e5a-9fa7-cef81ed4f479.jpg,71a5a175-7770-4657-ad01-db618544d450.jpg,410691b6-9095-479b-b109-def33985090c.jpg');

-- ----------------------------
-- Table structure for note
-- ----------------------------
DROP TABLE IF EXISTS `note`;
CREATE TABLE `note`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键（唯一标识每条数据）',
  `content_id` int NULL DEFAULT 0,
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '笔记详情内容',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容类型：gl（攻略）/tk（图库）/sp（视频）',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容标题',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '作者名称',
  `likes` int NOT NULL DEFAULT 0 COMMENT '点赞数',
  `liked` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已点赞（0=未点赞，1=已点赞）',
  `imgUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容图片地址',
  `userimg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户头像地址',
  `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `imgs` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '额外图片列表（逗号分隔）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE,
  INDEX `idx_author`(`author`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '库洛米笔记内容表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of note
-- ----------------------------
INSERT INTO `note` VALUES (1, 1, '1号位 黑森林陷落 142.2g\r\n2号位 迷失欢乐园 114.5g\r\n3号位 黑夜幽梦 104.8g\r\n4号位 游园幻梦 118.9g\r\n5号位 白巧异梦 139.8g\r\n6号位 隐藏今夜 好梦156.9g\r\n7号位 白日梦剧场 124.9g\r\n8号位 古堡夜游 126.9g', 'gl', '攻略分享|三丽鸥白昼之月盲盒（隐藏款）', '在逃库洛米', 971, 0, 'gl1.jpg', 'tx1.jpg', '2026-03-10 00:28:09', '2026-03-10 02:38:31', NULL);
INSERT INTO `note` VALUES (2, 2, '1号位 黑森林陷落 142.2g\r\n2号位 迷失欢乐园 114.5g\r\n3号位 黑夜幽梦 104.8g\r\n4号位 游园幻梦 118.9g\r\n5号位 白巧异梦 139.8g\r\n6号位 隐藏今夜 好梦156.9g\r\n7号位 白日梦剧场 124.9g\r\n8号位 古堡夜游 126.9g', 'gl', '百分百抽中库洛米白昼之月盲盒攻略（隐藏款）', '在逃库洛米', 713, 1, 'gl2.jpg', 'tx2.jpg', '2026-03-10 00:28:09', '2026-03-10 02:39:03', NULL);
INSERT INTO `note` VALUES (3, 3, 'miniso全新白昼之月系列拿下！\r\n真的别太好看了！\r\n隐藏款-今夜，好梦 156.3g（断层重）\r\n手感：上手沉甸甸，六面摇不动，闷声无打盒感。\r\n', 'gl', '【手感攻略】隐藏明盒三丽鸥库洛米白昼之月', '咕得', 134, 0, 'gl3.jpg', 'tx3.jpg', '2026-03-10 00:28:09', '2026-03-10 02:41:17', NULL);
INSERT INTO `note` VALUES (4, 4, 'miniso全新白昼之月系列拿下！\r\n真的别太好看了！\r\n隐藏款-今夜，好梦 156.3g（断层重）\r\n手感：上手沉甸甸，六面摇不动，闷声无打盒感。\r\nminiso全新白昼之月系列拿下！\r\n真的别太好看了！\r\n隐藏款-今夜，好梦 156.3g（断层重）\r\n手感：上手沉甸甸，六面摇不动，闷声无打盒感。\r\n', 'gl', '三丽鸥库洛米白昼之月盲盒重量手感攻略', '今天你购物了吗', 343, 0, 'gl4.jpg', 'tx4.jpg', '2026-03-10 00:28:09', '2026-03-10 02:50:44', NULL);
INSERT INTO `note` VALUES (5, 5, NULL, 'gl', '三丽鸥玫瑰之夜盲盒超详细手感攻略', '职业在逃丽人', 149, 0, 'gl5.jpg', 'tx5.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (6, 6, NULL, 'gl', '攻略分享|三丽鸥白昼之月盲盒（隐藏款）', '在逃库洛米', 971, 0, 'gl6.jpg', 'tx6.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (7, 7, NULL, 'gl', '百分百抽中库洛米白昼之月盲盒攻略（隐藏款）', '在逃库洛米', 713, 1, 'gl7.jpg', 'tx7.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (8, 8, NULL, 'gl', '【手感攻略】隐藏明盒三丽鸥库洛米白昼之月', '咕得', 134, 0, 'gl8.jpg', 'tx8.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (9, 9, 'miniso全新白昼之月系列拿下！\r\n真的别太好看了！\r\n隐藏款-今夜，好梦 156.3g（断层重）\r\n手感：上手沉甸甸，六面摇不动，闷声无打盒感。\r\nminiso全新白昼之月系列拿下！', 'gl', '三丽鸥库洛米白昼之月盲盒重量手感攻略', '今天你购物了吗', 344, 1, 'gl9.jpg', 'tx9.jpg', '2026-03-10 00:28:09', '2026-03-10 17:29:06', 'gl9-1.jpg, gl9-2.jpg, gl9-3.jpg');
INSERT INTO `note` VALUES (10, 10, '1号位 黑森林陷落 142.2g\r\n2号位 迷失欢乐园 114.5g\r\n3号位 黑夜幽梦 104.8g\r\n4号位 游园幻梦 118.9g\r\n5号位 白巧异梦 139.8g\r\n6号位 隐藏今夜 好梦156.9g\r\n7号位 白日梦剧场 124.9g\r\n8号位 古堡夜游 126.9g', 'gl', '三丽鸥玫瑰之夜盲盒超详细手感攻略', '职业在逃丽人', 150, 1, 'gl10.jpg', 'tx10.jpg', '2026-03-10 00:28:09', '2026-03-10 17:46:16', 'gl10-1.jpg, gl10-2.jpg, gl10-3.jpg,gl10-4.jpg');
INSERT INTO `note` VALUES (11, 11, '1号位 黑森林陷落 142.2g\r\n2号位 迷失欢乐园 114.5g\r\n3号位 黑夜幽梦 104.8g\r\n4号位 游园幻梦 118.9g\r\n5号位 白巧异梦 139.8g\r\n6号位 隐藏今夜 好梦156.9g\r\n7号位 白日梦剧场 124.9g\r\n8号位 古堡夜游 126.9g', 'gl', '三丽鸥玫瑰之夜盲盒超详细手感攻略', '职业在逃丽人', 150, 1, 'gl11.jpg', 'tx11.jpg', '2026-03-10 00:28:09', '2026-03-10 02:52:00', NULL);
INSERT INTO `note` VALUES (12, 1, NULL, 'tk', '库洛米头像', '名创优品线上购', 562, 0, 'tk1.jpg', 'tx1.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (13, 2, NULL, 'tk', '打个劫|库洛米', '苏蜜。', 297, 1, 'tk2.jpg', 'tx2.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (14, 3, NULL, 'tk', '姜被切成四块会变成什么', '泡芙小喵', 491, 0, 'tk3.jpg', 'tx3.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (15, 4, NULL, 'tk', '库洛米举牌', '糖醋兔丸', 750, 0, 'tk4.jpg', 'tx4.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (16, 5, NULL, 'tk', '库洛米贴纸|拾柒书秒变可爱风', '酥酥拾柒私人', 330, 0, 'tk5.jpg', 'tx5.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (17, 6, NULL, 'tk', '库洛米', '在逃库洛米', 971, 0, 'tk6.jpg', 'tx6.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (18, 7, NULL, 'tk', '打个劫|库洛米', '在逃库洛米', 713, 1, 'tk7.jpg', 'tx7.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (19, 8, NULL, 'tk', '姜被切成四块会变成什么', '咕得', 134, 0, 'tk8.jpg', 'tx8.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (20, 9, NULL, 'tk', '库洛米举牌', '今天你购物了吗', 343, 0, 'tk9.jpg', 'tx9.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (21, 10, NULL, 'tk', '库洛米贴纸|拾柒书秒变可爱风', '职业在逃丽人', 149, 0, 'tk10.jpg', 'tx10.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (22, 11, NULL, 'tk', '库洛米头像', '职业在逃丽人', 149, 0, 'tk11.jpg', 'tx11.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (23, 12, NULL, 'tk', '打个劫|库洛米', '职业在逃丽人', 149, 0, 'tk12.jpg', 'tx12.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (24, 13, NULL, 'tk', '姜被切成四块会变成什么', '职业在逃丽人', 149, 0, 'tk13.jpg', 'tx13.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (25, 14, NULL, 'tk', '库洛米举牌', '职业在逃丽人', 150, 1, 'tk14.jpg', 'tx14.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (26, 1, NULL, 'sp', '我宣布这次库库巨好看！美我一大跳啊啊啊啊', 'Raggeb', 2101, 0, 'sp1.jpg', 'tx1.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (27, 2, NULL, 'sp', '给自己一百只库洛米（64/100）', '狂暴小哆啦', 1286, 1, 'sp2.jpg', 'tx2.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (28, 3, NULL, 'sp', '20平库洛米主卧~今天爆改我的库洛米浴室', '等我吃饱再来', 527, 0, 'sp3.jpg', 'tx3.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (29, 4, NULL, 'sp', '入冬好物！这个库洛米棉拖可爱到爆炸！！', '菜心同学', 170, 0, 'sp4.jpg', 'tx4.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (30, 5, NULL, 'sp', '冬日必入！库洛米厚底拖鞋暖到心巴上', '职业在逃丽人', 149, 0, 'sp5.jpg', 'tx5.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (31, 6, NULL, 'sp', '库洛米', '杏仁hh', 9, 0, 'sp6.jpg', 'tx6.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (32, 7, NULL, 'sp', '又抽到库洛米', '饼干', 2312, 1, 'sp7.jpg', 'tx7.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (33, 8, NULL, 'sp', '你敢信这一整套库洛米文具只要...', '猪在你心里', 298, 0, 'sp8.jpg', 'tx8.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (34, 9, NULL, 'sp', '我宣布这次库库巨好看！美我一大跳啊啊啊啊', '今天你购物了吗', 343, 0, 'sp9.jpg', 'tx9.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (35, 10, NULL, 'sp', '给自己一百只库洛米（64/100）', '职业在逃丽人', 149, 0, 'sp10.jpg', 'tx10.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (36, 11, NULL, 'sp', '20平库洛米主卧~今天爆改我的库洛米浴室', '职业在逃丽人', 149, 0, 'sp11.jpg', 'tx11.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (37, 12, NULL, 'sp', '入冬好物！这个库洛米棉拖可爱到爆炸！！', '职业在逃丽人', 149, 0, 'sp12.jpg', 'tx12.jpg', '2026-03-10 00:28:09', '2026-03-10 00:28:09', NULL);
INSERT INTO `note` VALUES (40, 0, '库洛米来啦', 'sp', '测试', 'test2', 1, 1, 'd359f863-3cf4-4e08-90f1-ca5c2e7ed8a1.jpg', '7e5799cd-4f04-4bc9-ba57-66d7ddaf3acc.jpg', '2026-03-11 15:53:32', '2026-03-11 15:53:32', 'b942958d-7367-4b17-93e5-9ed98c0b68e6.jpg,8281416b-1e58-4c03-ba1b-ec83f6c9c291.jpg');

-- ----------------------------
-- Table structure for note_comment
-- ----------------------------
DROP TABLE IF EXISTS `note_comment`;
CREATE TABLE `note_comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `note_id` bigint NOT NULL COMMENT '关联笔记ID',
  `note_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联笔记类型（gl/tk/sp）',
  `user_id` bigint NOT NULL COMMENT '评论用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论用户名（冗余，避免关联查询）',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除 0-未删 1-已删',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_note_id_type`(`note_id`, `note_type`) USING BTREE COMMENT '联合索引，加速查询'
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '笔记评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of note_comment
-- ----------------------------
INSERT INTO `note_comment` VALUES (1, 11, 'gl', 0, 'test2', '哈哈', '2026-03-10 15:57:26', 0);
INSERT INTO `note_comment` VALUES (2, 9, 'gl', 0, 'test2', '哈哈', '2026-03-10 16:11:27', 0);
INSERT INTO `note_comment` VALUES (3, 11, 'gl', 0, 'test2', '111', '2026-03-10 16:12:14', 0);
INSERT INTO `note_comment` VALUES (4, 11, 'gl', 0, 'test2', '1111', '2026-03-10 16:12:16', 0);
INSERT INTO `note_comment` VALUES (5, 11, 'gl', 0, 'test2', '1111', '2026-03-10 16:12:18', 0);
INSERT INTO `note_comment` VALUES (6, 11, 'gl', 0, 'test2', '111', '2026-03-10 16:12:24', 0);
INSERT INTO `note_comment` VALUES (7, 11, 'gl', 0, 'test2', '222', '2026-03-10 16:15:21', 0);
INSERT INTO `note_comment` VALUES (8, 11, 'gl', 0, 'test', 'aa', '2026-03-10 18:08:38', 0);
INSERT INTO `note_comment` VALUES (9, 11, 'gl', 0, 'test', 'ahah', '2026-03-10 20:09:03', 0);
INSERT INTO `note_comment` VALUES (10, 11, 'gl', 0, 'test', 'asas', '2026-03-11 00:39:37', 0);
INSERT INTO `note_comment` VALUES (11, 11, 'gl', 0, 'sss', 'haha', '2026-03-11 00:48:31', 0);
INSERT INTO `note_comment` VALUES (12, 10, 'gl', 0, 'sss', 'oo', '2026-03-11 00:48:40', 0);
INSERT INTO `note_comment` VALUES (13, 10, 'gl', 0, 'sss', 'oo', '2026-03-11 00:48:58', 0);
INSERT INTO `note_comment` VALUES (14, 9, 'gl', 0, 'sss', 'ww', '2026-03-11 00:49:14', 0);
INSERT INTO `note_comment` VALUES (15, 10, 'gl', 0, 'sss', 'ss', '2026-03-11 00:52:14', 0);
INSERT INTO `note_comment` VALUES (16, 10, 'gl', 0, 'test', 'aa', '2026-03-11 00:56:33', 0);
INSERT INTO `note_comment` VALUES (17, 10, 'gl', 0, 'test', 'wa', '2026-03-11 00:57:59', 0);
INSERT INTO `note_comment` VALUES (18, 11, 'gl', 0, 'sss', 'haha', '2026-03-11 14:21:15', 0);
INSERT INTO `note_comment` VALUES (19, 11, 'gl', 0, 'asd', 'oo', '2026-03-11 14:29:31', 0);
INSERT INTO `note_comment` VALUES (20, 10, 'gl', 0, 'asd', 'haha', '2026-03-11 14:29:45', 0);
INSERT INTO `note_comment` VALUES (21, 25, 'tk', 0, 'asd', '哇哦', '2026-03-11 14:32:09', 0);
INSERT INTO `note_comment` VALUES (22, 25, 'tk', 0, 'asd', '好看', '2026-03-11 14:34:10', 0);
INSERT INTO `note_comment` VALUES (23, 11, 'gl', 0, 'test2', '记笔记', '2026-03-11 14:34:54', 0);
INSERT INTO `note_comment` VALUES (24, 40, 'sp', 0, 'test2', '哇哦', '2026-03-11 15:53:45', 0);
INSERT INTO `note_comment` VALUES (25, 11, 'gl', 0, 'test1', 'wao', '2026-03-11 17:16:27', 0);
INSERT INTO `note_comment` VALUES (26, 11, 'gl', 0, 'test1', 'oo', '2026-03-11 17:16:44', 0);
INSERT INTO `note_comment` VALUES (27, 11, 'gl', 0, 'test2', 'test', '2026-03-11 17:17:21', 0);
INSERT INTO `note_comment` VALUES (28, 11, 'gl', 0, 'sss', '好', '2026-03-14 16:06:25', 0);

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品唯一ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `category` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品分类：hot(销量排名)/mh(盲盒)/wo(玩偶)/zb(周边)/qt(其他)',
  `price` decimal(10, 2) NOT NULL COMMENT '商品基础价格（保留2位小数）',
  `sales` int UNSIGNED NULL DEFAULT 0 COMMENT '销量（前端显示“已售XX件”）',
  `stock` int NOT NULL DEFAULT 0 COMMENT '商品库存',
  `img_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '商品主图URL',
  `corner_img_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '角标图片URL（仅hot分类商品用）',
  `styles` json NULL COMMENT '商品款式（JSON格式，存储款式名称、图片、价格等）',
  `imgs` json NULL COMMENT '商品多图（JSON数组，存储多张图片URL）',
  `detail_imgs` json NULL COMMENT '商品详情图（JSON数组）',
  `sizes` json NULL COMMENT '商品尺寸（JSON格式，存储尺寸名称、价格等）',
  `is_deleted` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '是否删除：0-未删 1-已删（逻辑删除）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint NULL DEFAULT 1 COMMENT '1=上架 0=下架',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category`(`category`) USING BTREE COMMENT '按分类查询索引（提升查询速度）',
  INDEX `idx_name`(`name`) USING BTREE COMMENT '按名称搜索索引（提升搜索速度）'
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表（包含所有分类）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, '【生日礼物】 AOGER/澳捷尔 三丽鸥系列 库洛米 毛绒玩具 黑色制服连衣裙 38cm', 'hot', 59.00, 200, 99, 'hot1.jpg', 'ph1.png', NULL, '[\"hot1.jpg\", \"hot1-1.jpg\", \"hot1-2.jpg\", \"hot1-3.jpg\", \"hot1-4.jpg\"]', '[\"hot1--1.jpg\", \"hot1--2.jpg\", \"hot1--3.jpg\", \"hot1--4.jpg\", \"hot1--5.jpg\", \"hot1--6.jpg\", \"hot1--7.jpg\", \"hot1--8.jpg\"]', '[{\"label\": \"38cm\", \"price\": \"¥59\", \"value\": \"38cm\"}, {\"label\": \"50cm\", \"price\": \"¥79\", \"value\": \"50cm\"}, {\"label\": \"25cm\", \"price\": \"¥48\", \"value\": \"25cm\"}]', 0, '2026-03-06 17:31:13', '2026-03-09 18:24:12', 1);
INSERT INTO `product` VALUES (2, 'MINISO/名创优品 三丽鸥 岛屿孤系列 搪胶毛绒 盲盒', 'hot', 68.00, 200, 96, 'hot2.jpg', 'ph2.png', '[{\"img\": \"hot2.jpg\", \"label\": \"随机一款\", \"price\": \"¥76\", \"value\": \"随机一款\"}, {\"img\": \"hot2-1.jpg\", \"label\": \"库洛米\", \"price\": \"¥126\", \"value\": \"库洛米\"}, {\"img\": \"hot2-2.jpg\", \"label\": \"布丁狗\", \"price\": \"¥73.9\", \"value\": \"布丁狗\"}, {\"img\": \"hot2-3.jpg\", \"label\": \"凯蒂猫\", \"price\": \"¥85\", \"value\": \"凯蒂猫\"}, {\"img\": \"hot2-4.jpg\", \"label\": \"美乐蒂\", \"price\": \"¥135\", \"value\": \"美乐蒂\"}, {\"img\": \"hot2-5.jpg\", \"label\": \"大耳狗\", \"price\": \"¥96\", \"value\": \"大耳狗\"}, {\"img\": \"hot2-6.jpg\", \"label\": \"帕恰狗\", \"price\": \"¥89\", \"value\": \"帕恰狗\"}]', '[\"hot2.jpg\", \"hot2-1.jpg\", \"hot2-2.jpg\", \"hot2-3.jpg\", \"hot2-4.jpg\", \"hot2-5.jpg\", \"hot2-6.jpg\"]', '[\"hot2--1.jpg\", \"hot2--2.jpg\", \"hot2--3.jpg\", \"hot2--4.jpg\", \"hot2--5.jpg\", \"hot2--6.jpg\"]', NULL, 0, '2026-03-06 17:31:13', '2026-03-09 18:38:15', 1);
INSERT INTO `product` VALUES (3, 'Sanrio/三丽鸥 和风系列 库洛米挂件 周边', 'hot', 158.00, 200, 98, 'hot3.jpg', 'ph3.png', NULL, '[\"hot3.jpg\", \"hot3-1.jpg\", \"hot3-2.jpg\", \"hot3-3.jpg\", \"hot3-4.jpg\"]', '[\"hot3--1.jpg\", \"hot3--2.jpg\", \"hot3--3.jpg\"]', NULL, 0, '2026-03-06 17:31:13', '2026-03-09 14:06:30', 1);
INSERT INTO `product` VALUES (4, 'TOPTOY 三丽鸥 库洛米面包派对积木', 'hot', 179.00, 200, 100, 'hot4.jpg', 'ph4.png', NULL, '[\"hot4.jpg\", \"hot4-1.jpg\", \"hot4-2.jpg\", \"hot4-3.jpg\", \"hot4-4.jpg\"]', '[\"hot4--1.jpg\", \"hot4--2.jpg\", \"hot4--3.jpg\", \"hot4--4.jpg\", \"hot4--5.jpg\", \"hot4--6.jpg\", \"hot4--7.jpg\", \"hot4--8.jpg\"]', NULL, 0, '2026-03-06 17:31:13', '2026-03-09 14:06:31', 1);
INSERT INTO `product` VALUES (5, 'TOPTOY 库洛米闪闪偶像系列 摆件 盲盒', 'hot', 95.00, 200, 100, 'hot5.jpg', 'ph5.png', '[{\"img\": \"hot5.jpg\", \"label\": \"随机一款\", \"price\": \"¥31.8\", \"value\": \"随机一款\"}, {\"img\": \"hot5-1.jpg\", \"label\": \"妆点星梦\", \"price\": \"¥29.9\", \"value\": \"妆点星梦\"}, {\"img\": \"hot5-2.jpg\", \"label\": \"全能偶像\", \"price\": \"¥47\", \"value\": \"全能偶像\"}, {\"img\": \"hot5-3.jpg\", \"label\": \"练习生活\", \"price\": \"¥47\", \"value\": \"练习生活\"}, {\"img\": \"hot5-4.jpg\", \"label\": \"时尚宠儿\", \"price\": \"¥47\", \"value\": \"时尚宠儿\"}, {\"img\": \"hot5-5.jpg\", \"label\": \"梦的起点\", \"price\": \"¥47\", \"value\": \"梦的起点\"}, {\"img\": \"hot5-6.jpg\", \"label\": \"歌曲录制\", \"price\": \"¥47\", \"value\": \"歌曲录制\"}, {\"img\": \"hot5-7.jpg\", \"label\": \"粉心萌签\", \"price\": \"¥199\", \"value\": \"粉心萌签\"}]', '[\"hot5.jpg\", \"hot5-1.jpg\", \"hot5-2.jpg\", \"hot5-3.jpg\", \"hot5-4.jpg\", \"hot5-5.jpg\", \"hot5-6.jpg\", \"hot5-7.jpg\"]', '[\"hot5--1.jpg\", \"hot5--2.jpg\", \"hot5--3.jpg\", \"hot5--4.jpg\", \"hot5--5.jpg\", \"hot5--6.jpg\", \"hot5--7.jpg\", \"hot5--8.jpg\"]', NULL, 0, '2026-03-06 17:31:13', '2026-03-09 18:39:38', 1);
INSERT INTO `product` VALUES (6, '【新品】 TOPTOY 库洛米魔女的盛典系列 盲盒 单个随机', 'mh', 45.00, 200, 100, 'mh1.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 17:48:34', '2026-03-09 14:06:35', 1);
INSERT INTO `product` VALUES (7, 'TOPTOY 三丽鸥家族天使花园系列 毛绒挂饰 潮玩盲盒 随机一款', 'mh', 49.50, 200, 100, 'mh2.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 17:48:34', '2026-03-09 14:06:37', 1);
INSERT INTO `product` VALUES (8, 'TOPTOY 三丽鸥 库洛米 校园日记系列 盲盒 随机1个', 'mh', 29.00, 200, 100, 'mh3.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 17:48:34', '2026-03-09 14:06:39', 1);
INSERT INTO `product` VALUES (9, 'TOPTOYxSanrio/三丽鸥 蒸汽朋克系列 盲盒 单个盲盒', 'mh', 27.00, 200, 100, 'mh4.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 17:48:34', '2026-03-09 14:06:41', 1);
INSERT INTO `product` VALUES (10, 'TOPTOY 三丽鸥 浪漫婚礼系列 盲盒 随机1款', 'mh', 30.00, 200, 100, 'mh5.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 17:48:34', '2026-03-09 14:06:42', 1);
INSERT INTO `product` VALUES (11, 'TOPTOY 三丽鸥家族-库洛米扑克王国系列 盲盒 单个随机', 'mh', 29.90, 200, 100, 'mh1.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:14:51', '2026-03-09 14:06:44', 1);
INSERT INTO `product` VALUES (12, 'AOGER/澳捷尔 流金系列 库洛米 毛绒公仔 库洛米 23cm', 'wo', 48.50, 200, 100, 'wo1.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:12:50', '2026-03-09 14:06:46', 1);
INSERT INTO `product` VALUES (13, 'Sanrio/三丽鸥 白金系列 库洛米 毛绒玩具 库洛米', 'wo', 49.10, 200, 100, 'wo2.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:12:50', '2026-03-09 14:06:48', 1);
INSERT INTO `product` VALUES (14, 'AOGER/澳捷尔 洛丽塔系列 库洛米 毛绒玩具 库洛米', 'wo', 88.00, 200, 100, 'wo3.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:12:50', '2026-03-09 14:06:49', 1);
INSERT INTO `product` VALUES (15, 'AOGER/澳捷尔 宴会系列 库洛米 毛绒玩具 黑色 20cm', 'wo', 439.00, 200, 100, 'wo4.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:12:50', '2026-03-09 14:06:51', 1);
INSERT INTO `product` VALUES (16, 'AOGER/澳捷尔 花语精灵系列 库洛米 毛绒公仔 库洛米', 'wo', 45.90, 200, 100, 'wo5.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:12:50', '2026-03-09 14:06:52', 1);
INSERT INTO `product` VALUES (17, 'TOPTOY 甜美童话系列 库洛米 毛绒玩具 库洛米', 'wo', 158.00, 200, 100, 'wo6.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:12:50', '2026-03-09 14:06:53', 1);
INSERT INTO `product` VALUES (19, 'Sanrio/三丽鸥 猫猫系列 库洛米 毛绒挂件 周边 库洛米', 'zb', 278.00, 200, 100, 'zb1.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:21:43', '2026-03-09 14:06:55', 1);
INSERT INTO `product` VALUES (20, 'Sanrio/三丽鸥 日光浴系列 毛绒挂件 周边 库洛米 21cm', 'zb', 148.00, 200, 100, 'zb2.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:21:43', '2026-03-09 14:06:56', 1);
INSERT INTO `product` VALUES (21, 'Sanrio/三丽鸥 梦幻蕾丝 库洛米毛绒挂件 周边 库洛米', 'zb', 224.00, 200, 100, 'zb3.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:21:43', '2026-03-09 14:06:58', 1);
INSERT INTO `product` VALUES (22, 'Sanrio/三丽鸥 芭蕾系列 库洛米挂件 周边 默认配色', 'zb', 147.00, 200, 100, 'zb4.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:21:43', '2026-03-09 14:06:59', 1);
INSERT INTO `product` VALUES (23, 'Sanrio/三丽鸥 天使白熊系列 库洛米毛绒挂件 周边 库洛米', 'zb', 117.00, 200, 100, 'zb5.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:21:43', '2026-03-09 14:07:01', 1);
INSERT INTO `product` VALUES (24, 'Sanrio三丽鸥 库洛米 乐园太空杯 周边', 'zb', 79.00, 200, 100, 'zb6.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:21:43', '2026-03-09 14:07:03', 1);
INSERT INTO `product` VALUES (25, 'TOPTOY 三丽鸥 学院系列 灵感实验室 拼装模型 TC2069 500+粒', 'qt', 113.00, 200, 100, 'qt1.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:24:32', '2026-03-09 14:07:05', 1);
INSERT INTO `product` VALUES (26, 'AOGER/澳捷尔 三丽鸥宴会系列 库洛米抱枕 毛绒玩具 库洛米 35*26cm', 'qt', 44.00, 200, 100, 'qt2.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:24:32', '2026-03-09 14:07:06', 1);
INSERT INTO `product` VALUES (27, 'TOPTOY 街景系列 库洛米紫晶城堡 拼装积木 TC3201 1700粒', 'qt', 355.00, 200, 100, 'qt3.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:24:32', '2026-03-09 14:07:08', 1);
INSERT INTO `product` VALUES (28, 'MINISO/名创优品 三丽鸥 月牙甜梦系列 库洛米柔和可调节房间电视机造型长续航小夜灯 周边 库洛米', 'qt', 20.80, 200, 100, 'qt4.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:24:32', '2026-03-09 14:07:09', 1);
INSERT INTO `product` VALUES (29, 'Sanrio 三丽鸥x澳捷尔 库洛米/大耳狗 保暖手套 基础色', 'qt', 69.00, 200, 100, 'qt5.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:24:32', '2026-03-09 14:07:11', 1);
INSERT INTO `product` VALUES (30, 'Sanrio/三丽鸥 星空系列 3D水晶球灯发光摆件 周边 库洛米 单品', 'qt', 99.00, 200, 100, 'qt6.jpg', NULL, NULL, NULL, NULL, NULL, 0, '2026-03-06 18:24:32', '2026-03-11 18:57:41', 1);
INSERT INTO `product` VALUES (31, '测试', 'qt', 100.00, 0, 100, '17aa8d7c-0164-43eb-a0b3-5510ba987d6f.jpg', '', NULL, NULL, NULL, NULL, 0, '2026-03-11 18:47:49', '2026-03-11 18:57:55', 0);
INSERT INTO `product` VALUES (32, '测试', 'mh', 100.00, 0, 1000, '48f3471a-f307-4967-a32e-1d8cb38c19e0.jpg', '', NULL, NULL, NULL, NULL, 0, '2026-03-11 18:58:31', '2026-03-11 19:12:37', 0);
INSERT INTO `product` VALUES (33, '测试', 'mh', 12.00, 0, 100, 'b42c7661-beae-44c0-8e92-c250411ab1d8.jpg', '', NULL, NULL, NULL, NULL, 0, '2026-03-11 19:11:38', '2026-03-11 23:04:17', 0);
INSERT INTO `product` VALUES (34, '测试', 'mh', 100.00, 0, 99, 'e608e18d-85c0-4657-ac6b-0732b5da9e61.jpg', '', NULL, NULL, NULL, NULL, 0, '2026-03-11 23:04:53', '2026-03-11 23:04:53', 1);

-- ----------------------------
-- Table structure for sys_order
-- ----------------------------
DROP TABLE IF EXISTS `sys_order`;
CREATE TABLE `sys_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号（唯一）',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '下单用户（关联sys_user.username）',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `pay_status` tinyint NOT NULL DEFAULT 0 COMMENT '支付状态：0-待支付 1-待发货 2-已发货 3-已完成 4-已取消',
  `consignee` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '收货人',
  `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '收货电话',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '收货地址',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_order
-- ----------------------------
INSERT INTO `sys_order` VALUES (8, '202603091729565330est2', 'test2', 152.00, 4, 'asa', 'sasa', 'sas', '2026-03-09 17:29:56', '2026-03-09 17:29:56');
INSERT INTO `sys_order` VALUES (9, '202603091841188354test', 'test', 31.80, 4, 'asaas', 'asas', 'asas', '2026-03-09 18:41:19', '2026-03-09 18:41:19');
INSERT INTO `sys_order` VALUES (10, '202603091847222401test', 'test', 59.00, 0, 'asas', 'saas', 'asas', '2026-03-09 18:47:23', '2026-03-09 18:47:23');
INSERT INTO `sys_order` VALUES (11, '202603091848244447test', 'test', 76.00, 2, 'asas', 'saas', 'sass', '2026-03-09 18:48:24', '2026-03-13 00:15:25');
INSERT INTO `sys_order` VALUES (12, '202603092039315503test', 'test', 76.00, 1, 'assa', 'asas', 'asas', '2026-03-09 20:39:31', '2026-03-09 23:03:57');
INSERT INTO `sys_order` VALUES (13, '202603100003515392est2', 'test2', 177.00, 4, 'asaa', 'asas', 'asas', '2026-03-10 00:03:52', '2026-03-10 00:04:10');
INSERT INTO `sys_order` VALUES (14, '202603110058392973test', 'test', 158.00, 4, 'sss', 'sss', 'ss', '2026-03-11 00:58:39', '2026-03-11 00:58:39');
INSERT INTO `sys_order` VALUES (15, '202603111828344280sss', 'sss', 76.00, 3, 'asas', 'asas', 'asas', '2026-03-11 18:28:34', '2026-03-13 00:32:44');
INSERT INTO `sys_order` VALUES (16, '202603111836477406sss', 'sss', 158.00, 3, 'asas', 'asas', 'saasa', '2026-03-11 18:36:48', '2026-03-11 18:37:26');
INSERT INTO `sys_order` VALUES (17, '202603112320149874sss', 'sss', 100.00, 0, 'aaa', 'aa', 'aa', '2026-03-11 23:20:14', '2026-03-11 23:20:14');

-- ----------------------------
-- Table structure for sys_order_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_order_item`;
CREATE TABLE `sys_order_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  `order_id` bigint NOT NULL COMMENT '关联订单ID',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联订单编号',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '下单用户',
  `product_id` bigint NOT NULL COMMENT '商品ID（关联product.id）',
  `product_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `product_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '商品图片',
  `category` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '商品分类',
  `price` decimal(10, 2) NOT NULL COMMENT '商品单价',
  `quantity` int NOT NULL COMMENT '购买数量',
  `subtotal` decimal(10, 2) NOT NULL COMMENT '小计金额',
  `size` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '商品尺寸',
  `style` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '商品款式',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_order_item
-- ----------------------------
INSERT INTO `sys_order_item` VALUES (10, 8, '202603091729565330est2', 'test2', 2, 'MINISO/名创优品 三丽鸥 岛屿孤系列 搪胶毛绒 盲盒', '/src/assets/img/hot2.jpg', 'hot', 76.00, 2, 152.00, '', 'random', '2026-03-09 17:29:56');
INSERT INTO `sys_order_item` VALUES (11, 9, '202603091841188354test', 'test', 5, 'TOPTOY 库洛米闪闪偶像系列 摆件 盲盒', '/src/assets/img/hot5.jpg', 'hot', 31.80, 1, 31.80, '', '随机一款', '2026-03-09 18:41:19');
INSERT INTO `sys_order_item` VALUES (12, 10, '202603091847222401test', 'test', 1, '【生日礼物】 AOGER/澳捷尔 三丽鸥系列 库洛米 毛绒玩具 黑色制服连衣裙 38cm', '/src/assets/img/hot1.jpg', 'hot', 59.00, 1, 59.00, '38cm', '', '2026-03-09 18:47:23');
INSERT INTO `sys_order_item` VALUES (13, 11, '202603091848244447test', 'test', 2, 'MINISO/名创优品 三丽鸥 岛屿孤系列 搪胶毛绒 盲盒', '/src/assets/img/hot2.jpg', 'hot', 76.00, 1, 76.00, '', '随机一款', '2026-03-09 18:48:25');
INSERT INTO `sys_order_item` VALUES (14, 12, '202603092039315503test', 'test', 2, 'MINISO/名创优品 三丽鸥 岛屿孤系列 搪胶毛绒 盲盒', '/src/assets/img/hot2.jpg', 'hot', 76.00, 1, 76.00, '', '随机一款', '2026-03-09 20:39:31');
INSERT INTO `sys_order_item` VALUES (15, 13, '202603100003515392est2', 'test2', 1, '【生日礼物】 AOGER/澳捷尔 三丽鸥系列 库洛米 毛绒玩具 黑色制服连衣裙 38cm', '/src/assets/img/hot1.jpg', 'hot', 59.00, 3, 177.00, '38cm', '', '2026-03-10 00:03:52');
INSERT INTO `sys_order_item` VALUES (16, 14, '202603110058392973test', 'test', 3, 'Sanrio/三丽鸥 和风系列 库洛米挂件 周边', '/src/assets/img/hot3.jpg', 'hot', 158.00, 1, 158.00, '', '', '2026-03-11 00:58:39');
INSERT INTO `sys_order_item` VALUES (17, 15, '202603111828344280sss', 'sss', 2, 'MINISO/名创优品 三丽鸥 岛屿孤系列 搪胶毛绒 盲盒', '/src/assets/img/hot2.jpg', 'hot', 76.00, 1, 76.00, '', '随机一款', '2026-03-11 18:28:34');
INSERT INTO `sys_order_item` VALUES (18, 16, '202603111836477406sss', 'sss', 3, 'Sanrio/三丽鸥 和风系列 库洛米挂件 周边', '/src/assets/img/hot3.jpg', 'hot', 158.00, 1, 158.00, '', '', '2026-03-11 18:36:48');
INSERT INTO `sys_order_item` VALUES (19, 17, '202603112320149874sss', 'sss', 34, '测试', 'http://localhost:8080/api/spimg/e608e18d-85c0-4657-ac6b-0732b5da9e61.jpg', 'mh', 100.00, 1, 100.00, '', '', '2026-03-11 23:20:14');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名（唯一）',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码（建议加密存储）',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '昵称',
  `user_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像路径',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '邮箱',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_username_time` datetime NULL DEFAULT NULL COMMENT '用户名最后修改时间',
  `status` tinyint NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE COMMENT '用户名唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'testuser', '$2a$10$7V95R8bK9s8G7Z6X5C4V3B2N1M0L9K8J7H6G5F4D3S2A1Q0W9E8R7T6Y5U4I3O2P1', '测试用户', NULL, '', '2026-03-07 16:39:23', '2026-03-07 16:39:23', NULL, 1);
INSERT INTO `sys_user` VALUES (11, 'asd', '$2a$10$eof2plxc2MJnrz9pLoRJbOS1LZ/sKrG5nVAzbtiSkYWrT1tzqPXZm', 'sss', '/avatar/c72f8922-5fb0-49e5-9c79-962276347f67.jpg', '2304028859@qq.com', '2026-03-07 18:13:01', '2026-03-11 14:20:23', '2026-03-11 14:20:23', 1);
INSERT INTO `sys_user` VALUES (12, 'test1', '$2a$10$LUenfgQt6w6Dcoed2dnrFOka90nQtn4aU5bEbH8nNhqbE37IiWv0W', '新用户', NULL, '2304028859@qq.com', '2026-03-07 18:13:31', '2026-03-07 18:13:31', NULL, 0);
INSERT INTO `sys_user` VALUES (13, 'sss', '$2a$10$fL0LFrwVGymTgUJZ/19bCO8liLHoAVQNdQE8IhshYzow2EWmbIC3y', 'sss', '/avatar/5e9ea426-621b-486d-85e6-da9e64f20f79.jpg', '2304028859@qq.com', '2026-03-07 18:17:20', '2026-03-11 18:01:58', NULL, 1);
INSERT INTO `sys_user` VALUES (14, '123', '$2a$10$kaHFUA/alJtXVD7AapDmdOrvlNtWrxAI6Z57p1jXK1DeulztaV21i', '新用户', NULL, '2304028859@qq.com', '2026-03-13 18:13:01', '2026-03-13 18:13:01', NULL, 1);

-- ----------------------------
-- Table structure for user_audit
-- ----------------------------
DROP TABLE IF EXISTS `user_audit`;
CREATE TABLE `user_audit`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '申请用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请用户名',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '审核类型: nickname/avatar',
  `old_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '旧值',
  `new_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请新值',
  `status` tinyint NULL DEFAULT 0 COMMENT '0=待审核 1=通过 2=拒绝',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_audit
-- ----------------------------
INSERT INTO `user_audit` VALUES (1, 13, 'test2', '2', '', '/avatar/7798b2b1-3a45-4df7-a8e5-1b6550d27886.jpg', 1, '2026-03-11 17:48:19', '2026-03-11 17:48:19');
INSERT INTO `user_audit` VALUES (2, 13, 'test2', '1', '', 'sss', 2, '2026-03-11 17:48:50', '2026-03-11 17:48:50');
INSERT INTO `user_audit` VALUES (3, 13, 'test2', '2', '', '/avatar/5e9ea426-621b-486d-85e6-da9e64f20f79.jpg', 1, '2026-03-11 18:01:58', '2026-03-11 18:01:58');
INSERT INTO `user_audit` VALUES (4, 13, 'test2', '1', '', 'sss', 2, '2026-03-11 18:02:19', '2026-03-11 18:02:19');
INSERT INTO `user_audit` VALUES (5, 13, 'test2', '1', '', 'sss', 1, '2026-03-11 18:02:33', '2026-03-11 18:02:33');

-- ----------------------------
-- Table structure for user_cart
-- ----------------------------
DROP TABLE IF EXISTS `user_cart`;
CREATE TABLE `user_cart`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '数据库自增主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名（关联用户表，区分不同用户）',
  `product_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品ID（对应前端cartItem的id）',
  `category` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '商品分类',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `price` decimal(10, 2) NOT NULL COMMENT '商品单价',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '商品图片完整URL',
  `size` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '选中的尺寸',
  `style` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '选中的款式',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '商品数量',
  `subtotal` decimal(10, 2) NOT NULL COMMENT '小计（单价*数量）',
  `checked` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否选中（0=否，1=是）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_product_size_style`(`username`, `product_id`, `size`, `style`) USING BTREE COMMENT '唯一索引：同一用户的同一商品+尺寸+款式只能有一条记录'
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'kurimo商城购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_cart
-- ----------------------------
INSERT INTO `user_cart` VALUES (22, 'test2', '3', 'hot', 'Sanrio/三丽鸥 和风系列 库洛米挂件 周边', 158.00, '/src/assets/img/hot3.jpg', '', '', 1, 158.00, 0, '2026-03-09 14:57:20', '2026-03-09 17:51:01');

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '视频主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '视频标题',
  `user` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发布用户',
  `count` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '播放量（字符串类型，兼容“21万”“71.5万”等格式）',
  `date` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发布日期（字符串类型，兼容“2024年7月8日”“昨天 00：20”等格式）',
  `cover_img` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '封面图片名称（如spfm1.jpg）',
  `video_file` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '视频文件名称（如sp1.mp4）',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '删除标记（0=未删除，1=已删除）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '库洛米视频表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of video
-- ----------------------------
INSERT INTO `video` VALUES (1, '奇妙旋律美乐蒂第四季01（高清）', '山鹅雨花v粉色字幕版', '21万', '2024年7月8日', 'spfm1.jpg', 'sp1.mp4', 1, '2026-03-09 02:15:00');
INSERT INTO `video` VALUES (2, '美乐蒂&库洛米', '智微六世', '3937', '2025年12月24日', 'spfm2.jpg', 'sp2.mp4', 0, '2026-03-09 02:15:00');
INSERT INTO `video` VALUES (3, '库洛米变成美乐蒂之后…', 'GNxZM', '71.5万', '2022年4月9日', 'spfm3.jpg', 'sp3.mp4', 0, '2026-03-09 02:15:00');
INSERT INTO `video` VALUES (4, '多了两只一模一样的巴库', '微风清许', '520', '昨天 00：20', 'spfm4.jpg', 'sp4.mp4', 0, '2026-03-09 02:15:00');
INSERT INTO `video` VALUES (5, '✯奇幻魔法melody第四季:星光闪闪☆彡', '深蓝浅纸', '2.2万', '2025年8月24日', 'spfm5.jpg', 'sp5.mp4', 0, '2026-03-09 02:15:00');
INSERT INTO `video` VALUES (6, '奇幻魔法03 我的脸不管怎么看都那么可爱呢', 'sanrio漫剪', '3.4万', '2024年4月2日', 'spfm6.jpg', 'sp6.mp4', 0, '2026-03-09 02:15:00');
INSERT INTO `video` VALUES (7, '奇妙旋律美乐蒂第四季01（高清）', '山鹅雨花v粉色字幕版', '21万', '2024年7月8日', 'spfm1.jpg', 'sp1.mp4', 0, '2026-03-09 02:15:00');
INSERT INTO `video` VALUES (8, '美乐蒂&库洛米', '智微六世', '3937', '2025年12月24日', 'spfm2.jpg', 'sp2.mp4', 0, '2026-03-09 02:15:00');
INSERT INTO `video` VALUES (9, '库洛米变成美乐蒂之后…', 'GNxZM', '71.5万', '2022年4月9日', 'spfm3.jpg', 'sp3.mp4', 0, '2026-03-09 02:15:00');
INSERT INTO `video` VALUES (10, '多了两只一模一样的巴库', '微风清许', '520', '昨天 00：20', 'spfm4.jpg', 'sp4.mp4', 0, '2026-03-09 02:15:00');
INSERT INTO `video` VALUES (11, '✯奇幻魔法melody第四季:星光闪闪☆彡', '深蓝浅纸', '2.2万', '2025年8月24日', 'spfm5.jpg', 'sp5.mp4', 0, '2026-03-09 02:15:00');
INSERT INTO `video` VALUES (12, '奇幻魔法03 我的脸不管怎么看都那么可爱呢', 'sanrio漫剪', '3.4万', '2024年4月2日', 'spfm6.jpg', 'sp6.mp4', 0, '2026-03-09 02:15:00');
INSERT INTO `video` VALUES (13, '测试', 'test', '21万', '2026.03.12', '35441f4b-b4bf-4d41-9905-18ce402bdb7a.jpg', '20f21611-4511-4fbd-96c9-ed9e2d503add.mp4', 0, '2026-03-12 00:07:53');
INSERT INTO `video` VALUES (14, '库洛米', '我', '1万', '2026.03.12', 'fa475c01-f65b-42c7-b410-fb45cef093d7.jpg', '43923f12-b059-453b-8a3e-bea2551fb711.mp4', 0, '2026-03-12 00:13:32');

SET FOREIGN_KEY_CHECKS = 1;
