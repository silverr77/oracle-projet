package com.imagengine.demo.controller;

import com.imagengine.demo.service.ImageService;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
public class ImageController {

    ImageService imageService=new ImageService();

    @GetMapping("/")
    public String addImage() throws IOException{
        //imageService.insertNewImage();
        //imageService.saveImage();
        imageService.getImage();
        return ""+imageService.getLastId();
    }

}
