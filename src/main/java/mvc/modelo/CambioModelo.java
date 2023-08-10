package mvc.modelo;

import java.util.List;

public interface CambioModelo {
    void nextPanel();

    void showAnswers(List<Integer> chapter);

    void setPlayers(List<String> names);

    void restart();

    void setOptions(boolean easyStatus, boolean mediumStatus, boolean hardStatus, int lastChapter);
}
