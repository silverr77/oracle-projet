package com.imagengine.demo.service;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import	oracle.ord.im.OrdImage; // Pour la classe OrdImage
import	oracle.ord.im.OrdImageSignature; // Pour la classe OrdImageSignature


public class ImageService {
    private OrdImageSignature sign;
    private OracleResultSet rset;
    public void insertNewImage()
    {
        String sql ="INSERT INTO IMAGE (id) values(image_seq.nextval)";
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
    
    public void saveImage(String imagePath) throws IOException {
    	this.insertNewImage();
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
                    image.loadDataFromFile(imagePath);
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

    

   
    public OrdImage getImage(int id){
    	System.out.println("-----------get image------------");
        try {
        	System.out.println("create statement");
        	Statement  stmt = Connect.getConnection().createStatement();
            String sql = "SELECT image FROM image WHERE id="+id;
            System.out.println("execute query");
        	OracleResultSet rset =(OracleResultSet) stmt.executeQuery(sql);
        	System.out.println("fin execute query");
            OrdImage imgObj =null;
           
            if(rset.next()){
                imgObj =(OrdImage) rset.getORAData(1, OrdImage.getORADataFactory());
                imgObj.getDataInFile("E:\\IRISI\\result\\image"+id+".png");
               
                }
            System.out.println("image get");
            return imgObj;
        }catch(Exception ex) {
        	System.out.println("erreur"+ex);
        }
        return null;
        
    }
    
    
    public List<OrdImage> isSimilar(String path){
    	List<OrdImage> imagesSimilar=new ArrayList<OrdImage>();
    	float seuil = 35;
    	String commande ="color=1 texture=1 shape=1";
    	try {
    		this.saveImage(path);
    		int id_image1=this.getLastId();
    		String sql1 = "SELECT signature FROM image WHERE id="+id_image1;
    		OraclePreparedStatement statement1 = (OraclePreparedStatement) Connect.getConnection().prepareStatement(sql1);
            this.rset=(OracleResultSet) statement1.executeQuery();
            OrdImageSignature sign_Image=null;
            if(this.rset.next()) {
            	sign_Image= (OrdImageSignature) this.rset.getORAData("signature", OrdImageSignature.getORADataFactory());
            }
            
            //delete image1 from table
            String query = "delete from image where id = "+id_image1;
            PreparedStatement preparedStmt = Connect.getConnection().prepareStatement(query);
            preparedStmt.execute();
            //fin delete
            
            //get all images
            String sql2 = "SELECT signature,image FROM image";
    		OraclePreparedStatement statement2 = (OraclePreparedStatement) Connect.getConnection().prepareStatement(sql2);
            this.rset=(OracleResultSet) statement2.executeQuery();
            while(this.rset.next()) {
            	OrdImage image = (OrdImage) this.rset.getORAData("image", OrdImage.getORADataFactory());
                OrdImageSignature signature = (OrdImageSignature) this.rset.getORAData("signature", OrdImageSignature.getORADataFactory());
                
                if(OrdImageSignature.isSimilar(signature,sign_Image,commande,seuil)==1) {
                	imagesSimilar.add(image);
                }
            }
            
    		
    	}catch(Exception e) {
    		System.out.println(e);
    	}
    	
    	return imagesSimilar;
    }
    
    public void ImageCreation(List<OrdImage> images) {
  
    		images.forEach(i->{
        		 try {
					i.getDataInFile("E:\\IRISI\\result\\resulats\\image"+System.currentTimeMillis()+".png");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	});
    	
    }
    
    
    public void compareBetweenImages(String path1,String path2) {
    	System.out.println("compare between images ");
    	try {
    		this.saveImage(path1);
    		int id_image1=this.getLastId();
    		String sql1 = "SELECT signature FROM image WHERE id="+id_image1;
    		OraclePreparedStatement statement1 = (OraclePreparedStatement) Connect.getConnection().prepareStatement(sql1);
            this.rset=(OracleResultSet) statement1.executeQuery();
            OrdImageSignature sign=null;
            if(this.rset.next()) {
            	sign= (OrdImageSignature) this.rset.getORAData("signature", OrdImageSignature.getORADataFactory());
            }
            
            //delete image1 from table
            String query = "delete from image where id = "+id_image1;
            PreparedStatement preparedStmt = Connect.getConnection().prepareStatement(query);
            preparedStmt.execute();
            //fin delete
            preparedStmt.close();
            
    		this.saveImage(path2);
    		int id_image2=this.getLastId();
    		String sql2 = "SELECT signature FROM image WHERE id="+id_image2;
    		OraclePreparedStatement statement2 = (OraclePreparedStatement) Connect.getConnection().prepareStatement(sql2);
            this.rset=(OracleResultSet) statement2.executeQuery();
            OrdImageSignature sign1=null;
            if(this.rset.next()) {
            	sign1= (OrdImageSignature) this.rset.getORAData("signature", OrdImageSignature.getORADataFactory());
            }
            
          //delete image1 from table
            String query1 = "delete from image where id = "+id_image2;
            PreparedStatement preparedStmt1 = Connect.getConnection().prepareStatement(query1);
            preparedStmt1.execute();
          //fin delete
            preparedStmt1.close();
    		System.out.println("Fin --- image and signature : initialisation");

            System.out.println("debut de comparaison");
            
            String commande ="color=1 texture=0 shape=0 location=0";
            float score = OrdImageSignature.evaluateScore(sign, sign1,commande);
            
            
            System.out.println("fin de comparaison");

            System.out.println("Score : "+score);
            
            
    	}catch(Exception e) {
    		System.out.println(e);
    	}
   
        
    }
    
    
}

