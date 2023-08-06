package mvc.vista;

public interface InformaVista {

    void setNewPanel(String image);
    void setFeedback(String string);

    void setScore(int score);
    void setStatus(String status);

    void endGame();
}
