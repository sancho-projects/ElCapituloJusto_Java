package mvc.controlador;

import java.util.List;

public interface Controlador {
    void goPressed();

    void initializeGame(List<String> names);

    void tryAgain();
}
