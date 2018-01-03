#”√ªß±Ì
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
			'relation_weibouser' AS redis_key,
			rps_id AS hkey,
			CONCAT(user_id,'#',person_id,'#',case when user_type=2 then rps_name else 's' end ) AS hval
		FROM
			linksus_relation_weibouser
	) AS t;