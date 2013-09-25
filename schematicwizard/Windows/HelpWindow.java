
package schematicwizard.Windows;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import schematicwizard.Windows.IOManager.FileSaver;

/**
 *
 * HelpWindow - Brandon Altarejos
 * Created on September 1, 2013
 *
 * This will function as the window that will explain the features of the
 * program, as well as hold credit to everything done.
 * 
 **/

public class HelpWindow extends JFrame implements ListSelectionListener{

    // All the help labels
    String[] helpList = new String[]{"Save / Load",
        "Wire",
        "Unwire",
        "Add Component",
        "Remove Component",
        "Credits"};
    String wireHelp = "The tool, Wire, will add a wire to the station.\n"
            + "When you are close to an available wire node of "
            + "a component, the work station will show a line that leads to the "
            + "node, as well as draw a circle on top of the wire node.\n"
            + "When you first click in the proximity of a wire node, "
            + "it will keep track of that first wire node until you click near "
            + "another wire node.";
    String unwireHelp = "The tool, Unwire, will remove an existing wire from the "
            + "station.\n"
            + "When you are close to the middle of a wire, the work station will "
            + "draw a line to the closest midpoint of an existing wire.\n"
            + "Clicking will cause that wire to be removed. ";
    String addcomponentHelp = "The tool, Add Component, will add a new component "
            + "to the work station.\n"
            + "The component that will be added will be based off what is "
            + "selected in the components window.\n"
            + "If you cannot see the components window, go to "
            + "Options>Components>Hide, and make sure the check box is unchecked.\n"
            + "Pressing the left mouse button will lock the component in place.\n"
            + "Dragging it horizontally will cause the component to rotate.\n"
            + "Letting go of the button will add the component.\n";
    String removecomponentHelp = "The tool, Remove Component, will remove an "
            + "existing component.\n"
            + "The work station will show a line from the mouse that leads to "
            + "the middle of the closest component.\n"
            + "Clicking will remove the closest component and all wires "
            + "connected to it.\n";
    String creditsHelp = "All programming was done by Brandon Altarejos\n"
            + "All initial component pictures were taken from "
            + "http://www.rapidtables.com/electric/electrical_symbols.htm";
    
    int currentIndex = 0;
    
    JPanel helpWindowContent;
    JTextArea textArea;
    JList list;
    
    @SuppressWarnings("LeakingThisInConstructor")
    HelpWindow(Rectangle r){
        
        super("Help");
        this.setBounds(r.x+(r.width/4), 
                r.y+(r.height/4), 
                r.width/2, 
                r.height/2);
        
        helpWindowContent = new JPanel();
        helpWindowContent.setLayout(new BoxLayout(helpWindowContent,BoxLayout.X_AXIS));
        
        list = new JList(helpList);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(helpList.length);
        list.addListSelectionListener(this);
        
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setMinimumSize(new Dimension(r.width/8,r.height/8));
        helpWindowContent.add(listScroller);
        
        textArea = new JTextArea(this.getCurrentHelpText(helpList[list.getSelectedIndex()]));
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        helpWindowContent.add(textArea);
        
        this.add(helpWindowContent);
        this.setResizable(false);
        this.setVisible(true);
        
        
        
    }
    
    //----------------- HELPERS -----------------//
    
    private void updateTextArea(){
        
        textArea.setText(this.getCurrentHelpText(helpList[list.getSelectedIndex()]));
        
        
    }
    
    // Deals with preparing the text for the currently selected index
    private String getCurrentHelpText(String s){
        
        switch (s) {
            case "Save / Load":
                return "All saves should be found in " + FileSaver.SAVE_DIRECTORY
                        + File.separator + " and use the file type "
                        + FileSaver.SAVE_FILE_TYPE + ". "
                        + "Saves have a specific save format, so loading random "
                        + FileSaver.SAVE_FILE_TYPE + " files may not work.";
            case "Wire":
                return wireHelp;
            case "Unwire":
                return unwireHelp;
            case "Add Component":
                return addcomponentHelp;
            case "Remove Component":
                return removecomponentHelp;
            case "Credits":
                return creditsHelp;
            default:
                return "";
        }
        
    }

    //----------------- LIST SELECTION LISTENER -----------------//

    @Override
    public void valueChanged(ListSelectionEvent e) {
        
        this.updateTextArea();
        
    }

}
