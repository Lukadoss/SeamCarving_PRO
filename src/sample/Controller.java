package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Lukado on 23. 11. 2016.
 */
public class Controller {
    public BorderPane mainStage;
    public Button picBut;
    public ImageView pic1, pic2, pic3, pic4;
    public TextField seams;
    private String filePath;
    private Desktop desktop = Desktop.getDesktop();
    private Processor prcs;

    public void solve(ActionEvent actionEvent) {

        if (filePath != null && isParsable(seams.getText())) {
            prcs = new Processor(filePath, Integer.parseInt(seams.getText()));
            pic2.setImage(new Image("file:" + prcs.getEnergyImg()));
            pic3.setImage(new Image("file:" + prcs.getSeamImg()));
            pic4.setImage(new Image("file:" + prcs.getOutputImg()));
        }
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

    public void openSource(MouseEvent mouseEvent) {
        if (filePath != null) {
            try {
                desktop.open(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openSeam(MouseEvent mouseEvent) {
        if (prcs != null) {
            try {
                desktop.open(new File(prcs.getSeamImg()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void openEnergy(MouseEvent mouseEvent) {
        if (prcs != null) {
            try {
                desktop.open(new File(prcs.getEnergyImg()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openResult(MouseEvent mouseEvent) {
        if (prcs != null) {
            try {
                desktop.open(new File(prcs.getOutputImg()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isParsable(String input) {
        boolean parsable = true;
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            parsable = false;
        }
        return parsable;
    }
}
