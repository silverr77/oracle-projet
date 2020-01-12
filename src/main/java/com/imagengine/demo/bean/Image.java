package com.imagengine.demo.bean;

public class Image {
	private int id;
	private String titre;
	private String path;
	
	public Image() {
		super();
	}
	public Image(int id, String titre, String path) {
		super();
		this.id = id;
		this.titre = titre;
		this.path = path;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
	
	
	
}
