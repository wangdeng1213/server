#账号用户关系
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
			'relation_user_account' AS redis_key,
			CONCAT(account_id,'#',user_id) AS hkey,
			CONCAT(flag_relation,'#',interact_type) AS hval
		FROM
			linksus_relation_user_account
	) AS t;