
package schematicwizard.Windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import schematicwizard.Windows.IOManager.ComponentData;
import schematicwizard.Windows.GraphicManager.DrawManager;
import schematicwizard.Windows.IOManager.FileLoader;

/**
 *
 * DrawStation - Brandon Altarejos
 * Created on July 27, 2013
 *
 * This will be the work station where the user will draw their schematics of
 * their circuits.
 * 
 **/

public class DrawStation extends JInternalFrame implements MouseListener, 
        MouseMotionListener, InternalFrameListener{

    // Reference to all of the program information
    MainInterface desktop;
    
    // Window Properties of DrawStation
    JPanel drawPanel;
    private DrawManager drawer;
    public boolean hasSaved = false;
    private boolean loadIsSuccessful;
    
    // Mouse properties
    int mouseCurrentX = 0; // Where the mouse currently is
    int mouseCurrentY = 0;
    int mouseImportantX = 0; // An important position for the mouse which is
    int mouseImportantY = 0; // used in the different modes
    
    // Add Mode Values
    double currentRotation = 0;
    double rotationSpeed = 1;
    
    // Wire Mode Values
    boolean inWire = false;
    int[] start;
    
    // The Default Draw Station
    @SuppressWarnings("LeakingThisInConstructor")
    DrawStation(MainInterface d, int i){
        
        super("Work Station " + i,true,true,true,true);
        this.setBounds(200,0,500,500);
        this.desktop = (MainInterface)d;
        this.setDefaultCloseOperation(0);
        
        this.getContentPane().add(drawPanel = new DrawPanel(),BorderLayout.CENTER);
        
        this.drawer = new DrawManager();
        
        this.addInternalFrameListener(this);
        this.drawPanel.addMouseListener(this);
        this.drawPanel.addMouseMotionListener(this);
        
        setVisible(true);
        
    }
    
    // The Copying Support for Draw Station
    @SuppressWarnings("LeakingThisInConstructor")
    DrawStation(DrawStation ds){
        
        super(ds.getTitle(),true,true,true,true);
        this.setBounds(ds.getBounds());
        this.desktop = ds.desktop;
        this.setDefaultCloseOperation(0);
        
        this.drawer = ds.drawer;
        this.getContentPane().add(this.drawPanel = new DrawPanel(),BorderLayout.CENTER);
        
        this.addInternalFrameListener(this);
        this.drawPanel.addMouseListener(this);
        this.drawPanel.addMouseMotionListener(this);
        
        this.setVisible(true);
        
    }
    
    // The Draw Station with a File Loader
    @SuppressWarnings("LeakingThisInConstructor")
    DrawStation(MainInterface d, FileLoader fileLoader){
        
        super(fileLoader.getFileName(),true,true,true,true);
        this.setBounds(200,0,500,500);
        this.desktop = (MainInterface)d;
        this.setDefaultCloseOperation(0);
        
        this.getContentPane().add(drawPanel = new DrawPanel(),BorderLayout.CENTER);
        
        this.drawer = new DrawManager();
        
        this.addInternalFrameListener(this);
        this.drawPanel.addMouseListener(this);
        this.drawPanel.addMouseMotionListener(this);
        
        setVisible(true);
        
        this.loadIsSuccessful = fileLoader.loadAllData(this.drawer,
                this.desktop.componentLoader);
        this.hasSaved=true;
        
    }
    
    //----------------- ACCESSORS -----------------//
    
    public boolean isLoadComplete(){
        return this.loadIsSuccessful;
    }
    public DrawManager getDrawManager(){
        return this.drawer;
    }
    
    //----------------- MOUSE HANDLER -----------------//
    /*
     * Handles all events with the mouse, using the following codes:
     * 0 = Mouse Click
     * 1 = Mouse Press
     * 2 = Mouse Release
     * 3 = Mouse Dragged
     * 4 = Mouse Moved
     */
    @SuppressWarnings("ConvertToStringSwitch")
    private void mouseHandler(int event){
        
        // Mouse Press Events
        if (event == 1){
            
            if (desktop.PROG_mode.equals("ADD")){
            
                mouseImportantX = mouseCurrentX;
                mouseImportantY = mouseCurrentY;
            
            }else if (desktop.PROG_mode.equals("REMOVE")){
                
                this.drawer.removeClosestComponent(mouseCurrentX, mouseCurrentY);
                
            }else if (desktop.PROG_mode.equals("WIRE")){
                
                if (!inWire){
                    
                    start = this.drawer.getClosestWireNodePosition(mouseCurrentX, mouseCurrentY);
                    
                    if (start != null)
                        inWire = true;
                    
                }else{
                    
                    int[] end = this.drawer.getClosestWireNodePosition(mouseCurrentX, mouseCurrentY);
                    
                    // Takes only a unique node
                    if (end != null && start[0] != end[0] && start[1] != end[1] && drawer.isUniqueWire(start, end)){
                        
                        this.drawer.addWire(start, end,desktop.PROG_colorW);
                        inWire = false;
                        
                    }
                    
                }
                
            }else if (desktop.PROG_mode.equals("UNWIRE")){
                
                this.drawer.removeClosestWire(mouseCurrentX, mouseCurrentY);
                
            }
            
        }
        // Mouse Release Events
        else if (event == 2){
            
            if (desktop.PROG_mode.equals("ADD")){

                ComponentData temp = desktop.componentLoader.getComponentData(desktop.PROG_component);
                
                this.drawer.addNewComponent(desktop.PROG_component, 
                        temp.getImage(), 
                        mouseImportantX - temp.getImage().getWidth()/2, 
                        mouseImportantY - temp.getImage().getHeight()/2, 
                        currentRotation,
                        temp.getWireNodes());
                currentRotation = 0;

            }
        }
        // Mouse Dragged Events
        else if (event == 3){
            
            if (desktop.PROG_mode.equals("ADD")){
                
                currentRotation = ((mouseCurrentX - mouseImportantX)/rotationSpeed) % 360;
                
            }
            
        }
        // Mouse Motion Events
        else if (event == 4){
            
            if (desktop.PROG_mode.equals("ADD")){
                
                mouseImportantX = mouseCurrentX;
                mouseImportantY = mouseCurrentY;
                
            }
            if (inWire && !desktop.PROG_mode.equals("WIRE")){
            
                inWire=false;
            
            }
            
        }
        
    }
    
    //----------------- MOUSE LISTENER -----------------//
    @Override
    public void mouseClicked(MouseEvent e) {
        
        this.mouseHandler(0);
        repaint();
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
        this.mouseHandler(1);
        repaint();
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
        this.mouseHandler(2);
        repaint();
        
    }
    
    //----------------- MOUSE MOTION LISTENER -----------------//
    @Override
    public void mouseDragged(MouseEvent e) {
        
        mouseCurrentX = e.getX();
        mouseCurrentY = e.getY();
        this.mouseHandler(3);
        repaint();
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
        mouseCurrentX = e.getX();
        mouseCurrentY = e.getY();
        this.mouseHandler(4);
        repaint();
        
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        
        repaint();
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
        repaint();
        
    }
    
     //----------------- INTERNAL FRAME LISTENER -----------------//
    @Override
    public void internalFrameOpened(InternalFrameEvent e) {}

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        
        desktop.removeDrawStation(this.getTitle());
        
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {}

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {}

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {}

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {}

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {}
    
    //----------------- GRAPHICS JPANEL -----------------//
    class DrawPanel extends JPanel{
        
        @Override
        public void paintComponent(Graphics g){
            
            g.setColor(Color.WHITE);
            g.fillRect(0,0,this.getSize().width,this.getSize().height);
            
            drawer.drawAll(g);
            
            // Things to be drawn over all of the data
            if (desktop.PROG_mode.equals("ADD")){
                
                // Draws the projected component that is active
                BufferedImage temporaryComponent = desktop.componentLoader.getComponentImage(desktop.PROG_component);
                AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(currentRotation), 
                        temporaryComponent.getWidth()/2,
                        temporaryComponent.getHeight()/2);
                AffineTransformOp txop = new AffineTransformOp(tx,
                        AffineTransformOp.TYPE_BILINEAR);
                
                g.drawImage(txop.filter(temporaryComponent,null), 
                        mouseImportantX - temporaryComponent.getWidth()/2, 
                        mouseImportantY - temporaryComponent.getHeight()/2, 
                        null);
                
            }else if (desktop.PROG_mode.equals("REMOVE")){
                
                // Draws a red line to the closest component
                int[] closestPos = drawer.getClosestComponentPosition(mouseCurrentX, 
                        mouseCurrentY,
                        true);
                g.setColor(Color.RED);
                if (closestPos != null)
                    g.drawLine(mouseCurrentX, mouseCurrentY, closestPos[0], closestPos[1]);
            
            }else if (desktop.PROG_mode.equals("WIRE")){
                
                // Draws a red line to the closest wire node
                int[] closestPos = drawer.getClosestWireNodePosition(mouseCurrentX,
                        mouseCurrentY);
                g.setColor(Color.RED);
                if (closestPos != null)
                    g.drawLine(mouseCurrentX,mouseCurrentY,closestPos[0],closestPos[1]);
                
                drawer.drawWireNodePositions(g, mouseCurrentX, mouseCurrentY);
                
                if (inWire){
                    
                    g.setColor(desktop.PROG_colorW);
                    g.fillOval(start[0]-3, start[1]-3, 7, 7);
                    
                }
                
            }else if (desktop.PROG_mode.equals("UNWIRE")){
            
                int[] closestPos = drawer.getClosestWire(mouseCurrentX, mouseCurrentY);
                g.setColor(Color.RED);
                if (closestPos != null)
                    g.drawLine(mouseCurrentX,mouseCurrentY,closestPos[0],closestPos[1]);
                    
            }else{
            
                g.setColor(Color.RED);
                g.fillRect(mouseCurrentX-2,mouseCurrentY-2,4,4);
            
            }
            
        }
        
    }

}