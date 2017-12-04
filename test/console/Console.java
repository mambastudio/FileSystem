/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console;

import filesystem.core.OutputInterface;
import javafx.scene.control.TextArea;

/**
 *
 * @author user
 */
public class Console implements OutputInterface {
    private final TextArea textArea;

    private String sceneParsingTime = "___";
    private String bvhBuildingTime = "___";
    private String renderingTime = "___";
    
    public Console(TextArea textArea)
    {
        this.textArea = textArea;
        display();
    }
    @Override
    public void print(String key, String string) {
        switch (string) {
            case "scene":
                sceneParsingTime = string;
                break;
            case "bvh":
                bvhBuildingTime = string;
                break;
            case "render":
                renderingTime = string;
                break;
            default:
                break;
        }
    }
    
    
    public final void display()
    {
        StringBuilder timeStringBuilder = new StringBuilder();
        timeStringBuilder.append("Scene parsing : ").append(sceneParsingTime).append("\n");
        timeStringBuilder.append("BVH building  : ").append(bvhBuildingTime).append("\n");
        timeStringBuilder.append("Rendering     : ").append(renderingTime);
        textArea.setText(timeStringBuilder.toString());
    }
}
