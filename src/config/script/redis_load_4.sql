#Œ¢≤©≥ÿ¡–±Ì
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
			'weibo_pool' AS redis_key,
			CONCAT(mid,'#',weibo_type) AS hkey,
			CONCAT('1','#','1','#',src_mid) AS hval
		FROM
			linksus_weibo_pool
	) AS t;