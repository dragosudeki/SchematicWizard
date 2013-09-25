
package schematicwizard;

import schematicwizard.Windows.MainInterface ;

/**
 *
 * SchematicWizard - Brandon Altarejos
 * Created on July 27, 2013
 *
 * This will just simply run the main interface
 * 
 **/

public class SchematicWizard{
    
    // Starts the program
    public static void main(String[] args) {
        
        javax.swing.SwingUtilities.invokeLater(
                new Runnable (){
                
                    @Override
                    public void run() {
                        
                        MainInterface main = new MainInterface();
                        
                    }
                
                });
        
    }

}
