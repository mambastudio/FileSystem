/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.core;

/**
 *
 * @author user
 */
public class OutputFactory {
    private static OutputInterface output = null;
    
    public static void setOutput(OutputInterface output)
    {
        OutputFactory.output = output;
    }
    
    public static void print(String key, String string)
    {
        output.print(key, string);
    }
}
