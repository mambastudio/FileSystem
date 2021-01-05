/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.fx.treecomponents;

import filesystem.core.file.FileObject;
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
    
    private String[] extensions = null;    
    
    public FileTreeItem(FileObject file)
    {
        this(file, FileIconManager.getIcon(file));         
    }
    
    public FileTreeItem(FileObject file, String... extensions)
    {
        this(file, FileIconManager.getIcon(file), extensions);         
    }
    
   
    public FileTreeItem(FileObject file, ImageView view, String... extensions)
    {
        super(file, view);
        this.extensions = extensions;
    }
    
    
    @Override
    public ObservableList<TreeItem<FileObject>> getChildren() 
    {

        if (isFirstTimeChildren) {              
            isFirstTimeChildren = false;
            ///if(!super.getChildren().isEmpty())
                super.getChildren().setAll(buildChildren(this));
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
                    children.add(new FileTreeItem(fileObject, extensions));          
            return children;
        }
        return FXCollections.emptyObservableList();
    }
    
    
}
