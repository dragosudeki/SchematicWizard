
package schematicwizard.Windows;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import schematicwizard.Windows.IOManager.ComponentData;

/** 
 *
 * ComponentStation - Brandon Altarejos
 * Created on July 27, 2013
 *
 * This is the component list window which will contain a list of all
 * of the components, so the user can select them to use them.
 * 
 **/

public class ComponentStation extends JInternalFrame{
    
    MainInterface desk ;
    private JList list ;
    private JLabel image;
    int listIndex = 0;
    
    // Component List
    private ComponentData[] components;
    
    // The Component Station (JInternalFrame with only a JList)
    @SuppressWarnings("unchecked")
    ComponentStation (MainInterface d) {
        
        // Setting up the new JInternalFrame
        super("Components");
        this.setLocation(0,0);
        this.desk = (MainInterface)d;
        
        // Orders the components lexicographically
        components = desk.componentLoader.getComponentDataList();
        
        // Sets up the picture of component for the window
        
        // Sets up the list for the window
        list = new JList(desk.componentLoader.getComponentNameList());
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setSelectedIndex(0);
        list.addListSelectionListener(new ComponentSelection());
        list.setVisibleRowCount(10);
        
        // Sets up the scroller for the list
        JScrollPane listScroller = new JScrollPane(list);
        add (listScroller);
        
        desk.PROG_component = components[listIndex].getName();
        
        pack();
        setVisible(true);
        
    }
    
    // The Component Station (JInternalFrame with only a JList)
    @SuppressWarnings("unchecked")
    ComponentStation (ComponentStation componentStation, int visibleRow) {
        
        // Setting up the new JInternalFrame
        super("Components");
        this.setLocation(componentStation.getLocation());
        this.desk = componentStation.desk;
        
        // Orders the components lexicographically
        this.components = componentStation.components;
        
        // Sets up the picture of component for the window
        
        // Sets up the list for the window
        list = new JList(desk.componentLoader.getComponentNameList());
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setSelectedIndex(componentStation.list.getSelectedIndex());
        list.addListSelectionListener(new ComponentSelection());
        list.setVisibleRowCount(visibleRow);
        
        // Sets up the scroller for the list
        JScrollPane listScroller = new JScrollPane(list);
        add (listScroller);
        
        pack();
        componentStation.dispose();
        setVisible(true);
        
    }
    
    // Toggles the visibility of the Component Station
    void toggleHide(){
        
        this.setVisible(!this.isVisible());
        
    }
    
    // Handles the user's interaction with the list
    private class ComponentSelection implements ListSelectionListener{

        @Override
        public void valueChanged(ListSelectionEvent e) {
            
            int num = list.getSelectedIndex();
            desk.PROG_component = components[num].getName();
            
        }
        
    }

}
