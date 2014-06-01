package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.geom.Point2D;

/**
 *
 * @author Francesco
 */
public final class MyPoint extends Point2D {

    private int x;
    private int y;
    
    public MyPoint(double x, double y){
        this.setLocation(x, y);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
    }
}
