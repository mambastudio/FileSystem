/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.explorer;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import filesystem.core.file.FileObject;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 *
 * @author user
 */
public class FileSystemListCellFactory implements Callback<ListView<FileObject>, ListCell<FileObject>>{

    @Override
    public ListCell<FileObject> call(ListView<FileObject> param) {
        ListCell<FileObject> cell = new ListCell<FileObject>() {
            @Override
            protected void updateItem(FileObject item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null)
                {
                    MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.HARDDISK);
                    icon.setSize("16");
                    icon.setFill(Color.CADETBLUE);
                    setText(item.toString());
                    setGraphic(icon);
                }
            }
        };
        return cell;
    }
    
}
