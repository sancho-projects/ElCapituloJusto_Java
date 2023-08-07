package mvc.vista;

public interface InformaVista {

    void setNewPanel(String image);
    void setFeedbackGeneral(String string);

    void setScore(int index, int score);
    void setStatus(String status);

    void endGame();

    void setFeedback(int i, String s);
}
