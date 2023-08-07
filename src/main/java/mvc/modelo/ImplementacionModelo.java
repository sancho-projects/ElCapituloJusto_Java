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

    public void setVista(ImplementacionVista vista) {
        this.vista = vista;
    }

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
            String difficulty = "";
            switch (row.get(2)) {
                case 1:
                    difficulty = "easy";
                    break;
                case 2:
                    difficulty = "medium";
                    break;
                case 3:
                    difficulty = "hard";
                    break;
            }

            panels.add(new Panel(fileName, chapter, difficulty));
        }
    }

    @Override
    public void nextPanel() {
        if (turn==10) {
            vista.setFeedbackGeneral("FIN DEL JUEGO.");
            vista.endGame();
        }
        else {
            if (it == null) it = panels.iterator();

            currentPanel = null;
            while (it.hasNext() && currentPanel == null) {
                Panel newPanel = it.next();
                if (true) { //cumple los requisitos
                    currentPanel = newPanel;
                }

            }
            if (currentPanel == null) {
                System.out.println("No hay más viñetas.");
            } else {
                vista.setNewPanel(currentPanel.getImage());
                vista.setFeedbackGeneral("");
                for (int i = 0; i < getNumPlayers(); i++) {
                    vista.setFeedback(i,"");
                }
                vista.setStatus("Comprobar");
            }
        }
    }

    @Override
    public void showAnswers(List<Integer> chapters) {
        int actualChapter = currentPanel.getRightChapter();
        for (int i=0; i<players.size(); i++) {
            Player player = players.get(i);
            int guessedChapter = chapters.get(i);
            String string = "";
            if (guessedChapter == actualChapter) {
                player.addScore(-10);
                string = "¡Exacto! Ni más ni menos.";

            } else {
                int difference = Math.abs(guessedChapter - actualChapter);
                player.addScore(difference);
                if (difference < 10) {
                    string = "¡Casi, casi! Una pena";
                } else if (difference > 100) {
                    string = "Creo que te has equivocado de arco.";
                } string += " Te has ido por " + difference + " capítulos.";

            }
            vista.setFeedback(i, string);
            vista.setScore(i, player.getScore());
        }
        vista.setFeedbackGeneral("La respuesta correcta era " + actualChapter + ".");
        vista.setStatus("Siguiente");
        turn++;
    }

    @Override
    public void setPlayers(List<String> names) {
        for (String name: names){
            players.add(new Player(name));
        }
    }


    @Override
    public String getScoreBoard() {
        HighScore.readFile();

        for (Player player : players) {
            if (player.getScore() < HighScore.getMaxHighScore()) {
                HighScore.update(player);
            }
        }

        StringBuilder str = new StringBuilder();
        str.append("PUNTUACIÓN\t\tNOMBRE\n");
        for (Player player: HighScore.getPlayers()) {
            str.append(player.getScore() + "\t\t\t\t" + player.getName() + "\n");
        }
        return str.toString();
    }

    @Override
    public String getFinalScore() {
        StringBuilder str = new StringBuilder();
        str.append("RESULTADO FINAL:\n");
        for (Player player: players){
            str.append(" → " + player.getName() + ": " + player.getScore() + " puntos.\n");
        }
        return str.toString();
    }

    @Override
    public String getPlayerName(int index) {
        return players.get(index).getName();
    }

    @Override
    public int getNumPlayers() {
        return players.size();
    }
}
