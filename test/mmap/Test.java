/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmap;

import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class Test {
    
    static float[] vArray;
    static float[] vtArray;
    static float[] vnArray;
    static int[] fArray;
    
        
    public static void main(String... args) throws Exception
    {
        RandomAccessFile file = new RandomAccessFile("C:\\Users\\user\\Documents\\Scene3d\\simplebox\\hair.obj", "rw");
        MappedByteBuffer buffer = file.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());     
        parse1(buffer);    
        parse2(buffer);
    }
    
    public static void parse1(MappedByteBuffer buffer)
    {
        CharBuffer cbuffer = Charset.forName("UTF-8").decode(buffer);
            
        int v = 0;
        int vt = 0;
        int vn = 0;
        int f = 0;
        int o = 0;
      
        while(cbuffer.hasRemaining())
        {            
            char c = cbuffer.get();             
            if(c == 'v') 
                if(cbuffer.hasRemaining())
                {
                    char nxt = cbuffer.get();
                    switch (nxt) {
                        case 't':
                            vt++;
                            break;
                        case 'n':
                            vn++;
                            break;
                        case ' ':
                            v++;
                            break;
                    }
                }
            if(c == 'f')
                if(cbuffer.hasRemaining())
                {
                    char nxt = cbuffer.get();
                    switch (nxt) {
                        case ' ':
                            f++;
                            break;
                    }                   
                }
            if(c == 'o')
                if(cbuffer.hasRemaining())
                {
                    char nxt = cbuffer.get();
                    switch (nxt) {
                        case ' ':
                            o++;
                            break;
                    }                   
                }               
                 
        }
               
        vArray = new float[v*3];
        vtArray = new float[vt*2];
        vnArray = new float[vn*3];
        fArray = new int[f*10];
        
        System.out.println("v     " +v);
        System.out.println("vn    " +vn);
        System.out.println("vt    " +vt);
        System.out.println("f     " +f);
        System.out.println("o     " +o);
        
    }
    
    public static void parse2(MappedByteBuffer buffer)
    {
        buffer.rewind();
        CharBuffer cbuffer = Charset.forName("UTF-8").decode(buffer);
                    
        while(cbuffer.hasRemaining())
        {            
            char c = cbuffer.get();             
            if(c == 'v') 
                if(cbuffer.hasRemaining())
                {
                    char nxt = cbuffer.get();
                    switch (nxt) {
                        case 't':
                            readLine(cbuffer);
                            break;
                        case 'n':
                            readLine(cbuffer);                            
                            break;
                        case ' ':                        
                            readLine(cbuffer);                            
                            break;
                    }
                }
            if(c == 'f')
            {
                 if(cbuffer.hasRemaining())
                {
                    char nxt = cbuffer.get();
                    switch (nxt) {
                        case ' ':
                            readLine(cbuffer);
                            break;
                    }                   
                }
            }
            if(c == 'o')
            {
                 if(cbuffer.hasRemaining())
                {
                    char nxt = cbuffer.get();
                    switch (nxt) {
                        case ' ':
                            readLine(cbuffer);
                            break;
                    }                   
                }
            }            
        }         
    }
    
    public static char peekNextChar(CharBuffer cbuffer)
    {
        int currentPostion = cbuffer.position();            
        char peekChar = cbuffer.get();
        cbuffer.position(currentPostion);
        return peekChar;
    }    
   
    public static String readLine(CharBuffer cbuffer)
    {
        goBack(cbuffer, 2);
        String line = "";
        while(true)
        {
            char c = cbuffer.get();
            if(c == '\n' || c == '\r')
                break;
            else
                line += c;
        }            
        return line;
    }
    
    public static void goBack(CharBuffer cbuffer, int step)
    {
        int position = cbuffer.position();
        cbuffer.position(position - step);
    }
    
}
