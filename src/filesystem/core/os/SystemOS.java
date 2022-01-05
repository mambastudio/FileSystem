/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.core.os;

import com.sun.jna.Platform;
import static filesystem.core.os.SystemOS.OS.WINDOWS;

/**
 *
 * @author user
 */
public final class SystemOS {
    // create enums for each operating system
    public enum OS {
        WINDOWS, LINUX, MAC, SOLARIS
    };
    
    private SystemOS()
    {
        
    }
        
    public static boolean isWindows()
    {
        return getOperatingSystem() == WINDOWS;
    }
    
    public static WindowsOS getWindows()
    {
        if(Platform.isWindows())
            return new WindowsOS();
        else
            return null;
    }
    
    public static OS getOperatingSystem()
    {         
        if (Platform.isWindows()) {
            return OS.WINDOWS;
        }
 
        else if (Platform.isLinux()) {
            return OS.LINUX;
        }
 
        else if (Platform.isMac()) {
            return OS.MAC;
        }
 
        else if (Platform.isSolaris()) {
            return OS.SOLARIS;
        }
 
        return null;
    }
}
