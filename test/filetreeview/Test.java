/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filetreeview;

import filesystem.core.file.FileObject;
import filesystem.treeview.FileTreeCellFactory;
import filesystem.treeview.FileTreeView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class Test extends Application {
    @Override
    public void start(Stage ps) {
        
        StackPane root = new StackPane(); 
        FileTreeView fileView = new FileTreeView();
        fileView.setRootFile(new FileObject("C:\\Users\\user\\Desktop\\Notes\\Workshops\\Next Generation Reactors and EPR"), 
                true, 
                true, 
                ".nts");
        fileView.setTreeCellFactory(new FileTreeCellFactory());
        fileView.monitorChange();
        fileView.setEditable(true);
        
        root.getChildren().add(fileView);
        
        Scene scene = new Scene(root, 400, 500);  
        ps.setScene(scene);
        ps.show();
        ps.setOnCloseRequest(e -> System.exit(0));

    }

    public static void main(String[] args) {
        launch(args);
    }
}
