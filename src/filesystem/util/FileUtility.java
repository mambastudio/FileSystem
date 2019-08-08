/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.util;

import filesystem.core.FileObject;
import static filesystem.util.FileUtility.FileOption.IN_TEMPORARY_DIR_DONT_DELETE;
import static filesystem.util.FileUtility.FileOption.TEMPORARY;
import static filesystem.util.FileUtility.FileOption.TEMPORARY_RANDOM;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class FileUtility 
{    
    public enum FileOption{TEMPORARY, IN_TEMPORARY_DIR_DONT_DELETE, TEMPORARY_RANDOM};
    
    public static Path createFile(String prefix, String suffix, FileOption... options)
    {
        Path path = Paths.get(prefix, suffix);       
        return createFile(path, options);
    }
    
    public static Path createFile(Path path, FileOption... options)
    {        
       if(Files.exists(path))
            return path;
        try
        {           
            if(options.length == 0)
            {
                Path newPath = Files.createFile(path);
                return newPath;
            }
            else if(options[0].equals(TEMPORARY))
            {                
                Path newPath = Files.createFile(path);
                newPath.toFile().deleteOnExit();
                return newPath;
            }
            else if(options[0].equals(IN_TEMPORARY_DIR_DONT_DELETE))
            {
                Path newPath = Paths.get(getTemporaryDirectory().toString(), path.toString());
                if(Files.exists(newPath))
                    return newPath;                
                return Files.createFile(newPath);
            }
        }
        catch(IOException ex)
        {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        return null; 
    }
    
    public static Path createDirectory(String directory)
    {
        Path path = Paths.get(directory);
        if(Files.exists(path))
            return path;
        try {
            return Files.createDirectory(path);
        } catch (IOException ex) {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static FileObject createDirectoryFileObject(String directory)
    {
        return new FileObject(createDirectory(directory));
    }
        
    public static Path getTemporaryDirectory()
    {
        return Paths.get(System.getProperty("java.io.tmpdir"));
    }
      
    public static boolean fileExistsInTemporaryDirectory(String fileName)
    {
        Path temporaryDirectory = getTemporaryDirectory();
        Path filePath = Paths.get(temporaryDirectory.toString(), fileName);
        return filePath.toFile().exists();
    }
    
    public static Path getFilePathFromTemporaryDirectory(String fileName)
    {
        return Paths.get(getTemporaryDirectory().toString(), fileName);
    }
    
    public static boolean exists(Path path)
    {
        return Files.exists(path);
    }
    
    public static String readAll(Path path)
    {
        try {
            byte[] stringBytes = Files.readAllBytes(path);
            return new String(stringBytes);
        } catch (IOException ex) {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
        
    public static void readLines(Path path, Consumer<String> consumer)
    {
        try
        {                        
            List<String> lines = Files.readAllLines(path);
            lines.forEach(consumer);
            
        } catch (IOException ex) {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public static void writeLines(Path path, String... lines)
    {        
        try {
            Files.write(path, Arrays.asList(lines));
        } catch (IOException ex) {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    public static void writeLines(Path path, ArrayList<String> lines)
    {
        try {
            Files.write(path, lines);
        } catch (IOException ex) {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static FileObject[] getChildren(Path path)
    {
        return null;
    }
}
