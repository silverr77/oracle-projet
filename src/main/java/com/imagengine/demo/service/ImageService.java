package com.imagengine.demo.service;


import	oracle.ord.im.OrdImage; // Pour la classe OrdImage
import	oracle.ord.im.OrdImageSignature; // Pour la classe OrdImageSignature
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;


public class ImageService {
    private OrdImageSignature sign;
    private OracleResultSet rset;
    public void insertNewImage()
    {
        String sql ="INSERT INTO IMAGE (id) values(18)";
        PreparedStatement stmt = null;
        try {
            // Connect.getConnection().setAutoCommit(true);
            stmt = Connect.getConnection().prepareStatement(sql);
            stmt.executeQuery();
            Connect.getConnection().commit();
            stmt.close();
            System.out.println("Insetion done  ");
        } catch (SQLException ex) {
            System.out.println("Insertion Failed");
        }
    }
    public int getLastId()
    {
        
        int id_Max=0;
        String sql ="SELECT MAX(id) from Image";
        PreparedStatement stmt = null;
        try {
            stmt = Connect.getConnection().prepareStatement(sql);

            rset = (OracleResultSet) stmt.executeQuery();
            if (rset.next()) 
            {
                id_Max=rset.getInt(1);
                System.out.println("id===>"+id_Max);
            }
            Connect.getConnection().commit();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("Insertion Failed");
        }
        return id_Max;
    }
    
    public void saveImage() throws IOException {
    	int id=this.getLastId();
    	System.out.println("id save image ====> "+id);
    	
    	String sql = "SELECT image FROM image WHERE id="+id+" FOR UPDATE";
    	
    	PreparedStatement stmt = null;
    	try {
            // Connect.getConnection().setAutoCommit(true);
            stmt = Connect.getConnection().prepareStatement(sql);
            //stmt.executeQuery();
            this.rset=(OracleResultSet) stmt.executeQuery();
            if(rset.next()){
            	
            	//initialises images and signateur 
                String sql2 = "UPDATE image SET image=OrdImage.init(),signature=OrdImagesignature.init() WHERE id="+id;
                OraclePreparedStatement statement1 = (OraclePreparedStatement) Connect.getConnection().prepareStatement(sql2);
                statement1.execute();
                Connect.getConnection().commit();
                System.out.println("fin de initialisation");
                //fin initialisation
                
                //debut insertion
                sql2 = "SELECT image,signature FROM image WHERE id="+id+" FOR UPDATE";
                OraclePreparedStatement statement2 = (OraclePreparedStatement) Connect.getConnection().prepareStatement(sql2);
                this.rset=(OracleResultSet) statement2.executeQuery();
                
                if(this.rset.next()) {
                	OrdImage image = (OrdImage) this.rset.getORAData("image", OrdImage.getORADataFactory());
                    OrdImageSignature sign = (OrdImageSignature) this.rset.getORAData("signature", OrdImageSignature.getORADataFactory());
                    image.loadDataFromFile("E:\\IRISI\\anime.png");
                    image.setProperties();
                    System.out.println("set proprietie");
                    if(image.checkProperties()){
                    	sign.generateSignature(image);
                    	 String sql3 = "UPDATE image SET image=?, signature=? WHERE id="+id;
                    	 System.out.println("update image ....");
                    	 OraclePreparedStatement statement3 = (OraclePreparedStatement) Connect.getConnection().prepareStatement(sql3);
                    	 System.out.println("fin update image");
                    	 statement3.setORAData(1, image);
                         statement3.setORAData(2, sign);
                         statement3.execute();
                         System.out.println("image inserted hahaha");
                    }
                    
                    
                }
                
                //fin insertion
                
            }
            Connect.getConnection().commit();
            stmt.close();
            System.out.println("Insetion done  ");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    	
    	
    	
    	
   	
    	
    }

   
    public void getImage(){
    	System.out.println("-----------get image------------");
        try {
        	System.out.println("create statement");
        	Statement  stmt = Connect.getConnection().createStatement();
            String sql = "SELECT image FROM image WHERE id="+this.getLastId();
            System.out.println("execute query");
        	OracleResultSet rset =(OracleResultSet) stmt.executeQuery(sql);
        	System.out.println("fin execute query");
            OrdImage imgObj =null;
           
            if(rset.next()){
                imgObj =(OrdImage) rset.getORAData(1, OrdImage.getORADataFactory());
                imgObj.getDataInFile("E:\\IRISI\\result\\test.png");
               
                }
            System.out.println("image get");
        }catch(Exception ex) {
        	System.out.println("erreur"+ex);
        }
        
    }
    
}

