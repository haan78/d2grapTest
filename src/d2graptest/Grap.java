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
        
        if (t == null) return;
        
        grap.setColor(c);
        
        grap.drawString("A", t.A.getX(), t.A.getY());
        grap.drawString("B", t.B.getX(), t.B.getY());
        grap.drawString("C", t.C.getX(), t.C.getY());
        
        grap.drawLine(t.A.getX(), t.A.getY(), t.B.getX(), t.B.getY());
        grap.drawLine(t.B.getX(), t.B.getY(), t.C.getX(), t.C.getY());
        grap.drawLine(t.C.getX(), t.C.getY(), t.A.getX(), t.A.getY());
        
    }
    
    private boolean areCrossing(GPoint a,GPoint b,GPoint c, GPoint d) {
        double ua;
        double ub;
        double ua1 = ((d.getX() - c.getX()) * (a.getY()-c.getY())) - ( (d.getY()-c.getY()) * (a.getX()-c.getX()) );
        double ua2 = ((d.getY() - c.getY()) * (b.getX()-a.getX())) - ( (d.getX()-c.getX()) * (b.getY()-a.getY()) );
        
        double ub1 = ((b.getX() - a.getX()) * (a.getY()-c.getY())) - ( (b.getY()-a.getY()) * (a.getX()-c.getX()) );
        double ub2 = ((d.getY() - c.getY()) * (b.getX()-a.getX())) - ( (d.getX()-c.getX()) * (b.getY()-a.getY()) );
        
        if ((ub2 == 0) || (ua2==0)) return false;
        
        ua = ua1 / ua2;
        ub = ub1 / ub2;
        
        if (
             (ua > 0)
             && (ua < 1)
             && (ub > 0)
             && (ub < 1)
            ) {
            return true;
        }
                
        
        return false;
    }
    
    private void print(String s,GPoint p) {
        grap.drawString(s, p.getX(),p.getY());
    }
    
    private GPoint getNearestPointOfLine(GTriangle t) {
        
        GPoint from = t.getABCenter();
        double dis = Math.sqrt( Math.pow(width,2) + Math.pow(height,2) );
        GPoint result = null;
        for (int i =0; i<points.length; i++ ) {
            GTriangle tt = new GTriangle( points[i],t.A,t.B );
            if ( points[i].equals(t.A) ) print("1", points[i]);
            else if (points[i].equals(t.B)) print("2", points[i]);
            else if (points[i].equals(t.C)) print("3", points[i]);
            else if ( areCrossing(points[i], from, t.C, t.B) ) print("4", points[i]);
            else if ( areCrossing(points[i], from, t.C, t.A) ) print("5", points[i]);
            else if (tt.getRatio()< 1.1) print("6",points[i]);
            else if (points[i].getDistance(from)>=dis) print("7", points[i]);
            else {
                dis = points[i].getDistance(from);
                result = points[i];
            }
        }
        lastDistance = dis;
        return result;
    }
    
    private GTriangle getNextTriangle(GTriangle t) {
        /*GPoint na = getNearestPointOfLine(t);
        double da = lastDistance;
        GPoint nb = getNearestPointOfLine(t);
        double db = lastDistance;*/
        GPoint nc = getNearestPointOfLine(t);
        double dc = lastDistance;
        
        /*if ( (da<db) && (da<dc) ) {
            return new GTriangle(na,t.B,t.C);
        } if ( (db<da) && (db<dc) ) {
            return new GTriangle(nb,t.A,t.C);
        } else {
            return new GTriangle(nc,t.A,t.B);
        }*/
        
        return new GTriangle(nc,t.A,t.B);
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
