/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileobject;

import filesystem.core.file.FileObject;
import java.io.File;

/**
 *
 * @author user
 */
public class Main {
    public static void main(String... args)
    {
        //FileObject file = new FileObject();
       
        FileObject file = new FileObject("C:\\Users\\user\\Desktop\\testasd");
        
        
        System.out.println(file);
        System.out.println(file.exists());
        file.ifAbsentCreateFile();
        System.out.println(file.exists());
        file.rename("kubafu.joe");
    }
}
