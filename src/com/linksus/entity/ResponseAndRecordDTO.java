package com.linksus.entity;

import java.io.Serializable;

/**\
 * ����ֱ�����ת������
 * @author a
 *
 */
public class ResponseAndRecordDTO implements Serializable{

	private Long id; //΢��ID
	private int comments;//������
	private int reposts;//ת����

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
