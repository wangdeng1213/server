package com.linksus.entity;

import java.io.Serializable;

/**\
 * 更新直发表和转发表用
 * @author a
 *
 */
public class ResponseAndRecordDTO implements Serializable{

	private Long id; //微博ID
	private int comments;//评论数
	private int reposts;//转发数

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public int getComments(){
		return comments;
	}

	public void setComments(int comments){
		this.comments = comments;
	}

	public int getReposts(){
		return reposts;
	}

	public void setReposts(int reposts){
		this.reposts = reposts;
	}

}
