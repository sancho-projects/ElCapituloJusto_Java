package mvc.modelo;

import auxiliares.HighScore;
import auxiliares.Panel;
import auxiliares.Player;
import auxiliares.lectura.Table;
import auxiliares.lectura.csv.CSVUnlabeledFileReader;
import mvc.vista.ImplementacionVista;
import mvc.vista.InformaVista;

import java.util.*;

public class ImplementacionModelo implements CambioModelo, InterrogaModelo {
    private int MAX_TURNOS = 10;
    private InformaVista vista;
    public ImplementacionModelo(){
        turn = 0;
        loadPanels();
    }

    private Iterator<Panel> it;
    private Set<Panel> panels = new HashSet<>();
    private List<Player> players = new ArrayList<>();
    private Panel currentPanel = null;
    private int turn;
    private Set<Integer> conditions = new HashSet<>(4);
    private int limitChapter;

    private void loadPanels() {
        CSVUnlabeledFileReader csv = new CSVUnlabeledFileReader();
        Table table = csv.readTableFromSource("src/main/resources/solution.csv");

        List<Integer> randomNumbers = new ArrayList<>();
        for (int i=0; i < table.getNumRows(); i++){
            randomNumbers.add(i);
        }
        Collections.shuffle(randomNumbers);

        for (int i: randomNumbers){
            List<Integer> row = table.getRowAt(i).getData();

            String fileName = row.get(0) + ".png";
            int chapter = row.get(1);
            int difficulty = row.get(2);

            panels.add(new Panel(fileName, chapter, difficulty));
        }
    }

    @Override
    public void nextPanel() {
        if (turn==MAX_TURNOS) {
            vista.finalScore();
        }
        else {
            if (it == null) it = panels.iterator();

            currentPanel = null;
            while (it.hasNext() && currentPanel == null) {
                Panel newPanel = it.next();
                if (conditions.contains(newPanel.getDifficulty()) && newPanel.getRightChapter() <= limitChapter) {
                    currentPanel = newPanel;
                }

            }
            if (currentPanel == null) {
                vista.outOfPanels();
            } else {
                vista.setNewPanel(currentPanel.getImage());
                vista.setFeedbackGeneral("Viñeta "+ (turn+1) + "/" + MAX_TURNOS);
                for (int i = 0; i < getNumPlayers(); i++) {
                    vista.setFeedback(i,"");
                }
                vista.setStatus("Comprobar");
            }
        }
    }

    @Override
    public void restart() {
        turn = 0;
        for (int i=0; i<players.size(); i++) {
            players.get(i).restartGame();
            vista.setScore(i, 0);
        }
        nextPanel();
    }

    @Override
    public void showAnswers(List<Integer> chapters) {
        int actualChapter = currentPanel.getRightChapter();
        for (int i=0; i<players.size(); i++) {
            Player player = players.get(i);
            int guessedChapter = chapters.get(i);
            String feedback = "";
            if (guessedChapter == actualChapter) {
                player.addScore(-10);
                feedback = "¡Exacto! Ni más ni menos.";

            } else {
                int difference = Math.abs(guessedChapter - actualChapter);
                player.addScore(difference);
                if (difference < 3) {
                    feedback = "¡Ayyyyy! A nada.";
                } else if (difference < 10) {
                    feedback = "¡Casi, casi! Una pena.";
                } else if (difference > 600) {
                    feedback = "¿Pero tú te has visto One Piece?";
                } else if (difference > 100) {
                    feedback = "Creo que te has equivocado de arco.";
                }
                feedback += " Te has ido por " + difference + " capítulos.";

            }
            vista.setFeedback(i, feedback);
            vista.setScore(i, player.getScore());
        }
        vista.setFeedbackGeneral("La respuesta correcta era " + actualChapter + ".");
        vista.setStatus("Siguiente");
        turn++;
    }

    // SETTERS
    public void setVista(ImplementacionVista vista) {
        this.vista = vista;
    }

    @Override
    public void setPlayers(List<String> names) {
        for (String name: names){
            players.add(new Player(name));
        }
    }

    @Override
    public void setOptions(boolean admitEasy, boolean admitMedium, boolean admitHard, int lastChapter) {
        limitChapter = lastChapter;
        if (admitEasy) conditions.add(1);
        if (admitMedium) conditions.add(2);
        if (admitHard) conditions.add(3);
    }

    // GETTERS: Interroga modelo
    @Override
    public int getNumPlayers() {
        return players.size();
    }

    @Override
    public String getPlayerName(int index) {
        return players.get(index).getName();
    }

    @Override
    public String getFinalScore() {
        StringBuilder str = new StringBuilder();

        PriorityQueue<Player> finalOrderPlayers = new PriorityQueue<>(players.size());
        for (Player player : players)
            finalOrderPlayers.add(player);
        str.append(">>> Y EL GANADOR ES " + finalOrderPlayers.element().getName() + "\n\n");

        str.append("CLASIFICACIÓN:\n");
        for (int i=0; i<players.size(); i++) {
            Player player = finalOrderPlayers.remove();
            str.append( (i+1) + "º " + player.getName() + ": " + player.getScore() + " puntos.\n");
        }
        return str.toString();
    }

    @Override
    public String getHighScoreBoard() {
        HighScore highScore = new HighScore();
        for (Player player: players) {
            if (player.getScore() < highScore.getMaxHighScore()) {
                highScore.update(player);
            }
        }

        StringBuilder str = new StringBuilder();
        str.append("PUNTUACIÓN\t\tNOMBRE\n");
        str.append("\n");
        for (Player player: highScore.getPlayers()) {
            str.append(player.getScore() + "\t\t\t\t" + player.getName() + "\n");
        }
        return str.toString();
    }


    @Override
    public String getRegisterAtTurn(int playerIndex, int turn) {
        StringBuilder str = new StringBuilder();
        str.append("- En el turno " + (turn+1) + ", ");
        int difference = players.get(playerIndex).getScoreAtTurn(turn);
        if (difference <= 0) {
            str.append("acertaste el capítulo.");
        } else if (difference < 10) {
            str.append("te quedaste a tan solo " + difference + " capítulo(s).");
        } else{
            str.append("te fuiste de " + difference + " capítulos.");
        }
        return str.toString();
    }

    @Override
    public String getMinRecord(int playerIndex) {
        StringBuilder str = new StringBuilder();
        int min = players.get(playerIndex).getMinScore();
        List<Integer> minTurns = players.get(playerIndex).getTurnMin();
        if (min<=0) {
            str.append("Has acertado el capítulo ");
            if (minTurns.size() == 1) {
                str.append("una vez.");
            } else {
                str.append(minTurns.size() + " veces.");
            }
        } else {
            if (minTurns.size() == 1){
                str.append("La vez que más te acercaste fue en el turno " + minTurns.get(0) +
                        " que fue a " + min + " capítulo(s).");
            } else {
                str.append("Varias veces te has quedado a "+ min + " capítulos.");
            }
        }
        return str.toString();
    }

    @Override
    public String getMaxRecord(int playerIndex) {
        StringBuilder str = new StringBuilder();
        int max = players.get(playerIndex).getMaxScore();
        List<Integer> maxTurns = players.get(playerIndex).getTurnMax();
        if (maxTurns.size() == 1) {
            str.append("La vez que más te has alejado ha sido en el turno " + (maxTurns.get(0)+1) +
                    ", y te fuiste por " + max + " capítulos.");
        } else {
            str.append("Lo que más te has alejado han sido " + max + "capítulos. Aunque varias veces.");
        }
        return str.toString();
    }

    @Override
    public int getTURNS() {
        return MAX_TURNOS;
    }


}
