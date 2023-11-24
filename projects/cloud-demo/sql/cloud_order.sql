/*
 Navicat Premium Data Transfer

 Source Server         : local-db
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : cloud_order

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 23/11/2023 16:03:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE `tb_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `num` int(11) NULL DEFAULT 0 COMMENT '商品数量',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ordername`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_order
-- ----------------------------
INSERT INTO `tb_order` VALUES (1, 1, 'Apple 苹果 iPhone 12', 699900.00, 1);
INSERT INTO `tb_order` VALUES (2, 2, '雅迪 yadea 新国标电动车', 20990.00, 1);
INSERT INTO `tb_order` VALUES (3, 3, '骆驼（CAMEL） 休闲运动服', 43900.00, 1);
INSERT INTO `tb_order` VALUES (4, 4, '小米14 Pro 双模5G 骁龙8第三代', 35900.00, 1);
INSERT INTO `tb_order` VALUES (5, 5, 'OPPO Reno3 Pro 双模5G', 299900.00, 1);
INSERT INTO `tb_order` VALUES (6, 6, '美的（Midea）新能效空调', 544900.00, 1);
INSERT INTO `tb_order` VALUES (7, 2, '西昊/SIHOO 人体工学椅', 79900.00, 1);
INSERT INTO `tb_order` VALUES (8, 3, '梵班（FAMDBANN）', 31900.00, 1);

SET FOREIGN_KEY_CHECKS = 1;
