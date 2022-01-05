/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.explorer;

import filesystem.core.file.FileObject;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class FileNavigateBidirectional {
    private final ArrayList<FileObject> list;
    private int index = -1;
    
    public FileNavigateBidirectional()
    {
        list = new ArrayList();
    }
    
    public void reset(FileObject file)
    {
        list.clear();
        index = -1;
        addFileObject(file);
    }
    
    public void addFileObject(FileObject file)
    {        
        index++;
        list.subList(index, list.size()).clear();
        list.add(file);        
    }
    
    public FileObject goBack()
    {
        if(canGoBackward())
        {
            index--;
            return list.get(index);
        }
        return null;
    }
    
    public FileObject goForward()
    { 
        if(canGoForward())
        {
            index++;
            return list.get(index);
        }
        
        return null;
    }
        
    public boolean canGoForward()
    {
        return isWithinRange(index + 1);
    }
    
    public boolean canGoBackward()
    {
        return isWithinRange(index - 1);
    }
    
    private boolean isWithinRange(int index)
    {
        return index >-1 && index < list.size();
    }
}
