mysql -h10.10.2.113 -pjava_cloud@123 -P7001 -ucloud_java  linksus_c3  --skip-column-names --raw  --default-character-set=utf8 < redis_load_1.sql | redis-cli -a linksus --pipe 
mysql -h10.10.2.113 -pjava_cloud@123 -P7001 -ucloud_java  linksus_c3  --skip-column-names --raw  --default-character-set=utf8 < redis_load_2.sql | redis-cli -a linksus --pipe 
mysql -h10.10.2.113 -pjava_cloud@123 -P7001 -ucloud_java  linksus_c3  --skip-column-names --raw  --default-character-set=utf8 < redis_load_3.sql | redis-cli -a linksus --pipe 
mysql -h10.10.2.113 -pjava_cloud@123 -P7001 -ucloud_java  linksus_c3  --skip-column-names --raw  --default-character-set=utf8 < redis_load_4.sql | redis-cli -a linksus --pipe 
mysql -h10.10.2.113 -pjava_cloud@123 -P7001 -ucloud_java  linksus_c3  --skip-column-names --raw  --default-character-set=utf8 < redis_load_5.sql | redis-cli -a linksus --pipe 