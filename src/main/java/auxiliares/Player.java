package auxiliares;

public class Player {
    private int score;
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public String getName() {
        return name;
    }
}
