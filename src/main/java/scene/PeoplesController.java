package scene;

import data.Data;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Document;
import model.Post;
import org.controlsfx.control.PrefixSelectionComboBox;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.ResourceBundle;

public class PeoplesController implements Initializable {
    @FXML
    public PrefixSelectionComboBox<Post> responsiblePost;
    @FXML
    public TextField responsibleFace;
    @FXML
    public PrefixSelectionComboBox<Post> checkingPost;
    @FXML
    public TextField checkingFace;
    public Button closeWithoutSaveButton;
    public Button savePeoplesButton;

    Document document;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Data data = new Data();
        document = Document.getInstance();
        data.getFooter(document);
        responsiblePost.getItems().addAll(document.getResponsiblePosts());
        checkingPost.getItems().addAll(document.getCheckingPosts());
    }

    public void savePeoples(ActionEvent actionEvent) {
        Post post = checkingPost.getSelectionModel().getSelectedItem();
        if(post == null) {
            showMessage("Должность проверяющего не выбрана", "");
            return;
        }
        document.setCurrentCheckingPost(post);

        post = responsiblePost.getSelectionModel().getSelectedItem();
        if(post == null) {
            showMessage("Должность ответственного не выбрана", "");
            return;
        }
        document.setCurrentResponsiblePost(post);

        String name = checkingFace.getText();
        if(name == null || name.isEmpty()) {
            showMessage("ФИО проверяющего не введено", "");
            return;
        }
        document.setCurrentCheckingFace(name);

        name = responsibleFace.getText();
        if(name == null || name.isEmpty()) {
            showMessage("ФИО ответственного не введено", "");
            return;
        }
        document.setCurrentResponsibleFace(name);


        Stage stage = (Stage)savePeoplesButton.getScene().getWindow();
        stage.close();
    }

    private void showMessage(String message, String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Что-то пошло не так");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void closeWithoutSave(ActionEvent actionEvent) {
        Stage stage = (Stage)closeWithoutSaveButton.getScene().getWindow();
        stage.close();
    }
}
