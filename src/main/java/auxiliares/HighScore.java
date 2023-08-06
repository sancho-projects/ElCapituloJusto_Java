package auxiliares;

import auxiliares.lectura.RowForPlayer;
import auxiliares.lectura.TableForPlayer;
import auxiliares.lectura.csv.CSVForPlayerFileReader;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class HighScore {
    private static List<Player> highscores = new LinkedList<>();

    public static void rewrite() {
        FileWriter file = null;
        PrintWriter pw;
        try {
            file = new FileWriter("src/main/resources/highScore.csv");
            pw = new PrintWriter(file);

            pw.println("score,player");
            for (Player player: highscores) {
                pw.println(player.getScore()+","+ player.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != file)
                    file.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void readFile() {
            CSVForPlayerFileReader csv = new CSVForPlayerFileReader();
            TableForPlayer table = (TableForPlayer) csv.readTableFromSource("src/main/resources/highScore.csv");

            for (int i = 0; i < table.getNumRows(); i++) {
                RowForPlayer row = table.getRowAt(i);
                int score = row.getData().get(0);
                String name = row.getPlayerName();
                highscores.add(new Player(name, score));
            }
    }

    public static List<Player> getPlayers() {
        return highscores;
    }

    public static int getSize() {
        return highscores.size();
    }
    
    public static void add(int i, Player player){
        highscores.add(i,player);
    }

    public static void cut() {
        for (int i = 5; i < HighScore.getSize(); i++) {
            highscores.remove(i);
        }
    }

    public static int getMaxHighScore() {
            if (highscores.size() < 5) return Integer.MAX_VALUE;
            return highscores.get( highscores.size() -1 ).getScore();
        }

    public static void update(Player player) {
            for (int i = 0; i < highscores.size(); i++){
                Player aspirant = highscores.get(i);
                if (player.getScore() < aspirant.getScore()){
                    highscores.add(i,player);
                    break;
                } else if (i == highscores.size() - 1) {
                    highscores.add(player);
                    break;
                }
            }
            cut();
            rewrite();
        }


}
