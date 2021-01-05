/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.fx.treecomponents;

import filesystem.core.file.FileObject;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author user
 */
public class FileTreeCellImpl implements FileTreeCellFunctions{

    @Override
    public void updateItem(FileTreeCell cell, FileObject file, boolean empty) {
        if (empty)
        {
            cell.setText(null);
            cell.setGraphic(null);
        }
        else
        {  
            TreeItem item = cell.getTreeItem(); 
            
            cell.setGraphic(cell.getTreeItem().getGraphic()); 
            if(cell.getTreeItem().getParent() != null)
            {
                setContextMenu(cell);
            }
            if(file.isRoot())
                cell.setText(file.getRootName());
            else          
                cell.setText(file.getName());              
        }
    }

    @Override
    public void setContextMenu(FileTreeCell cell) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem rename = new MenuItem("Rename");
        rename.setOnAction(e -> {
            //get tree item and its parent
            TreeItem currentItem = cell.getTreeItem(); 
            TreeItem parentItem  = currentItem == null ? null : currentItem.getParent();
            cell.startEdit();            
        });
        contextMenu.getItems().addAll(rename);
        cell.setContextMenu(contextMenu);
    }
    
    @Override
    public void startEdit(FileTreeCell cell) {    
        
        //get tree item and its parent
        TreeItem currentItem = cell.getTreeItem(); 
        TreeItem parentItem  = currentItem == null ? null : currentItem.getParent();
        //prevent editing root
        if(parentItem == null)
            return;
        
        TextField textField = createTextField(cell);        
        cell.setText(null);
        cell.setGraphic(textField);
        textField.selectAll();
        textField.requestFocus();
    }

    @Override
    public void cancelEdit(FileTreeCell cell) {     
        
        cell.setText(cell.getTreeItem().getValue().getName());
        cell.setGraphic(cell.getTreeItem().getGraphic());  
        
    }
    
    @Override
    public void commitIfEdit(FileTreeCell cell)
    {
        cell.commitModeProperty.set(true);
        
       
    }
    
    @Override
    public void cellMouseClicked(FileTreeCell cell, MouseEvent e){
        
        //get tree item and its parent
        TreeItem currentItem = cell.getTreeItem(); 
        TreeItem parentItem  = currentItem == null ? null : currentItem.getParent();
        
        if(e.getClickCount() == 2)
        {
            if(currentItem != null)
            {
                System.out.println(cell);
            }
        }      
    }       
    
    private TextField createTextField(FileTreeCell cell) {
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
