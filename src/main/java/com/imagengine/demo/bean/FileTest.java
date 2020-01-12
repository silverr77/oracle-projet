package com.imagengine.demo.bean;

import org.springframework.web.multipart.MultipartFile;

public class FileTest {
	
	private MultipartFile file;
	private String titre;
	public FileTest(MultipartFile file, String titre) {
		super();
		this.file = file;
		this.titre = titre;
	}
	public FileTest() {
		super();
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	};
	
	

}
