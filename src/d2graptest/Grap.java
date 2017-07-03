/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package d2graptest;

import java.awt.Color;
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
    private Rectangle pRec;
    private GPoint center;
    private double lastDistance;
    
    private GPoint[] points;
    
    public Grap( Graphics grap,int width, int height ) {
        this.grap = grap;
        this.height = height;
        this.width = width;
    }
    
    private GTriangle firstTriangle(GPoint pos) {
        
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
        
        for (int i=0; i<points.length; i++) {
            GTriangle t = new GTriangle();
            t.C = points[points.length-i-3];
            t.B = points[points.length-i-2];
            t.A = points[points.length-i-1];
            if ( t.getRatio() > 1.1 ) {
                /*GPoint[] pl2 = new GPoint[points.length-i-2];
                System.arraycopy(points, 0, pl2, 0, pl2.length);
                points = pl2;                */
                return t;
            } else {
                //drawTriangle(t, Color.red);
            }
        }
        return null;
    }
    
    private void drawTriangle(GTriangle t,Color c) {
        
        grap.setColor(c);
        
        grap.drawString("A", t.A.getX(), t.A.getY());
        grap.drawString("B", t.B.getX(), t.B.getY());
        grap.drawString("C", t.C.getX(), t.C.getY());
        
        grap.drawLine(t.A.getX(), t.A.getY(), t.B.getX(), t.B.getY());
        grap.drawLine(t.B.getX(), t.B.getY(), t.C.getX(), t.C.getY());
        grap.drawLine(t.C.getX(), t.C.getY(), t.A.getX(), t.A.getY());
        
    }
    
    private GPoint getNearestPointOfLine(GPoint a,GPoint b, GPoint center) {
        
        GPoint c = new GPoint( (int)(a.getX() + b.getX())/2 , (int)(a.getY() + b.getY())/2);
        double dis = Math.sqrt( Math.pow(width,2) + Math.pow(height,2) );
        GPoint result = null;
        for (int i =0; i<points.length; i++ ) {
            if ( 
                    (!points[i].equals(c)) 
                    && (!points[i].equals(a)) 
                    && (!points[i].equals(b)) 
                    && (points[i].getDistance(center) > points[i].getDistance(c)) 
                    && (points[i].getDistance(c)<dis)
                ) {
                dis = points[i].getDistance(c);
                result = points[i];
            }
        }
        lastDistance = dis;
        return result;
    }
    
    private GTriangle getNextTriangle(GTriangle t) {
        GPoint na = getNearestPointOfLine(t.B,t.C,t.getCenter());
        double da = lastDistance;
        GPoint nb = getNearestPointOfLine(t.A,t.C,t.getCenter());
        double db = lastDistance;
        GPoint nc = getNearestPointOfLine(t.A,t.B,t.getCenter());
        double dc = lastDistance;
        
        if ( (da<db) && (da<dc) ) {
            return new GTriangle(na,t.B,t.C);
        } if ( (db<da) && (db<dc) ) {
            return new GTriangle(nb,t.A,t.C);
        } else {
            return new GTriangle(nc,t.A,t.B);
        }
        
        
    }
    
    public  void  draw() {
        
        grap.clearRect(0, 0, width, height);
        
        Random rx = new Random();
        Random ry = new Random();
        
        points = new GPoint[100];
        
        int minx = width;
        int miny = height;
        int maxx = 0;
        int maxy = 0;
        
        for (int i=0; i<points.length; i++) {
            int x = rx.nextInt(width);
            int y = ry.nextInt(height);
            grap.drawLine(x,y,x,y);
            points[i] = new GPoint(x,y); 
            
            if (x<minx) minx = x;
            if (y<miny) miny = y;
            if (x>maxx) maxx = x;
            if (y>maxy) maxy = y;            
        }
        
        pRec = new Rectangle(minx, miny, maxx-minx, maxy-miny);               
        
        GTriangle t = firstTriangle(new GPoint( (int)(maxx+minx)/2 ,(int)(maxy+miny)/2 ));
        
        grap.setColor(Color.BLACK);
        grap.drawString( "@" , (int)(maxx+minx)/2 ,(int)(maxy+miny)/2 );
        
        center = t.getCenter();
        grap.setColor(Color.BLUE);
        grap.drawString("X", center.getX(), center.getY());
        
        drawTriangle(t, Color.BLACK);
        
        GTriangle nt = getNextTriangle(t);
        drawTriangle(nt, Color.red);
        
                
    }
    
}
