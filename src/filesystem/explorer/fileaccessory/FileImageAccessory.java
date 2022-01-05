/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.explorer.fileaccessory;

import filesystem.core.file.FileObject;
import filesystem.explorer.FileAccessory;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author user
 */
public class FileImageAccessory extends FileAccessory{
    private final ImageView imageView = new ImageView();
    private final Label message = new Label("click image for preview");
    
    public FileImageAccessory()
    {
        this.setMinWidth(200);
        this.getChildren().add(message);
    }

    @Override
    public ChangeListener<FileObject> createListener() {
        ChangeListener<FileObject> listener = (obs, ov, nv)->{
            if(nv != null)
            {
                try {
                    imageView.setImage(new Image(nv.getFile().toURI().toURL().toExternalForm(), 150, 150, true, true, true));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(FileImageAccessory.class.getName()).log(Level.SEVERE, null, ex);
                }
                getChildren().set(0, imageView);
            }
            else
            {
                getChildren().set(0, message);
                imageView.setImage(null);
                
            }
        };
        
        return listener;
    }
    
}
