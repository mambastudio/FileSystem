/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.fx.treecomponents;

import filesystem.core.FileObject;
import filesystem.fx.icons.FileIconManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

/**
 *
 * @author user
 */
public class FileTreeItem extends TreeItem<FileObject> {
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private boolean isLeaf;
    
    public FileTreeItem(FileObject file)
    {
        super(file, FileIconManager.getIcon(file));        
    }
        
    public FileTreeItem(FileObject file, boolean expand)
    {
        super(file, FileIconManager.getIcon(file));
        this.expandedProperty().set(expand);
    }
    
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
                children.add(new FileTreeItem(fileObject));
            return children;
        }
        return FXCollections.emptyObservableList();
    }
}
