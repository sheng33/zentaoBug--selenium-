import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class Controller {
    private Thread thread;
    private ZenTao xt = null;
    @FXML
    private Button start;

    @FXML
    private Button upLoad;

    @FXML
    private Label fileName;

    @FXML
    void StartEnter(MouseEvent event) {
        start.setCursor(Cursor.HAND);
    }

    @FXML
    void StartPresse(MouseEvent event) throws InterruptedException, AWTException, IOException {

        if (fileName.getText().equals("")){
            JOptionPane.showMessageDialog(null, "请先上传文件", "错误", JOptionPane.ERROR_MESSAGE);
        }else{
            thread = new Thread() {
                @Override
                public void run() {
                    try {
                        xt.start();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (AWTException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }
    }

    @FXML
    void loadFile(MouseEvent event) throws Exception {
        FileSystemView sys = FileSystemView.getFileSystemView();
        FileChooser fileChooser = new FileChooser();
        File file = new File(sys.getHomeDirectory().toString());
        System.out.println("xt:"+System.getProperty("os.name"));
        fileChooser.setInitialDirectory(file);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("xls,xlsx", "*.xls","*.xlsx")
        );
        File result =fileChooser.showOpenDialog(null);
        System.out.println(result.getAbsolutePath());
        xt = new ZenTao(result.getAbsolutePath());
        System.out.println(result.getAbsolutePath());
        String[] strings = result.getAbsolutePath().split("\\\\");
        fileName.setText(strings[strings.length-1]);
    }

    @FXML
    void upLoadEnter(MouseEvent event) {
        upLoad.setCursor(Cursor.HAND);
    }


}
