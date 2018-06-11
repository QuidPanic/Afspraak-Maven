package me.pieterandries.afsprakensysteem.afspraaktoevoegen;

        import me.pieterandries.afsprakensysteem.AbstractTitleMain;

        /*
        Deze class zorgt er voor dat het onderdeel Afpsraak Toevoegen gerund kan worden deze extend de
        me.pieterandries.afsprakensysteem.AbstractTitleMain die zelf de me.pieterandries.afsprakensysteem.AbstractMain weer extend.
         */

public class AfspraakToevoegenMain extends AbstractTitleMain {
    public AfspraakToevoegenMain(){
        super("FXMLAfspraakToevoegen.fxml", "Afspraak Toevoegen");
    }


}
