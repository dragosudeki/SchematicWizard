
package schematicwizard.Windows.IOManager;

import java.awt.image.BufferedImage;

/**
 *
 * ComponentData - Brandon Altarejos
 * Created on August 5, 2013
 *
 * This will hold the image and name of the component, which will be created
 * from the Component Loader
 * 
 **/

public class ComponentData implements Comparable<ComponentData>{

    private BufferedImage componentImage;
    private String componentName;
    
    // Every pair of integers will contain the position of the wire
    // node
    private int[] wireNodes;
    
    // ComponentData Object
    ComponentData(String name,BufferedImage image, int[] wn){
        
        this.componentName = name;
        this.componentImage = image;
        wireNodes = wn;
        
    }
    
    //----------------- COMPARABLE<COMPONENT> -----------------//
    // Alphabetically compares using the name of the component
    @Override
    public int compareTo(ComponentData o){
        
        return this.componentName.compareTo(o.componentName);
    
    }
    
    //----------------- ACCESSORS -----------------//
    public BufferedImage getImage(){
        return this.componentImage;
    }
    public String getName(){
        return this.componentName;
    }
    public int[] getWireNodes(){
        return this.wireNodes;
    }
    
}
