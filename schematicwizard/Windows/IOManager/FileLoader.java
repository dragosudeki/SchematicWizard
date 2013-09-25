
package schematicwizard.Windows.IOManager;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import schematicwizard.Windows.GraphicManager.DrawManager;
import schematicwizard.Windows.MainInterface;

/**
 *
 * FileLoader - Brandon Altarejos
 * Created on August 29, 2013
 *
 * This takes care of taking care of data from the Saves directory,
 * and warn the user if any problem is encountered when following the strict
 * saving from FileSaver. It is an object, so that it can take the file
 * and be 'injected' into a Draw Station, add things itself into the
 * Draw Station.
 * 
 **/

public class FileLoader extends JDialog implements ActionListener{

    // The objects that are displayed on the small FileLoader frame
    private JList list = null;
    private MainInterface main;
    
    // The holders of the data of the files that you can load and the file
    // that you are going to load
    private boolean isCanceled = false;
    private String currentFile = null;
    private String[] fileNames = null;
    
    // The default file loader
    public FileLoader(MainInterface m){
        
        super(m,"Load");
        this.main = m;
        fileNames = getFileNames();
        
        if (fileNames == null || fileNames.length < 1){
        
            isCanceled = true;
            JOptionPane.showMessageDialog(null, 
                    "No save file has been detected", 
                    "Error:", 
                    JOptionPane.ERROR_MESSAGE);
        
        }else{
            
            this.setModal (true);
            this.setAlwaysOnTop (true);
            this.setResizable(false);
            this.setModalityType (ModalityType.APPLICATION_MODAL);
            
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
            
            list = new JList(fileNames);
            list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            list.setLayoutOrientation(JList.VERTICAL);
            list.setSelectedIndex(0);
            currentFile = fileNames[0];
            list.setVisibleRowCount(10);
            
            JScrollPane listScroller = new JScrollPane(list);
            panel.add (listScroller);
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
            
            JButton button = new JButton("Load");
            button.addActionListener(this);
            button.setActionCommand("LOAD");
            buttonPanel.add(button);
            
            button = new JButton("Cancel");
            button.addActionListener(this);
            button.setActionCommand("CANCEL");
            buttonPanel.add(button);
            
            panel.add(buttonPanel);
            
            this.add(panel);
            this.pack();
            Rectangle b = main.getBounds();
            this.setLocation(b.x + (b.width/2) - (this.getWidth()/2), 
                    b.y + (b.height/2) - (this.getHeight()/2));
            this.setVisible(true);
            
        }
        
    }
    
    // Searches if a save already exists with the name n
    private String[] getFileNames(){
        
        File directory = new File(FileSaver.SAVE_DIRECTORY);
        File[] saves = directory.listFiles(new FilenameFilter(){
                
            @Override
            public boolean accept(File dir, String filename){
                
                return filename.endsWith(FileSaver.SAVE_FILE_TYPE);
            
            }
        
        });
        
        if (saves == null || saves.length < 1)
            return null;
        
        String[] names = new String[saves.length];
        
        for (int i=0;i<saves.length;i++)
            names[i] = saves[i].getName().substring(0,
                    saves[i].getName().length()-FileSaver.SAVE_FILE_TYPE.length());
        
        return names;
        
    }
    
    
    
    //----------------- ACCESSORS -----------------//
    public boolean isCanceled(){
        return isCanceled;
    }
    public String getFileName(){
        return currentFile;
    }
    public String getFilePath(){
        return FileSaver.SAVE_DIRECTORY+File.separator+this.getFileName()+FileSaver.SAVE_FILE_TYPE;
    }
    
    //----------------- FILE LOADER -----------------//
    
    // Checks if the String s follows the String sPredict
    private boolean followsSaveFormat(String s, String sPredict){
        
        if (s != null && s.equals(sPredict))
            return true;
        
        JOptionPane.showMessageDialog(main,
                "The file loaded is not in the correct save format. "
                + "Please make sure you are loading from a file created by this program.\n"
                + s+" was read.\n"
                + sPredict+" was expected.", 
                "File Loader Error:", 
                JOptionPane.ERROR_MESSAGE);
        return false;
        
    }
    
    // Creates a string of '-' with the length of the String s
    private String createFollowingLine(String s){
        
        String r = "";
        for (int i=0;i<s.length();i++)
            r = r.concat("-");
        return r;
        
    }
    
    private boolean continueLoadingWithoutComponent(String componentName){
        
        int n = JOptionPane.showConfirmDialog(main, 
                componentName+" cannot be found in the list of components."
                + "Ignoring the error can result in:\n"
                + "- Initially there will 'floating' wires that lead to the missing components, you can remove "
                + "by using the unwire tool\n"
                + "- Saving will cause the missing component to be excluded in the save, losing it's data.\n"
                + " Would you like to continue?",
                "Ignore Missing Component:",
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.INFORMATION_MESSAGE);
        
        return (n == JOptionPane.YES_OPTION);
        
    }
    
    // Loads the data following the format used by FileSaver.writeDataToSaveFile(File save, DrawStation d)
    public boolean loadAllData(DrawManager dm, ComponentLoader cl){
        
        try {
            
            BufferedReader br = new BufferedReader(new FileReader(this.getFilePath()));
            
            String line = br.readLine(); // Eliminates the name
            if(!this.followsSaveFormat(line,
                    this.getFileName()+FileSaver.SAVE_FILE_TYPE))
                return false;
            
            line = br.readLine(); // Eliminates the following line
            if(!this.followsSaveFormat(line,
                    this.createFollowingLine(this.getFileName()+FileSaver.SAVE_FILE_TYPE)))
                return false;
            
            line = br.readLine(); // Eliminates the number of components
            if (!line.endsWith(" Components"))
                return false;
            int n = Integer.parseInt(line.substring(0, line.length()-11));
            
            String header = line;
            line = br.readLine(); // Eliminates the following line
            if (!this.followsSaveFormat(line, this.createFollowingLine(header)))
                return false;
            
            // Goes through all components
            for (int i=0;i<n;i++){
                
                line = br.readLine(); // Eliminates the name
                
                if(!cl.isAKnownComponent(line)){
                    
                    if (this.continueLoadingWithoutComponent(line)){ // Chooses to continue
                        
                        br.readLine(); // Eliminates the x
                        br.readLine(); // Eliminates the y
                        br.readLine(); // Eliminates the rotation
                        
                    }else // Chooses to stop the loading process
                        return false;
                
                }else{ // Component is in the list of components
                    
                    int x = Integer.parseInt(br.readLine()); // Eliminates the x
                    int y = Integer.parseInt(br.readLine()); // Eliminates the y
                    double r = Double.parseDouble(br.readLine()); // Eliminates the rotation
                    dm.addNewComponent(line, 
                            cl.getComponentImage(line), 
                            x, 
                            y, 
                            r, 
                            cl.getComponentWireNodes(line));
                
                }
                
            }
            
            line = br.readLine(); // Eliminates the following line
            if (!this.followsSaveFormat(line, this.createFollowingLine(header)))
                return false;
            
            line = br.readLine(); // Eliminates the number of wires
            if (!line.endsWith(" Wires"))
                return false;
            n = Integer.parseInt(line.substring(0, line.length()-6));
            
            header = line;
            line = br.readLine(); // Eliminates the following line
            if (!this.followsSaveFormat(line, this.createFollowingLine(header)))
                return false;
            
            // Goes through all wires
            for (int i=0;i<n;i++){
                
                int x1 = Integer.parseInt(br.readLine()); // Eliminates the x
                int y1 = Integer.parseInt(br.readLine()); // Eliminates the y1
                int x2 = Integer.parseInt(br.readLine()); // Eliminates the x2
                int y2 = Integer.parseInt(br.readLine()); // Eliminates the y2
                int r = Integer.parseInt(br.readLine()); // Eliminates the red
                int b = Integer.parseInt(br.readLine()); // Eliminates the blue
                int g = Integer.parseInt(br.readLine()); // Eliminates the green
                dm.addWire(new int[] {x1, y1}, 
                        new int[] {x2, y2}, 
                        new Color(r,g,b));
                
            }
            
            line = br.readLine(); // Eliminates the following line
            if (!this.followsSaveFormat(line, this.createFollowingLine(header)))
                return false;
            
            br.close();
            
            return true;
            
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(main, 
                    ex.getMessage(), 
                    "loadAllData() FileNotFound Error:", 
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(main, 
                    ex.getMessage(), 
                    "loadAllData() IOException Error:", 
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(main, 
                    "Expected a line to be an integer, but got something else."
                    + "Please make sure you are loading from a file created by this program.", 
                    "Loading Error:", 
                    JOptionPane.ERROR_MESSAGE);
        }
        
        return false;
        
    }
    
    //----------------- ACTION LISTENER -----------------//
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getActionCommand().equals("LOAD")){
            
            currentFile = fileNames[list.getSelectedIndex()];
            this.setVisible(false);
            main.loadDrawStation(this);
        
        }else{
            
            System.out.println("FILELOADER CLOSE");
            this.dispose();
            
        }
        
        
        
    }

}
