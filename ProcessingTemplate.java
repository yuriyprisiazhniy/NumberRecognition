/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocv_proba;

import java.io.*;
import com.googlecode.javacv.cpp.opencv_core;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;

/**
 *
 * @author Yura
 */
public class ProcessingTemplate {
    
    private int[][] mat;
    private int n,m;
    private File flt;
    private FileWriter wrt;
    public ProcessingTemplate(opencv_core.IplImage template,char ch) throws IOException{
        n = template.asCvMat().rows();
        m = template.asCvMat().cols();
        mat = new int[n][m];
        for(int i=0;i<n;i++)
            for(int j=0;j<m;j++)
            {
                mat[i][j]=0;
                if (cvGet2D(template.asCvMat(), i, j).getVal(0) == 0) mat[i][j] = 1;
            }
        flt = new File("E:\\Simbols\\"+ch+".txt");
        wrt = new FileWriter(flt);
    }
    public ProcessingTemplate(int n, int m){
        this.n = n;
        this.m = m;
    }
    public void fill( int i, int j, int k,int[][] mat){
        if(((i-1)>=0) &&(mat[i-1][j]==0) )
            mat[i-1][j] = k+1;
        if(((i+1)<n) &&(mat[i+1][j]==0) )
             mat[i+1][j] = k+1;
        if(((j-1)>=0) &&(mat[i][j-1]==0) )
            mat[i][j-1] = k+1;
        if(((j+1)<m) &&(mat[i][j+1]==0) )
            mat[i][j+1] = k+1;
        if(((j+1)<m) &&((i-1)>=0)&&(mat[i-1][j+1]==0) )
            mat[i-1][j+1] = k+1;
        if(((j+1)<m) &&((i+1)<n)&&(mat[i+1][j+1]==0) )
            mat[i+1][j+1] = k+1;
        if(((j-1)>=0) &&((i+1)<n)&&(mat[i+1][j-1]==0) )
            mat[i+1][j-1] = k+1;
        if(((j-1)>=0) &&((i-1)>=0)&&(mat[i-1][j-1]==0) )
            mat[i-1][j-1] = k+1;
    }
    public boolean check(int[][] mat){
        for (int i=0;i<n;i++)
            for (int j=0;j<m;j++)
                if(mat[i][j] == 0) return true;
        return false;
    }
    public int[][] getMat(){
        return mat;
    }
    public void processing(int[][] mat) throws IOException{
 
        
        int k = 1;
        while(check(mat)){
            for(int i=0;i<n;i++)
                for(int j=0;j<m;j++)
                    if(mat[i][j] == k) fill(i,j,k,mat);

         k++;
            
        }
        /*for(int i=0;i<n;i++)
        {
            for(int j=0;j<m;j++)
                wrt.write(Integer.toString(mat[i][j])+" ");
            wrt.write('\n');
        }
        
        wrt.close();*/
        /*System.out.println(n+"x"+m);
        for(int i=0;i<n;i++)
        {    for(int j=0;j<m;j++)
         System.out.print(mat[i][j]+" ");
        System.out.println();
        }*/
    }
}
