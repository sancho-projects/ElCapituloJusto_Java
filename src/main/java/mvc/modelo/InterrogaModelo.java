package mvc.modelo;

public interface InterrogaModelo {
    String getHighScoreBoard();
    String getFinalScore();

    String getPlayerName(int index);
    int getNumPlayers();
    int getTURNS();

    String getRegisterAtTurn(int playerIndex, int turn);

    String getMinRecord(int playerIndex);

    String getMaxRecord(int playerIndex);
}
