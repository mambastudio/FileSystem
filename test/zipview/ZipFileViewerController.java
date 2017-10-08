/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zipview;

import filesystem.core.FileObject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;

/**
 * FXML Controller class
 *
 * @author user
 */
public class ZipFileViewerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    TreeView zipFileView;
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
        FileObject file = new FileObject("C:\\Users\\user\\Desktop\\Desktop.zip");
        zipFileView.setRoot(new ZipTreeItem(file));
        zipFileView.setCellFactory(p -> new ZipTreeCell());
    }   
        
    
    public void close(ActionEvent e)
    {
        System.exit(0);
    }
}
