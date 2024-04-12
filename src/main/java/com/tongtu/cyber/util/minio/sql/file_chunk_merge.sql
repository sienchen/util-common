/*
 Navicat Premium Data Transfer

 Source Server         : 10.253.129.6(开发)
 Source Server Type    : PostgreSQL
 Source Server Version : 100014
 Source Host           : 10.253.129.6:5432
 Source Catalog        : xytocc_gh
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 100014
 File Encoding         : 65001

 Date: 21/03/2024 11:37:58
*/


-- ----------------------------
-- Table structure for file_chunk_merge
-- ----------------------------
DROP TABLE IF EXISTS "public"."file_chunk_merge";
CREATE TABLE "public"."file_chunk_merge" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "file_real_name" varchar(255) COLLATE "pg_catalog"."default",
  "file_name" varchar(255) COLLATE "pg_catalog"."default",
  "suffix" varchar(32) COLLATE "pg_catalog"."default",
  "file_path" varchar(255) COLLATE "pg_catalog"."default",
  "file_type" varchar(32) COLLATE "pg_catalog"."default",
  "total_size" varchar(100) COLLATE "pg_catalog"."default",
  "md5" varchar(255) COLLATE "pg_catalog"."default",
  "created_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "created_time" timestamp(6),
  "update_time" timestamp(6),
  "material_duration" int4,
  "upload_ids" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."file_chunk_merge"."id" IS 'ID';
COMMENT ON COLUMN "public"."file_chunk_merge"."file_real_name" IS '文件真实名称';
COMMENT ON COLUMN "public"."file_chunk_merge"."file_name" IS '文件名';
COMMENT ON COLUMN "public"."file_chunk_merge"."suffix" IS '文件后缀';
COMMENT ON COLUMN "public"."file_chunk_merge"."file_path" IS '文件路径';
COMMENT ON COLUMN "public"."file_chunk_merge"."file_type" IS '文件类型';
COMMENT ON COLUMN "public"."file_chunk_merge"."total_size" IS '文件总大小';
COMMENT ON COLUMN "public"."file_chunk_merge"."md5" IS 'md5校验码
';
COMMENT ON COLUMN "public"."file_chunk_merge"."material_duration" IS '文件时长(秒)(页数)';
COMMENT ON COLUMN "public"."file_chunk_merge"."upload_ids" IS '文件缩略图(base64)';
COMMENT ON TABLE "public"."file_chunk_merge" IS '文件分片合并记录';

-- ----------------------------
-- Primary Key structure for table file_chunk_merge
-- ----------------------------
ALTER TABLE "public"."file_chunk_merge" ADD CONSTRAINT "gh_train_material_copy1_pkey" PRIMARY KEY ("id");
