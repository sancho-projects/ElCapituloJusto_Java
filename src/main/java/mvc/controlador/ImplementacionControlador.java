package mvc.controlador;

import mvc.modelo.CambioModelo;
import mvc.modelo.ImplementacionModelo;
import mvc.vista.ImplementacionVista;
import mvc.vista.InterrogaVista;

import java.util.List;

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
        if (vista.getStatus().equals("Siguiente")){
            modelo.nextPanel();
        } else {
            modelo.showAnswers(vista.getChapters());
        }

    }

    @Override
    public void sendNames(List<String> names) {
        for (int i=0; i<names.size(); i++) {
            String name = names.get(i);
            if (name.equals("")) {
                names.set(i,"Jugador " + (i+1) );
            }
        }
        modelo.setPlayers(names);
    }

}
