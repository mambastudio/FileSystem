/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.fx.treecomponents;

import filesystem.core.file.FileObject;
import javafx.scene.control.TreeItem;

/**
 *
 * @author user
 */
public class FileTreeItemSearch {
    public static FileTreeItem search(FileTreeItem item, FileObject file)
    {
        if(item.getValue().equals(file)) return item; // hit!
        
        // continue on the children:
        FileTreeItem result = null;
        for(TreeItem<FileObject> child : item.getChildren()){
             result = search((FileTreeItem) child, file);
             if(result != null) return result; // hit!
        }
    
        return null;
    }
}
