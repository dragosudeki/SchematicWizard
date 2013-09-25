
package schematicwizard.Windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import schematicwizard.Windows.IOManager.ComponentLoader;
import schematicwizard.Windows.IOManager.FileLoader;
import schematicwizard.Windows.IOManager.FileSaver;

/**
 *
 * MainInterface - Brandon Altarejos
 * Created on July 27, 2013
 *
 * This is the main window of the program. It contains all of the
 * functions of the program and connects all of the programs together.
 * 
 **/

public class MainInterface extends JFrame implements ActionListener{
    
    // Window Properties of the Main Interface
    final int WIND_INDEX = 100;
    final Dimension WIND_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    String LOGO = "Images/SchematicWizard.png";
    boolean WIND_decorated = true ;
    
    // File IO Handlers
    ComponentLoader componentLoader;
    FileLoader fileLoader;
    
    // Frames Inside of the Main Interface
    JDesktopPane desktop;
    ComponentStation componentStation;
    DrawStationDynamicArray drawStations;
    HelpWindow helpWindow;
    
    // Shared Program Information
    String PROG_mode ;
    String PROG_component ;
    Color PROG_colorW = Color.BLACK;
    
    // The Main Interface (JDesktopFrame)
    public MainInterface(){
        
        // Setting up the visual properties of the new JFrame
        super("Schematic Wizard");
        this.setBounds(WIND_INDEX, 
                WIND_INDEX,
                WIND_SIZE.width - WIND_INDEX*2,
                WIND_SIZE.height - WIND_INDEX*2);
        JFrame.setDefaultLookAndFeelDecorated(WIND_decorated);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        // Changes the icon of the JFrame
        try {
            this.setIconImage(ImageIO.read(this.getClass().getResource(LOGO)));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        
        desktop = new JDesktopPane();
        this.setContentPane(desktop);
        this.setJMenuBar(this.createMenuBar());
        
        componentLoader = new ComponentLoader();
        
        PROG_mode = "None";
        PROG_component = "None";
        
        // Displays the miniature windows inside the JFrame
        this.createComponentStation();
        drawStations = new DrawStationDynamicArray();
        this.createDrawStation();
        
        System.out.println(System.getProperty("user.dir"));
        
        // Displays the JFrame
        this.setVisible(true);
        
    }
    
    // Creates the Component Station - Default
    private void createComponentStation(){
    
        componentStation = new ComponentStation(this);
        desktop.add(componentStation);
    
    }
    
    // Creates the Component Station - Customized
    private void createComponentStation(int visibleRow){
    
        componentStation = new ComponentStation(componentStation,visibleRow);
        desktop.add(componentStation);
    
    }
    
    // Creates the Draw Station
    private DrawStation createDrawStation(){
        
        DrawStation d = drawStations.addNewDrawStation(this);
        drawStations.updateDrawStations(this.desktop);
        return d;
        
    }
    
    // Attempts to load a draw station
    public DrawStation loadDrawStation(FileLoader fl){
        
        DrawStation d = drawStations.addNewDrawStation(this, fl);
        if (d==null)
            return null;
        drawStations.updateDrawStations(this.desktop);
        return d;
        
    }
    
    // Removes the draw station with the name ex
    void removeDrawStation(String ex){
        
        drawStations.removeDrawStation(ex);
        drawStations.updateDrawStations(this.desktop);
        
    }
    
    private int getComponentRowCount(){
        
        String input = JOptionPane.showInputDialog(null,
                    "Please enter an integer value",
                    "Change the number of visible components",
                    JOptionPane.QUESTION_MESSAGE);
        int n;
        
        try{
            
            n = Integer.parseInt(input);
            
        }catch(NumberFormatException nfe){
            
            System.err.println(nfe.getMessage());
            return -1;
            
        }
        
        return n;
        
    }
    
    //----------------- MENU BAR -----------------//
    private JMenuBar createMenuBar(){
        
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu Setup
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menu);
        
        JMenuItem menuItem = new JMenuItem("New");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, 
                ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("NEW");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Save");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 
                ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("SAVE");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Save As");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 
                ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
        menuItem.setActionCommand("SAVE-AS");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Load");
        menuItem.setMnemonic(KeyEvent.VK_L);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, 
                ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("LOAD");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Help");
        menuItem.setActionCommand("HELP");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Quit");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 
                ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("QUIT");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        // Tools Menu Setup
        menu = new JMenu("Tools");
        ButtonGroup group = new ButtonGroup();
        menu.setMnemonic(KeyEvent.VK_M);
        menuBar.add(menu);
        
        JRadioButtonMenuItem menuItemRadio = new JRadioButtonMenuItem("Wire");
        menuItemRadio.setMnemonic(KeyEvent.VK_W);
        menuItemRadio.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, 
                ActionEvent.ALT_MASK));
        menuItemRadio.setActionCommand("WIRE");
        menuItemRadio.addActionListener(this);
        group.add(menuItemRadio);
        menu.add(menuItemRadio);
        
        menuItemRadio = new JRadioButtonMenuItem("Unwire");
        menuItemRadio.setMnemonic(KeyEvent.VK_U);
        menuItemRadio.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, 
                ActionEvent.ALT_MASK));
        menuItemRadio.setActionCommand("UNWIRE");
        menuItemRadio.addActionListener(this);
        group.add(menuItemRadio);
        menu.add(menuItemRadio);
        
        menuItemRadio = new JRadioButtonMenuItem("Add Component");
        menuItemRadio.setMnemonic(KeyEvent.VK_A);
        menuItemRadio.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, 
                ActionEvent.ALT_MASK));
        menuItemRadio.setActionCommand("ADD");
        menuItemRadio.addActionListener(this);
        group.add(menuItemRadio);
        menu.add(menuItemRadio);
        
        menuItemRadio = new JRadioButtonMenuItem("Remove Component");
        menuItemRadio.setMnemonic(KeyEvent.VK_R);
        menuItemRadio.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, 
                ActionEvent.ALT_MASK));
        menuItemRadio.setActionCommand("REMOVE");
        menuItemRadio.addActionListener(this);
        group.add(menuItemRadio);
        menu.add(menuItemRadio);
        
        // Options Menu Setup
        menu = new JMenu("Options");
        menuBar.add(menu);
        
        // Sub Menu for the options of the Components
        JMenu subMenu = new JMenu("Components");
        
        JCheckBoxMenuItem menuItemCheckBox = new JCheckBoxMenuItem("Hide");
        menuItemCheckBox.setActionCommand("COMP-HIDE");
        menuItemCheckBox.addActionListener(this);
        subMenu.add(menuItemCheckBox);
        
        menuItem = new JMenuItem("Change List Visibility Size");
        menuItem.setActionCommand("COMP-NUMB");
        menuItem.addActionListener(this);
        subMenu.add(menuItem);
        
        menu.add(subMenu);
        
        // Sub Menu for the options of the Wire Colors
        subMenu = new JMenu("Wire Color");
        group = new ButtonGroup();
        
        menuItemRadio = new JRadioButtonMenuItem("Black");
        menuItemRadio.setActionCommand("WIRE-BLACK");
        menuItemRadio.setSelected(true);
        menuItemRadio.addActionListener(this);
        group.add(menuItemRadio);
        subMenu.add(menuItemRadio);
        
        menuItemRadio = new JRadioButtonMenuItem("Blue");
        menuItemRadio.setActionCommand("WIRE-BLUE");
        menuItemRadio.addActionListener(this);
        group.add(menuItemRadio);
        subMenu.add(menuItemRadio);
        
        menuItemRadio = new JRadioButtonMenuItem("Gray");
        menuItemRadio.setActionCommand("WIRE-GRAY");
        menuItemRadio.addActionListener(this);
        group.add(menuItemRadio);
        subMenu.add(menuItemRadio);
        
        menuItemRadio = new JRadioButtonMenuItem("Green");
        menuItemRadio.setActionCommand("WIRE-GREEN");
        menuItemRadio.addActionListener(this);
        group.add(menuItemRadio);
        subMenu.add(menuItemRadio);
        
        menuItemRadio = new JRadioButtonMenuItem("Magenta");
        menuItemRadio.setActionCommand("WIRE-MAGENTA");
        menuItemRadio.addActionListener(this);
        group.add(menuItemRadio);
        subMenu.add(menuItemRadio);
        
        menuItemRadio = new JRadioButtonMenuItem("Orange");
        menuItemRadio.setActionCommand("WIRE-ORANGE");
        menuItemRadio.addActionListener(this);
        group.add(menuItemRadio);
        subMenu.add(menuItemRadio);
        
        menuItemRadio = new JRadioButtonMenuItem("Red");
        menuItemRadio.setActionCommand("WIRE-RED");
        menuItemRadio.addActionListener(this);
        group.add(menuItemRadio);
        subMenu.add(menuItemRadio);
        
        menuItemRadio = new JRadioButtonMenuItem("Yellow");
        menuItemRadio.setActionCommand("WIRE-YELLOW");
        menuItemRadio.addActionListener(this);
        group.add(menuItemRadio);
        subMenu.add(menuItemRadio);
        
        menu.add(subMenu);
 
        return menuBar;
        
    }
    
    //----------------- ACTION LISTENER -----------------//
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String act = e.getActionCommand();
        
        switch (act) {
            case "NEW":
                createDrawStation();
                break;
            case "SAVE":
                if (drawStations.getActiveDrawStation() !=null)
                    FileSaver.saveDrawStationData(drawStations.getActiveDrawStation(),false);
                break;
            case "SAVE-AS":
                if (drawStations.getActiveDrawStation() !=null)
                    FileSaver.saveDrawStationData(drawStations.getActiveDrawStation(),true);
                break;
            case "LOAD":
                    fileLoader = new FileLoader(this);
                break;
            case "QUIT":
                System.exit(0);
                break;
            case "WIRE":
                PROG_mode = act;
                break;
            case "UNWIRE":
                PROG_mode = act;
                break;
            case "ADD":
                PROG_mode = act;
                break;
            case "REMOVE":
                PROG_mode = act;
                break;
            case "COMP-HIDE":
                componentStation.toggleHide();
                break;
            case "COMP-NUMB":
                int i = getComponentRowCount();
                
                if (i < 1){
                    
                    // Error Message
                    JOptionPane.showMessageDialog(null,
                            "You Entered an invalid integer value",
                            "Error:",
                            JOptionPane.ERROR_MESSAGE);
                
                }else{
                
                    createComponentStation(i);
                    
                }
                
                break;
            case "HELP":
                helpWindow = new HelpWindow(this.getBounds());
                break;
            case "WIRE-BLACK":
                PROG_colorW = Color.BLACK;
                break;
            case "WIRE-BLUE":
                PROG_colorW = Color.BLUE;
                break;
            case "WIRE-GRAY":
                PROG_colorW = Color.GRAY;
                break;
            case "WIRE-GREEN":
                PROG_colorW = Color.GREEN;
                break;
            case "WIRE-MAGENTA":
                PROG_colorW = Color.MAGENTA;
                break;
            case "WIRE-ORANGE":
                PROG_colorW = Color.ORANGE;
                break;
            case "WIRE-RED":
                PROG_colorW = Color.RED;
                break;
            case "WIRE-YELLOW":
                PROG_colorW = Color.YELLOW;
                break;
            default:
                // Error Message
                JOptionPane.showMessageDialog(null,
                        "This command has not been implemented yet",
                        "Error:",
                        JOptionPane.ERROR_MESSAGE);
                break;
            
        }
        
    }
    
}
