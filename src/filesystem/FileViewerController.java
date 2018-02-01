package filesystem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import filesystem.core.FileObject;
import filesystem.fx.FileSystemTreeView;
import filesystem.fx.icons.FileIconManager;
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
        
        FileObject file = new FileObject();        
        FileSystemTreeView fileView = new FileSystemTreeView();
        fileView.setRootFile(file);        
        pane.getChildren().add(fileView);
    }   
        
    
    public void close(ActionEvent e)
    {
        System.exit(0);
    }
}
