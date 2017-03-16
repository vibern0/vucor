/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obernardovieira.vucor.frames;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author user
 */
public class PicPanel extends JPanel
{
    private BufferedImage image;
    private int jw, jh;
    public PicPanel(String fname, int image_width, int image_height,
            int jpanel_width, int jpanel_height){
        
        setBounds(0,0,image_width,image_height);
        jw = jpanel_width;
        jh = jpanel_height;
        
        try
        {
            image = ImageIO.read(new File(fname));
        }
        catch (IOException ioe)
        {
            System.out.println(ioe.toString());
            System.out.println("Could not read in the pic");
        }
    }
    public PicPanel(String fname, int image_width, int image_height,
            int jpanel_width, int jpanel_height, Color background){
        
        setBounds(0,0,image_width,image_height);
        jw = jpanel_width;
        jh = jpanel_height;
        
        try
        {
            image = ImageIO.read(new File(fname));
        }
        catch (IOException ioe)
        {
            System.out.println(ioe.toString());
            System.out.println("Could not read in the pic");
        }
        if(background != null)
        {
            setBackground(background);
        }
    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(image,0,0,jw,jh,this);
    }
}
