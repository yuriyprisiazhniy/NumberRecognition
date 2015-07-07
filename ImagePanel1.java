/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocv_proba;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
/**
 *
 * @author Yura
 */
public class ImagePanel1 extends JPanel{
    private BufferedImage image;
    public ImagePanel1(int k){
        this.image = NewJFrame.findingSymbols.get(k).getBufferedImage();
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(image != null)
        g.drawImage(image, 0, 0, null);
        
    }
    
}
