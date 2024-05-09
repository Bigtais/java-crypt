package cryptography.javacrypt.controllers;

import cryptography.javacrypt.services.HashService;
import cryptography.javacrypt.services.IHashingService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the hash calculation tab.
 */
public class HashController implements Initializable {

    @FXML
    TextField inputFilePathField;

    @FXML
    ChoiceBox<String> algorithmChoiceBox;

    @FXML
    TextArea outputHashTextArea;

    @FXML
    ProgressBar progressBar;

    private IHashingService hashingService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hashingService = new HashService();

        algorithmChoiceBox.getItems().addAll(
                hashingService.getAvailableAlgorithms()
        );

        progressBar.setStyle("-fx-accent: #00cc00");
    }


    /**
     * Action performed when clicking the button to search for an input file.
     */
    @FXML
    public void onSearchInputFileClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose input file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedFile = fileChooser.showOpenDialog(progressBar.getScene().getWindow());

        if (selectedFile != null)
            inputFilePathField.setText(selectedFile.getAbsolutePath());

    }

    /**
     * Action performed when clicking the button to calculate the hash of the input file.
     */
    @FXML
    protected void onCalculateHashClick() {
        var path = inputFilePathField.getText();
        if (path.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error calculating the hash", "The file path is not specified");
            progressBar.setProgress(0);
            return;
        }

        var algorithm = algorithmChoiceBox.getValue();
        if (algorithm == null) {
            showAlert(Alert.AlertType.ERROR, "Error calculating the hash", "The algorithm is not specified");
            progressBar.setProgress(0);
            return;
        }

        var task = hashingService.getHashCalculationTask(path, algorithm);
        progressBar.progressProperty().bind(task.progressProperty());

        task.setOnSucceeded(workerStateEvent -> {
            var result = task.getValue();
            outputHashTextArea.setText(result);

            showAlert(Alert.AlertType.INFORMATION, "Hashing complete", "Hash calculated successfully !");

            progressBar.progressProperty().unbind();
            progressBar.setProgress(0);
        });

        task.setOnFailed(workerStateEvent -> {
            showAlert(Alert.AlertType.ERROR, "Error calculating the hash", task.getException().getMessage());

            progressBar.progressProperty().unbind();
            progressBar.setProgress(0);
        });


        new Thread(task).start();
    }

    /**
     * Shows an alert dialog containing information about the alert.
     * @param alertType The type of dialog to show.
     * @param title The title of the alert dialog.
     * @param content The content of the alert dialog.
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        var alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
