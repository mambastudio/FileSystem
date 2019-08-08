/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import filesystem.util.FileUtility;
import filesystem.util.FileUtility.FileOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class FileUtilityTest {
    public static void main(String... args)
    {
        test2();
    }
    
    public static void test1()
    {
        Path path = Paths.get("josto.txt");
        path = FileUtility.createFile(path, FileOption.IN_TEMPORARY_DIR_DONT_DELETE);
        
        FileUtility.writeLines(path, "classpath = josto/josto/kubafu",
                                     "init dir  = josto/kubafu/kondoo");
        FileUtility.readLines(path, string -> System.out.println(Arrays.asList(string.split("="))));
        
        System.out.println(path);
        System.out.println("exists " +FileUtility.fileExistsInTemporaryDirectory("josto.txt"));
        path.toFile().delete();
        System.out.println("exists " +FileUtility.fileExistsInTemporaryDirectory("josto.txt"));
    }
    
    public static void test2()
    {
        System.out.println(FileUtility.fileExistsInTemporaryDirectory("ctracer.txt"));
        //FileUtility.readLines(FileUtility.getFilePathFromTemporaryDirectory("ctracer.txt"), string -> System.out.println(Arrays.asList(string.split("="))));
        FileUtility.writeLines(FileUtility.getFilePathFromTemporaryDirectory("ctracer.txt"), "scenepath = .");
    }
}
