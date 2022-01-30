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
    private final ArrayList<TableDataInfo> list;
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
        list.add(new TableDataInfo(file));        
    }
    
    public TableDataInfo getCurrent()
    {
        return list.get(index);
    }
    
    public TableDataInfo goBack()
    {
        if(canGoBackward())
        {
            index--;
            return list.get(index);
        }
        return null;
    }
    
    public TableDataInfo goForward()
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
    
    public static class TableDataInfo
    {
        private final FileObject file;
        private int tableIndex = -1;
        
        TableDataInfo(FileObject file)
        {
            this.file = file;
            
        }
        
        public FileObject getFile()
        {
            return file;
        }
        
        public int getTableIndex()
        {
            return tableIndex;
        }
        
        public void setTableIndex(int tableIndex)
        {
            this.tableIndex = tableIndex;
        }
        
    }
}
