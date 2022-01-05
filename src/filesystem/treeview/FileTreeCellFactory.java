/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.treeview;

import filesystem.core.file.FileObject;
import filesystem.fx.icons.FileIconManager;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import jfx.tree.TreeCellFactory;

/**
 *
 * @author user
 */
public class FileTreeCellFactory implements TreeCellFactory<FileObject> {
    @Override
    public final void updateIfAbsent(TreeCell<FileObject> cell, TreeItem<FileObject> treeItem, FileObject item)
    {
        cell.setGraphic(null);
        cell.setText(null);
    }
    
    @Override
    public final void updateIfPresent(TreeCell<FileObject> cell, TreeItem<FileObject> treeItem, FileObject item)
    {
        setCellIcon(cell, treeItem);
    }
    
    @Override
    public void initCellContextMenu(TreeCell<FileObject> cell, TreeItem<FileObject> treeItem, FileObject item)
    {
        if(treeItem != null && treeItem.getParent()!= null)
            if(item.isLeaf())
            {
                ContextMenu contextMenu = new ContextMenu();
                MenuItem cut = new MenuItem("Cut");
                MenuItem copy = new MenuItem("Copy");
                MenuItem paste = new MenuItem("Paste");
                contextMenu.getItems().addAll(cut, copy, paste);
                cell.setContextMenu(contextMenu);
            }
            else if(item.isDirectory())
            {
                ContextMenu contextMenu = new ContextMenu();
                MenuItem cut = new MenuItem("Create");
                MenuItem copy = new MenuItem("Rename");
                contextMenu.getItems().addAll(cut, copy);
                cell.setContextMenu(contextMenu);
            }
    }
    
    @Override
    public void startEditCell(TreeCell<FileObject> cell, TreeItem<FileObject> treeItem) {    
        
        //get tree item and its parent       
        TreeItem<FileObject> parentItem  = treeItem == null ? null : treeItem.getParent();
        //prevent editing root and directory
        if(parentItem == null || treeItem == null || treeItem.getValue().isDirectory())
            return;
        
        TextField textField = createTextField(cell);        
        cell.setText(null);
        cell.setGraphic(textField);
        textField.selectAll();
        textField.requestFocus();
    }

    @Override
    public void cancelEditCell(TreeCell<FileObject> cell, TreeItem<FileObject> item) {     
        
        cell.setText(cell.getTreeItem().getValue().getName());
        setCellIcon(cell, item);
        
        
    }
    
    private void setCellIcon(TreeCell<FileObject> cell, TreeItem<FileObject> item)
    {
        if(item.getParent() == null)
            cell.setGraphic(FileIconManager.getIcon("home"));
        else if(item.getValue().isDirectory())
            cell.setGraphic(FileIconManager.getIcon("folder"));
        else if(item.isLeaf())
            cell.setGraphic(FileIconManager.getIcon("file"));
        cell.setText(item.getValue().getName());
    }
    
  
    private TextField createTextField(TreeCell<FileObject> cell) {
        FileTreeItem currentItem = (FileTreeItem) cell.getTreeItem(); 
        FileObject file = currentItem.getValue();
        
        TextField textField = new TextField(file.getName());
        textField.setOnKeyPressed(t -> {
            if (t.getCode() == KeyCode.ENTER) 
            {
                String newName = textField.getText();
                file.rename("wtf" + Math.random()); //to deal with case sensitive file rename of windows
                boolean success = file.rename(newName);
                if(success)
                    cell.commitEdit(file);
            } 
            else if (t.getCode() == KeyCode.ESCAPE) 
            {
                cell.cancelEdit();
            }
        }); 
        textField.focusedProperty().addListener((o, oldValue, newValue)->{
            if(!newValue)
                cell.cancelEdit();
        });
        textField.setMinSize(10, 10);
        return textField;
    }
}
