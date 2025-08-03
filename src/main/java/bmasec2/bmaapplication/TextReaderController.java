package bmasec2.bmaapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;

public class TextReaderController
{
    @javafx.fxml.FXML
    private TextArea textAreaType;
    @javafx.fxml.FXML
    private Label labelStore;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void loadOnAction(ActionEvent actionEvent) {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(TextWriterController.filename)
        )) {
            while (true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                textAreaType.appendText(line + "\n");

            }

        } catch (RuntimeException | FileNotFoundException e) {
            labelStore.setText("Something Went Wrong!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @javafx.fxml.FXML
    public void backOnAction(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("text-writer.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("BMA Application");
        stage.setScene(scene);
        stage.show();


    }
}