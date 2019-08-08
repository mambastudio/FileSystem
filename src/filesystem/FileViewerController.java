package filesystem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import filesystem.core.monitor.FileMonitor;
import filesystem.core.FileObject;
import filesystem.fx.FileSystemTreeView;
import filesystem.fx.icons.FileIconManager;
import filesystem.fx.treecomponents.FileTreeItem;
import filesystem.fx.treecomponents.FileTreeItemSearch;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author user
 */
public class FileViewerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    StackPane pane;
    @FXML
    Button open;
    @FXML
    Button createFolder;
    @FXML
    Button createFile;
    @FXML
    TextField nameField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        FileIconManager.put("md", FileIconManager.class, "info16x16.png");
        FileIconManager.put("xml", FileIconManager.class, "xml24x24.png");
        FileIconManager.put("fxml", FileIconManager.class, "fxml22x22.png");
        FileIconManager.put("java", FileIconManager.class, "java20x20.png");
        FileIconManager.put("class", FileIconManager.class, "class22x22.png");
        FileIconManager.put("jar", FileIconManager.class, "archive22x22.png");
        FileIconManager.put("zip", FileIconManager.class, "archive22x22.png");
        FileIconManager.put("png", FileIconManager.class, "image20x20.png");
        FileIconManager.put("jpg", FileIconManager.class, "image20x20.png");
        
        FileObject root = FileObject.getSystemRootArray()[0];
        System.out.println(root.getPath());
                
        FileSystemTreeView fileView = new FileSystemTreeView(root);          
        pane.getChildren().add(fileView);
        
        /*
        FileMonitor monitor = new FileMonitor(root);
        monitor.processEvents();
        monitor.registerCreate((parent, child) -> {
            FileTreeItem parentTreeItem = FileTreeItemSearch.search(fileView.getRootFileTreeItem(), parent);
            parentTreeItem.refresh();
        });        
        monitor.registerDelete((parent, child) -> {
            FileTreeItem parentTreeItem = FileTreeItemSearch.search(fileView.getRootFileTreeItem(), parent);
            parentTreeItem.refresh();
        });
*/
    }   
        
    
    public void close(ActionEvent e)
    {
        System.exit(0);
    }
}
