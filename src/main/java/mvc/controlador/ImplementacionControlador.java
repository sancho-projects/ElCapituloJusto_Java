package mvc.controlador;

import mvc.modelo.CambioModelo;
import mvc.modelo.ImplementacionModelo;
import mvc.vista.ImplementacionVista;
import mvc.vista.InterrogaVista;

public class ImplementacionControlador implements Controlador{
    private InterrogaVista vista;
    private CambioModelo modelo;

    public void setVista(ImplementacionVista vista) {
        this.vista = vista;
    }

    public void setModelo(ImplementacionModelo modelo) {
        this.modelo = modelo;
    }

    @Override
    public void goPressed() {
        String file = vista.getPanel();
        if (vista.getStatus().equals("Siguiente")){
            modelo.nextPanel();
        } else {
            modelo.showAnswer(vista.getChapter());
        }

    }
}
