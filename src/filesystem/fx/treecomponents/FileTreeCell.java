/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.fx.treecomponents;

import filesystem.core.FileObject;
import javafx.scene.control.TreeCell;

/**
 *
 * @author user
 */
public class FileTreeCell extends TreeCell<FileObject>{
    
    public FileTreeCell()
    {
        super();
    }
    
    @Override
    public void updateItem(FileObject file, boolean empty) 
    {
        super.updateItem(file, empty);
        if (empty)
        {
            setText(null);
            setGraphic(null);
        }
        else
        {     
            setGraphic(getTreeItem().getGraphic());
            setText(file.getName());
        }
    }
}
