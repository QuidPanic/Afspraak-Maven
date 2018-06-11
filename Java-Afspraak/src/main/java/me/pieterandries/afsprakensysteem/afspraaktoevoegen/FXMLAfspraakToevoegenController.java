package me.pieterandries.afsprakensysteem.afspraaktoevoegen;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FXMLAfspraakToevoegenController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField behandeling;
    @FXML
    private TextField afspraakDatum;
    @FXML
    private TextField afspraakId;
    @FXML
    private TextField klantID;
    @FXML
    private JFXButton opslaanButton;
    @FXML
    private JFXButton annulerenButton;

    DatabaseDerby databaseDerby;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseDerby = DatabaseDerby.getInstance();

        controleerData();
    }

    //Method om afspraak naar de me.pieterandries.afsprakensysteem.database te schrijven
    @FXML
    private void afspraakToevoegen(ActionEvent actionEvent) throws SQLException {
        String afspraakBehandeling = behandeling.getText();
        String datum = afspraakDatum.getText();
        String afspraakID = afspraakId.getText();
        String klantid = klantID.getText();

        //Wanneer de velden leeg zijn (.isEmpty) komt er een error message om deze graag allemaal in te vullen
        if (afspraakBehandeling.isEmpty() || datum.isEmpty() || afspraakID.isEmpty() || klantid.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Graag alle velden invullen A.U.B.");
            alert.showAndWait();
            return;
        }
        // writes de volgende values in tabel Afspraak (Pushes data into the me.pieterandries.afsprakensysteem.database)
        PreparedStatement pushStatement = DatabaseDerby.getDbConnection().prepareStatement("INSERT INTO AFSPRAAK VALUES (?, ?, ?, ?)");
        pushStatement.setString(1, afspraakBehandeling);
        pushStatement.setString(2, datum);
        pushStatement.setString(3, afspraakID);
        pushStatement.setString(4, klantid);
        DatabaseDerby.doesUserExists(klantid);

        // Als de afspraak is toegevoegd krijgen we een message Success
        if (databaseDerby.execAction(pushStatement)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
            //Als het fout is gegaan krijgen we een message Mislukt
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Mislukt");
            alert.showAndWait();
        }

    }


    private void controleerData(){
        String qu = "SELECT id FROM AFSPRAAK";
        ResultSet rs = databaseDerby.execQuery(qu);
        try {
            while (rs.next()){
                String idz = rs.getString("id");
                System.out.println(idz);
        }

        } catch (SQLException ex) {
            Logger.getLogger(FXMLAfspraakToevoegenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void annuleren(ActionEvent actionEvent) {
        Stage stage = (Stage) annulerenButton.getScene().getWindow();
        stage.close();
    }
}
