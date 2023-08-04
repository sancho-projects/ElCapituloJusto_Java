package main;

import mvc.controlador.ImplementacionControlador;
import javafx.application.Application;
import javafx.stage.Stage;
import mvc.modelo.ImplementacionModelo;
import mvc.vista.ImplementacionVista;

import java.io.IOException;

/**
 * JavaFX App
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        ImplementacionControlador controlador = new ImplementacionControlador();
        ImplementacionModelo modelo = new ImplementacionModelo();
        ImplementacionVista vista = new ImplementacionVista(stage);

        modelo.setVista(vista);

        controlador.setVista(vista);
        controlador.setModelo(modelo);

        vista.setModelo(modelo);
        vista.setControlador(controlador);
        vista.creaGUI();
    }

    public static void main(String[] args) {
        launch();
    }

}