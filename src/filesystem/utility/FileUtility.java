/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.utility;

import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class FileUtility {
    public static FileSystem createZip(Path path, boolean create)
    {
        try 
        {
            // convert the filename to a URI           
            final URI uri = URI.create("jar:file:" + path.toUri().getPath());
            final Map<String, String> env = new HashMap<>();
            if (create) {
                env.put("create", "true");
            }
            return FileSystems.newFileSystem(uri, env);
        } 
        catch (IOException ex) {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Path getZipPath(Path path)
    {
        if(doesExtensionEndWithZip(path))
        {
            try 
            {
                // convert the filename to a URI           
                final URI uri = URI.create("jar:file:" + path.toUri().getPath());
                final Map<String, String> env = new HashMap<>();                
                FileSystem fs =  FileSystems.newFileSystem(uri, env);
                return fs.getPath("/");
            } 
            catch (IOException ex) {
                Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
        return null;
    }
    
    public static boolean doesExtensionEndWithZip(Path path)
    {
        return path.toString().toLowerCase().endsWith(".zip");
    }
    
   
    public static ArrayList<Path> getChildren(Path path)
    {
        ArrayList<Path> children = new ArrayList<>();
        FileSystem zipFileSystem = FileUtility.createZip(path, false); 
        Path root =  zipFileSystem.getPath("/");
        System.out.println(Files.isDirectory(root));
        try (DirectoryStream<Path> dirs = Files.newDirectoryStream(path)) 
        {            
            for(Path dir : dirs)            
                children.add(dir);            
        }
        catch(IOException ex)
        {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return children;
    }
}
