package cryptography.javacrypt.controllers;

import cryptography.javacrypt.services.AlgorithmsProviderService;
import cryptography.javacrypt.services.EncryptionService;
import cryptography.javacrypt.services.IAlgorithmsProviderService;
import cryptography.javacrypt.services.IEncryptionService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the file encryption tab.
 */
public class EncryptionController implements Initializable {

    @FXML
    TextField inputFilePathField, outputFilePathField;

    @FXML
    ChoiceBox<String> algorithmChoiceBox, encryptionModeChoiceBox, encryptionPaddingChoiceBox;

    @FXML
    ChoiceBox<Integer> keyLengthChoiceBox;

    @FXML
    PasswordField passwordField;

    @FXML
    ProgressBar progressBar;

    IAlgorithmsProviderService algorithmsProviderService;

    IEncryptionService encryptionService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        encryptionService = new EncryptionService();
        algorithmsProviderService = new AlgorithmsProviderService();

        algorithmChoiceBox.getItems().addAll(
                algorithmsProviderService.getAvailableEncryptionAlgorithms()
        );

        encryptionModeChoiceBox.getItems().addAll(
                algorithmsProviderService.getAvailableEncryptionModes()
        );

        encryptionPaddingChoiceBox.getItems().addAll(
                algorithmsProviderService.getAvailablePaddingModes()
        );

        progressBar.setStyle("-fx-accent: #00cc00");

        algorithmChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
            (observableValue, oldNumber, newNumber) -> {
                keyLengthChoiceBox.getItems().clear();
                var algorithm = algorithmChoiceBox.getItems().get(newNumber.intValue());

                if (algorithm != null)
                    keyLengthChoiceBox.getItems().addAll(
                        algorithmsProviderService.getAvailableKeyLength(algorithm)
                    );
        });
    }

    /**
     * Action performed when clicking the button to search for an input file.
     */
    @FXML
    public void onSearchInputFileClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedFile = fileChooser.showOpenDialog(progressBar.getScene().getWindow());
        if (selectedFile != null)
            inputFilePathField.setText(selectedFile.getAbsolutePath());
    }

    /**
     * Action performed when clicking the button to search for an output file.
     */
    @FXML
    public void onSearchOutputFileClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose file");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedFolder = directoryChooser.showDialog(progressBar.getScene().getWindow());
        if (selectedFolder != null)
            outputFilePathField.setText(selectedFolder.getAbsolutePath() + "/output");
    }

    /**
     * Action performed when clicking the button to encrypt the input file.
     */
    @FXML
    protected void onEncryptClick() {
        var inputPath = inputFilePathField.getText();
        if (inputPath.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error encrypting the file", "The input file path is not specified");
            progressBar.setProgress(0);
            return;
        }

        var outputPath = outputFilePathField.getText();
        if (outputPath.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error encrypting the file", "The output file path is not specified");
            progressBar.setProgress(0);
            return;
        }

        var algorithm = algorithmChoiceBox.getValue();
        if (algorithm == null) {
            showAlert(Alert.AlertType.ERROR, "Error encrypting the file", "The algorithm is not specified");
            progressBar.setProgress(0);
            return;
        }

        var encryptionMode = encryptionModeChoiceBox.getValue();
        if (encryptionMode == null) {
            showAlert(Alert.AlertType.ERROR, "Error encrypting the file", "The encryption mode is not specified");
            progressBar.setProgress(0);
            return;
        }

        var encryptionPadding = encryptionPaddingChoiceBox.getValue();
        if (encryptionPadding == null) {
            showAlert(Alert.AlertType.ERROR, "Error encrypting the file", "The encryption padding is not specified");
            progressBar.setProgress(0);
            return;
        }

        var password = passwordField.getText();
        if (password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error encrypting the file", "The password is not specified");
            progressBar.setProgress(0);
            return;
        }

        var keyLength = keyLengthChoiceBox.getValue();
        if (keyLength == null) {
            showAlert(Alert.AlertType.ERROR, "Error encrypting the file", "The key length is not specified");
            progressBar.setProgress(0);
            return;
        }

        var task = encryptionService.getFileEncryptionTask(
                inputPath,
                outputPath,
                algorithm,
                keyLength,
                encryptionMode,
                encryptionPadding,
                password.toCharArray()
                );

        progressBar.progressProperty().bind(task.progressProperty());

        task.setOnSucceeded(workerStateEvent -> {

            showAlert(Alert.AlertType.INFORMATION, "Encryption complete", "The file was encrypted successfully !");

            progressBar.progressProperty().unbind();
            progressBar.setProgress(0);
        });

        task.setOnFailed(workerStateEvent -> {
            showAlert(Alert.AlertType.ERROR, "Error encrypting the file", task.getException().getMessage());

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
