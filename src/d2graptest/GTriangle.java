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
public class GTriangle {
    public GPoint A;
    public GPoint B;
    public GPoint C;
    
    public GPoint getABCenter() {
        double x = (B.getX() - A.getX() / 2 ) + A.getX();
        double y = (B.getY() - A.getY() / 2 ) + A.getY();
        return new GPoint( (int)x , (int)y);
    }
}
