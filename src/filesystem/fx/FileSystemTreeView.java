/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.fx;

import filesystem.core.FileObject;
import filesystem.fx.callback.FileCallBack;
import filesystem.fx.icons.FileIconManager;
import filesystem.fx.treecomponents.FileTreeCell;
import filesystem.fx.treecomponents.FileTreeItem;
import java.io.File;
import java.net.URI;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.text.Text;

/**
 *
 * @author user
 */
public class FileSystemTreeView extends TreeView<FileObject>
{
    FileTreeItem rootFile = null;
    
    public FileSystemTreeView(File directory)
    {
        FileObject rFile = new FileObject(directory);        
        setRootFile(rFile);        
    }
    
    public FileSystemTreeView(URI uri)
    {
        FileObject rFile = new FileObject(uri);
        setRootFile(rFile);        
    }
    
    public FileSystemTreeView(String directory)
    {
        FileObject rFile = new FileObject(directory);
        setRootFile(rFile);       
    }
    
    public FileSystemTreeView(FileObject rFile)
    {
        setRootFile(rFile);        
    }
    
    public FileSystemTreeView()
    {
        
    }
    
    public FileObject getSelectedFileObject()
    {
        return getSelectionModel().getSelectedItem().getValue();
    }
    
    public FileTreeItem getRootFileTreeItem()
    {
        return (FileTreeItem)getRoot();
    }
    
    public FileObject getRootFileObject()
    {
        return getRoot().getValue();
    }
    
    public void reload()
    {
        setRootFile(getRootFileObject());
    }
    
    public void reload(String... extensions)
    {
        setRootFile(getRootFileObject(), extensions);
    }
    
    public final void setRootFile(FileObject file, String... extensions)
    {
        if(file.isNotDirectory())
            throw new IllegalArgumentException("Must be directory");
        
        setRoot(new FileTreeItem(file, FileIconManager.getIcon("home"), extensions));
        setCellFactory(p -> new FileTreeCell());
    }
    
    public final void setRootFile(FileObject file, boolean rootexpand, String... extensions)
    {
        if(file.isNotDirectory())
            throw new IllegalArgumentException("Must be directory");
        FileTreeItem root = new FileTreeItem(file, FileIconManager.getIcon("home"), extensions);
        root.setExpanded(rootexpand);
        
        setRoot(root);
        setCellFactory(p -> new FileTreeCell());
    }
    
    public final void setDoubleMouseClickOnTreeNode(FileCallBack<FileObject> callBack)
    {
        this.setOnMouseClicked(e -> {
            Node node = e.getPickResult().getIntersectedNode();
            if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null))
                if(e.getClickCount() == 2)
                {
                    FileObject file = getSelectedFileObject();
                    callBack.call(file);
                }
        });        
    }
}
