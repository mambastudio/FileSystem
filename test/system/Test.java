/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.ShlObj;
import com.sun.jna.platform.win32.WinDef;
import filesystem.core.os.SystemOS;
import filesystem.core.os.WindowsOS;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String... args)
    {
        WindowsOS win = SystemOS.getWindows();
        System.out.println(win.getDesktopDirectory());
        System.out.println(win.getPicturesDirectory());
        System.out.println(win.getDocumentsDirectory());
    }
}
