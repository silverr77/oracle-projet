package com.imagengine.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imagengine.demo.bean.Compare;
import com.imagengine.demo.bean.FileTest;
import com.imagengine.demo.bean.Image;
import com.imagengine.demo.service.ImageService;

@RestController
@CrossOrigin("*")
public class ImageController {

    ImageService imageService=new ImageService();
    
 
    @GetMapping("/images")
    public List<Image> getAllImages(){
    	return imageService.getAllImages();
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteImage(@PathVariable int id) {
    	imageService.deleteImage(id);
    }
    
    @PostMapping("/compare")
    public float ImageCompareScore(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2, @RequestParam("commande") String commande) throws IOException {
    	Compare compare = new Compare();
    	
    	Files.write(Paths.get(System.getProperty("user.dir")+"/images/compare1.png"),file1.getBytes());         
        compare.setPath1(System.getProperty("user.dir")+"/images/compare1.png");
        
        Files.write(Paths.get(System.getProperty("user.dir")+"/images/compare2.png"),file2.getBytes());         
        compare.setPath2(System.getProperty("user.dir")+"/images/compare2.png");
        
        compare.setCommande(commande);
        
    	return imageService.compareBetweenImages(compare);
    }
    
    @PostMapping("/recherche")
    public List<Image> getImagesRechercher(@RequestParam("file1") MultipartFile file1) throws IOException{
    	Compare compare = new Compare();
    	
    	Files.write(Paths.get(System.getProperty("user.dir")+"/images/recherche.png"),file1.getBytes());         
        compare.setPath1(System.getProperty("user.dir")+"/images/recherche.png");
    	return imageService.isSimilar(compare);
    }
    
    @PostMapping("/save")
    public Image SaveImage(@RequestParam("file") MultipartFile file) throws Exception {
    	Files.write(Paths.get(System.getProperty("user.dir")+"/images/image.png"),file.getBytes());
        Image image = new Image();
        image.setPath(System.getProperty("user.dir")+"/images/image.png");
        //image.setTitre(titre);
        imageService.saveImage(image);
    	return image;
    }
    
    
    

}
