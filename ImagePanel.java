/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocv_proba;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
/**
 *
 * @author Yura
 */
public class ImagePanel extends JPanel{
    private BufferedImage image;
    public ImagePanel(){
        this.image = Ocv_proba.loadImage.getBufferedImage();
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
        
    }
}
