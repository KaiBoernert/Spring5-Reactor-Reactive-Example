package de.oio.server.model;

import java.util.Date;

import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 *  The simple Database object, note that this is also use as a DTO in communication for simplicity reasons
 */
@XmlRootElement(name = "taskitem")
@Document
public class TaskItem {
	@Id
	private Long id = null;
	private String title;
	private String description;
	private Date dueDate;
	private boolean isDone;

	public TaskItem() {
	}

	public TaskItem(String title, String description, Date dueDate, boolean isDone) {
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.isDone = isDone;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean done) {
		isDone = done;
	}
}
