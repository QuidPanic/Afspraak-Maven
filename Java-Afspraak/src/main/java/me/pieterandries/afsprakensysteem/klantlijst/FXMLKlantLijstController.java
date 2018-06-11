package me.pieterandries.afsprakensysteem.klantlijst;


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
import me.pieterandries.afsprakensysteem.afspraaktoevoegen.FXMLAfspraakToevoegenController;
import me.pieterandries.afsprakensysteem.database.DatabaseDerby;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLKlantLijstController implements Initializable {
    @FXML
    public JFXButton verwijderButton;
    @FXML
    private TableView<Klant> tableView;

    @FXML
    private TableColumn<Klant, String> naamColumn;

    @FXML
    private TableColumn<Klant, String> idColumn;

    @FXML
    private TableColumn<Klant, String> telColumn;

    @FXML
    private TableColumn<Klant, String> mailColumn;

    @FXML
    private TableColumn<Klant, String> woonplaatsColumn;

    ObservableList<FXMLKlantLijstController.Klant> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCol();
        loadData();
    }

    private void initCol() {
        naamColumn.setCellValueFactory(new PropertyValueFactory<>("naam"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        telColumn.setCellValueFactory(new PropertyValueFactory<>("telefoonnummer"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        woonplaatsColumn.setCellValueFactory(new PropertyValueFactory<>("woonplaats"));

    }

    private void loadData() {
        DatabaseDerby derby = DatabaseDerby.getInstance();
        String qu = "SELECT * FROM KLANT";
        ResultSet rs = derby.execQuery(qu);
        try {
            while (rs.next()) {
                String naam = rs.getString("naam");
                String id = rs.getString("id");
                String tel = rs.getString("tel");
                String email = rs.getString("email");
                String woonplaats = rs.getString("woonplaats");

                list.add(new Klant(naam, id, tel, email, woonplaats));//<-- Observable ArrayList de bovenstaande queries worden aan de list toegevoegd

            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLAfspraakToevoegenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.getItems().setAll(list);
    }

    public void verwijderSelectie(ActionEvent actionEvent) throws SQLException {
        DatabaseDerby derby = DatabaseDerby.getInstance();
        Klant klant = tableView.getSelectionModel().getSelectedItem();
        String qu= "DELETE FROM KLANT where id = '" + klant.getId() +"'";
        tableView.getItems().removeAll(tableView.getSelectionModel().getSelectedItem());
        DatabaseDerby.getStatement().executeUpdate(qu);

    }


    public static class Klant {
        private final SimpleStringProperty naam;
        private final SimpleStringProperty id;
        private final SimpleStringProperty telefoonnummer;
        private final SimpleStringProperty email;
        private final SimpleStringProperty woonplaats;

        public Klant(String naam, String id, String telefoonnummer, String email, String woonplaats) {
            this.naam = new SimpleStringProperty(naam);
            this.id = new SimpleStringProperty(id);
            this.telefoonnummer = new SimpleStringProperty(telefoonnummer);
            this.email = new SimpleStringProperty(email);
            this.woonplaats = new SimpleStringProperty(woonplaats);
        }
        //Getters

        public String getNaam() {
            return naam.get();
        }


        public String getId() {
            return id.get();
        }


        public String getTelefoonnummer() {
            return telefoonnummer.get();
        }


        public String getEmail() {
            return email.get();
        }


        public String getWoonplaats() {
            return woonplaats.get();
        }

    }
}

