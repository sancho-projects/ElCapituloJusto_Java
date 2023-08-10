package mvc.vista;

import java.util.List;

public interface InterrogaVista {

    String getStatus();
    List<Integer> getChapters();

    boolean getEasyStatus();
    boolean getMediumStatus();
    boolean getHardStatus();
    int getLastChapter();

}
