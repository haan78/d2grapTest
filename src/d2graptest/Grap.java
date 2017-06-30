/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package d2graptest;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

/**
 *
 * @author BARIS
 */
public class Grap {

    private Graphics grap;
    private int width;
    private int height;
    private GPoint[] points;
    
    public Grap( Graphics grap,int width, int height ) {
        this.grap = grap;
        this.height = height;
        this.width = width;
    }
    
    private GTriangle firstTriangle(GPoint pos) {
        GTriangle t = new GTriangle();
        GPoint p;
        for (int i=0; i<points.length; i++) {            
            for (int j=i; j<points.length; j++) {
                if ( points[j].getDistance(pos) > points[i].getDistance(pos) ) {
                    p = points[i];
                    points[i] = points[j];
                    points[j] = p;
                }
            }
        }
        t.C = points[points.length-3];
        t.B = points[points.length-2];
        t.A = points[points.length-1];
        return t;
    }
    
    public  void  draw() {
        
        Random rx = new Random();
        Random ry = new Random();
        
        points = new GPoint[100];
        
        for (int i=0; i<points.length; i++) {
            int x = rx.nextInt(width);
            int y = ry.nextInt(height);
            grap.drawLine(x,y,x,y);
            points[i] = new GPoint(x,y);                        
        }
        
        
        GTriangle t = firstTriangle(new GPoint(0,0));
        
        grap.drawLine(t.A.getX(), t.A.getY(), t.B.getX(), t.B.getY());
        grap.drawLine(t.B.getX(), t.B.getY(), t.C.getX(), t.C.getY());
        grap.drawLine(t.C.getX(), t.C.getY(), t.A.getX(), t.A.getY());
        
        grap.drawString("A", t.A.getX(), t.A.getY());
        grap.drawString("B", t.B.getX(), t.B.getY());
        grap.drawString("C", t.C.getX(), t.C.getY());
        
                
    }
    
}
