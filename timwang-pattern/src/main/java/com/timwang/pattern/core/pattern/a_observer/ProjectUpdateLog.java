package com.timwang.pattern.core.pattern.a_observer;

import java.io.Serializable;
import java.util.Date;

public class ProjectUpdateLog implements Serializable {
	private Integer id;

	private Integer projectId;

	private Integer type;

	private String newContent;

	private String oldContent;

	private Integer createUserId;

	private Integer createUserName;

	private Date createTime;

	private String note ;

	private static final long serialVersionUID = 1L;


	public static ProjectUpdateLog instance(Integer projectId, Integer type, String newContent, String note, Integer userId){
		ProjectUpdateLog log = new ProjectUpdateLog();
		log.setProjectId(projectId);
		log.setType(type);
		log.setNewContent(newContent);
		log.setNote(note);
		log.setCreateTime(new Date());
		log.setCreateUserId(userId);
		return log;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getNewContent() {
		return newContent;
	}

	public void setNewContent(String newContent) {
		this.newContent = newContent;
	}

	public String getOldContent() {
		return oldContent;
	}

	public void setOldContent(String oldContent) {
		this.oldContent = oldContent;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public Integer getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(Integer createUserName) {
		this.createUserName = createUserName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}