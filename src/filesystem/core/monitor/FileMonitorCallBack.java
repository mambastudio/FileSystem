/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.core.monitor;

import filesystem.core.file.FileObject;

/**
 *
 * @author user
 */
public interface FileMonitorCallBack {
    public void execute(FileObject parent, FileObject child);
}
