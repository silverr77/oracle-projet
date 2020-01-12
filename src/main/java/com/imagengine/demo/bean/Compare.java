package com.imagengine.demo.bean;

public class Compare {
	private String path1;
	private String path2;
	private String Commande;
	
	
	
	
	
	public Compare() {
		super();
	}
	public Compare(String path1, String path2, String commande) {
		super();
		this.path1 = path1;
		this.path2 = path2;
		Commande = commande;
	}
	public String getPath1() {
		return path1;
	}
	public void setPath1(String path1) {
		this.path1 = path1;
	}
	public String getPath2() {
		return path2;
	}
	public void setPath2(String path2) {
		this.path2 = path2;
	}
	public String getCommande() {
		return Commande;
	}
	public void setCommande(String commande) {
		Commande = commande;
	}
	
	
	
	
}
