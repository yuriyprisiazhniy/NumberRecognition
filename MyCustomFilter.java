/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocv_proba;
import java.io.*;
/**
 *
 * @author Yura
 */
public class MyCustomFilter extends javax.swing.filechooser.FileFilter{
    @Override
    public boolean accept (File file){
        return (file.getAbsolutePath().endsWith(".jpg") || (file.isDirectory())) ;
    }
    @Override
    public String getDescription(){
        return "Images (.jpg)";
    }
}
