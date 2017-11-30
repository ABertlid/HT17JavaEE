package com.anneli.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Serie implements Serializable{

	private static final long serialVersionUID = -3141667461142743881L;
	private int id;
	private String title;
	
	public Serie() {
		
	}
	
	public Serie(int id, String title) {

		this.id = id;
		this.title = title;
	}

	public int getId() {
		System.out.println("i Serie-bönan och hämtar id: " +id);
		return id;
	}
	
	public String getTitle() {
		System.out.println("i Serie-bönan och hämtar titel: " +title);
		return title;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Serie [id=" + id + ", title=" + title + "]";
	}
	

}
