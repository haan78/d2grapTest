/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package d2graptest;

/**
 *
 * @author BARIS
 */
public class GPoint {
    private int x;
    private int y;

    public GPoint(int x,int y) {
        this.x = x;
        this.y = y;        
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public double getDistance(GPoint p) {
        return Math.sqrt( ( Math.pow(p.getX()-x, 2) + Math.pow(p.getY()-y, 2) ) );
    }
    
    
}
