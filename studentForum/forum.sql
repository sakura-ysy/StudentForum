/*
 Navicat Premium Data Transfer

 Source Server         : hwk
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : forum

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 23/03/2022 12:50:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bms_billboard
-- ----------------------------
DROP TABLE IF EXISTS `bms_billboard`;
CREATE TABLE `bms_billboard`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公告',
  `create_time` datetime NULL DEFAULT NULL COMMENT '公告时间',
  `show` tinyint(1) NULL DEFAULT NULL COMMENT '1：展示中，0：过期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '全站公告' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bms_billboard
-- ----------------------------
INSERT INTO `bms_billboard` VALUES (2, 'R1.0 开始已实现护眼模式 ,妈妈再也不用担心我的眼睛了。', '2020-11-19 17:16:19', 0);
INSERT INTO `bms_billboard` VALUES (4, '系统已更新至最新版1.0.1', NULL, 1);

-- ----------------------------
-- Table structure for bms_comment
-- ----------------------------
DROP TABLE IF EXISTS `bms_comment`;
CREATE TABLE `bms_comment`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '内容',
  `user_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作者ID',
  `topic_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'topic_id',
  `create_time` datetime NOT NULL COMMENT '发布时间',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `parent_comment_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `replyTo_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_audited` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已审核，1-是，0-否',
  `is_pass` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否通过，1-是，0-否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bms_comment
-- ----------------------------
INSERT INTO `bms_comment` VALUES ('1427304110847279106', '黄豆自己给自己1号帖子评论', '1427303372125487106', '1349631541260595202', '2021-08-17 00:19:53', NULL, NULL, NULL, b'1', b'1');
INSERT INTO `bms_comment` VALUES ('1427304260596514818', '黄瓜自己给黄豆1号帖子评论', '1427303424550092802', '1349631541260595202', '2021-08-17 00:20:28', NULL, NULL, NULL, b'1', b'1');
INSERT INTO `bms_comment` VALUES ('1427304769046822914', '黄瓜给黄豆1号帖子的一级评论的再评论', '1427303424550092802', '1349631541260595202', '2021-08-17 00:22:30', NULL, '1427304260596514818', NULL, b'1', b'1');
INSERT INTO `bms_comment` VALUES ('1427311875086970882', '黄桃回复黄豆1号评论下黄瓜的评论5', '1427305164020236289', '1349631541260595202', '2021-08-17 00:50:44', NULL, '1427304260596514818', '1427303424550092802', b'1', b'1');
INSERT INTO `bms_comment` VALUES ('1506462230890172417', '1级评论：黄瓜第1次评论黄花1号帖子', '1427303424550092802', '1506457531038412801', '2022-03-23 10:45:59', NULL, NULL, NULL, b'1', b'1');
INSERT INTO `bms_comment` VALUES ('1506464836668628993', '2级评论：黄花回复黄瓜', '1506457058206134273', '1506457531038412801', '2022-03-23 10:56:20', NULL, '1506462230890172417', '1427303424550092802', b'1', b'1');

-- ----------------------------
-- Table structure for bms_follow
-- ----------------------------
DROP TABLE IF EXISTS `bms_follow`;
CREATE TABLE `bms_follow`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被关注人ID',
  `follower_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关注人ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 129 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户关注' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bms_follow
-- ----------------------------
INSERT INTO `bms_follow` VALUES (65, '1329723594994229250', '1317498859501797378');
INSERT INTO `bms_follow` VALUES (85, '1332912847614083073', '1332636310897664002');
INSERT INTO `bms_follow` VALUES (129, '1349290158897311745', '1349618748226658305');
INSERT INTO `bms_follow` VALUES (130, '1427303424550092802', '1506457058206134273');

-- ----------------------------
-- Table structure for bms_post
-- ----------------------------
DROP TABLE IF EXISTS `bms_post`;
CREATE TABLE `bms_post`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '标题',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'markdown内容',
  `user_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作者ID',
  `view` int NOT NULL DEFAULT 0 COMMENT '浏览统计',
  `top` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否置顶，1-是，0-否',
  `essence` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否加精，1-是，0-否',
  `section_id` int NULL DEFAULT 0 COMMENT '专栏ID',
  `is_audited` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已审核，1-是，0-否',
  `is_pass` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否通过，1-是，0-否',
  `create_time` datetime NOT NULL COMMENT '发布时间',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '话题表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bms_post
-- ----------------------------
INSERT INTO `bms_post` VALUES ('1333447953558765569', '1', '12\n2\n\n', '1427303372125487106', 0, 0, 0, 77, b'0', b'0', 0, b'1', b'1', '2020-12-01 00:29:01', '2020-12-03 23:56:51');
INSERT INTO `bms_post` VALUES ('1349362401438392322', '2021 健康，快乐', '2021的`FLAG`\n\n1. 技能进步\n2. 没有烦恼\n3. 发财 :smile:\n\n', '1427303372125487106', 0, 0, 0, 21, b'0', b'0', 0, b'1', b'1', '2021-01-13 22:27:21', '2021-01-14 17:30:13');
INSERT INTO `bms_post` VALUES ('1334481725322297346', 'hello，spring-security', ':hibiscus: spring-security\n\n', '1427303372125487106', 0, 0, 0, 46, b'0', b'0', 0, b'1', b'1', '2020-12-03 20:56:51', NULL);
INSERT INTO `bms_post` VALUES ('1332650453142827009', '哈哈哈，helloworld', '这是第一篇哦\n\n> hi :handshake: 你好\n\n`hello world`\n\n:+1: 很好\n', '1349290158897311745', 0, 0, 0, 29, b'0', b'0', 1, b'1', b'1', '2020-11-28 19:40:02', '2020-11-28 19:46:39');
INSERT INTO `bms_post` VALUES ('1333432347031646209', '哈哈哈，换了个dark主题', '主题更换为Dark\n\n', '1349290158897311745', 0, 0, 0, 6, b'0', b'0', 0, b'1', b'1', '2020-11-30 23:27:00', NULL);
INSERT INTO `bms_post` VALUES ('1333668258587750401', '嘿嘿，测试一下啊', '大家好\n`Hello everyone!`\n\n\n\n', '1427303424550092802', 0, 0, 0, 7, b'0', b'0', 0, b'1', b'1', '2020-12-01 15:04:26', '2020-12-01 16:49:14');
INSERT INTO `bms_post` VALUES ('1332682473151635458', '我要发财', '2021 冲冲冲！！！\n\n', '1427303424550092802', 0, 0, 0, 94, b'0', b'0', 2, b'1', b'1', '2020-11-28 21:47:16', '2020-11-30 19:40:22');
INSERT INTO `bms_post` VALUES ('1349631541260595202', '权限部分 OK', '1. 创建 ok\n2. 修改 ok\n3. 删除 ok\n\n', '1427305164020236289', 0, 0, 0, 17, b'0', b'0', 0, b'1', b'1', '2021-01-14 16:16:49', '2021-01-14 16:18:53');
INSERT INTO `bms_post` VALUES ('1333676096156528641', '测试', '测试\n\n', '1427305164020236289', 0, 0, 0, 38, b'0', b'0', 0, b'1', b'1', '2020-12-01 15:35:34', NULL);
INSERT INTO `bms_post` VALUES ('1332681213400817665', '聚合查询并统计', '* [x] SQL：\n\n```sql\nSELECT s.*,\nCOUNT(t.id) AS topics\nFROM section s\nLEFT JOIN topic t\nON s.id = t.section_id\nGROUP BY s.title\n```\n\n', '1427305164020236289', 0, 0, 0, 55, b'0', b'0', 1, b'1', b'1', '2020-11-28 21:42:16', '2020-11-29 15:00:42');
INSERT INTO `bms_post` VALUES ('1335149981733449729', '视频嵌入', ':+1:\n\n[https://www.bilibili.com/video/BV1w64y1f7w3](https://www.bilibili.com/video/BV1w64y1f7w3)\n\n[1](https://www.bilibili.com/video/BV1tp4y1x72w)\n\n```\n.vditor-reset pre > code\n```\n\n```\npublic class HelloWorld {\n\npublic static void main(String[] args) {\n    System.out.println(\"Hello World!\");\n}\n}\n```\n\n', '1427303424550092802', 0, 0, 0, 41, b'0', b'0', 0, b'0', b'0', '2020-12-05 17:12:16', '2021-01-14 13:06:16');
INSERT INTO `bms_post` VALUES ('1506457531038412801', '黄花1号帖子', '黄花11111', '1506457058206134273', 2, 1, 0, 0, b'0', b'0', 0, b'1', b'1', '2022-03-23 10:27:19', NULL);

-- ----------------------------
-- Table structure for bms_post_tag
-- ----------------------------
DROP TABLE IF EXISTS `bms_post_tag`;
CREATE TABLE `bms_post_tag`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tag_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标签ID',
  `topic_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '话题ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `tag_id`(`tag_id`) USING BTREE,
  INDEX `topic_id`(`topic_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '话题-标签 中间表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bms_post_tag
-- ----------------------------
INSERT INTO `bms_post_tag` VALUES (36, '1332650453377708034', '1332650453142827009');
INSERT INTO `bms_post_tag` VALUES (37, '1332681213568589825', '1332681213400817665');
INSERT INTO `bms_post_tag` VALUES (38, '1332681213631504385', '1332681213400817665');
INSERT INTO `bms_post_tag` VALUES (39, '1332682473218744321', '1332682473151635458');
INSERT INTO `bms_post_tag` VALUES (40, '1332913064463794178', '1332913064396685314');
INSERT INTO `bms_post_tag` VALUES (41, '1332913064530903041', '1332913064396685314');
INSERT INTO `bms_post_tag` VALUES (42, '1333432347107143681', '1333432347031646209');
INSERT INTO `bms_post_tag` VALUES (43, '1333432347107143682', '1333432347031646209');
INSERT INTO `bms_post_tag` VALUES (44, '1333447953697177602', '1333447953558765569');
INSERT INTO `bms_post_tag` VALUES (45, '1332913064463794178', '1333668258587750401');
INSERT INTO `bms_post_tag` VALUES (46, '1333676096320106498', '1333676096156528641');
INSERT INTO `bms_post_tag` VALUES (47, '1333695976742268930', '1333695976536748034');
INSERT INTO `bms_post_tag` VALUES (48, '1334481725519429634', '1334481725322297346');
INSERT INTO `bms_post_tag` VALUES (49, '1333447953697177602', '1335149981733449729');
INSERT INTO `bms_post_tag` VALUES (50, '1349362401597775874', '1349362401438392322');
INSERT INTO `bms_post_tag` VALUES (51, '1349631541306732545', '1349631541260595202');
INSERT INTO `bms_post_tag` VALUES (52, '1506457531105521666', '1506457531038412801');

-- ----------------------------
-- Table structure for bms_promotion
-- ----------------------------
DROP TABLE IF EXISTS `bms_promotion`;
CREATE TABLE `bms_promotion`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '广告标题',
  `link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '广告链接',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '广告推广表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bms_promotion
-- ----------------------------
INSERT INTO `bms_promotion` VALUES (1, '开发者头条', 'https://juejin.cn/', '开发者头条');
INSERT INTO `bms_promotion` VALUES (2, '并发编程网', 'https://juejin.cn/', '并发编程网');
INSERT INTO `bms_promotion` VALUES (3, '掘金', 'https://juejin.cn/', '掘金');

-- ----------------------------
-- Table structure for bms_tag
-- ----------------------------
DROP TABLE IF EXISTS `bms_tag`;
CREATE TABLE `bms_tag`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标签ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '标签',
  `topic_count` int NOT NULL DEFAULT 0 COMMENT '关联话题',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '标签表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bms_tag
-- ----------------------------
INSERT INTO `bms_tag` VALUES ('1332650453377708034', 'java', 1);
INSERT INTO `bms_tag` VALUES ('1332681213568589825', 'css', 1);
INSERT INTO `bms_tag` VALUES ('1332681213631504385', 'mongodb', 1);
INSERT INTO `bms_tag` VALUES ('1332682473218744321', 'python', 1);
INSERT INTO `bms_tag` VALUES ('1332913064463794178', 'vue', 2);
INSERT INTO `bms_tag` VALUES ('1332913064530903041', 'react', 1);
INSERT INTO `bms_tag` VALUES ('1333432347107143681', 'node', 1);
INSERT INTO `bms_tag` VALUES ('1333432347107143682', 'mysql', 1);
INSERT INTO `bms_tag` VALUES ('1333447953697177602', 'flask', 2);
INSERT INTO `bms_tag` VALUES ('1333676096320106498', 'spring', 1);
INSERT INTO `bms_tag` VALUES ('1333695976742268930', 'django', 1);
INSERT INTO `bms_tag` VALUES ('1334481725519429634', 'security', 1);
INSERT INTO `bms_tag` VALUES ('1349362401597775874', 'tensorflow', 1);
INSERT INTO `bms_tag` VALUES ('1349631541306732545', 'pytorch', 1);
INSERT INTO `bms_tag` VALUES ('1506457531105521666', '黄花', 1);

-- ----------------------------
-- Table structure for bms_tip
-- ----------------------------
DROP TABLE IF EXISTS `bms_tip`;
CREATE TABLE `bms_tip`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '内容',
  `author` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '作者',
  `type` tinyint NOT NULL COMMENT '1：使用，0：过期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '每日赠言' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bms_tip
-- ----------------------------
INSERT INTO `bms_tip` VALUES (1, '多锉出快锯，多做长知识。', '佚名', 1);
INSERT INTO `bms_tip` VALUES (2, '未来总留着什么给对它抱有信心的人。', '佚名', 1);
INSERT INTO `bms_tip` VALUES (3, '一个人的智慧不够用，两个人的智慧用不完。', '谚语', 1);
INSERT INTO `bms_tip` VALUES (4, '十个指头按不住十个跳蚤', '傣族', 1);
INSERT INTO `bms_tip` VALUES (5, '言不信者，行不果。', '墨子', 1);
INSERT INTO `bms_tip` VALUES (6, '攀援而登，箕踞而遨，则几数州之土壤，皆在衽席之下。', '柳宗元', 1);
INSERT INTO `bms_tip` VALUES (7, '美德大都包含在良好的习惯之内。', '帕利克', 1);
INSERT INTO `bms_tip` VALUES (8, '人有不及，可以情恕。', '《晋书》', 1);
INSERT INTO `bms_tip` VALUES (9, '明·吴惟顺', '法不传六耳', 1);
INSERT INTO `bms_tip` VALUES (10, '真正的朋友应该说真话，不管那话多么尖锐。', '奥斯特洛夫斯基', 1);
INSERT INTO `bms_tip` VALUES (11, '时间是一切财富中最宝贵的财富。', '德奥弗拉斯多', 1);
INSERT INTO `bms_tip` VALUES (12, '看人下菜碟', '民谚', 1);
INSERT INTO `bms_tip` VALUES (13, '如果不是怕别人反感，女人决不会保持完整的严肃。', '拉罗什福科', 1);
INSERT INTO `bms_tip` VALUES (14, '爱是春暖花开时对你满满的笑意', '佚名', 1);
INSERT INTO `bms_tip` VALUES (15, '希望是坚韧的拐杖，忍耐是旅行袋，携带它们，人可以登上永恒之旅。', '罗素', 1);
INSERT INTO `bms_tip` VALUES (18, '天国般的幸福，存在于对真爱的希望。', '佚名', 1);
INSERT INTO `bms_tip` VALUES (19, '我们现在必须完全保持党的纪律，否则一切都会陷入污泥中。', '马克思', 1);
INSERT INTO `bms_tip` VALUES (20, '在科学上没有平坦的大道，只有不畏劳苦沿着陡峭山路攀登的人，才有希望达到光辉的顶点。', '马克思', 1);
INSERT INTO `bms_tip` VALUES (21, '懒惰的马嫌路远', '蒙古', 1);
INSERT INTO `bms_tip` VALUES (22, '别忘记热水是由冷水烧成的', '非洲', 1);

-- ----------------------------
-- Table structure for certification
-- ----------------------------
DROP TABLE IF EXISTS `certification`;
CREATE TABLE `certification`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `user_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作者id',
  `achievement` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '成就名',
  `key_word` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '关键词',
  `tags` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标签',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '内容',
  `files` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '文件列表/JSON格式',
  `agree` int NULL DEFAULT NULL COMMENT '同意票',
  `disagree` int NULL DEFAULT NULL COMMENT '反对票',
  `num_limit` int NOT NULL COMMENT '票数限制',
  `num_sum` int NULL DEFAULT NULL COMMENT '总得票',
  `is_pass` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否通过',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of certification
-- ----------------------------
INSERT INTO `certification` VALUES ('1450301467108864001', '1349290158897311745', '黄瓜1号认证', '大创项目', '大创', '黄瓜要申请大创项目的认证', NULL, 0, 0, 20, 0, b'0', '2021-10-19 11:23:10', NULL);
INSERT INTO `certification` VALUES ('1450306024530604033', '1349290158897311745', '黄瓜2号认证', '大创项目', '大创;测试;', '黄瓜要申请大创项目的认证', NULL, 2, 0, 20, 2, b'0', '2021-10-19 11:41:16', NULL);

-- ----------------------------
-- Table structure for certification_tag_name
-- ----------------------------
DROP TABLE IF EXISTS `certification_tag_name`;
CREATE TABLE `certification_tag_name`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `certification_count` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of certification_tag_name
-- ----------------------------

-- ----------------------------
-- Table structure for certification_vote
-- ----------------------------
DROP TABLE IF EXISTS `certification_vote`;
CREATE TABLE `certification_vote`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `certification_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '该票投给的认证id',
  `voter_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '投票者id',
  `is_agree` bit(1) NOT NULL COMMENT '是否同意',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of certification_vote
-- ----------------------------
INSERT INTO `certification_vote` VALUES ('1450320831324323841', '1450306024530604033', '1349290158897311745', b'1');
INSERT INTO `certification_vote` VALUES ('1450321306727616514', '1450306024530604033', '1349290158897311745', b'1');
INSERT INTO `certification_vote` VALUES ('1450324184217350145', '1450306024530604033', '1450324004080381953', b'1');

-- ----------------------------
-- Table structure for cetification_to_tags
-- ----------------------------
DROP TABLE IF EXISTS `cetification_to_tags`;
CREATE TABLE `cetification_to_tags`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `certification_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '认证id',
  `tag_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '认证对应的tag',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cetification_to_tags
-- ----------------------------

-- ----------------------------
-- Table structure for company
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `user_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `company_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '企业表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of company
-- ----------------------------

-- ----------------------------
-- Table structure for exception_log
-- ----------------------------
DROP TABLE IF EXISTS `exception_log`;
CREATE TABLE `exception_log`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `requ_param` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求param',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '异常名',
  `message` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '异常信息',
  `user_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员ID',
  `user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员用户名',
  `method` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '方法',
  `url` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '请求url',
  `ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求ip',
  `create_time` datetime NOT NULL COMMENT '操作时间',
  `ver` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作版本号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '异常日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exception_log
-- ----------------------------
INSERT INTO `exception_log` VALUES ('1506460228533727233', '{}', 'java.lang.NullPointerException', 'java.lang.NullPointerException:null\n	com.example.backend.module.notify.controller.NotifyController.getPraiseNotify(NotifyController.java:37)\ncom.example.backend.module.notify.controller.NotifyController$$FastClassBySpringCGLIB$$bf4d1509.invoke(<generated>)\norg.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\norg.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:771)\norg.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\norg.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)\norg.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(AspectJAfterThrowingAdvice.java:62)\norg.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\norg.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)\norg.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:95)\norg.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\norg.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)\norg.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:691)\ncom.example.backend.module.notify.controller.NotifyController$$EnhancerBySpringCGLIB$$7e0c1ba4.getPraiseNotify(<generated>)\nsun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\nsun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\nsun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\njava.lang.reflect.Method.invoke(Method.java:498)\norg.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190)\norg.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138)\norg.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:105)\norg.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:878)\norg.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:792)\norg.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\norg.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1040)\norg.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:943)\norg.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\norg.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:626)\norg.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:733)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:92)\norg.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\norg.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\norg.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\norg.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202)\norg.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)\norg.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:542)\norg.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:143)\norg.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\norg.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)\norg.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)\norg.apache.coyote.http11.Http11Processor.service(Http11Processor.java:374)\norg.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)\norg.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:888)\norg.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1597)\norg.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\njava.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\njava.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)\norg.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\njava.lang.Thread.run(Thread.java:748)\n', NULL, NULL, 'com.example.backend.module.notify.controller.NotifyController.getPraiseNotify', '/api/notify/praise', '127.0.0.1', '2022-03-23 10:38:02', NULL);
INSERT INTO `exception_log` VALUES ('1506463625525612546', '{}', 'java.lang.NullPointerException', 'java.lang.NullPointerException:null\n	com.example.backend.module.notify.controller.NotifyController.getCommentNotify(NotifyController.java:54)\ncom.example.backend.module.notify.controller.NotifyController$$FastClassBySpringCGLIB$$bf4d1509.invoke(<generated>)\norg.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\norg.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:771)\norg.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\norg.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)\norg.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(AspectJAfterThrowingAdvice.java:62)\norg.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\norg.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)\norg.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:95)\norg.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\norg.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)\norg.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:691)\ncom.example.backend.module.notify.controller.NotifyController$$EnhancerBySpringCGLIB$$6b32d025.getCommentNotify(<generated>)\nsun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\nsun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\nsun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\njava.lang.reflect.Method.invoke(Method.java:498)\norg.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190)\norg.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138)\norg.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:105)\norg.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:878)\norg.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:792)\norg.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\norg.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1040)\norg.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:943)\norg.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\norg.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:626)\norg.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:733)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:92)\norg.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\norg.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\norg.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\norg.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202)\norg.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)\norg.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:542)\norg.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:143)\norg.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\norg.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)\norg.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)\norg.apache.coyote.http11.Http11Processor.service(Http11Processor.java:374)\norg.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)\norg.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:888)\norg.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1597)\norg.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\njava.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\njava.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)\norg.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\njava.lang.Thread.run(Thread.java:748)\n', NULL, NULL, 'com.example.backend.module.notify.controller.NotifyController.getCommentNotify', '/api/notify/comment', '127.0.0.1', '2022-03-23 10:51:32', NULL);
INSERT INTO `exception_log` VALUES ('1506466427127758849', '{\"notifyId\":\"1430089709954019330\"}', 'org.springframework.jdbc.UncategorizedSQLException', 'org.springframework.jdbc.UncategorizedSQLException:\r\n### Error updating database.  Cause: java.sql.SQLException: Incorrect integer value: \'UNREAD\' for column \'status\' at row 1\r\n### The error may exist in com/example/backend/module/notify/mapper/NotifyMapper.java (best guess)\r\n### The error may involve com.example.backend.module.notify.mapper.NotifyMapper.update-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE notify  SET notifier=?, receiver=?, target_id=?, type=?, status=?, create_time=?,  status=?      WHERE (id = ?)\r\n### Cause: java.sql.SQLException: Incorrect integer value: \'UNREAD\' for column \'status\' at row 1\n; uncategorized SQLException; SQL state [HY000]; error code [1366]; Incorrect integer value: \'UNREAD\' for column \'status\' at row 1; nested exception is java.sql.SQLException: Incorrect integer value: \'UNREAD\' for column \'status\' at row 1\n	org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:89)\norg.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:81)\norg.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:81)\norg.mybatis.spring.MyBatisExceptionTranslator.translateExceptionIfPossible(MyBatisExceptionTranslator.java:88)\norg.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:440)\ncom.sun.proxy.$Proxy96.update(Unknown Source)\norg.mybatis.spring.SqlSessionTemplate.update(SqlSessionTemplate.java:287)\ncom.baomidou.mybatisplus.core.override.MybatisMapperMethod.execute(MybatisMapperMethod.java:65)\ncom.baomidou.mybatisplus.core.override.MybatisMapperProxy$PlainMethodInvoker.invoke(MybatisMapperProxy.java:148)\ncom.baomidou.mybatisplus.core.override.MybatisMapperProxy.invoke(MybatisMapperProxy.java:89)\ncom.sun.proxy.$Proxy130.update(Unknown Source)\nsun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\nsun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\nsun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\njava.lang.reflect.Method.invoke(Method.java:498)\norg.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:344)\norg.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:198)\norg.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\norg.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:139)\norg.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\norg.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:212)\ncom.sun.proxy.$Proxy131.update(Unknown Source)\ncom.example.backend.module.notify.controller.NotifyController.readNotifyById(NotifyController.java:85)\ncom.example.backend.module.notify.controller.NotifyController$$FastClassBySpringCGLIB$$bf4d1509.invoke(<generated>)\norg.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\norg.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:771)\norg.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\norg.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)\norg.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(AspectJAfterThrowingAdvice.java:62)\norg.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\norg.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)\norg.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:95)\norg.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\norg.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)\norg.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:691)\ncom.example.backend.module.notify.controller.NotifyController$$EnhancerBySpringCGLIB$$6b32d025.readNotifyById(<generated>)\nsun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\nsun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\nsun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\njava.lang.reflect.Method.invoke(Method.java:498)\norg.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190)\norg.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138)\norg.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:105)\norg.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:878)\norg.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:792)\norg.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\norg.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1040)\norg.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:943)\norg.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\norg.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:626)\norg.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:733)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:92)\norg.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\norg.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\norg.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\norg.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202)\norg.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)\norg.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:542)\norg.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:143)\norg.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\norg.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)\norg.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)\norg.apache.coyote.http11.Http11Processor.service(Http11Processor.java:374)\norg.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)\norg.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:888)\norg.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1597)\norg.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\njava.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\njava.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)\norg.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\njava.lang.Thread.run(Thread.java:748)\n', '1427303424550092802', 'huanggua', 'com.example.backend.module.notify.controller.NotifyController.readNotifyById', '/api/notify/read/1506464836739932162', '127.0.0.1', '2022-03-23 11:02:40', NULL);
INSERT INTO `exception_log` VALUES ('1506468076881760257', '{\"actionId\":\"2\"}', 'java.lang.NullPointerException', 'java.lang.NullPointerException:null\n	com.example.backend.module.notify.controller.NotifyController.countNotify(NotifyController.java:96)\ncom.example.backend.module.notify.controller.NotifyController$$FastClassBySpringCGLIB$$bf4d1509.invoke(<generated>)\norg.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\norg.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:771)\norg.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\norg.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)\norg.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(AspectJAfterThrowingAdvice.java:62)\norg.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\norg.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)\norg.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:95)\norg.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\norg.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)\norg.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:691)\ncom.example.backend.module.notify.controller.NotifyController$$EnhancerBySpringCGLIB$$558e5cb1.countNotify(<generated>)\nsun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\nsun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\nsun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\njava.lang.reflect.Method.invoke(Method.java:498)\norg.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190)\norg.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138)\norg.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:105)\norg.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:878)\norg.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:792)\norg.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\norg.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1040)\norg.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:943)\norg.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\norg.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:626)\norg.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:733)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:92)\norg.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\norg.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\norg.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\norg.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\norg.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202)\norg.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)\norg.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:542)\norg.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:143)\norg.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\norg.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)\norg.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)\norg.apache.coyote.http11.Http11Processor.service(Http11Processor.java:374)\norg.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)\norg.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:888)\norg.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1597)\norg.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\njava.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\njava.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)\norg.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\njava.lang.Thread.run(Thread.java:748)\n', NULL, NULL, 'com.example.backend.module.notify.controller.NotifyController.countNotify', '/api/notify/count/unread/101', '127.0.0.1', '2022-03-23 11:09:13', NULL);

-- ----------------------------
-- Table structure for file_user
-- ----------------------------
DROP TABLE IF EXISTS `file_user`;
CREATE TABLE `file_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `file_url` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件url',
  `user_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `file_url`(`file_url`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色-权限 中间表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_user
-- ----------------------------

-- ----------------------------
-- Table structure for mission
-- ----------------------------
DROP TABLE IF EXISTS `mission`;
CREATE TABLE `mission`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务id',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '内容',
  `reward` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '奖励金额',
  `user_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发布人ID',
  `create_time` datetime NOT NULL COMMENT '发布时间',
  `dead_time` datetime NULL DEFAULT NULL COMMENT '截止时间',
  `now_sum` int NOT NULL DEFAULT 0 COMMENT '目前领取人数',
  `sum_limit` int NOT NULL DEFAULT 0 COMMENT '限制人数',
  `view` int NOT NULL DEFAULT 0 COMMENT '浏览统计',
  `is_audit` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否还在审核，1：是，0：否',
  `is_full` bit(1) NOT NULL DEFAULT b'0' COMMENT '领取人员是否已满，1：是，0：否',
  `is_over` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已结束，1：是，0：否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mission
-- ----------------------------

-- ----------------------------
-- Table structure for mission_receiver
-- ----------------------------
DROP TABLE IF EXISTS `mission_receiver`;
CREATE TABLE `mission_receiver`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务id',
  `mission_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务ID',
  `receiver_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接受者ID',
  `receiver_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接受者用户名',
  `is_finish` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否完成，1：是，0：否',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `mission_id`(`mission_id`) USING BTREE,
  INDEX `receiver_id`(`receiver_id`) USING BTREE,
  INDEX `receiver_name`(`receiver_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务-接受者 中间表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mission_receiver
-- ----------------------------

-- ----------------------------
-- Table structure for mission_tag
-- ----------------------------
DROP TABLE IF EXISTS `mission_tag`;
CREATE TABLE `mission_tag`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tag_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标签ID',
  `mission_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `tag_id`(`tag_id`) USING BTREE,
  INDEX `mission_id`(`mission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务-标签 中间表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mission_tag
-- ----------------------------

-- ----------------------------
-- Table structure for notify
-- ----------------------------
DROP TABLE IF EXISTS `notify`;
CREATE TABLE `notify`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通知主键',
  `notifier` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通知者',
  `receiver` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '被通知者',
  `target_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通知来源',
  `type` int NOT NULL COMMENT '通知来源枚举',
  `status` int NOT NULL COMMENT '通知状态',
  `create_time` datetime NOT NULL COMMENT '通知发布时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notify
-- ----------------------------
INSERT INTO `notify` VALUES ('1506459902283984898', '1427303424550092802', '1427303372125487106', '1349362401438392322', 101, 0, '2022-03-23 10:36:44');
INSERT INTO `notify` VALUES ('1506459945699225601', '1427303424550092802', '1506457058206134273', '1506457531038412801', 101, 0, '2022-03-23 10:36:54');
INSERT INTO `notify` VALUES ('1506462230990835714', '1427303424550092802', '1506457058206134273', '1506457531038412801', 103, 0, '2022-03-23 10:45:59');
INSERT INTO `notify` VALUES ('1506462468417802242', '1427303424550092802', '1506457058206134273', '1506457531038412801', 102, 0, '2022-03-23 10:46:56');
INSERT INTO `notify` VALUES ('1506464836739932162', '1506457058206134273', '1427303424550092802', '1506464836668628993', 201, 1, '2022-03-23 10:56:21');
INSERT INTO `notify` VALUES ('1506464836739932163', '1506457058206134273', '1506457058206134273', '1506464836668628993', 201, 0, '2022-03-23 10:56:21');
INSERT INTO `notify` VALUES ('1506465083432116225', '1506457058206134273', '1427303424550092802', '1427303424550092802', 301, 0, '2022-03-23 10:57:19');

-- ----------------------------
-- Table structure for oper_log
-- ----------------------------
DROP TABLE IF EXISTS `oper_log`;
CREATE TABLE `oper_log`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `module` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能模块',
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `des` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作描述',
  `method` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作方法',
  `ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求IP',
  `requ_param` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '请求参数',
  `resp_param` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '返回参数',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户ID',
  `user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户名',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求url',
  `create_time` datetime NOT NULL COMMENT '操作时间',
  `ver` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作版本号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oper_log
-- ----------------------------
INSERT INTO `oper_log` VALUES ('1450295696258252801', 'User', 'post', '用户登录', 'com.example.doubaotest.controller.UserController.loginSuccess', '127.0.0.1', '{}', '{\"code\":200,\"data\":{\"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyTmFtZSI6ImFkbWluIiwiZXhwIjoxNjM4MjEyNDEzfQ.g5eKu6kiLzqeIlHCi93M1SWtqWUmbEpHsP6RNc-MDj2K0F9xWeB6PfmubSFcWSA5V1sU5nlI8vr--kOJith6HQ\"},\"message\":\"登录成功\"}', '1349290158897311745', 'admin', '/user/login', '2021-10-19 11:00:14', NULL);
INSERT INTO `oper_log` VALUES ('1450296081932894210', 'User', 'post', '用户登录', 'com.example.doubaotest.controller.UserController.loginSuccess', '127.0.0.1', '{}', '{\"code\":200,\"data\":{\"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyTmFtZSI6ImFkbWluIiwiZXhwIjoxNjM4MjEyNTA1fQ.z2gLPYMJArf9Rt0nDGUJvgMpeIoF7Cq8-G94v1Sak8bejXNfqs4LQMZnGJNPvecCSpiuzK-z8DXxjkQh_FPy_Q\"},\"message\":\"登录成功\"}', '1349290158897311745', 'admin', '/user/login', '2021-10-19 11:01:46', NULL);
INSERT INTO `oper_log` VALUES ('1450298941521371137', 'User', 'post', '用户登录', 'com.example.doubaotest.controller.UserController.loginSuccess', '127.0.0.1', '{}', '{\"code\":200,\"data\":{\"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyTmFtZSI6ImFkbWluIiwiZXhwIjoxNjM4MjEzMTg3fQ.jzcMxGyCxCmW8hheYYi8WiW0Gw8u1wJaE3JxkSwS7yas3XYAsh27THsBL4Mcw2hw1L-4gsD8-AteIwot3sCuFw\"},\"message\":\"登录成功\"}', '1349290158897311745', 'admin', '/user/login', '2021-10-19 11:13:08', NULL);
INSERT INTO `oper_log` VALUES ('1450299062338297858', 'User', 'post', '用户登录', 'com.example.doubaotest.controller.UserController.loginSuccess', '127.0.0.1', '{}', '{\"code\":200,\"data\":{\"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyTmFtZSI6ImFkbWluIiwiZXhwIjoxNjM4MjEzMjE2fQ.6GIhJoH9Yb0Oy8RypxDX_UBfe7CzxJa4cVbYST4m4tp-getn_Ll68ml5X0xHTz4kyRla5FWtD6Dwjn_eUZ6FIg\"},\"message\":\"登录成功\"}', '1349290158897311745', 'admin', '/user/login', '2021-10-19 11:13:36', NULL);
INSERT INTO `oper_log` VALUES ('1450314196979720194', 'User', 'post', '用户登录', 'com.example.doubaotest.controller.UserController.loginSuccess', '127.0.0.1', '{}', '{\"code\":200,\"data\":{\"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyTmFtZSI6ImFkbWluIiwiZXhwIjoxNjM4MjE2ODI0fQ.xCxlJ05QT0ZhLk0g1GrhLgr0mPMdamFJxvDETEV587FW74IOKjZR8JZ7RfdkrH-2BDN_hK2pth-G6bJ32NffcA\"},\"message\":\"登录成功\"}', '1349290158897311745', 'admin', '/user/login', '2021-10-19 12:13:45', NULL);
INSERT INTO `oper_log` VALUES ('1450324073206706178', 'User', 'post', '用户登录', 'com.example.doubaotest.controller.UserController.loginSuccess', '127.0.0.1', '{}', '{\"code\":200,\"data\":{\"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyTmFtZSI6Imh1YW5nZ3VhIiwiZXhwIjoxNjM4MjE5MTc5fQ.XbJXr0W2Ud1AQ7a-JCgPUe0lQ-amZOAhxGglBhH9XHCF2ixfjpUYs4OuHWeieFJmbot0wsinhMTSQSqJha_GfA\"},\"message\":\"登录成功\"}', '1450324004080381953', 'huanggua', '/user/login', '2021-10-19 12:52:59', NULL);
INSERT INTO `oper_log` VALUES ('1506462231250882562', '123', 'get', '123', 'com.example.backend.module.user.controller.UserPostReactController.addFirstComment', '127.0.0.1', '{}', '{\"code\":200,\"data\":{\"content\":\"1级评论：黄瓜第1次评论黄花1号帖子\",\"createTime\":1648003559214,\"id\":\"1506462230890172417\",\"isAudited\":true,\"isPass\":true,\"topicId\":\"1506457531038412801\",\"userId\":\"1427303424550092802\"},\"message\":\"操作成功\"}', '1427303424550092802', 'huanggua', '/api/react/comment/addI', '2022-03-23 10:45:59', NULL);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `perm_name` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限名称',
  `perm_tag` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限tag',
  `url` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限对应url',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `perm_name`(`perm_name`) USING BTREE,
  INDEX `perm_tag`(`perm_tag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, '创建团队', 'teamForm', '/team/form');
INSERT INTO `permission` VALUES (2, '招募队友', 'teamRecruit', '/team/recruit');
INSERT INTO `permission` VALUES (3, '任务发布', 'missionRelease', '/mission/release');
INSERT INTO `permission` VALUES (4, '任务认证', 'missionAuthenticate', '/mission/authenticate');
INSERT INTO `permission` VALUES (5, '创建实验室', 'labForm', '/lab/form');
INSERT INTO `permission` VALUES (6, '实验室成员招募', 'labRecruit', '/lab/recruit');
INSERT INTO `permission` VALUES (7, '发布岗位', 'jobRelease', '/job/release');
INSERT INTO `permission` VALUES (8, '招聘', 'jobRecruit', '/job/recruit');
INSERT INTO `permission` VALUES (9, '接收任务', 'missionReceive', '/mission/receive');
INSERT INTO `permission` VALUES (10, '管理员最高权限', 'admin', '/admin');

-- ----------------------------
-- Table structure for post_collect
-- ----------------------------
DROP TABLE IF EXISTS `post_collect`;
CREATE TABLE `post_collect`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `post_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '帖子ID',
  `user_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `post_id`(`post_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '帖子-收藏 中间表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of post_collect
-- ----------------------------
INSERT INTO `post_collect` VALUES (52, '1506457531038412801', '1427303424550092802');

-- ----------------------------
-- Table structure for post_praise
-- ----------------------------
DROP TABLE IF EXISTS `post_praise`;
CREATE TABLE `post_praise`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `post_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '帖子ID',
  `user_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `post_id`(`post_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '帖子-点赞 中间表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of post_praise
-- ----------------------------
INSERT INTO `post_praise` VALUES (52, '1349362401438392322', '1427303424550092802');
INSERT INTO `post_praise` VALUES (53, '1506457531038412801', '1427303424550092802');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `user_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `school` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学校',
  `academy` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学院',
  `major` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专业',
  `student_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学号',
  `grade` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年级',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `username`(`username`) USING BTREE,
  INDEX `student_number`(`student_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '学生表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('1450324004080381954', '1450324004080381953', 'huanggua', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES ('1506457058227105794', '1506457058206134273', 'huanghua', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `user_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `school` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学校',
  `academy` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学院',
  `direction` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '研究方向',
  `work_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工号',
  `position` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职位',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `username`(`username`) USING BTREE,
  INDEX `work_number`(`work_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '教师表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teacher
-- ----------------------------

-- ----------------------------
-- Table structure for ums_user
-- ----------------------------
DROP TABLE IF EXISTS `ums_user`;
CREATE TABLE `ums_user`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  `username` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `alias` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '密码',
  `role` int NOT NULL DEFAULT 4 COMMENT '角色, 1学生 2教师 3 企业 4普通用户',
  `avatar` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `work_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学号/工号',
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `score` int NOT NULL DEFAULT 0 COMMENT '积分',
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'token',
  `bio` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个人简介',
  `active` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否激活，1：是，0：否',
  `status` bit(1) NULL DEFAULT b'1' COMMENT '状态，1：使用，0：停用',
  `role_id` int NULL DEFAULT NULL COMMENT '用户角色',
  `create_time` datetime NOT NULL COMMENT '加入时间',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_name`(`username`) USING BTREE,
  INDEX `user_email`(`email`) USING BTREE,
  INDEX `user_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ums_user
-- ----------------------------
INSERT INTO `ums_user` VALUES ('1349290158897311745', 'admin', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, 'https://s3.ax1x.com/2020/12/01/DfHNo4.jpg', 'admin@qq.com', 'U123456', NULL, 50, '', '自由职业者', b'1', b'1', 0, '2021-01-13 17:40:17', NULL);
INSERT INTO `ums_user` VALUES ('1427303372125487106', 'huangdou', 'huangdou', 'e10adc3949ba59abbe56e057f20f883e', 4, 'https://s3.ax1x.com/2020/12/01/DfHNo4.jpg', 'huanggua@qq.com', NULL, '1234567', 30, '', '叽里呱啦', b'1', b'1', 1, '2021-10-19 12:52:43', NULL);
INSERT INTO `ums_user` VALUES ('1427303424550092802', 'huanggua', 'huanggua', 'e10adc3949ba59abbe56e057f20f883e', 4, 'https://s3.ax1x.com/2020/12/01/DfHNo4.jpg', 'huanggua@qq.com', NULL, '123456', 35, '', '叽里呱啦', b'1', b'1', 1, '2021-10-19 12:52:43', NULL);
INSERT INTO `ums_user` VALUES ('1427305164020236289', 'huangtao', 'huangtao', 'e10adc3949ba59abbe56e057f20f883e', 4, 'https://s3.ax1x.com/2020/12/01/DfHNo4.jpg', 'huanggua@qq.com', NULL, '1234567', 40, '', '叽里呱啦', b'1', b'1', 1, '2021-10-19 12:52:43', NULL);
INSERT INTO `ums_user` VALUES ('1506457058206134273', 'huanghua', 'huanghua', 'e10adc3949ba59abbe56e057f20f883e', 4, 'https://s3.ax1x.com/2020/12/01/DfHNo4.jpg', 'huanghua@qq.com', NULL, '15560106663', 45, '', NULL, b'1', b'1', 1, '2022-03-23 10:25:26', NULL);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键 ',
  `user_id` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  `role_id` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户-角色 关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1349290158897311745', '0');
INSERT INTO `user_role` VALUES ('1450324004080381955', '1450324004080381953', '1');

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
`id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
`title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '标题',
`content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'markdown内容',
`user_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作者ID',
`reward` int NOT NULL DEFAULT 0 COMMENT '赏金',
`ans_num` int NOT NULL DEFAULT 0 COMMENT '回答数',
`view` int NOT NULL DEFAULT 0 COMMENT '浏览统计',
`is_solved` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已经解决，1-是，0-否',
`is_canceled` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否被撤销，1-是，0-否',
`is_audited` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已审核，1-是，0-否',
`is_pass` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否通过，1-是，0-否',
`create_time` datetime NOT NULL COMMENT '发布时间',
`modify_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
INDEX `user_id`(`user_id`) USING BTREE,
INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '问题表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES ('1507006107998052353', 'test1', '刘备为什么扛起RPG?','1427303372125487106',6,0,0,0,0,1,1,'2021-01-13 17:40:17','2021-01-13 17:40:17');
INSERT INTO `question` VALUES ('1507562785629872129', 'test2', '陈睿为什么要摸电线?','1427303424550092802',10,1,0,0,0,1,1,'2021-01-13 17:40:17','2021-01-13 17:40:17');

-- ----------------------------
-- Table structure for answer
-- ----------------------------
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer`  (
`id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
`content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'markdown内容',
`user_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作者ID',
`question_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所处问题的id',
`reply_to_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论的回答id',
`is_adopted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已经解决，1-是，0-否',
`is_audited` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已审核，1-是，0-否',
`is_pass` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否通过，1-是，0-否',
`create_time` datetime NOT NULL COMMENT '发布时间',
`modify_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
INDEX `user_id`(`user_id`) USING BTREE,
INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '回答表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of answer
-- ----------------------------
INSERT INTO `answer` VALUES ('1507380780434833409', '输麻了', '1349290158897311745','1507006107998052353',null,0,1,1,'2021-01-13 17:40:17','2021-01-13 17:40:17');
INSERT INTO `answer` VALUES ('1507549574302011394', '哈哈哈', '1349290158897311745','1507006107998052353','1507380780434833409',0,1,1,'2021-01-13 17:40:17','2021-01-13 17:40:17');
