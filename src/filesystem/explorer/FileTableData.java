/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.explorer;


import filesystem.core.file.FileObject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author user
 */
public class FileTableData {
    private String fileName = null;
    private String lastModified = null;
    private String fileSize = null;
    private String fileExtension = null;
    
    private ObjectProperty<FileObject> fileObject = null;
    
    public FileTableData(FileObject fileObject) {        
        init(fileObject.getNameWithoutExtension(), fileObject.getLastModified(), fileObject.getSizeString(), fileObject.getFileExtension());
        this.fileObject = new SimpleObjectProperty<>(fileObject);
    }

    private void init(String firstName, String lastName, String fileSize, String fileExtension) {
        
        this.fileName = firstName;
        this.lastModified = lastName;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
    }
    
    public FileObject getFileObject()
    {
        return fileObject.get();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLastModified() {
        return lastModified;
    }
    
    public String getFileExtension()
    {
        if(!fileObject.get().isDirectory())
            return fileExtension;
        else
            return "-";
    }
    
    public void setFileExtension(String fileExtension)
    {
        this.fileExtension = fileExtension;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }    
    
    public ObjectProperty<FileObject> fileObjectProperty()
    {
        return fileObject;
    }
    
    public String getFileSize() {
        if(!fileObject.get().isDirectory())
            return fileSize;
        else
            return "-";
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
