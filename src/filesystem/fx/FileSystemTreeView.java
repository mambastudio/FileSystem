/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.fx;

import filesystem.fx.treecomponents.FileTreeCellImpl;
import filesystem.fx.treecomponents.FileTreeFactory;
import filesystem.core.file.FileObject;
import filesystem.core.monitor.FileMonitor;
import filesystem.fx.icons.FileIconManager;
import filesystem.fx.treecomponents.FileTreeCell;
import filesystem.fx.treecomponents.FileTreeItem;
import filesystem.fx.treecomponents.FileTreeItemSearch;
import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 *
 * @author user
 */
public class FileSystemTreeView extends TreeView<FileObject>
{
    private String cellImplID = "treecell";
    
    private FileTreeItem rootFile = null;    
    private boolean isTreeCellEditable = true;
        
    public FileSystemTreeView()
    {
        this(new FileObject());
    }
    
    public FileSystemTreeView(String directory)
    {
        this(new FileObject(directory));
    }
    
    public FileSystemTreeView(URI uri)
    {
        this(new FileObject(uri));
    }
    
    public FileSystemTreeView(File directory)
    {
        this(new FileObject(directory));     
    }
    
    public FileSystemTreeView(FileObject rFile)
    {
        setRootFile(rFile);        
    }
    
    public FileObject getSelectedFileObject()
    {
        TreeItem<FileObject> item = getSelectionModel().getSelectedItem();
        return item == null ? null : item.getValue();
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
        setRootFile(file, false, extensions);
    }
    
    public void setCellEditable(boolean isTreeCellEditable)
    {
        this.isTreeCellEditable = isTreeCellEditable;
    }
    
    public void setCellImplID(String cellImplID)
    {
        this.cellImplID = cellImplID;
    }
    
    public final void setRootFile(FileObject file, boolean rootexpand, String... extensions)
    {       
        //set cell implementation factory        
        FileTreeFactory.putCellImpl(cellImplID, new FileTreeCellImpl());
        
        
        if(file.isNotDirectory())
            throw new IllegalArgumentException("Must be directory");
        rootFile = new FileTreeItem(file, FileIconManager.getIcon("home"), extensions);
        rootFile.setExpanded(rootexpand);
        setRoot(rootFile);
        setEditable(isTreeCellEditable);
        
        //set tree cell render
        setCellFactory(p -> new FileTreeCell(cellImplID));      
        getChildren().sort(null);
    }  
    
    //Auto update tree if there is modification of files outside the app
    public void monitorChange()
    {        
        FileMonitor monitor = new FileMonitor(rootFile.getValue());
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
}
