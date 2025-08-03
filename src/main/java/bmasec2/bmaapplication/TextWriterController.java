package bmasec2.bmaapplication;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TextWriterController
{
    @javafx.fxml.FXML
    private TextArea textAreaType;
    @javafx.fxml.FXML
    private Label labelStore;

    public static final String filename = "data.txt";

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void saveOnAction(ActionEvent actionEvent) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filename, true)
        )) {

            String content = textAreaType.getText();
            writer.write(content);
            labelStore.setText("Successfully saved to" + " " + filename);

        } catch (RuntimeException e) {
            labelStore.setText("Something is Wrong!");
        }

    }

    @javafx.fxml.FXML
    public void clearOnAction(ActionEvent actionEvent) {
        labelStore.setText("");
    }
}