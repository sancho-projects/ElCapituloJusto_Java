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
        player = new Player(giveName());
        loadPanels();
    }

    private String giveName() {
        //provisional
        System.out.print("Dime tu nombre: ");
        Scanner sc = new Scanner(System.in);
        String nombre = sc.next();
        sc.close();
        return nombre;
    }

    private Iterator<Panel> it;
    private Set<Panel> panels = new HashSet<>();
    private Player player;
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
            vista.setFeedback("FIN DEL JUEGO.");
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
                vista.setFeedback("");
                vista.setStatus("Comprobar");
            }
        }
    }

    @Override
    public void showAnswer(int chapter) {
        int guessedChapter = chapter;
        int actualChapter = currentPanel.getRightChapter();
        if (guessedChapter == actualChapter){
            player.addScore(-10);
            vista.setFeedback("¡Exacto! Era ese capítulo.");
        } else {
            int difference = Math.abs(guessedChapter-actualChapter);
            player.addScore(difference);
            String feedback = "";
            if (difference<10){
                feedback = "¡Casi, casi! ";
            } else if (difference>100){
                feedback = "Creo que te has equivocado de arco. ";
            }
            vista.setFeedback(feedback + "La respuesta correcta era "+ actualChapter +
                    ". Te has ido por "+ difference +" capítulos.");
        }
        vista.setScore(player.getScore());
        vista.setStatus("Siguiente");
        turn++;
    }


    @Override
    public String getScoreBoard() {
        HighScore.readFile();
        if (player.getScore() < HighScore.getMaxHighScore()){
            HighScore.update(player);
        }

        StringBuilder str = new StringBuilder();
        str.append("PUNTUACIÓN\t\tNOMBRE\n");
        for (Player player: HighScore.getPlayers()) {
            str.append(player.getScore() + "\t\t\t\t" + player.getName() + "\n");
        }
        return str.toString();
    }

    @Override
    public int getScore() {
        return player.getScore();
    }

}
