
package schematicwizard.Windows.GraphicManager;

import java.awt.image.BufferedImage;

/**
 *
 * Component - Brandon Altarejos
 * Created on August 7, 2013
 *
 * This class will hold all of the properties of the component
 * so that it can be drawn when needed
 * 
 **/

public class Component implements Comparable<Component>{

    // Properties of the component that change how it looks
    String compName;
    private int posX, posY; 
    private double rotation; // In Degrees
    private int[] wireNodes;
    
    // Properties of the component that is gotten from calculation
    private int radius;
    
    // The Image of the actual component
    BufferedImage compImage;
    
    // The default component constructor
    Component(String n, BufferedImage bi,int x, int y, double r, int[] wn){
        
        this.compName = n;
        this.compImage = bi;
        this.posX = x;
        this.posY = y;
        this.rotation = r%360;
        this.wireNodes = applyTransformationsToWireNodes(wn);
        
        this.radius = (int) Math.sqrt(Math.pow(this.compImage.getHeight()/2,2)+Math.pow(this.compImage.getWidth()/2, 2));
        this.radius = Math.abs(radius);
        
    }
    
    // The component constructor for copying
    Component(Component c){
        
        this.compName = c.compName;
        this.compImage = c.compImage;
        this.posX = c.posX;
        this.posY = c.posY;
        this.rotation = c.rotation;
        this.radius = c.radius;
        
    }
    
    //----------------- MUTATORS -----------------//
    void setPosition(int x, int y){
        this.posX = x; 
        this.posY = y;
    }
    void setRotation(int r){
        this.rotation = r;
    }
    void setImage(BufferedImage bi){
        this.compImage = bi;
    }
    
    //----------------- ACCESSORS -----------------//
    BufferedImage getImage(){
        return this.compImage;
    }
    public String getName(){
        return this.compName;
    }
    public int getX(){
        return this.posX;
    }
    public int getY(){
        return this.posY;
    }
    int getWidth(){ // Returns the width of the compImage
        return this.compImage.getWidth();
    }
    int getHeight(){ // Returns the height of the compImage
        return this.compImage.getHeight();
    }
    int getCentreX(){ // Returns the x of the centre of the component
        return this.posX + this.getWidth()/2;
    }
    int getCentreY(){ // Returns the y of the centre of the component
        return this.posY + this.getHeight()/2;
    }
    public double getRotation(){
        return this.rotation;
    }
    int[] getWireNodes(){
        return this.wireNodes;
    }
    int getRadius(){
        return this.radius;
    }
    // The code used to identify each component
    @Override
    public String toString(){
        
        return this.posX+"~"+this.posY+"~"+this.compName+"~"+this.rotation;
        
    }
    
    //----------------- HELPERS -----------------//
    
    /*
     * First translates them so that they can be rotated around the origin
     * (0,0) and then rotates them according the component's rotation
     * and then translates them back to where they are on their correct
     * positions
     */
    private int[] applyTransformationsToWireNodes(int[] wn){
        
        int[] wnNew = new int[wn.length];
        
        for (int i=0;i<wn.length;i=i+2){
            
            double c = Math.cos(Math.toRadians(this.rotation));
            double s = Math.sin(Math.toRadians(this.rotation));
            double x = wn[i] - this.getWidth()/2;
            double y = wn[i+1] - this.getHeight()/2;
            wnNew[i] = (int)(x*c - y*s) + this.getCentreX();
            wnNew[i+1] = (int)(x*s + y*c) + this.getCentreY();
            
        }
        
        return wnNew;
        
    }
    
    //----------------- COMPARABLE<COMPONENT> -----------------//
    /*
     * First : Compares position from left to right, up to down
     * Second : Compares alphabetically the name of the component
     * Third : Compares rotation from 0 to 360
     */
    @Override
    public int compareTo(Component o) {
        
        return this.toString().compareTo(o.toString());
              
    }
    
}
