package mvc.vista;

import mvc.controlador.Controlador;
import mvc.controlador.ImplementacionControlador;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mvc.modelo.ImplementacionModelo;
import mvc.modelo.InterrogaModelo;

public class ImplementacionVista implements InterrogaVista, InformaVista{
    private final Stage stage;
    private Controlador controlador;
    private InterrogaModelo modelo;

    public ImplementacionVista(Stage stage) {
        this.stage = stage;
    }

    public void setModelo(ImplementacionModelo modelo) {
        this.modelo = modelo;
    }

    public void setControlador(ImplementacionControlador controlador) {
        this.controlador = controlador;
    }

    public void creaGUI() {
        Label saludo = new Label("Hola mundo!");
        Scene scene = new Scene(saludo, 300, 50);
        stage.setTitle("HELLOFX");
        stage.setScene(scene);
        stage.show();
    }
}
