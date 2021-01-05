/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.core;

/**
 *
 * @author user
 */
public class UI {
    
    private static OutputInterface ui = null;
    private UI(){}
    
    public final static void setConsole(OutputInterface ui) {        
        UI.ui = ui;
    }
    
    public final static void print(String key, String string)
    {
        if(ui != null)
            ui.print(key, string);
    }
}
