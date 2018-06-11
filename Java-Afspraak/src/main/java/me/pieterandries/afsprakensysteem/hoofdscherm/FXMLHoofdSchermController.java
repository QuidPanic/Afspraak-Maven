package me.pieterandries.afsprakensysteem.hoofdscherm;

import com.jfoenix.effects.JFXDepthManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.pieterandries.afsprakensysteem.database.DatabaseDerby;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLHoofdSchermController implements Initializable {
    @FXML
    public Text afspraakKlantTxt;
    @FXML
    private TextField klantIDInput;
    @FXML
    private TextField afspraakIDInput;
    @FXML
    private HBox klant_info;
    @FXML
    private Text contactTxt;
    @FXML
    private Text klantTxt;
    @FXML
    private Text datumTxt;
    @FXML
    private Text behandelingTxt;
    @FXML
    private HBox afspraak_info;

    DatabaseDerby databaseDerby;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JFXDepthManager.setDepth(afspraak_info, 1);
        JFXDepthManager.setDepth(klant_info, 1);
        databaseDerby = DatabaseDerby.getInstance();
    }

    @FXML
    private void loadAfspraakToevoegen(ActionEvent event) {
        loadWindow("/me/pieterandries/afsprakensysteem/afspraaktoevoegen/FXMLAfspraakToevoegen.fxml", "Afspraak Toevoegen");
    }

    @FXML
    private void loadAfsprakenLijst(ActionEvent event) {
        loadWindow("/me/pieterandries/afsprakensysteem/afsprakenlijst/FXMLAfsprakenLijst.fxml", "Afsprakenlijst");
    }


    @FXML
    private void loadKlantToevoegen(ActionEvent event) {
        loadWindow("/me/pieterandries/afsprakensysteem/klanttoevoegen/FXMLKlantToevoegen.fxml", "Klant Toevoegen");
    }

    @FXML
    private void loadKlantenLijst(ActionEvent event) {
        loadWindow("/me/pieterandries/afsprakensysteem/klantlijst/FXMLKlantLijst.fxml", "Klantenlijst");
    }
    private void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(FXMLHoofdSchermController.class.getName()).log(Level.SEVERE, null, e);
        }
    }



    public void loadKlantInfo(ActionEvent actionEvent) throws SQLException {

        String id = klantIDInput.getText();
        String qu = "SELECT * FROM KLANT WHERE id = '" + id + "'";
        ResultSet rs = databaseDerby.execQuery(qu);
        Boolean flag = false;

        while (rs.next()){
            String klantNaam = rs.getString("naam");
            String klantContact = rs.getString("tel");
            klantTxt.setText(klantNaam);
            contactTxt.setText(klantContact);

            flag = true;
        }
        if (!flag){
            klantTxt.setText("Er is geen klant met het ingevoerde klant ID");
            contactTxt.setText("");
        }
    }

    public void loadAfspraakInfo(ActionEvent actionEvent) throws SQLException {
        String id = afspraakIDInput.getText();
        String qu = "SELECT * FROM AFSPRAAK WHERE id = '" + id + "'";
        ResultSet rs = databaseDerby.execQuery(qu);
        boolean flag = false;

        while (rs.next()){
            String behandelingNaam = rs.getString("behandeling");
            String behandelingDatum = rs.getString("afspraakDatum");
            String behandelingKlant = rs.getString("klantid");
            behandelingTxt.setText(behandelingNaam);
            datumTxt.setText(behandelingDatum);

            qu = "SELECT * FROM KLANT WHERE id = '" + behandelingKlant + "'";
            ResultSet klantSet = databaseDerby.execQuery(qu); // Dit hoort er maar enkel 1 te zijn
            if (klantSet.next()) { // Als dit false is, was de klant niet gevonden. In de kolom blijft de ID staan.
                behandelingKlant = klantSet.getString("naam");
            }
            afspraakKlantTxt.setText(behandelingKlant);
            flag = true;
        }
        if (!flag){
            behandelingTxt.setText("Er is geen afspraak met het ingevoerde afspraak ID");
            datumTxt.setText("");
            afspraakKlantTxt.setText("");
        }
    }
}
