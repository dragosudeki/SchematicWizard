
package schematicwizard.Windows.IOManager;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * ComponentLoader - Brandon Altarejos
 * Created on August 5, 2013
 *
 * This will read the text files that will be associated with the original
 * component list (Components.txt included in .jar) and the custom
 * component list (CustomComponents.txt located outside of .jar) and add them
 * to a list for reference.
 * 
 **/

public class ComponentLoader {
    
    private Set<ComponentData> componentList;
    
    public ComponentLoader(){
        
        componentList = null;
        
        // Loading the component
        try {
            
            //componentFile = new File(this.getClass().getResource("Components.txt").toURI());
            this.loadOriginalComponents();
        
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, 
                        ex.getMessage(), 
                        "ComponentLoader() Error:", 
                        JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, 
                        ex.getMessage(), 
                        "ComponentLoader() Error:", 
                        JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    // Will load all the original components that were supplied with the build
    private void loadOriginalComponents() throws FileNotFoundException, IOException{
        
        componentList = new TreeSet<>();
        
        // Adds all components to the list
        BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("Components.txt")));
        
        String name = br.readLine();
        BufferedImage img;
        
        while (name != null){
            
            try{
                
                img = ImageIO.read(this.getClass().getResource("Images/" + name + " Symbol.GIF"));
                
                int points = Integer.parseInt(br.readLine());
                int[] wireNodes = new int[points*2];
                
                for (int i=0;i<wireNodes.length;i++){
                    
                    wireNodes[i] = Integer.parseInt(br.readLine());
                    
                }
                
                componentList.add(new ComponentData(name,img,wireNodes));
            
            }catch(IOException | IllegalArgumentException ex){
             
                JOptionPane.showMessageDialog(null, 
                        "There was a problem finding a symbol for "+name, 
                        "loadOriginalComponents() Error:", 
                        JOptionPane.ERROR_MESSAGE);
                
            }
            
            name = br.readLine();
            
        }
        
    }
    
    //----------------- ACCESSORS -----------------//
    
    public ComponentData[] getComponentDataList(){
        return componentList.toArray(new ComponentData[0]);
    }
    
    // Allows other classes to have access to the list of components
    public String[] getComponentNameList(){
        
        ComponentData[] compDataList = componentList.toArray(new ComponentData[0]);
        String[] compNameList = new String[compDataList.length];
        
        for (int i=0;i<compDataList.length;i++){
            compNameList[i] = compDataList[i].getName();
        }
        
        return compNameList;
    }
    
    // Gets the image of the component with the name, componentName
    public BufferedImage getComponentImage(String componentName){
        
        ComponentData[] compDataList = componentList.toArray(new ComponentData[0]);
        
        for (int i=0;i<compDataList.length;i++){
            
            if (compDataList[i].getName().equals(componentName))
                return compDataList[i].getImage();
            
        }
        
        return null;
        
    }
    
    // Gets the wire nodes of the component with the name, componentName
    public int[] getComponentWireNodes(String componentName){
        
        ComponentData[] compDataList = componentList.toArray(new ComponentData[0]);
        
        for (int i=0;i<compDataList.length;i++){
            
            if (compDataList[i].getName().equals(componentName))
                return compDataList[i].getWireNodes();
            
        }
        
        return null;
        
    }
    
    // Gives the data of the component with name componentName
    public ComponentData getComponentData(String componentName){
        
        ComponentData[] compDataList = componentList.toArray(new ComponentData[0]);
        
        for (int i=0;i<compDataList.length;i++){
            
            if (compDataList[i].getName().equals(componentName))
                return compDataList[i];
            
        }
        
        return null;
        
    }
    
    // Checks if the componentName is part of the component list
    public boolean isAKnownComponent(String componentName){
        
        ComponentData[] compDataList = componentList.toArray(new ComponentData[0]);
        
        for (ComponentData cd: compDataList)
            if (cd.getName().equals(componentName))
                return true;
        
        return false;
        
    }
    
}
