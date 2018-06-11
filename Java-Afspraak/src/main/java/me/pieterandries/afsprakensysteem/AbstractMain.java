package me.pieterandries.afsprakensysteem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
Deze class extends de Application. Deze class zorgt er voor dat de schermen uitgevoerd kunnen worden. Elke main in de desbetreffende package worden door deze class uitgevoerd d.m.v de super te gebruiken en de FXML mee te geven.
 */

public class AbstractMain extends Application {

    private final String resourceFile;


    public AbstractMain(String file, String resourceFile){
        this.resourceFile = resourceFile;

    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(resourceFile));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

}
