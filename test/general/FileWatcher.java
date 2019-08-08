/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import filesystem.core.monitor.FileMonitor;
import filesystem.core.FileObject;
import java.io.IOException;

/**
 *
 * @author user
 */
public class FileWatcher {
    public static void main(String... args) throws IOException, InterruptedException
    {
        FileObject root = new FileObject("C:\\Users\\user\\Desktop\\TextEditor");
        
        FileMonitor monitor = new FileMonitor(root);        
        monitor.processEvents();
        monitor.registerModify((file, child) -> {
            System.out.println("File modified: " +child.getName());
        });
    }
}
