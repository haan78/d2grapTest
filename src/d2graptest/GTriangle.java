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
    
    
    
    public double getRatio() {
        double dc = A.getDistance(B);
        double db = C.getDistance(A);
        double da = B.getDistance(C);
        
        if ( (da > db) && (da > dc) ) {
            return (db+dc) / da;
        } else if ( (db > da) && (db > dc) ) {
            return (dc+da) /  db;
        } else {
            return (db+da) / dc;
        }      
    }
    
    public double getArea() {
        return ( ( B.getX()-A.getX() ) * (C.getY()- A.getY()) ) - ( ( B.getY() - A.getY() ) * ( C.getX() - A.getX() ) );
    }

    public GTriangle(GPoint A, GPoint B, GPoint C) {
        this.A = A;
        this.B = B;
        this.C = C;
    }

    public GTriangle() {
    }
    
    public GPoint getCenter() {
        GPoint bc = getBCCenter();
        return new GPoint( (int)(( A.getX()+(2*bc.getX()) )/3) , (int)(( A.getY()+(2*bc.getY()) )/3) );
    }
    
    public GPoint getABCenter() {
        return new GPoint( (int)( A.getX()+B.getX() )/2 , (int)( A.getY() + B.getY() )/2 );
    }
    
    public GPoint getBCCenter() {
        return new GPoint( (int)( B.getX()+C.getX() )/2 , (int)( B.getY() + C.getY() )/2 );
    }
    
    public GPoint getACCenter() {
        return new GPoint( (int)( A.getX()+C.getX() )/2 , (int)( A.getY() + C.getY() )/2 );
    }
}
