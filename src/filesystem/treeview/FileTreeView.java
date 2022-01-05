/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.treeview;

import filesystem.core.file.FileObject;
import filesystem.core.monitor.FileMonitor;
import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import jfx.tree.TreeCellFactory;

/**
 *
 * @author user
 */
public class FileTreeView extends TreeView<FileObject>{
    private FileTreeItem rootFile = null; 
    private final FileMonitor monitor = new FileMonitor();
    
    public FileTreeView()
    {
        this(new FileObject());
    }
    
    public FileTreeView(String directory)
    {
        this(new FileObject(directory));
    }
    
    public FileTreeView(URI uri)
    {
        this(new FileObject(uri));
    }
    
    public FileTreeView(File directory)
    {
        this(new FileObject(directory));     
    }
    
    public FileTreeView(FileObject rFile)
    {
        setRootFile(rFile);        
        this.setCellFactory(new FileTreeCellFactory());
    }
    
    public FileObject getSelectedFileObject()
    {
        TreeItem<FileObject> item = getSelectionModel().getSelectedItem();
        return item == null ? null : item.getValue();
    }
    
    public final void setRootFile(FileObject file, String... extensions)
    {
        setRootFile(file, false, false, extensions);
    }
    
    public FileTreeItem getRootFileTreeItem()
    {
        return (FileTreeItem)getRoot();
    }
    
    public FileObject getRootFileObject()
    {
        return getRoot().getValue();
    }
    
    public final void setRootFile(FileObject file, boolean rootexpand, boolean onlyFiles, String... extensions)
    {       
        if(file.isNotDirectory())
            throw new IllegalArgumentException("Must be directory");
        rootFile = new FileTreeItem(file, onlyFiles, extensions);
        rootFile.setExpanded(rootexpand);
        setRoot(rootFile);
        getSelectionModel().select(0);
        //setEditable(isTreeCellEditable);
    }  
    
    public void setTreeCellFactory(TreeCellFactory<FileObject> factory)
    {
        this.setCellFactory(factory);
    }
    
    //Auto update tree if there is modification of files outside the app
    public void monitorChange()
    {        
        monitor.setMonitor(rootFile.getValue());
        monitor.processEvents();
        
        //parent and child are current state even after modification (no previous record)
        monitor.registerCreate((parent, child) -> {             
            FileTreeItem parentTreeItem = FileTreeItemSearch.search(getRootFileTreeItem(), parent);
            FileTreeItem childTreeItem = FileTreeItemSearch.search(getRootFileTreeItem(), child);       
            if(childTreeItem != null) //it has been renamed if childTreeItem is present
            {
                childTreeItem.setValue(child); //update display
                if(!childTreeItem.isLeaf()) //if folder refresh everything
                {
                    boolean expanded = childTreeItem.isExpanded(); //make sure it remains expanded if expanded already
                    childTreeItem.refresh();
                    childTreeItem.setExpanded(expanded);
                }
            }
            else //this must be a new file creation
            {
                if(parentTreeItem == null) return;
                boolean expanded = parentTreeItem.isExpanded();
                parentTreeItem.refresh();
                parentTreeItem.setExpanded(expanded);
                
            }
            
        });        
        
        //child is previous state after deletion 
        monitor.registerDelete((parent, child) -> {
           
            FileTreeItem parentTreeItem = FileTreeItemSearch.search(getRootFileTreeItem(), parent);
            FileTreeItem childTreeItem = FileTreeItemSearch.search(getRootFileTreeItem(), child);
            // //check if file has been deleted, hence remove childTreeItem
            if(childTreeItem != null)
               parentTreeItem.getChildren().removeAll(childTreeItem);          
        });
    }
    
     public void createFolderInSelected(String folderName)
    {        
        FileObject current = getSelectedFileObject();
        if(current == null) current = getRootFileObject();
        if(current.isNotDirectory()) current = new FileObject(current.getPath().getParent());
        
        if(folderName != null && !folderName.isEmpty())
        {
            FileObject folder = new FileObject(Paths.get(current.getPathName(), folderName));
            folder.ifAbsentCreateDirectorate();
        }
    }
    
    public void createFileInSelected(String fileName)
    {        
        FileObject current = getSelectedFileObject();
        if(current == null) current = getRootFileObject();
        if(current.isNotDirectory()) current = new FileObject(current.getPath().getParent());
        
        if(fileName != null && !fileName.isEmpty())
        {
            FileObject file = new FileObject(Paths.get(current.getPathName(), fileName));
            file.ifAbsentCreateFile();
        }
    }
    
    public void createFileInRoot(String fileName)
    {        
        FileObject current = getRootFileObject();        
        if(current.isNotDirectory()) current = new FileObject(current.getPath().getParent());
        
        if(fileName != null && !fileName.isEmpty())
        {
            FileObject file = new FileObject(Paths.get(current.getPathName(), fileName));
            file.ifAbsentCreateFile();
        }
    }
    
    public File getSelectedFile()
    {
        return getSelectionModel().getSelectedItem().getValue().getFile();
    }
}
