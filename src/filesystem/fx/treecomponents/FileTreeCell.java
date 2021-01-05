/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.fx.treecomponents;

import filesystem.core.file.FileObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author user
 * 
 * https://docs.oracle.com/javafx/2/ui_controls/tree-view.htm#BABEJCHA
 * https://stackoverflow.com/questions/20635192/how-to-create-popup-menu
 * https://stackoverflow.com/questions/46436974/disable-treeitems-default-expand-collapse-on-double-click-javafx-8?noredirect=1&lq=1
 * 
 */
public class FileTreeCell extends TreeCell<FileObject>{
    private final String treeCellImpl;
    
    public BooleanProperty commitModeProperty = new SimpleBooleanProperty(false);
    
    public FileTreeCell(String treeCellImpl)
    {
        super();    
        this.treeCellImpl = treeCellImpl;
        this.setOnMouseClicked(e->{
            FileTreeFactory.getCellImpl(treeCellImpl).cellMouseClicked(this, e);
        });
        
        
    }
    
    @Override
    public void startEdit() {
        super.startEdit();
        FileTreeFactory.getCellImpl(treeCellImpl).startEdit(this);
    }

    @Override
    public void cancelEdit() 
    {
        super.cancelEdit();
        FileTreeFactory.getCellImpl(treeCellImpl).cancelEdit(this);
    }
    
    @Override
    public void commitEdit(FileObject value)
    {        
        super.commitEdit(value);
        FileTreeFactory.getCellImpl(treeCellImpl).commitIfEdit(this);
    }
    
    @Override
    public void updateItem(FileObject file, boolean empty) 
    {
        super.updateItem(file, empty);
        FileTreeFactory.getCellImpl(treeCellImpl).updateItem(this, file, empty);        
    }    
    
    
}
