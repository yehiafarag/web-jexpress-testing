/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package web.diva.server.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Yehia Farag
 */
public class GroupColorUtil {
    
    public GroupColorUtil(){}
    
    public String getImageColor(String hashColor,String path){
    try{
        Color color = hex2Rgb(hashColor);
       BufferedImage image = new BufferedImage(8, 9,BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
       graphics.setBackground(color);
       graphics.setColor(color);
       graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        File f = new File(path, "gc1.png");
        ImageIO.write(image, "PNG", f);
        // Reading a Image file from file system
           FileInputStream imageInFile = new FileInputStream(f);
           byte imageData[] = new byte[(int) f.length()];
           imageInFile.read(imageData);
           String base64 = com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(imageData);


//com.google.gwt.user.server.Base64Utils.toBase64(imageData); 
            base64 = "data:image/png;base64," + base64;
            
            
            
//             BufferedImage image1 = new BufferedImage(8, 9,BufferedImage.TYPE_INT_RGB);
//        Graphics2D graphics1 = image1.createGraphics();
//       graphics1.setBackground(Color.YELLOW);
//       graphics1.setColor(Color.YELLOW);
//       graphics1.fillRect(0, 0, image.getWidth(), image.getHeight());
//        File f1 = new File(path, "gc1.png");
//        ImageIO.write(image1, "PNG", f1);
//        
//        
      
        return base64;
    }catch(IOException ioexp){ioexp.printStackTrace();}
        return null;
    }
    
    /**
 * 
 * @param colorStr e.g. "#FFFFFF"
 * @return 
 */
private Color hex2Rgb(String colorStr) {
    return new Color(
            Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
            Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
}
    
    
}
