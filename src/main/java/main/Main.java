package main;

import mvc.controlador.ImplementacionControlador;
import javafx.application.Application;
import javafx.stage.Stage;
import mvc.modelo.ImplementacionModelo;
import mvc.vista.ImplementacionVista;

/**
 * JavaFX App
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        ImplementacionControlador controlador = new ImplementacionControlador();
        ImplementacionModelo modelo = new ImplementacionModelo();
        ImplementacionVista vista = new ImplementacionVista(stage);

        modelo.setVista(vista);

        controlador.setVista(vista);
        controlador.setModelo(modelo);

        vista.setModelo(modelo);
        vista.setControlador(controlador);
        vista.creaMenuGUI();
    }

    public static void main(String[] args) {
        launch();
    }

}