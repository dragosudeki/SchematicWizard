
package schematicwizard.Windows.GraphicManager;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * DrawManager - Brandon Altarejos
 * Created on August 7, 2013
 *
 * This class will hold data and draw all symbols, wires, and etc.
 * 
 * Terms:
 *  Active Component - A component which has the mouse located within its
 *      radius.
 * 
 **/

public class DrawManager {
    
    // The array that will hold the data of all of the Wires
    Wire[] wireArray;
    
    // The set and array that will hold the data of the Components
    Set<Component> componentsSet;
    Component[] componentsArray;
    
    // The default constructor for DrawManager
    public DrawManager(){
        
        componentsSet = new TreeSet<>();
        componentsArray = null;
        wireArray = null;
        
    }
    
    // Draws everything on graphics g
    public void drawAll(Graphics g){
        
        this.drawComponents(g);
        this.drawWires(g);
        
    }
    
    //----------------- WIRE MANAGER -----------------//
    
    // Draws all the wires
    private void drawWires(Graphics g){
        
        if (wireArray != null)
            for (int i=0;i<wireArray.length;i++){
                
                g.setColor(wireArray[i].getColor());
                g.drawLine(wireArray[i].getX1(), 
                        wireArray[i].getY1(), 
                        wireArray[i].getX2(), 
                        wireArray[i].getY2());
                g.drawLine(wireArray[i].getX1()-1, 
                        wireArray[i].getY1(), 
                        wireArray[i].getX2()-1, 
                        wireArray[i].getY2());
                g.drawLine(wireArray[i].getX1(), 
                        wireArray[i].getY1()-1, 
                        wireArray[i].getX2(), 
                        wireArray[i].getY2()-1);
                g.drawLine(wireArray[i].getX1()+1, 
                        wireArray[i].getY1(), 
                        wireArray[i].getX2()+1, 
                        wireArray[i].getY2());
                g.drawLine(wireArray[i].getX1(), 
                        wireArray[i].getY1()+1, 
                        wireArray[i].getX2(), 
                        wireArray[i].getY2()+1);

            }
        
    }
    
    // Adds a new wire to the array
    public void addWire(int[] start, int[] finish, Color c){
        
        if (wireArray == null){
            
            wireArray = new Wire[1];
            wireArray[0] = new Wire(start,finish,c);
            
        }else{
            
            Wire[] temp = new Wire[wireArray.length+1];
            copyWireArray(wireArray,temp);
            temp[wireArray.length] = new Wire(start,finish,c);
            wireArray = new Wire[wireArray.length+1];
            copyWireArray(temp,wireArray);
            
        }
        
    }
    
    // Gets the closest wire to the position (x,y) using the wire's midpoints
    public int[] getClosestWire(int x, int y){
        
        int[] temp = null;
        int distance = -1;
        
        if(wireArray==null||wireArray.length<1)
            return temp;
                
        for (int i=0;i<wireArray.length;i++){
                
            int d = getDistance(x,y,wireArray[i].getMidX(),wireArray[i].getMidY());
                
            if (distance == -1 || d < distance){
                    
                distance = d;
                temp = new int[] {wireArray[i].getMidX(),wireArray[i].getMidY()};
               
            }
            
        }
        
        return temp;
        
    }
    
    // Removes the wire with the midpoint (x,y)
    public void removeWire(int x, int y){
        
        Wire[] wireArrayTemp = new Wire[wireArray.length-1];
        int j = 0;
        boolean isRemoved = false;
        
        for (int i=0;i<wireArray.length;i++){
            
            if (wireArray[i].getMidX() == x && wireArray[i].getMidY() == y && !isRemoved){
            
                isRemoved = true;
                continue;
            
            }
            
            wireArrayTemp[j] = new Wire(wireArray[i]);
            j++;
            
        }
        
        wireArray = new Wire[wireArray.length-1];
        copyWireArray(wireArrayTemp, wireArray);
        
    }
    
    // Removes the closest wire to the point (x,y) using the mid points of wires
    public void removeClosestWire(int x, int y){
        
        int[] wireMidPoint = getClosestWire(x,y);
        
        if (wireMidPoint != null)
            this.removeWire(wireMidPoint[0],wireMidPoint[1]);
        
    }
    
    // Removes all the wires that are attached to every node in nodeArray
    public void removeAllComponentsWires(int[] nodeArray){
        
        if (wireArray == null || wireArray.length<1)
            return;
        
        int amountRemoved = 0;
        int tempIndex = 0;
        Wire[] wireArrayTemp = new Wire[wireArray.length];
        
        // Checks through each wire
        for (int i=0;i<wireArray.length;i++){
                
            boolean isAttached = false;
                
            // Checks to see if the wire is attached to any of the nodes
            for (int j=0;j<nodeArray.length;j=j+2){

                if (wireArray[i].contains(nodeArray[j],nodeArray[j+1])){
                        
                    System.out.println(wireArray[i]+" must be removed");
                    isAttached = true;
                    amountRemoved++;
                    break;
                        
                }

            }
                
            // If the wire isn't attached then it adds it to the temporary 
            // array
            if (!isAttached){
                    
                wireArrayTemp[tempIndex] = new Wire(wireArray[i]);
                tempIndex++;
                    
            }

        }
        
        if (amountRemoved == 0)
            return;
        
        wireArray = new Wire[wireArray.length-amountRemoved];
        this.copyWireArray(wireArrayTemp,wireArray);
        
    }
    
    // Copies each element from src into the dst array, stopping when src has no
    // more indexes, or when dest cannot take any more values
    private void copyWireArray(Wire[] src, Wire[] dest){
        
        for (int i=0;i<src.length;i++)
            if (i >= dest.length)
                return;
            else
                dest[i] = new Wire(src[i]);
                
    }
    
    // Checks uniqueness of the wire
    public boolean isUniqueWire(int[] start, int[] end){
        
        Wire w = new Wire(start,end,Color.BLACK); // Any color works as it is a check
        
        if (wireArray != null)
            for (int i=0;i<wireArray.length;i++)
                if (wireArray[i].equals(w))
                    return false;
        return true;
        
    }
    
    //----------------- WIRE NODE MANAGER -----------------//
    
    // Draws the wire nodes of the active components
    public void drawWireNodePositions(Graphics g, int x, int y){
        
        if (componentsArray != null)
            for (int i=0;i<componentsArray.length;i++){
                
                int mouseDistance = getDistance(x,
                        y,
                        componentsArray[i].getCentreX(),
                        componentsArray[i].getCentreY());
                
                if (mouseDistance < componentsArray[i].getRadius()){
                 
                    int[] tempWN = componentsArray[i].getWireNodes();
                    g.setColor(Color.RED);
                    
                    for (int j=0;j<tempWN.length;j=j+2){
                        
                        g.fillOval(tempWN[j]-3, 
                                tempWN[j+1]-3, 
                                7, 
                                7);
                        
                    }
                    
                }
            }
        
    }
    
    /* 
     * Looks through each component filtering the unactive ones based
     * on the position (x,y) and then returns the position of the closest
     * wire node of the active component(s)
     */
    public int[] getClosestWireNodePosition(int x,int y){
        
        int[] posn = null;
        int distance = -1;
        
        if (componentsArray != null)
            for (int i=0;i<componentsArray.length;i++){
                
                int mouseDistance = getDistance(x,
                        y,
                        componentsArray[i].getCentreX(),
                        componentsArray[i].getCentreY());
                
                // Filters for active components
                if (mouseDistance < componentsArray[i].getRadius()){
                 
                    int[] tempWN = componentsArray[i].getWireNodes();
                    
                    // Gets closest wire node
                    for (int j=0;j<tempWN.length;j=j+2){
                        
                        int d = getDistance(x,
                                y,
                                tempWN[j],
                                tempWN[j+1]);
                        
                        if (distance == -1 || d < distance){
                            
                            distance = d;
                            posn = new int[] {tempWN[j],
                                tempWN[j+1]};
                            
                        }
                        
                    }
                    
                }
            }
        
        return posn;
        
    }
    
    //----------------- COMPONENTS MANAGER -----------------//
    
    // Draws all components
    private void drawComponents(Graphics g){
        
        if (componentsArray != null)
            for (int i=0;i<componentsArray.length;i++){
                
                AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(componentsArray[i].getRotation()), 
                        componentsArray[i].getWidth()/2,
                        componentsArray[i].getHeight()/2);
                AffineTransformOp txop = new AffineTransformOp(tx,
                        AffineTransformOp.TYPE_BILINEAR);
                
                g.drawImage(txop.filter(componentsArray[i].getImage(),null), 
                        componentsArray[i].getX(), 
                        componentsArray[i].getY(), 
                        null);
                
            }
            
    }
    
    // Adds a new component into the set and adds it into the array
    public void addNewComponent(String n,BufferedImage bi, int x, int y, double r, int[] wn){
        
        componentsSet.add(new Component(n,bi,x,y,r,wn));
        componentsArray = componentsSet.toArray(new Component[0]);
        
    }
    
    // Removes the first detected component at point (x,y) from the set and updates
    // the array
    public void removeComponent(int x, int y){
        
        boolean isRemoved = false;
        componentsSet = new TreeSet<>();
        
        for (int i=0;i<componentsArray.length;i++){
            
            if (componentsArray[i].getX() == x &&
                    componentsArray[i].getY() == y &&
                    !isRemoved){
                
                this.removeAllComponentsWires(componentsArray[i].getWireNodes());
                isRemoved = true;
                continue;
            
            }
            
            componentsSet.add(componentsArray[i]);
            
        }
        
        componentsArray = componentsSet.toArray(new Component[0]);
        
    }
    
    // Removes the component closest to the point (x,y)
    public void removeClosestComponent(int x, int y){
        
        int[] posn = getClosestComponentPosition(x,y,false);
        if (posn != null)
            this.removeComponent(posn[0], posn[1]);
        
    }
    
    // Returns the position (of the center of the component) that is closest to the position (x,y)
    public int[] getClosestComponentPosition(int x, int y, boolean needsCentre){
        
        if (componentsArray == null || componentsArray.length < 1)
            return null;
        else{
            
            // Initial closest component
            int index = 0;
            int distance = getDistance(x,
                    y,
                    componentsArray[0].getCentreX(),
                    componentsArray[0].getCentreY());
            
            // Checks if any component is closer
            for (int i=1;i<componentsArray.length;i++){
                
                int d = getDistance(x,
                        y,
                        componentsArray[i].getCentreX(),
                        componentsArray[i].getCentreY());
                
                if (d < distance){
                    
                    distance = d;
                    index = i;
                    
                }
                
            }
            
            if (needsCentre)
                return new int[] {componentsArray[index].getCentreX(), 
                    componentsArray[index].getCentreY()};
            else
                return new int[] {componentsArray[index].getX(),
                    componentsArray[index].getY()};
            
        }
        
    }
    
    //----------------- UNIVERSAL -----------------//
    
    // Used to calculate the distance between two points
    private int getDistance(int x1, int y1, int x2, int y2){
        
        int distance = (int) Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1, 2));
        if (distance<0)
            return -distance;
        else
            return distance;
        
    }
    
    //----------------- ACCESSORS -----------------//
    
    public Wire[] getAllWires(){
        return wireArray;
    }
    public Component[] getAllComponents(){
        return componentsArray;
    } 
    

}
