package com.smart_contacts.ENTITIES;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Todo {

	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	private String name;
	private String task;
	private String priority;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public Todo(Long id, String name, String task, String priority) {
		super();
		this.id = id;
		this.name = name;
		this.task = task;
		this.priority = priority;
	}
	public Todo() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Todo [id=" + id + ", name=" + name + ", task=" + task + ", priority=" + priority + "]";
	}
	
	
	
	
}
