/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.fx.treecomponents;

import filesystem.core.file.FileObject;
import filesystem.fx.treecomponents.FileTreeCell;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author user
 */
public interface FileTreeCellFunctions 
{
    public void updateItem(FileTreeCell cell, FileObject file, boolean empty); 
    public void setContextMenu(FileTreeCell cell);
    
    default void startEdit(FileTreeCell cell)
    {
        
    }
    
    default void commitIfEdit(FileTreeCell cell)
    {
        
    }
    
    default void cancelEdit(FileTreeCell cell)
    {
        
    }
    
    default void cellMouseClicked(FileTreeCell cell, MouseEvent e)
    {
        
    }
}
