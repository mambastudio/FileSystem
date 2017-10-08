/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileobject;

import filesystem.core.FileObject;
import java.nio.file.Path;

/**
 *
 * @author user
 */
public class Main {
    public static void main(String... args)
    {
        //FileObject file = new FileObject("C:\\Users\\user\\Desktop\\Desktop.zip");
        FileObject file = new FileObject("C:\\Users\\user\\Desktop\\Desktop.zip");
        Path path = file.getFileSystem().getPath("Depth1.zip");
        FileObject inner = new FileObject(path);        
    }
}
