/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zipview;

import filesystem.core.FileObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 *
 * @author user
 */
public class ZipTreeItem extends TreeItem<FileObject>
{
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private boolean isLeaf;
    
    public ZipTreeItem(FileObject file)
    {
        super(file);        
    }
    
    public ZipTreeItem(FileObject file, boolean expand)
    {
        super(file);
        this.expandedProperty().set(expand);
    }
    
    //
    @Override
    public ObservableList<TreeItem<FileObject>> getChildren() 
    {

        if (isFirstTimeChildren) {              
            isFirstTimeChildren = false;
            super.getChildren().setAll(buildChildren(this));
        }
        return super.getChildren();
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
        FileObject[] fileObjects = treeItem.getValue().getChildren();
        
        if(fileObjects != null)
        {
            ObservableList<TreeItem<FileObject>> children = FXCollections.observableArrayList();
            for(FileObject fileObject : fileObjects)            
                children.add(new ZipTreeItem(fileObject));
            return children;
        }
        return FXCollections.emptyObservableList();
    }
}
