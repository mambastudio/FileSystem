/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.fx;

import filesystem.core.FileObject;
import filesystem.fx.treecomponents.FileTreeCell;
import filesystem.fx.treecomponents.FileTreeItem;
import java.io.File;
import java.net.URI;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;

/**
 *
 * @author user
 */
public class FileSystemTreeView extends TreeView
{
    public FileSystemTreeView(File directory)
    {
        FileObject file = new FileObject(directory);        
        setRootFile(file);
    }
    
    public FileSystemTreeView(URI uri)
    {
        FileObject file = new FileObject(uri);
        setRootFile(file);
    }
    
    public FileSystemTreeView(String directory)
    {
        FileObject file = new FileObject(directory);
        setRootFile(file);
    }
    
    public FileSystemTreeView()
    {
        
    }
    
    public final void setRootFile(FileObject file)
    {
        if(!file.isDirectory())
            throw new IllegalArgumentException("Must be directory");
        
        setRoot(new FileTreeItem(file));
        setCellFactory(p -> new FileTreeCell());
    }
}
