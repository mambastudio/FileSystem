/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author user
 */
public class SystemFiles {
    public static void main(String... args) throws IOException, InterruptedException
    {
        Process p =  Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v Favorites");
        p.waitFor();

        InputStream in = p.getInputStream();
        byte[] b = new byte[in.available()];
        in.read(b);
        in.close();

        String myDocuments = new String(b);
        myDocuments = myDocuments.split("\\s\\s+")[4];
        
        System.out.println(myDocuments);
    }
}
