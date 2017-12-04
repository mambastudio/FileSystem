/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.core;

import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
    
    public FileObject(Path path)
    {
        if(path.toString().toLowerCase().endsWith(".zip") && !path.getFileSystem().toString().endsWith(".zip"))
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
        else
            this.path = path;
    }
    
    public boolean isFileSystem()
    {
        return (path.getParent() == null);
    }
    
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
    
    public FileSystem getFileSystem()
    {        
        return path.getFileSystem();
    }
    
    public boolean deleteOnExit()
    {
        try {
            return Files.deleteIfExists(path);
        } catch (IOException ex) {
            Logger.getLogger(FileObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally  {
            return false;
        }
    }
    
    public Path getPath()
    {
        return path;
    }
    
    public FileObject[] getChildren()
    {
        if(Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS))
        {
            ArrayList<FileObject> children = new ArrayList<>();
            try (DirectoryStream<Path> dirs = Files.newDirectoryStream(path)) 
            {                
                for(Path dir : dirs)                
                    children.add(new FileObject(dir));               
            }
            catch(IOException ex)
            {
                Logger.getLogger(FileObject.class.getName()).log(Level.SEVERE, null, ex);
            }            
            return children.toArray(new FileObject[children.size()]);
        }
        return null;
    }
    
    public ArrayList<FileObject> getChildrenList()
    {
        return new ArrayList<>(Arrays.asList(getChildren()));
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
    
    public String getPathName()
    {
        if(path.getFileName() == null)
            return path.getFileSystem().toString();
        else
            return path.toString();
    }
    
}
