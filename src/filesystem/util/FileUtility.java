/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.util;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class FileUtility 
{    
    public static Path createFile(Path path)
    {
        try {
            return Files.createFile(path);
        } catch (IOException ex) {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
        
    public static Path createFile(String directory, String file)
    {
        return createFile(FileSystems.getDefault().getPath(directory, file));
    }
    
    public static boolean exists(Path path)
    {
        return Files.exists(path);
    }
    
    public static Path createTempFile(String prefix, String suffix)
    {        
        try {
            Path tempPath = Files.createTempFile(prefix, suffix);
            tempPath.toFile().deleteOnExit();
            return tempPath;
        } catch (IOException ex) {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static String read(Path path)
    {
        try {
            byte[] stringBytes = Files.readAllBytes(path);
            return new String(stringBytes);
        } catch (IOException ex) {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static List<String> readLines(Path path)
    {
        try {
            return Files.readAllLines(path);
        } catch (IOException ex) {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static void write(Path path, String... stringArray)
    {
        try {
            Files.write(path, Arrays.asList(stringArray));
        } catch (IOException ex) {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void writeTempFile(String prefix, String suffix, String... stringArray)
    {
        try {
            Path path = createTempFile(prefix, suffix); 
            Files.write(path, Arrays.asList(stringArray));
        } catch (IOException ex) {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
