--数字切片 uploameta
CREATE TABLE `t_slide` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `wsi_batch_no` int(11) NOT NULL,
  `case_id` varchar(45) DEFAULT NULL,
  `slide_id` varchar(45) DEFAULT NULL,
  `file` varchar(100) DEFAULT '',
  `title` varchar(45) DEFAULT '',
  `expiry` int(11) DEFAULT 365,
  `md5sum` varchar(32) DEFAULT NULL,
  `model` varchar(45) DEFAULT 'unknown',
  `vendor` varchar(45) DEFAULT NULL,
  `fileSize` bigint(20) DEFAULT NULL,
  `local_filepath` varchar(300) DEFAULT '' COMMENT 'WSI所在的文件路径',
  `state` varchar(45) DEFAULT '' COMMENT '''0:未上传;1:已上传''',
  `remark` varchar(1000) DEFAULT '',
  `folder` varchar(200) DEFAULT '',
  `ingest_cmd` varchar(2000) DEFAULT '',
  `rdv_url` varchar(2000) DEFAULT NULL,
  `add_timestamp` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `update_timestamp` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `upload_fdt_cmd` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='DPMS的uploadmeta'

--数字切片批号
CREATE TABLE `t_wsi_batch` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_no` int(11) NOT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `add_timestamp` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '导入日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_wsi_batch_uni` (`batch_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8