package mvc.modelo;

import mvc.vista.ImplementacionVista;
import mvc.vista.InformaVista;

public class ImplementacionModelo implements CambioModelo, InterrogaModelo {
    private InformaVista vista;

    public void setVista(ImplementacionVista vista) {
        this.vista = vista;
    }

}
