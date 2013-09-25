
package schematicwizard.Windows.GraphicManager;

import java.awt.Color;

/**
 *
 * Wire - Brandon Altarejos
 * Created on August 15, 2013
 *
 * Holds the positions to place the wire
 * 
 **/

public class Wire{

    // Contains two pairs of positions
    private int[] points;
    Color wireColor;
    
    // Default wire constructor
    Wire(int[] s, int[] f, Color c){
        
        this.points = new int[] {s[0],s[1],f[0],f[1]};
        this.wireColor = c;
        
    }
    
    // Wire constructor for copying
    Wire(Wire w){
        
        this.points = w.points;
        this.wireColor = w.wireColor;
        
    }
    
    //----------------- ACCESSORS -----------------//
    
    public int[] getPositions(){
        return this.points;
    }
    public int getPositionX(int x){
        return this.points[x];
    }
    public int getX1(){
        return getPositionX(0);
    }
    public int getY1(){
        return getPositionX(1);
    }
    public int getX2(){
        return getPositionX(2);
    }
    public int getY2(){
        return getPositionX(3);
    }
    public int getMidX(){
        return (this.getX1() + this.getX2())/2;
    }
    public int getMidY(){
        return (this.getY1() + this.getY2())/2;
    }
    public Color getColor(){
        return this.wireColor;
    }
    
    //@@
    @Override
    public String toString(){
        
        return this.points[0]+","+this.points[1]+"->"+this.points[2]+","+this.points[3];
        
    }
    
    // Checks if the wire w is connected to the same points
    public boolean equals(Wire w) {
        
        return (this.getX1() == w.getX1() &&
                this.getX2() == w.getX2() &&
                this.getY1() == w.getY1() &&
                this.getY2() == w.getY2()) ||
                (this.getX1() == w.getX2() &&
                this.getX2() == w.getX1() &&
                this.getY1() == w.getY2() &&
                this.getY2() == w.getY1());
        
    }
    
    // Checks if the position (x,y) is one of the wire's position
    public boolean contains(int x,int y){
        
        return (this.getX1() == x &&
                this.getY1() == y) ||
                (this.getX2() == x &&
                this.getY2() == y);
        
    }

}
