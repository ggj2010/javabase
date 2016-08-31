CREATE TABLE `NewTable` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`page_id`  varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`image_url`  text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`tieba_name`  varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
PRIMARY KEY (`id`),
INDEX `index_tieba_image_page_id` (`page_id`) USING BTREE
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=2959
ROW_FORMAT=COMPACT
;