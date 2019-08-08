/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.util;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 *
 * @author user
 */
public class FileChooserManager 
{
    private static final HashMap<String, FileChooserMeta> fileChooserMetaList = new HashMap<>();
    
    public static void init(String name)
    {
        fileChooserMetaList.put(name, new FileChooserMeta());
    }
    
    public static FileChooser getFileChooser(String name)
    {
        return fileChooserMetaList.get(name).getFileChooser();
    }
    
    public static SimpleObjectProperty<File> getBindingFileProperty(String name)
    {
        return fileChooserMetaList.get(name).lastKnownDirectoryProperty;
    }
    
    public static Path getPathDirectory(String name)
    {
        return fileChooserMetaList.get(name).getPathDirectory();
    }
        
    public static File showOpenDialog(String name)
    {
        return showOpenDialog(name, null);
    }
    
    public static File showOpenDialog(String name, Window ownerWindow)
    {        
        FileChooserMeta metaChooser = fileChooserMetaList.get(name);
        File chosenFile = metaChooser.getFileChooser().showOpenDialog(ownerWindow);
        if(chosenFile != null)            
            metaChooser.lastKnownDirectoryProperty.setValue(chosenFile.getParentFile());        
        return chosenFile;
    }
    
    public static File showSaveDialog(String name)
    {
        return showSaveDialog(name, null);
    }

    public static File showSaveDialog(String name, Window ownerWindow)
    {                
        FileChooserMeta metaChooser = fileChooserMetaList.get(name);
        File chosenFile = metaChooser.getFileChooser().showSaveDialog(ownerWindow);
        if(chosenFile != null)            
            metaChooser.lastKnownDirectoryProperty.setValue(chosenFile.getParentFile());        
        return chosenFile;
    }
    
    private static class FileChooserMeta
    {
        FileChooser chooser = new FileChooser();
        SimpleObjectProperty<File> lastKnownDirectoryProperty = new SimpleObjectProperty<>();
        
        FileChooserMeta()
        {
            chooser.initialDirectoryProperty().bindBidirectional(lastKnownDirectoryProperty);
        }
        
        FileChooser getFileChooser()
        {
            return chooser;
        }
        
        Path getPathDirectory()
        {
            return lastKnownDirectoryProperty.getValue().toPath();
        }
    }
}
