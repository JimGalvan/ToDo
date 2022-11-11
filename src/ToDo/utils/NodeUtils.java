package ToDo.utils;

import javafx.scene.Node;
import javafx.scene.control.ListCell;

public class NodeUtils {

    public static String getNodeText(Node node){
       return ((ListCell<?>) node).getText();
    }
}
