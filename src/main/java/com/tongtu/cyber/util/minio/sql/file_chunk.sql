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

 Date: 21/03/2024 11:37:46
*/


-- ----------------------------
-- Table structure for file_chunk
-- ----------------------------
DROP TABLE IF EXISTS "public"."file_chunk";
CREATE TABLE "public"."file_chunk" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "file_name" varchar(255) COLLATE "pg_catalog"."default",
  "current_chunk_index" int4,
  "current_chunk_size" float4,
  "total_size" float8,
  "total_chunk" int4,
  "md5" varchar(45) COLLATE "pg_catalog"."default",
  "file_path" varchar(255) COLLATE "pg_catalog"."default",
  "file_type" varchar(255) COLLATE "pg_catalog"."default",
  "created_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar COLLATE "pg_catalog"."default",
  "update_time" timestamp(6),
  "created_time" timestamp(6),
  "chunk_size" float8,
  "complete_flag" int2
)
;
COMMENT ON COLUMN "public"."file_chunk"."file_name" IS '上传文件名';
COMMENT ON COLUMN "public"."file_chunk"."current_chunk_index" IS '当前分片下标';
COMMENT ON COLUMN "public"."file_chunk"."current_chunk_size" IS '当前分片大小';
COMMENT ON COLUMN "public"."file_chunk"."total_size" IS '文件总大小';
COMMENT ON COLUMN "public"."file_chunk"."total_chunk" IS '文件总分片数';
COMMENT ON COLUMN "public"."file_chunk"."md5" IS '文件唯一md5标识';
COMMENT ON COLUMN "public"."file_chunk"."file_path" IS '文件相对路径';
COMMENT ON COLUMN "public"."file_chunk"."file_type" IS '文件类型';
COMMENT ON COLUMN "public"."file_chunk"."chunk_size" IS '文件每片大小';
COMMENT ON COLUMN "public"."file_chunk"."complete_flag" IS '是否上传完成0未完成 1已完成';
COMMENT ON TABLE "public"."file_chunk" IS '文件分块记录';

-- ----------------------------
-- Primary Key structure for table file_chunk
-- ----------------------------
ALTER TABLE "public"."file_chunk" ADD CONSTRAINT "gh_train_file_chunk_copy1_pkey" PRIMARY KEY ("id");
