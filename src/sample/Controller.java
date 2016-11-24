package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;

/**
 * Created by Lukado on 23. 11. 2016.
 */
public class Controller {
    public BorderPane mainStage;
    public Button picBut;
    public ImageView pic1, pic2, pic3, pic4;
    private String filePath;
    private Desktop desktop = Desktop.getDesktop();

    public void solve(ActionEvent actionEvent) {
        Processor prcs = new Processor(filePath);
        pic2.setImage(prcs.getEnergyImg());
        pic3.setImage(prcs.getSeamImg());
        pic4.setImage(prcs.getOutputImg());
    }

    public void openFile(ActionEvent actionEvent) {
        Stage stage = (Stage) mainStage.getScene().getWindow();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Picture loader");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home")+System.getProperty("file.separator")+"Pictures")
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File file = fileChooser.showOpenDialog(stage);
        if (file != null){
            picBut.setText(file.getName());
            pic1.setImage(new Image(file.toURI().toString()));
            filePath = file.getPath();
        }

    }
}
