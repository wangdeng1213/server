#Î´·Ö×é ×éID
SELECT
	CONCAT(
		"*4\r\n",
		'$',
		LENGTH(redis_cmd),
		'\r\n',
		redis_cmd,
		'\r\n',
		'$',
		LENGTH(redis_key),
		'\r\n',
		redis_key,
		'\r\n',
		'$',
		LENGTH(hkey),
		'\r\n',
		hkey,
		'\r\n',
		'$',
		LENGTH(hval),
		'\r\n',
		hval,
		'\r'
	)
FROM
	(
		SELECT
			'HSET' AS redis_cmd,
			'rel_groupdef' AS redis_key,
			CONCAT(institution_id,'#',group_type) AS hkey,
			group_id AS hval
		FROM
			linksus_relation_group
		where group_type in (2,4,5)
	) AS t;