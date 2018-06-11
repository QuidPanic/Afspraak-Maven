package me.pieterandries.afsprakensysteem.klanttoevoegen;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import me.pieterandries.afsprakensysteem.database.DatabaseDerby;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FXMLKlantToevoegenController implements Initializable {
    DatabaseDerby databaseDerby;

    @FXML
    private TextField klantNaam;

    @FXML
    private TextField klantID;

    @FXML
    private TextField klantTel;

    @FXML
    private TextField klantMail;

    @FXML
    private TextField klantWoonplaats;

    @FXML
    private JFXButton opslaanButton;

    @FXML
    private JFXButton annulerenButton;

    @FXML
    private AnchorPane rootPaneKlant;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseDerby = DatabaseDerby.getInstance();
    }


    @FXML
    public void klantToevoegen(ActionEvent actionEvent) throws SQLException {
        String mKlantnaam = klantNaam.getText();
        String mKlantID = klantID.getText();
        String mKlantTel = klantTel.getText();
        String mKlantMail = klantMail.getText();
        String mKlantWoonplaats = klantWoonplaats.getText();

        //als één van deze waarden true is dan zal er een alert komen want we moeten alle velden invullen
        Boolean flag = mKlantnaam.isEmpty() || mKlantID.isEmpty() || mKlantTel.isEmpty() || mKlantMail.isEmpty() || mKlantWoonplaats.isEmpty();
        if (flag){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Graag alle velden invullen A.U.B.");
            alert.showAndWait();
            return;
        }
        //Pushing klant data naar KLANT
        PreparedStatement pushKlantStatement = DatabaseDerby.getDbConnection().prepareStatement("INSERT INTO KLANT VALUES (?, ?, ?, ?, ?)");
        pushKlantStatement.setString(1, mKlantnaam);
        pushKlantStatement.setString(2, mKlantID);
        pushKlantStatement.setString(3, mKlantTel);
        pushKlantStatement.setString(4, mKlantMail);
        pushKlantStatement.setString(5, mKlantWoonplaats);


        //Als het goed is gegaan krijgen we message Success
        if (databaseDerby.execAction(pushKlantStatement)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Opgeslagen");
            alert.showAndWait();
            return;
            //Als het fout is gegaan krijgen we een message Mislukt
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Mislukt");
            alert.showAndWait();
        }



    }

    //deze method zorgt ervoor dat we de window kunnen sluiten met de knop annuleren.
    public void annuleren(ActionEvent actionEvent) {
        Stage stage = (Stage) rootPaneKlant.getScene().getWindow();
        stage.close(); // Dit closed de huidige window wanneer er op annuleren wordt geklikt
    }
}
