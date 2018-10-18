------------------------------------------------------------------------------------------------
----------------- table li_user_collect --------------------------------------------------------
---------------- @author DHJT 2018-10-18 -------------------------------------------------------
------------------------------------------------------------------------------------------------
-- li_user_collect
CREATE TABLE `li_user_collect` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL COMMENT '类型',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `aid` int(11) DEFAULT NULL COMMENT '文章id',
  `iscollect` int(11) DEFAULT '1' COMMENT '是否收藏',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

INSERT INTO li_user_collect (url,title,author,createtime,`type`,aid,iscollect) VALUES 
('/topic/item/23',' E井在线商城招聘前端开发工程师(15-30k) ','livichen','2016-08-23 19:00:48.000','topic',23,1)
,('/topic/item/27',' css3中display:box 和display:flex区别 ','asf1988','2016-08-29 19:31:08.000','topic',27,1)
,('/topic/item/27',' css3中display:box 和display:flex有区别？ ','bbq2013','2016-08-29 19:41:25.000','topic',27,1)
,('/topic/item/28',' div垂直水平居中的几种方法 ','bbt1991','2016-09-02 10:14:56.000','topic',28,1)
,('/topic/item/31.html',' 新手求解，thinkjs如何生成随机验证码，并返回至前台？ ','livisky','2016-09-02 16:32:09.000','topic',31,1)
,('/topic/item/34.html',' liblog v2.0功能更新(2016/9/18) ','livisky','2016-09-18 19:14:01.000','topic',34,1)
;