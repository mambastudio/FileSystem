/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.explorer;

import filesystem.core.file.FileObject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.StackPane;

/**
 *
 * @author user
 */
public abstract class FileAccessory extends StackPane
{
    protected ObjectProperty<FileObject> fileProperty;
    private ChangeListener<FileObject> changeListener;
    
    
    private void init()
    {
        changeListener = createListener();
        fileProperty.addListener(changeListener);
    }
    
    public void setFileObjectProperty(ObjectProperty<FileObject> fileProperty)
    {
        this.fileProperty = fileProperty;
        init();
    }
    
    public void removeListener()
    {        
       fileProperty.removeListener(changeListener);
    }
    
    public abstract ChangeListener<FileObject> createListener();
}
