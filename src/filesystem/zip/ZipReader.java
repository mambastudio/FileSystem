/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.zip;

import filesystem.utility.FileUtility;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class ZipReader 
{
    private Path path = null;
    
    public ZipReader(String uri)
    {
        this.path = Paths.get(uri).normalize();        
    }
    
    public ZipReader(URI uri)
    {
        this.path = Paths.get(uri).normalize();
    }
    
    public ZipReader(Path path)
    {
        this.path = path.normalize();
    }
    
    public Path getRootPath()
    {
        return this.path;
    }
    
    public Path getActualRootPath()
    {
        return FileUtility.getZipPath(path);
    }
    
    public void unZipTo(String destDirectory)
    {
        try
        {
            FileSystem zipFileSystem = FileUtility.createZip(path, false);
            
            //get absolute file path
            Path destDir;
            if(!Paths.get(destDirectory).isAbsolute())           
                destDir = Paths.get(path.getParent().toString(), destDirectory);
            else
                destDir = Paths.get(destDirectory);
            
            //if the destination doesn't exist, create it
            if(Files.notExists(destDir))
            {                
                System.out.println(destDir + " does not exist. Creating...");
                Files.createDirectories(destDir);                
            }
            
            //walk down the tree
            Path root = zipFileSystem.getPath("/");            
            Files.walkFileTree(root, new SimpleFileVisitor<Path>(){             
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
                {
                    final Path destFile = Paths.get(destDir.toString(), file.toString());
                    System.out.printf("Extracting file %s to %s\n", file, destFile);
                    Files.copy(file, destFile, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException 
                {
                    final Path dirToCreate = Paths.get(destDir.toString(), dir.toString());
                    if(Files.notExists(dirToCreate)){
                        System.out.printf("Creating directory %s\n", dirToCreate);
                        Files.createDirectory(dirToCreate);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } 
        catch (IOException ex) {
            Logger.getLogger(ZipReader.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public String info()
    {
        StringBuilder builder = new StringBuilder();
        FileSystem zipFileSystem = FileUtility.createZip(path, false);
        Path root = zipFileSystem.getPath("");
        
        try
        {
            Files.walkFileTree(root, new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException 
                {
                    builder.append(getFileInfo(file));
                    return FileVisitResult.CONTINUE;
                }
 
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException 
                {                   
                    builder.append(getFileInfo(dir));
                    return FileVisitResult.CONTINUE;
                } 
            });
        }
        catch(IOException ex)
        {
            Logger.getLogger(ZipReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return builder.toString().replaceAll("\\s+$", "");
    }
    
    public ArrayList<Path> getAllPaths()
    {
        ArrayList<Path> paths = new ArrayList<>();
        FileSystem zipFileSystem = FileUtility.createZip(path, false); 
        Path root = zipFileSystem.getPath("\\");
        
        try
        {
            Files.walkFileTree(root, new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException 
                {                    
                    paths.add(file);
                    return FileVisitResult.CONTINUE;
                }
 
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException 
                {         
                    if(!dir.equals(root))
                        paths.add(dir.normalize());
                    return FileVisitResult.CONTINUE;
                } 
            });
        }
        catch(IOException ex)
        {
            Logger.getLogger(ZipReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return paths;
    }
    
    
    private String getFileInfo(Path file)
    {
        try
        {
            final DateFormat df = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
            final String modTime= df.format(new Date(
                                 Files.getLastModifiedTime(file).toMillis()));
            return String.format("%7d  %s  %s\n",
                                    Files.size(file),
                                    modTime,
                                    file);
        }
        catch(IOException ex)
        {
            Logger.getLogger(ZipReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
