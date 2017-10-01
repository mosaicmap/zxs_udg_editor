/*
 * Main.java
 *
 *  created: 29.5.2017
 *  charset: UTF-8
 */

package cz.mp.zxs.tools.udg_editor;

import static cz.mp.zxs.tools.udg_editor.Version.VERSION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZX Spectrum UDG Editor.
 * 
 * @author Martin Pokorný
 */
public class Main {            
    private static final Logger log;
    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
        log = LoggerFactory.getLogger(Main.class);
    }
        
    /** */
    private static void printVersion() {
        System.out.println(VERSION); 
    }
    
    /** */
    public static void main(String[] args) {
        log.info("version: " + VERSION);
        
        boolean optVersion = false;
        
        for(int i = 0; i < args.length; i++) {
            String opt = args[i].trim().toLowerCase();
            if (opt.equals("--version")) {
                optVersion = true;
            }
        }
        if (optVersion) {
            printVersion();
            System.exit(0);
        } 
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.info("create and show GUI");
                MainFrame.getInstance().setVisible(true);
            }
        });
    }

}   // Main.java
