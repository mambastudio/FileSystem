/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.core.file;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class FileObject 
{
    private Path path = null;
    
    public FileObject(String path)
    {
        this(Paths.get(path));
    }
    
    public FileObject(File file)
    {
        this(file.toPath());
    }
    
    public FileObject(URI uri)
    {
        this(new File(uri));
    }
    
    public FileObject()
    {
        this(Paths.get(".").toAbsolutePath());
    }
    
    public FileObject(Path path)
    {
        //how do I open zip and jar files
        /*if(path.toString().toLowerCase().endsWith(".zip") && !path.getFileSystem().toString().endsWith(".zip"))
        {
            try 
            {                
                // convert the filename to a URI           
                final URI uri = URI.create("jar:file:" + path.toUri().getPath());
                final Map<String, String> env = new HashMap<>();                
                FileSystem fs =  FileSystems.newFileSystem(uri, env);
                this.path = fs.getPath("/");
            } 
            catch (IOException ex) {
                Logger.getLogger(FileObject.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
        else */
        {            
            this.path = path;
        }
    }
    
    //get root folders C:, D:, E:, etc
    public static FileObject[] getSystemRootArray()
    {
        File[] fileRoots = File.listRoots();
        FileObject[] fileobjectRoots = new FileObject[fileRoots.length];
        for(int i = 0; i<fileRoots.length; i++)
        {
            FileObject f = new FileObject(fileRoots[i]);
            fileobjectRoots[i] = f;
        }
        
        return fileobjectRoots;
    }
    
    //get root folders C:, D:, E:, etc    
    public static ArrayList<FileObject> getSystemRootList()
    {
        ArrayList<FileObject> rootList = new ArrayList<>();
        FileObject[] rootArray = getSystemRootArray();        
        rootList.addAll(Arrays.asList(rootArray));
        return rootList;
    }
    
    //is C:, D:, E:, etc
    public boolean isFileSystem()
    {
        return (path.getParent() == null);
    }
    
    public boolean ifAbsentCreateDirectorate()
    {
        if(absent())
            try {
                Files.createDirectory(Paths.get(path.toString()));
                return exists();
            } catch (IOException ex) {
                Logger.getLogger(FileObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        return exists();
    }
    
    public boolean ifAbsentCreateFile()
    {
        if(absent())
            try {
                Files.createFile(Paths.get(path.toString()));
                return exists();
            } catch (IOException ex) {
                Logger.getLogger(FileObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        return exists();
    }
    
    public boolean delete()
    {       
        try {
            return  Files.deleteIfExists(path);
        } catch (IOException ex) {
            Logger.getLogger(FileObject.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return false;        
    }
    
    public boolean absent()
    {
        return !exists();
    }
    
    public boolean exists()
    {
        return Files.exists(path);
    }
    
    //fix me: (same as isFileSystem())
    public boolean isRoot()
    {
        return (path.getParent() == null);
    }
    
    public boolean isLeaf()
    {
        return !Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);
    }
    
    public boolean isDirectory()
    {
        return Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);
    }
    
    public boolean isNotDirectory()
    {
        return !isDirectory();
    }
    
    public FileSystem getFileSystem()
    {        
        return path.getFileSystem();
    }
    
    public String getRootName()
    {
        return path.getRoot().toString();
    }
        
    public WatchService getNewWatchService()
    {
        try {
            return getFileSystem().newWatchService();
        } catch (IOException ex) {
            Logger.getLogger(FileObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public boolean deleteOnExit()
    {
        try {
            return Files.deleteIfExists(path);
        } catch (IOException ex) {
            Logger.getLogger(FileObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
       
    }
    
    public Path getPath()
    {
        return path;
    }
    
    public FileObject[] getChildren()
    {
        if(Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS))
        {
            File directory = getFile();
            File[] files = directory.listFiles();
            FileObject[] fileObjects = new FileObject[files.length];
            
            for(int i = 0; i<files.length; i++)
                fileObjects[i] = new FileObject(files[i]);
            
            return fileObjects;
        }
        
        return null;
    }
    
    public FileObject[] getChildren(String... extensions)
    {        
        if(Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS))
        {
            File directory = getFile();
            File[] files;
            if(extensions.length != 0)        
                files = directory.listFiles(file -> {
                    FileObject fo = new FileObject(file);
                    if(fo.isDirectory()) return true;
                    boolean accept = false;                
                    for(String extension : extensions)
                        accept |= fo.hasFileExtension(extension);

                    return accept;
            });  
            else
                files = directory.listFiles();
            
            if(files.length == 0) return null;
            
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));
            
            FileObject[] fileObjects = new FileObject[files.length];
            
            for(int i = 0; i<files.length; i++)
                fileObjects[i] = new FileObject(files[i]);
            
            return fileObjects;
        }
        return null;
    }
    
    public String getName()
    {
        if(path.getFileName() == null)
        {                    
            Path root = Paths.get(path.getFileSystem().toString());                
            return root.getFileName().normalize().toString();
        }
        else
            return path.normalize().getFileName().toString().replace("/", "");
    }
    
    public String getNameWithoutExtension()
    {
        if(!hasExtension())
            return getName();
        else
        {
            String fileName = getName();
            int index = fileName.lastIndexOf(".");
            return fileName.substring(0, index);
        }
    }
    
    public boolean rename(String name)
    {
        try {
            path = Files.move(path, path.resolveSibling(name), REPLACE_EXISTING);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(FileObject.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public String getPathName()
    {
        if(path.getFileName() == null)
            return path.getFileSystem().toString();
        else
            return path.toString();
    }
    
    public String getFileExtension()            
    {
        String fileName = getName();
        int index = fileName.lastIndexOf(".");
        
        if (index > 0) 
            return fileName.substring(index + 1); 
        else 
            return null;
    }
    
    public boolean hasExtension()
    {
        if(isDirectory())
            return false;
        else return getFileExtension() != null;
    }
    
    public boolean hasFileExtension(String extension)
    {
        return (getFileExtension() != null) && (getFileExtension().contains(extension));
    }
    
    public File getFile()
    {
        return path.toFile();
    }
    
    public void openNative()
    {
        if(Desktop.isDesktopSupported())
            try {
                Desktop.getDesktop().open(path.toFile());
        } catch (IOException ex) {
            Logger.getLogger(FileObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        else
            throw new UnsupportedOperationException("No desktop application related to the file");
    }
    
    public boolean equals(FileObject file)
    {
        return this.getPath().equals(file.getPath());
    }
    
    @Override
    public String toString()
    {
        return path.toString();
    }
}
