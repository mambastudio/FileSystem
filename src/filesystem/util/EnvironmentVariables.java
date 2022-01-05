/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 *
 * @author user
 */
public class EnvironmentVariables {
    private final HashMap<String, String> variables = new HashMap<>();
    
    private final String tempFile;
    
    private int variableNameLength = 50;
    private int variableValueLength = 100;
    
    public EnvironmentVariables()
    {
        this.tempFile = "envtemp.txt";
    }
    
    public EnvironmentVariables(String tempFile)
    {
        this.tempFile = tempFile;
    }
    
    public EnvironmentVariables(int variableNameLength, int variableValueLength)
    {
        this();
        this.variableNameLength = variableNameLength;
        this.variableValueLength = variableValueLength;
    }
    
    public EnvironmentVariables(String tempFile, int variableNameLength, int variableValueLength)
    {
        this.tempFile = tempFile;
        this.variableNameLength = variableNameLength;
        this.variableValueLength = variableValueLength;
    }
    
    private boolean hasVariable(String variable)
    {
        boolean contains = false;
        
        for(String name : variables.keySet())
            contains |= name.trim().equalsIgnoreCase(variable);
        
        return contains;
    }
    
    public void registerName(String... names)
    {
        for(String string : names)
        {
            variables.put(
                    String.format("%" +variableNameLength+ "s", string),
                    String.format("%" +variableValueLength+ "s"," "));
        }
    }
    
    public void registerValue(String name, String variable)
    {
        if(hasVariable(name))
            variables.put(
                        String.format("%" +variableNameLength+ "s",  name),
                        String.format("%" +variableValueLength+ "s", variable));
    }
    
    public void writeAllToTemporaryFile()
    {
        
    }
    
    private Path getTemporaryDirectory()
    {
        return Paths.get(System.getProperty("java.io.tmpdir"));
    }
    
    private Path getTemporaryFile()
    {
        return Paths.get(getTemporaryDirectory().toString(), tempFile);
    }
    
    public boolean fileExistsInTemporaryDirectory()
    {  
        Path filePath = getTemporaryFile();
        return filePath.toFile().exists();
    }
}
