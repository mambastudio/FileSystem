/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.core.monitor;

import filesystem.fx.callback.FileMonitorCallBack;
import filesystem.core.file.FileObject;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class FileMonitor 
{
    private final WatchService watcher;
    private final Map<WatchKey, FileObject> keys;
    private boolean exit = false;
    
    private FileMonitorCallBack modify = null, create = null, delete = null;
    
    public FileMonitor(FileObject root)
    {
        this.watcher = root.getNewWatchService();
        this.keys = new HashMap<>();
        this.walkAndRegisterDirectories(root);
    }
    
    private void registerDirectory(FileObject dir) throws IOException 
    {       
        WatchKey key = dir.getPath().register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir);
    }
    
    private void walkAndRegisterDirectories(FileObject rootDirectory)
    {
        try {
            // register directory and sub-directories
            Files.walkFileTree(rootDirectory.getPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    registerDirectory(new FileObject(dir));
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(FileMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void exit()
    {
        exit = true;
    }
    
    public void processEvents()
    {
        Thread thread = new Thread(()->{
            while(exit != true)
            {
                // wait for key to be signalled
                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException x) {
                    return;
                }
                
                Path dir = keys.get(key).getPath();
                if (dir == null) {
                    System.err.println("WatchKey not recognized!!");
                    continue;
                }
                
                key.pollEvents().forEach((event) -> {
                    @SuppressWarnings("rawtypes")
                            WatchEvent.Kind kind = event.kind();
                    // Context for directory entry event is the file name of entry
                    @SuppressWarnings("unchecked")
                    Path name = ((WatchEvent<Path>)event).context();
                    Path child = dir.resolve(name);
                    FileObject childFile = new FileObject(child);
                    // do something useful
                    // print out event
                                        
                    // if directory is created, and watching recursively, then register it and its sub-directories
                    if (kind == ENTRY_CREATE) {
                        if (Files.isDirectory(child)) {
                            walkAndRegisterDirectories(new FileObject(child));
                        }
                    }
                    
                    if(kind == ENTRY_MODIFY)
                    {
                        if(modify != null)
                            modify.execute(keys.get(key), childFile);
                    }
                    else if(kind == ENTRY_CREATE)
                    {
                        if(create != null)
                            create.execute(keys.get(key), childFile);
                    }
                    else if(kind == ENTRY_DELETE)
                    {
                        if(delete != null)
                            delete.execute(keys.get(key), childFile);
                    }
                });
                
                // reset key and remove from set if directory no longer accessible
                boolean valid = key.reset();
                if (!valid) {
                    keys.remove(key);

                    // all directories are inaccessible
                    if (keys.isEmpty()) {
                        break;
                    }
                }
            }
        });
        thread.start();
    }
    
    public void registerDelete(FileMonitorCallBack callback)
    {
        this.delete = callback;
    }
    
    public void registerModify(FileMonitorCallBack callback)
    {
        this.modify = callback;
    }
    
    public void registerCreate(FileMonitorCallBack callback)
    {
        this.create = callback;
    }
}
