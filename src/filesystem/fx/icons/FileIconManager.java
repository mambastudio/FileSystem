/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.fx.icons;

import filesystem.core.file.FileObject;
import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author user
 */
public class FileIconManager {    
    private static final HashMap<String, FileIconData> fileStore = new HashMap<>();
    
    static
    {
        fileStore.put("home", new FileIconData(FileIconManager.class, "home20x20.png"));
        fileStore.put("folder", new FileIconData(FileIconManager.class, "folder16x16.png"));
        fileStore.put("file", new FileIconData(FileIconManager.class, "file16x16.png"));
    }
    
    public static ImageView getIcon(String string)
    {
        return fileStore.get(string).getImageView();
    }
    
    public static ImageView getIcon(Class<?> clazz, String iconPath)
    {
        return new ImageView(new Image(clazz.getResourceAsStream(iconPath)));
    }
    
    public static ImageView getIcon(FileObject file)
    {
        String extension = file.getFileExtension();
        
        if(extension != null)
            if(fileStore.containsKey(extension))
                return fileStore.get(extension).getImageView();
        
        if(file.isDirectory()) 
            return fileStore.get("folder").getImageView(); 
        else   
            return fileStore.get("file").getImageView();
    }
    
    public static void put(String name, Class<?> clazz, String iconPath)
    {
        fileStore.put(name, new FileIconData(clazz, iconPath));
    }
    
    private static class FileIconData
    {
        Class<?> clazz;
        String iconPath;
        
        FileIconData(Class<?> clazz, String iconPath)
        {
            this.clazz = clazz; this.iconPath = iconPath;
        }
        
        ImageView getImageView()
        {
            return new ImageView(new Image(clazz.getResourceAsStream(iconPath)));
        }
    }
}
