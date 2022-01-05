/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.treeview;

import filesystem.core.file.FileObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 *
 * @author user
 */
public class FileTreeItem extends TreeItem<FileObject> {
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private boolean isLeaf;
    private boolean onlyFiles = false;
    
    private String[] extensions = null;    
    
    public FileTreeItem(FileObject file, String... extensions)
    {
        super(file);
        this.extensions = extensions;
    }
       
    public FileTreeItem(FileObject file, boolean onlyFiles, String... extensions)
    {
        super(file);
        this.extensions = extensions;
        this.onlyFiles = onlyFiles;
    }
    
    
    @Override
    public ObservableList<TreeItem<FileObject>> getChildren() 
    {

        if (isFirstTimeChildren) {              
            isFirstTimeChildren = false;            
            Platform.runLater(()->super.getChildren().setAll(buildChildren(this)));
        }
        return super.getChildren();
    }
    
    public void refresh()
    {
        isFirstTimeChildren = true;
        isFirstTimeLeaf = true;
        isLeaf = false;
        getChildren();
    }
    
    @Override
    public boolean isLeaf() 
    {               
        if (isFirstTimeLeaf) {             
            isFirstTimeLeaf = false;           
            isLeaf = getValue().isLeaf(); //make sure it is not a directory
        }
        return isLeaf;
    }
    
    private ObservableList<TreeItem<FileObject>> buildChildren(TreeItem<FileObject> treeItem) 
    {
        FileObject[] fileObjects = treeItem.getValue().getChildren(extensions);            
        if(fileObjects != null)
        {
            ObservableList<TreeItem<FileObject>> children = FXCollections.observableArrayList();
            for(FileObject fileObject : fileObjects)  
            {                
                if(onlyFiles)
                {
                    if(fileObject.isNotDirectory())
                        children.add(new FileTreeItem(fileObject, extensions));                     
                }
                else
                {                    
                    children.add(new FileTreeItem(fileObject, extensions));
                }
                    
            }
            return children;
        }
        return FXCollections.emptyObservableList();
    }
    
    
}
