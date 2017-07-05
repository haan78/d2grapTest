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

    public Grap(Graphics grap, int width, int height) {
        this.grap = grap;
        this.height = height;
        this.width = width;
    }

    private GTriangle firstTriangle(GPoint pos) {

        GPoint p;

        for (int i = 0; i < points.length; i++) {
            for (int j = i; j < points.length; j++) {
                if (points[j].getDistance(pos) > points[i].getDistance(pos)) {
                    p = points[i];
                    points[i] = points[j];
                    points[j] = p;
                }
            }
        }

        for (int i = 0; i < points.length; i++) {
            GTriangle t = new GTriangle();
            t.C = points[points.length - i - 3];
            t.B = points[points.length - i - 2];
            t.A = points[points.length - i - 1];
            //if ( t.getRatio() > 1 ) {
            if (Math.abs(t.getArea()) > 2000) {
                /*GPoint[] pl2 = new GPoint[points.length-i-2];
                System.arraycopy(points, 0, pl2, 0, pl2.length);
                points = pl2;  
                 */
                return t;
            } else {
                //drawTriangle(t, Color.red);
            }
        }
        return null;
    }

    private void drawTriangle(GTriangle t, Color color) {

        if (t == null) {
            return;
        }

        grap.setColor(color);

        grap.drawString("A", t.A.getX() - 3, t.A.getY() - 3);
        grap.drawString("B", t.B.getX() - 3, t.B.getY() - 3);
        grap.drawString("C", t.C.getX() - 3, t.C.getY() - 3);

        GPoint c = t.getCenter();
        grap.drawString(String.valueOf(t.getArea()), c.getX() - 3, c.getY() - 3);

        grap.drawLine(t.A.getX(), t.A.getY(), t.B.getX(), t.B.getY());
        grap.drawLine(t.B.getX(), t.B.getY(), t.C.getX(), t.C.getY());
        grap.drawLine(t.C.getX(), t.C.getY(), t.A.getX(), t.A.getY());

    }

    private boolean areCrossing(GPoint a, GPoint b, GPoint c, GPoint d) {
        double ua;
        double ub;
        double ua1 = ((d.getX() - c.getX()) * (a.getY() - c.getY())) - ((d.getY() - c.getY()) * (a.getX() - c.getX()));
        double ua2 = ((d.getY() - c.getY()) * (b.getX() - a.getX())) - ((d.getX() - c.getX()) * (b.getY() - a.getY()));

        double ub1 = ((b.getX() - a.getX()) * (a.getY() - c.getY())) - ((b.getY() - a.getY()) * (a.getX() - c.getX()));
        double ub2 = ((d.getY() - c.getY()) * (b.getX() - a.getX())) - ((d.getX() - c.getX()) * (b.getY() - a.getY()));

        if ((ub2 == 0) || (ua2 == 0)) {
            return false;
        }

        ua = ua1 / ua2;
        ub = ub1 / ub2;

        if ((ua > 0)
                && (ua < 1)
                && (ub > 0)
                && (ub < 1)) {
            return true;
        }

        return false;
    }

    private boolean inTriangle(GTriangle t, GPoint p) {
        GPoint c = t.getCenter();
        if (areCrossing(t.A, t.B, p, c)) {
            return true;
        }
        if (areCrossing(t.B, t.C, p, c)) {
            return true;
        }
        if (areCrossing(t.C, t.A, p, c)) {
            return true;
        }
        return false;
    }

    private void print(String s, GPoint p) {
        grap.drawString(s, p.getX(), p.getY());
    }

    private GPoint getNearestPointOfLine(GTriangle t, int line) {

        GPoint from, p0, p1, p2;

        switch (line) {
            case 0: //line a
                from = t.getBCCenter();
                p0 = t.A;
                p1 = t.B;
                p2 = t.C;
                break;
            case 1: //line b
                from = t.getACCenter();
                p0 = t.B;
                p1 = t.A;
                p2 = t.C;
                break;
            default: //line c
                from = t.getABCenter();
                p0 = t.C;
                p1 = t.A;
                p2 = t.B;
                break;
        }
        double c_f = from.getDistance(t.getCenter());
        double dis = Math.sqrt(Math.pow(pRec.width, 2) + Math.pow(pRec.height, 2));
        GPoint result = null;
        for (int i = 0; i < points.length; i++) {
            GTriangle tt = new GTriangle(points[i], p1, p2);
            if (points[i].equals(p0)) {
                print("1", points[i]);
            } else if (points[i].equals(p1)) {
                print("2", points[i]);
            } else if (points[i].equals(p2)) {
                print("3", points[i]);
            } //else if ( points[i].getDistance(from) > points[i].getDistance(p0) ) print("K", points[i]);
            else if (areCrossing(points[i], from, p0, p1)) {
                print("4", points[i]);
            } else if (areCrossing(points[i], from, p0, p2)) {
                print("5", points[i]);
            } else if (tt.getArea() < 2000) {
                print("6", points[i]);
            } else if (!inTriangle(t, points[i])) {
                print("7", points[i]);
            } else if (points[i].getDistance(from) >= dis) {
                print("8", points[i]);
            } else {
                dis = points[i].getDistance(from);
                result = points[i];
            }
        }
        lastDistance = dis;
        return result;
    }

    private GTriangle getNextTriangle(GTriangle t, boolean first) {
        /*GPoint na = getNearestPointOfLine(t);
        double da = lastDistance;
        GPoint nb = getNearestPointOfLine(t);
        double db = lastDistance;*/
        double dis = Math.sqrt(Math.pow(pRec.width, 2) + Math.pow(pRec.height, 2));
        GPoint p = null;
        GTriangle tn = null;
        for (int i = (first ? 0 : 1); i < 3; i++) {

            GPoint nc = getNearestPointOfLine(t, i);
            if ((nc != null) && (lastDistance < dis)) {
                p = nc;
                dis = lastDistance;
                switch (i) {
                    case 0:
                        tn = new GTriangle(p, t.B, t.C);
                        break;
                    case 1:
                        tn = new GTriangle(p, t.A, t.C);
                        break;
                    case 2:
                        tn = new GTriangle(p, t.B, t.A);
                        break;
                    default:
                        break;
                }
            }

        }
        return tn;
        /*if ( (da<db) && (da<dc) ) {
            return new GTriangle(na,t.B,t.C);
        } if ( (db<da) && (db<dc) ) {
            return new GTriangle(nb,t.A,t.C);
        } else {
            return new GTriangle(nc,t.A,t.B);
        }*/
    }

    public void draw() {

        grap.clearRect(0, 0, width, height);

        Random rx = new Random();
        Random ry = new Random();

        points = new GPoint[200];

        int minx = width;
        int miny = height;
        int maxx = 0;
        int maxy = 0;

        for (int i = 0; i < points.length; i++) {
            int x = rx.nextInt(width);
            int y = ry.nextInt(height);
            grap.fillOval(x, y, 4, 4);
            points[i] = new GPoint(x, y);

            if (x < minx) {
                minx = x;
            }
            if (y < miny) {
                miny = y;
            }
            if (x > maxx) {
                maxx = x;
            }
            if (y > maxy) {
                maxy = y;
            }
        }

        pRec = new Rectangle(minx, miny, maxx - minx, maxy - miny);

        GTriangle t = firstTriangle(new GPoint((int) (maxx + minx) / 2, (int) (maxy + miny) / 2));

        drawTriangle(t, Color.BLACK);

        GTriangle nt = getNextTriangle(t,true);
        drawTriangle(nt, Color.red);
        nt = getNextTriangle(nt,false);
        drawTriangle(nt, Color.BLUE);
        nt = getNextTriangle(nt,false);
        drawTriangle(nt, Color.magenta);
        

    }

}
