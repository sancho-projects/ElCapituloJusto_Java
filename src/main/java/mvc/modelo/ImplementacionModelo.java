package mvc.modelo;

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
        loadPanels();
    }
    private Set<Panel> panels = new HashSet<>();
    private Iterator<Panel> it;
    private Panel currentPanel = null;
    private Player player = new Player("Jugador");

    public void setVista(ImplementacionVista vista) {
        this.vista = vista;
    }


    private void loadPanels() {
        CSVUnlabeledFileReader csv = new CSVUnlabeledFileReader();
        Table tabla = csv.readTableFromSource("src/main/resources/solution.csv");

        List<Integer> randomNumbers = new ArrayList<>();
        for (int i=0; i < tabla.getNumRows(); i++){
            randomNumbers.add(i);
        }
        Collections.shuffle(randomNumbers);

        for (int i: randomNumbers){
            List<Integer> row = tabla.getRowAt(i).getData();

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
        if (it==null) it = panels.iterator();

        currentPanel = null;
        while (it.hasNext() && currentPanel == null){
            Panel newPanel = it.next();
            if (true){ //cumple los requisitos
                currentPanel = newPanel;
            }

        }
        if (currentPanel == null) {
            System.out.println("No hay más viñetas.");
        }
        else{
            vista.newPanel(currentPanel.getImage());
            vista.setFeedback("");
            vista.setStatus("Comprobar");
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
    }

    private int getAnswer(){
        return currentPanel.getRightChapter();
    }
}
