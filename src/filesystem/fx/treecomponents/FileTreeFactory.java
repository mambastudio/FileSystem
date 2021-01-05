/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.fx.treecomponents;

import java.util.HashMap;

/**
 *
 * @author user
 */
public class FileTreeFactory {
    private final static HashMap<String, FileTreeCellFunctions> treeCellHash = new HashMap<>();
    
    public static FileTreeCellFunctions getCellImpl(String name)
    {
        return treeCellHash.get(name);
    }
    
    public static FileTreeCellFunctions putCellImpl(String name, FileTreeCellFunctions impl)
    {
        return treeCellHash.put(name, impl);
    }
}
