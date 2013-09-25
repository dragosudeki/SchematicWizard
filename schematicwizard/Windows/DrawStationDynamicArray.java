
package schematicwizard.Windows;

import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;
import schematicwizard.Windows.IOManager.FileLoader;

/**
 *
 * DrawStationDynamicArray - Brandon Altarejos
 * Created on July 30, 2013
 *
 * The Dynamic Array Structure for the Draw Station which allows
 * the user to have an infinite number of Draw Station windows
 * 
 **/

public class DrawStationDynamicArray {

    // Properties of the Dynamic Draw Station Array
    private DrawStation[] drawStationDArray;
    public int size;
    private int counter;
    
    // Properties for Draw Stations
    int addMode = 0;
    
    // The Constructor for the Array - Default
    DrawStationDynamicArray(){
        
        drawStationDArray = null;
        size = 0;
        counter = 0;
        
    }
    
    // Adds a new Draw Station into the dynamic array while updating it's properties
    DrawStation addNewDrawStation(MainInterface main){
        
        size++;
        counter++;
        
        if (drawStationDArray == null){
            
            // Starts a new Draw Station and starts the dynamic array
            drawStationDArray = new DrawStation[size];
            drawStationDArray[0] = new DrawStation(main,size);
            
        }else{
            
            // Creates a larger copy of the array and adds a new draw station
            DrawStation[]  drawStationDArrayTemp = new DrawStation[size];
            copyArray(drawStationDArray,drawStationDArrayTemp);
            drawStationDArrayTemp[size-1] = new DrawStation(main,counter);
            drawStationDArray = new DrawStation[size];
            copyArray(drawStationDArrayTemp,drawStationDArray);
            
        }
        
        return drawStationDArray[size-1];
        
    }
    
    /*
     * Adds a new draw station a injects the file loader into it. If the file
     * loader is successful then it is added to the array and returned, 
     * otherwise it returns null
     */
    DrawStation addNewDrawStation(MainInterface main, FileLoader load){
        
        DrawStation d = new DrawStation(main,load);
        
        if (!d.isLoadComplete())
            return null;
        
        size++;
        counter++;
        
        if (drawStationDArray == null){
            
            // Starts a new Draw Station and starts the dynamic array
            drawStationDArray = new DrawStation[size];
            drawStationDArray[0] = new DrawStation(main,size);
            
        }else{
            
            // Creates a larger copy of the array and adds a new draw station
            DrawStation[]  drawStationDArrayTemp = new DrawStation[size];
            copyArray(drawStationDArray,drawStationDArrayTemp);
            drawStationDArrayTemp[size-1] = new DrawStation(d);
            drawStationDArray = new DrawStation[size];
            copyArray(drawStationDArrayTemp,drawStationDArray);
            
        }
        
        return drawStationDArray[size-1];
        
    }
    
    //----------------- DRAW STATION MANAGER -----------------//
    
    // Copies the orig array into the dest array
    private void copyArray(DrawStation[] orig, DrawStation[] dest){
        
        for (int i=0;i<orig.length;i++){
            
            dest[i] = new DrawStation(orig[i]);
            orig[i].dispose();
            
        }
        
    }
    
    // Removes the desired frame from the array disposes
    void removeDrawStation(String name){
        
        size--;
        
        DrawStation[] drawStationDArrayTemp = new DrawStation[size];
        copyArrayExclude(drawStationDArray,drawStationDArrayTemp,name);
        drawStationDArray = new DrawStation[size];
        copyArray(drawStationDArrayTemp,drawStationDArray);
        
    }
    
    // Copies the orig array into the dest array and excludes the element with the name ex
    private void copyArrayExclude(DrawStation[] orig, DrawStation[] dest, String ex){
        
        int j = 0;
        
        for (int i=0;i<orig.length;i++){
            
            if (!orig[i].getTitle().equals(ex)){
                
                dest[j] = new DrawStation(orig[i]);
                j++;
                
            }
            
            orig[i].dispose();
            
        }
        
    }
    
    // Adds each draw station in the array into the JDesktopPane
    void updateDrawStations(JDesktopPane desktop){
        
        for (int i=0;i<size;i++){
            
            desktop.add(drawStationDArray[i]);
            
        }
        
        if (size > 0)
            try {
            
                drawStationDArray[size-1].setSelected(true);
            
            } catch (PropertyVetoException ex) {
            
                System.err.println(ex.getMessage());
        
            }
        
    }
    
    //----------------- ACCESSORS -----------------//
    
    DrawStation getActiveDrawStation(){
        
        for (DrawStation d:drawStationDArray)
            if (d.isSelected())
                return d;
        
        return null;
        
    }

}
