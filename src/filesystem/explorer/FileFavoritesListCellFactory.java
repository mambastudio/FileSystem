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
public class FileFavoritesListCellFactory implements Callback<ListView<FileObject>, ListCell<FileObject>>{
    @Override
    public ListCell<FileObject> call(ListView<FileObject> param) {
        ListCell<FileObject> cell = new ListCell<FileObject>() {
            @Override
            protected void updateItem(FileObject item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null)
                {
                    if(item.getName().toLowerCase().contains("desktop".toLowerCase()))
                    {
                        MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.DESKTOP_MAC);
                        icon.setSize("16");
                        icon.setFill(Color.CADETBLUE);
                        setGraphic(icon);
                    }
                    else if(item.getName().toLowerCase().contains("document".toLowerCase()))
                    {
                        MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.FILE_MULTIPLE);
                        icon.setSize("16");
                        icon.setFill(Color.CADETBLUE);
                        setGraphic(icon);
                    }
                    else if(item.getName().toLowerCase().contains("picture".toLowerCase()))
                    {
                        MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.FOLDER_IMAGE);
                        icon.setSize("16");
                        icon.setFill(Color.CADETBLUE);
                        setGraphic(icon);
                    }
                    else if(item.getName().toLowerCase().contains("program".toLowerCase()))
                    {
                        MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.CODEPEN);
                        icon.setSize("16");
                        icon.setFill(Color.CADETBLUE);
                        setGraphic(icon);
                    }
                    else
                    {
                        MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.FOLDER);
                        icon.setSize("16");
                        icon.setFill(Color.CADETBLUE);
                        setGraphic(icon);
                    }
                    setText(item.getName());
                }
            }
        };
        return cell;
    }
}
