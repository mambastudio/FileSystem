/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.explorer;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import filesystem.core.file.FileObject;
import filesystem.core.file.FileObject.ExploreType;
import static filesystem.core.file.FileObject.ExploreType.FILE;
import static filesystem.core.file.FileObject.ExploreType.FOLDER;
import static filesystem.core.file.FileObject.ExploreType.FOLDER_NOFILE;
import filesystem.core.os.SystemOS;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author user
 */
public class FileExplorer extends VBox{
    @FXML
    SplitPane quickAccessSplitPane;
    @FXML
    ListView<FileObject> systemListView;
    @FXML
    ListView<FileObject> favoritesListView;
    @FXML
    TextField filePathTextField;
    @FXML
    TextField fileNameTextField;
    @FXML
    ComboBox selectedExtensionFilterComboBox;
    @FXML
    TableView<FileTableData> fileTableView;
    @FXML
    TableColumn<FileTableData, String> fileNameColumn;
    @FXML
    TableColumn<FileTableData, String> lastModifiedColumn;
    @FXML
    TableColumn<FileTableData, String> fileSizeColumn;
    @FXML
    TableColumn<FileTableData, String> fileExtensionColumn;
    @FXML
    GridPane fileNameAndTypeGridPane;
    @FXML
    HBox fileViewHBox;
    
    FileNavigateBidirectional fileNavigate;
    
    ObjectProperty<FileObject> folderProperty;
    ObjectProperty<FileObject> fileProperty;
    
    boolean filePathTextFieldSelected = false;
    
    private ExploreType exploreType = FILE;
    
    private Consumer<MouseEvent> tableFileClick;
    
    private FileAccessory fileAccessory = null;
    
    /**
     * Specifies the extension filters used in the displayed file dialog.
     */
    private ObservableList<ExtensionFilter> extensionFilters =
            FXCollections.<ExtensionFilter>observableArrayList();
    private ObjectProperty<ExtensionFilter> selectedExtensionFilter;

    
    public FileExplorer()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
            "FileExplorer.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        } 
        
        init();
    }
    
    public FileExplorer(ExploreType exploreType)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
            "FileExplorer.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        } 
        
        this.exploreType = exploreType;
        init();
    }
    
    public void setFileNull()
    {
        fileProperty.set(null);
    }
    
    public void setFileAccessory(FileAccessory fileAccessory)
    {
        //in case we are setting null
        if(fileAccessory == null && this.fileAccessory != null)
        {
            this.fileAccessory.removeListener();
            this.fileViewHBox.getChildren().remove(1);
            this.fileAccessory = fileAccessory;
            return;
        }
        else if(fileAccessory == null && this.fileAccessory == null)
            return;
        
        //remove listener to the file object for the current accessory if it exists
        if(this.fileAccessory != null)        
            this.fileAccessory.removeListener();
        
        //add accessory
        this.fileAccessory = fileAccessory;
        this.fileAccessory.setFileObjectProperty(fileProperty);
        this.fileViewHBox.getChildren().add(1, fileAccessory);
    }
    
    private void init()
    {        
        
        //folder/directory property where to get its fileobject when selected or navigated into
        folderProperty = new SimpleObjectProperty();
        folderProperty.addListener((obs, ov, nv)->{
            if(nv != null)
                if(nv.isFileSystem())
                    filePathTextField.setText(nv.getRootName());
                else
                    filePathTextField.setText(nv.getPathName());
        });
        
        //file property where to get its fileobject when selected
        fileProperty = new SimpleObjectProperty();
        fileProperty.addListener((obs, ov, nv)->{
            if(nv != null)
            {
                fileNameTextField.setText(nv.getName());
                filePathTextField.setText(nv.getPathName());                
            } 
            else
                fileNameTextField.setText(null);
            
        });
        
        //init file navigate backward and forward
        fileNavigate = new FileNavigateBidirectional();
                
        SplitPane.setResizableWithParent(quickAccessSplitPane, Boolean.FALSE);
        
        //select all when focused in path display
        filePathTextField.focusedProperty().addListener((obs, ov, nv)->{
            Platform.runLater(()->{
                if (filePathTextField.isFocused() && !filePathTextField.getText().isEmpty()) 
                    filePathTextField.selectAll();
            });
        });
        
        
        
        systemListView.getItems().addAll(FileObject.getSystemRootList());
        systemListView.setCellFactory(new FileSystemListCellFactory());
        systemListView.getSelectionModel().selectedItemProperty().addListener((obs, oV, nV)->{
            initTable(nV);
            fileNavigate.reset(nV);
            FileObject fileObject = systemListView.getSelectionModel().getSelectedItem();
            folderProperty.set(fileObject);
            fileProperty.set(null);
        });
        systemListView.setOnMouseClicked(e->{     
            if(systemListView.getSelectionModel().getSelectedIndex()<0)
                return;
            
            if (e.getClickCount() == 2) //Checking double click
            {
                FileObject fileObject = systemListView.getSelectionModel().getSelectedItem();
                folderProperty.set(fileObject);
                fileProperty.set(null);
                if(fileObject.isDirectory())
                {
                    initTable(fileObject);
                    fileNavigate.reset(fileObject);
                }
            }
        });
        
        favoritesListView.setCellFactory(new FileFavoritesListCellFactory());
        favoritesListView.getSelectionModel().selectedItemProperty().addListener((obs, oV, nV)->{
            initTable(nV);
            fileNavigate.reset(nV);
            FileObject fileObject = favoritesListView.getSelectionModel().getSelectedItem();
            folderProperty.set(fileObject);
            fileProperty.set(null);
        });
        favoritesListView.setOnMouseClicked(e->{     
            if(favoritesListView.getSelectionModel().getSelectedIndex()<0)
                return;
            
            if (e.getClickCount() == 2) //Checking double click
            {
                FileObject fileObject = favoritesListView.getSelectionModel().getSelectedItem();
                folderProperty.set(fileObject);
                fileProperty.set(null);
                if(fileObject.isDirectory())
                {
                    initTable(fileObject);
                    fileNavigate.reset(fileObject);
                }
            }
        });
        
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        fileNameColumn.setSortable(false);
        lastModifiedColumn.setCellValueFactory(new PropertyValueFactory<>("lastModified"));
        lastModifiedColumn.setSortable(false);
        fileSizeColumn.setCellValueFactory(new PropertyValueFactory<>("fileSize"));
        fileSizeColumn.setSortable(false);
        fileExtensionColumn.setCellValueFactory(new PropertyValueFactory<>("fileExtension"));
        fileExtensionColumn.setSortable(false);
       
        fileNameColumn.setCellFactory(v -> new TableCell<FileTableData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    this.setText(item);
                    FileTableData data = (FileTableData) getTableRow().getItem();
                    if(data != null)              
                    {
                        if(data.getFileObject().isDirectory())
                        {
                            MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.FOLDER);
                            icon.setSize("18");
                            icon.setFill(Color.CADETBLUE);
                            setGraphic(icon);
                        }
                        else
                        {
                            MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.FILE_OUTLINE);
                            icon.setSize("18");
                            icon.setFill(Color.CADETBLUE);
                            setGraphic(icon);
                        }
                    }
                }
            }
        });
        
        //when row is selected, set current selected file object
        fileTableView.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv)->{
            //if no selection, return
            if(nv == null)
            {
                fileProperty.set(null);                
                return;
            }
            FileObject fileObject = nv.getFileObject();
            
            //is directory
            if(fileObject.isDirectory())
            {
                folderProperty.set(fileObject);
                fileProperty.set(null);
            }
            
            //is file (also complements the double click of tableview below (if statement))
            if(fileObject.isLeaf())
            {
                fileProperty.set(fileObject);
            }
        });
        
        //double click on table view
        fileTableView.setOnMouseClicked(e->{        
            if (e.getClickCount() == 2) //Checking double click
            {
                if(folderProperty.get()!= null & fileProperty.get() == null)
                {                    
                    initTable(folderProperty.get());
                    fileNavigate.addFileObject(folderProperty.get());
                }
                
            }
        });
        
        FileObject startFile = systemListView.getItems().get(0);
        if(startFile != null)
        {
            initTable(startFile);
            fileTableView.requestFocus();
            fileNavigate.reset(startFile);
            folderProperty.set(startFile);
            fileProperty.set(null);
        }
        
        if(SystemOS.isWindows())
        {
            favoritesListView.getItems().addAll(SystemOS.getWindows().getCommonDirectories());
        }
        
        //avoid focus when component initialization
        fileNameTextField.setFocusTraversable(false);
        
        //init combobox on file type filter extension
        selectedExtensionFilterComboBox.setItems(extensionFilters);        
        StringConverter<ExtensionFilter> converter = new StringConverter<ExtensionFilter>() {
            @Override
            public String toString(ExtensionFilter filter) {
                return filter.getDescription()+ " " +filter.extensions;
            }
            @Override
            public ExtensionFilter fromString(String string) {
                return null;
            }
        };
        selectedExtensionFilterComboBox.setConverter(converter);
        
        if(exploreType == FOLDER_NOFILE || exploreType == FOLDER)
        {
            fileNameAndTypeGridPane.setVisible(false);
            fileNameAndTypeGridPane.setManaged(false);
        }
    }
    
    public ExploreType getExploreType()
    {
        return exploreType;
    }
    
    public void initTable(FileObject fileObject)
    {
        if(fileObject.isReadable())
        {
            ExtensionFilter filter = getSelectedExtensionFilter();
            //System.out.println(filter);
            FileObject[] files;
            if(filter == null)
                files = fileObject.getChildren(false, exploreType);
            else
                files = fileObject.getChildren(false, exploreType, filter.getExtensionsArray());
            
            ArrayList<FileTableData> data = new ArrayList();   
            
            if(files == null)
            {
                fileTableView.setItems(null);
                return;
            }
            
            for(FileObject file: files)
            {                
                data.add(new FileTableData(file));
            }

            //https://staticfinal.blog/2015/05/08/automatically-sort-a-javafx-tableview/
            Callback<FileTableData,Observable[]> cb =(FileTableData fileData) -> new Observable[]{
                fileData.fileObjectProperty(),
            };

            ObservableList<FileTableData> observableList = FXCollections.observableArrayList(cb);
            observableList.addAll(data);

            SortedList<FileTableData> sortedList = new SortedList<>( observableList, 
                (FileTableData fileData1, FileTableData fileData2) -> {
                    int f1 = fileData1.getFileObject().isDirectory() ? 1 : 0;
                    int f2 = fileData2.getFileObject().isDirectory() ? 1 : 0;

                    if(f1 > f2) 
                    {
                        return -1;
                    } 
                    else if(f1 < f2)
                    {                            
                        return 1;
                    } 
                    else 
                    {
                        return 0;
                    }
                });

            fileTableView.setItems(sortedList);
            sortedList.comparatorProperty().bind(fileTableView.comparatorProperty());
            Collections.sort(observableList, (FileTableData fileData1, FileTableData fileData2) -> {
                int f1 = fileData1.getFileObject().isDirectory() ? 1 : 0;
                int f2 = fileData2.getFileObject().isDirectory() ? 1 : 0;

                if(f1 > f2) 
                {
                    return -1;
                } 
                else if(f1 < f2)
                {                            
                    return 1;
                } 
                else 
                {
                    return 0;
                }
            });
        }
        else
            fileTableView.setItems(null);
        
        fileTableView.scrollTo(0);
    }
    
    public void goForward(ActionEvent e)
    {
        FileObject fileObject = fileNavigate.goForward();
        if(fileObject != null)
        {
            initTable(fileObject);
            folderProperty.set(fileObject);
            fileProperty.set(null);
        }
    }
    
    public void goBackward(ActionEvent e)
    {
        FileObject fileObject = fileNavigate.goBack();
        if(fileObject != null)
        {
            initTable(fileObject);
            folderProperty.set(fileObject);        
            fileProperty.set(null);
        }
    }
    
    private void navigateButtonUpdate()
    {
        if(fileNavigate.canGoBackward())
        {
            
        }
    }
    
    public FileObject getSelectedFile()
    {
        return fileProperty.get();
    }
    
    public FileObject getSelectedFolder()
    {
        return folderProperty.get();
    }
    
    public ObservableList<ExtensionFilter> getExtensionFilters() {
        return extensionFilters;
    }
    
    public final ObjectProperty<ExtensionFilter> selectedExtensionFilterProperty() {
        if (selectedExtensionFilter == null) {
            selectedExtensionFilter =
                    new SimpleObjectProperty<>();
        }
        return selectedExtensionFilter;
    }

    public final void setSelectedExtensionFilter(ExtensionFilter filter) {
        selectedExtensionFilterProperty().setValue(filter);
    }

    public final ExtensionFilter getSelectedExtensionFilter() {
        return (selectedExtensionFilter != null)
                ? selectedExtensionFilter.get()
                : null;
    }
    
    public void addExtensions(ExtensionFilter... filters)
    {
        if(filters != null && filters.length > 0)
        {
            getExtensionFilters().addAll(filters);
            if(getSelectedExtensionFilter() == null)
            {
                setSelectedExtensionFilter(getExtensionFilters().get(0));
                initTable(folderProperty.get());
                Platform.runLater(()-> fileTableView.requestFocus());
                selectedExtensionFilterComboBox.getSelectionModel().selectFirst(); //set default selection
            }
        }
    }
    
    public static final class ExtensionFilter {
        private final String description;
        private final List<String> extensions;
        
        public ExtensionFilter(final String description,
                               final String... extensions) {
            validateArgs(description, extensions);

            this.description = description;
            this.extensions = Collections.unmodifiableList(
                                      Arrays.asList(extensions.clone()));
        }
        
        public ExtensionFilter(final String description,
                               final List<String> extensions) {
            final String[] extensionsArray =
                    (extensions != null) ? extensions.toArray(
                                               new String[extensions.size()])
                                         : null;
            validateArgs(description, extensionsArray);

            this.description = description;
            this.extensions = Collections.unmodifiableList(
                                      Arrays.asList(extensionsArray));
        }
        
        public String getDescription() {
            return description;
        }
        
        public List<String> getExtensions() {
            return extensions;
        }
        
        public String[] getExtensionsArray() {
            return extensions.toArray(new String[extensions.size()]);
        }
        
        private static void validateArgs(final String description,
                                         final String[] extensions) {
            if (description == null) {
                throw new NullPointerException("Description must not be null");
            }

            if (description.isEmpty()) {
                throw new IllegalArgumentException(
                        "Description must not be empty");
            }

            if (extensions == null) {
                throw new NullPointerException("Extensions must not be null");
            }

            if (extensions.length == 0) {
                throw new IllegalArgumentException(
                        "At least one extension must be defined");
            }

            for (String extension : extensions) {
                if (extension == null) {
                    throw new NullPointerException(
                            "Extension must not be null");
                }

                if (extension.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Extension must not be empty");
                }
            }
        }
    }
    
    public void setTableFileClick(Consumer<MouseEvent> mouseClickTable)
    {
        tableFileClick = mouseClickTable;
    }
}
