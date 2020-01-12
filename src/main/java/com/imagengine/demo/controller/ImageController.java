package com.imagengine.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imagengine.demo.bean.Compare;
import com.imagengine.demo.bean.Image;
import com.imagengine.demo.service.ImageService;

@RestController
@RequestMapping("/images")
public class ImageController {

    ImageService imageService=new ImageService();
    
 
    @GetMapping("/")
    public List<Image> getAllImages(){
    	return imageService.getAllImages();
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteImage(@PathVariable int id) {
    	imageService.deleteImage(id);
    }
    
    @PostMapping("/compare")
    public float ImageCompareScore(@RequestBody Compare compare) {
    	//score
    	return imageService.compareBetweenImages(compare);
    }
    
    @PostMapping("/recherche")
    public List<Image> getImagesRechercher(@RequestBody Compare compare){
    	return imageService.isSimilar(compare);
    }
    
    @PostMapping("/save")
    public void saveImage(@RequestBody Image image) throws IOException {
    	imageService.saveImage(image);
    }
    
    
    

}
