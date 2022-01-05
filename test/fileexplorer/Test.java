/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileexplorer;

import filesystem.explorer.fileaccessory.FileImageAccessory;
import static filesystem.core.file.FileObject.ExploreType.FILE;
import static filesystem.core.file.FileObject.ExploreType.FOLDER;
import static filesystem.core.file.FileObject.ExploreType.FOLDER_NOFILE;
import filesystem.core.os.SystemOS;
import filesystem.explorer.FileExplorer;
import filesystem.explorer.FileExplorer.ExtensionFilter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class Test extends Application{
    public static void main(String... args)
    {
        launch(args);
        
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FileExplorer explorer = new FileExplorer(FILE);
        explorer.addExtensions(
                new ExtensionFilter("Image Files", ".jpg", ".png")
        );        
        explorer.setFileAccessory(new FileImageAccessory());
        
        Scene scene = new Scene(explorer);        
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
    }
}
