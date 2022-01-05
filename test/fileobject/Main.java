/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileobject;

import filesystem.core.file.FileObject;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class Main {
    public static void main(String... args)
    {
        //FileObject file = new FileObject();
       
        FileObject file = new FileObject("C:\\Users\\user\\Desktop");
        ArrayList<FileObject> fileList = new ArrayList(Arrays.asList(file.getChildren(false, ".pdf")));
        fileList.forEach(fObj -> {
            System.out.println(fObj.getPathName());
        });
        
    }
}
