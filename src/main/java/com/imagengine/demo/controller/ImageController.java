package com.imagengine.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imagengine.demo.service.ImageService;

import oracle.ord.im.OrdImage;

@RestController
@RequestMapping("/images")
public class ImageController {

    ImageService imageService=new ImageService();

    @GetMapping("/")
    public String addImage() throws IOException{
        //imageService.insertNewImage();
        //imageService.saveImage();
        //imageService.getImage();
    	String path1="E:\\IRISI\\anime.png";
    	String path2="E:\\IRISI\\boku.jpg";
    	String path3="E:\\IRISI\\limilion.jpg";
    	String path4="E:\\IRISI\\limil2.png";
    	/*imageService.saveImage(path1);
    	imageService.saveImage(path2);
    	imageService.saveImage(path3);*/
    	List<OrdImage> imgs=null;
    	imgs=imageService.isSimilar(path1);
    	
    	imageService.ImageCreation(imgs);
    	
    	
    	//imageService.compareBetweenImages(path1,path4);
    	//imageService.getImage(6);
        return ""+imageService.getLastId();
    }

}
