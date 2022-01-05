/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.core.os;

import com.sun.jna.platform.win32.KnownFolders;
import com.sun.jna.platform.win32.Shell32Util;
import filesystem.core.file.FileObject;

/**
 *
 * @author user
 */
public class WindowsOS {
    
    
    protected WindowsOS()
    {
        
    }
    
    public FileObject[] getCommonDirectories()
    {
        FileObject[] directories = new FileObject[5];
        directories[0] = getDesktopDirectory();
        directories[1] = getDocumentsDirectory();
        directories[2] = getPicturesDirectory();
        directories[3] = getProgramDirectory();
        directories[4] = getDownloadDirectory();
        return directories;
    }
    
    public FileObject getProgramDirectory()
    {
        String downloadsDir = Shell32Util.getKnownFolderPath(
                KnownFolders.FOLDERID_Programs);
        return new FileObject(downloadsDir);
    }
    public FileObject getDesktopDirectory()
    {
        String downloadsDir = Shell32Util.getKnownFolderPath(
                KnownFolders.FOLDERID_Desktop);
        return new FileObject(downloadsDir);
    }
    public FileObject getDocumentsDirectory()
    {
        String downloadsDir = Shell32Util.getKnownFolderPath(
                KnownFolders.FOLDERID_Documents);
        return new FileObject(downloadsDir);
    }
    
    public FileObject getPicturesDirectory()
    {
        String downloadsDir = Shell32Util.getKnownFolderPath(
                KnownFolders.FOLDERID_Pictures);
        return new FileObject(downloadsDir);
    }  
    
    public FileObject getDownloadDirectory()
    {
        String downloadsDir = Shell32Util.getKnownFolderPath(
                KnownFolders.FOLDERID_Downloads);
        return new FileObject(downloadsDir);
    }
}
