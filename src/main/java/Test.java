import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class Test {
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));

        System.out.println(System.getProperty("user.home"));
        String str = "abode,1";
        String[] strings = str.split(",");
        for (String string:strings){
            System.out.println(string);
        }

    }

}
