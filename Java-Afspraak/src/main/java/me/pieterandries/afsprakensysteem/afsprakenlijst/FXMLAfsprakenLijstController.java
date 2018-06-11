package me.pieterandries.afsprakensysteem.afsprakenlijst;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import me.pieterandries.afsprakensysteem.afspraaktoevoegen.FXMLAfspraakToevoegenController;
import me.pieterandries.afsprakensysteem.database.DatabaseDerby;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLAfsprakenLijstController implements Initializable {


    ObservableList<Afspraak> list = FXCollections.observableArrayList(); //<--- We maken hier een nieuwe arraylist met datatype Afspraak we noemen deze list.
    @FXML
    public JFXButton verwijderButton;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Afspraak> tableView;
    @FXML
    private TableColumn<Object, Object> behandelingColumn;
    @FXML
    private TableColumn<Object, Object> afspraakDatumColumn;
    @FXML
    private TableColumn<Object, Object> afspraakIdColumn;
    @FXML
    private TableColumn<Object, Object> klantColumn; //De user die bij de afspraak hoort wordt in deze kolom weergegeven


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCol();
        LoadData();

    }

    private void initCol() {
        behandelingColumn.setCellValueFactory(new PropertyValueFactory<>("behandeling"));
        afspraakDatumColumn.setCellValueFactory(new PropertyValueFactory<>("afspraakDatum"));
        afspraakIdColumn.setCellValueFactory(new PropertyValueFactory<>("afspraakId"));
        klantColumn.setCellValueFactory(new PropertyValueFactory<>("klantid"));
    }

    private void LoadData() {
        DatabaseDerby derby = DatabaseDerby.getInstance();
        String qu = "SELECT * FROM AFSPRAAK";
        ResultSet rs = derby.execQuery(qu);
        try {
            while (rs.next()) {
                String behandeling = rs.getString("behandeling");
                String datum = rs.getString("afspraakDatum");
                String afspraakId = rs.getString("id");
                String klantenid = rs.getString("klantid");
                //Hier wordt de switch van id naar de naam van de klant gemaakt
                qu = "SELECT * FROM KLANT WHERE id = '" + klantenid + "'";
                ResultSet klantSet = derby.execQuery(qu); // Dit hoort er maar enkel 1 te zijn
                if (klantSet.next()) { // Als dit false is, was de klant niet gevonden. In de kolom blijft de ID staan.
                    klantenid = klantSet.getString("naam");
                }



                list.add(new Afspraak(behandeling, datum, afspraakId, klantenid)); //<-- Observable ArrayList de bovenstaande queries worden aan de list toegevoegd

            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLAfspraakToevoegenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.getItems().setAll(list);
    }

        public void verwijderSelectie(ActionEvent actionEvent) throws SQLException {
        DatabaseDerby derby = DatabaseDerby.getInstance();
        Afspraak afspraak = tableView.getSelectionModel().getSelectedItem();
        String qu= "DELETE FROM AFSPRAAK where id = '" + afspraak.getAfspraakId() +"'";
        tableView.getItems().removeAll(tableView.getSelectionModel().getSelectedItem());
        DatabaseDerby.getStatement().executeUpdate(qu);

        }

    public static class Afspraak {
        private final SimpleStringProperty behandeling;
        private final SimpleStringProperty afspraakDatum;
        private final SimpleStringProperty afspraakid;
        private final SimpleStringProperty klantid;

        Afspraak(String behandeling, String afspraakDatum, String afspraakid, String klantid) {
            this.behandeling = new SimpleStringProperty(behandeling);
            this.afspraakDatum = new SimpleStringProperty(afspraakDatum);
            this.afspraakid = new SimpleStringProperty(afspraakid);
            this.klantid = new SimpleStringProperty(klantid);
        }
        //Getters
        public String getBehandeling() {
            return behandeling.get();
        }

        public String getAfspraakDatum() {
            return afspraakDatum.get();
        }
        public String getAfspraakId(){ return afspraakid.get(); }
        public String getKlantid(){ return klantid.get();}

    }


}






