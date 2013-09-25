
package schematicwizard.Windows.IOManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
import schematicwizard.Windows.DrawStation;
import schematicwizard.Windows.GraphicManager.Component;
import schematicwizard.Windows.GraphicManager.Wire;

/**
 *
 * FileSaver - Brandon Altarejos
 * Created on August 28, 2013
 *
 * This takes care of saving the data from a Draw Station into the 
 * Saves directory with the correct formatting of saving. It contains
 * static methods because a save object is not needed, as it will do the
 * same thing with the given parameters
 * 
 **/

public class FileSaver {
    
    public static final String SAVE_DIRECTORY = System.getProperty("user.dir")
            +File.separator+"Saves";
    public static final String SAVE_FILE_TYPE = ".txt";
    
    // Asks user for the save name and saves the file
    public static void saveDrawStationData(DrawStation d, boolean isSaveAs){
        
        String name;
        
        if (!d.hasSaved || isSaveAs)
            name = JOptionPane.showInputDialog(null, 
                    "Enter the save name:", 
                    "Save Name:", 
                    JOptionPane.QUESTION_MESSAGE);
        else
            name = d.getTitle();
        
        setUpSaveFile(name,d);
        
    }
    
    // Searches if a save already exists with the name n
    private static File checkExistingSaves(String n){
        
        File directory = new File(SAVE_DIRECTORY);
        // If the directory doesnt exists immediately make it and send the file
        if (!directory.exists()){
            
            directory.mkdir();
            return new File(SAVE_DIRECTORY+File.separator+n+SAVE_FILE_TYPE);
            
        }
        
        File[] saves = directory.listFiles(new FilenameFilter(){
                
            @Override
            public boolean accept(File dir, String filename){
                
                return filename.endsWith(SAVE_FILE_TYPE);
            
            }
        
        });
        
        if (saves != null)
            for (File save:saves)
                if (save.getName().equals(n+SAVE_FILE_TYPE))
                    return save;
        
        return new File(SAVE_DIRECTORY+File.separator+n+SAVE_FILE_TYPE);
        
    }
    
    // Prepares the save the file while clearing up overwriting commands
    private static void setUpSaveFile(String n, DrawStation d){
        
        File newSave = checkExistingSaves(n);
        
        if (newSave.exists() && !d.getTitle().equals(n)){
            
            int confirm = JOptionPane.showConfirmDialog(null, 
                    n+SAVE_FILE_TYPE+" already exists. Overwrite existing file?", 
                    "Overwrite File?", 
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.NO_OPTION)
                return;
            
        }else if (!newSave.exists())
            try{
                
                newSave.createNewFile();
            
            }catch (IOException ex){
                
                JOptionPane.showMessageDialog(null,
                        ex.getMessage(), 
                        "setUpSaveFile() Error:", 
                        JOptionPane.ERROR_MESSAGE);
                return;
                
            }
        
        d.setTitle(n);
        writeDataToSaveFile(newSave, d);
        
    }
    
    // Writes to the new save file all the data in DrawStation d
    private static void writeDataToSaveFile(File save, DrawStation d){
        
        try (PrintWriter writer = new PrintWriter(save)){
            
            writer.println(save.getName());
            for (int i=0;i<save.getName().length();i++)
                writer.print("-");
            writer.println();
            
            int length;
            
            Component[] c = d.getDrawManager().getAllComponents();
            if (c == null || c.length<1)
                length = 0;
            else
                length = c.length;
            writer.println(length+" Components");
            for (int i=0;i<Math.max(Math.log10(length),1)+11;i++)
                writer.print("-");
            writer.println();
            
            // COMPONENTS
            if (c != null)
                for(Component currentC:c){

                    writer.println(currentC.getName());
                    writer.println(currentC.getX());
                    writer.println(currentC.getY());
                    writer.println(currentC.getRotation());

                }
            
            for (int i=0;i<Math.max(Math.log10(length),1)+11;i++)
                writer.print("-");
            writer.println();
            
            // WIRES
            Wire[] w = d.getDrawManager().getAllWires();
            if (w == null || w.length<1)
                length = 0;
            else
                length = w.length;
            writer.println(length+" Wires");
            for (int i=0;i<Math.max(Math.log10(length),1)+6;i++)
                writer.print("-");
            writer.println();
            
            if (w != null)
                for (Wire currentW:w){

                    writer.println(currentW.getX1());
                    writer.println(currentW.getY1());
                    writer.println(currentW.getX2());
                    writer.println(currentW.getY2());
                    writer.println(currentW.getColor().getRed());
                    writer.println(currentW.getColor().getBlue());
                    writer.println(currentW.getColor().getGreen());

                }
            
            for (int i=0;i<Math.max(Math.log10(length),1)+6;i++)
                writer.print("-");
            
            writer.close();
        
        }catch(FileNotFoundException ex){
            
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(), 
                    "writeDataToSaveFile() Error:", 
                    JOptionPane.ERROR_MESSAGE);
            
        }
        
    }

}
