/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem;

import filesystem.zip.ZipReader;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author user
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {       
        //ZipReader reader = new ZipReader("C:\\Users\\user\\Documents\\jd-gui-windows-1.4.0.zip");
        //System.out.println(reader.info());
        Path path1 = Paths.get("damba\\javafx\\Text.txt");
        Path path2 = Paths.get("java\\javafx");
        
        System.out.println(Paths.get(path2.toString(), path1.toString()));
    }
}
