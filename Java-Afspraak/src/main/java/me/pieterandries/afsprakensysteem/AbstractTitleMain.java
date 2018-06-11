package me.pieterandries.afsprakensysteem;

import javafx.stage.Stage;


public class AbstractTitleMain extends AbstractMain {
    private final String title;

    public AbstractTitleMain(String resourceFile, String title){
        super("FXMLHoofdScherm.fxml", resourceFile);
        this.title = title;
    }
    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        stage.setTitle(title);
        stage.sizeToScene();
    }


}
