package auxiliares;

public class Panel {
    private String image;
    private String difficulty;
    private int rightChapter;

    public Panel(String image, int rightChapter, String difficulty){
        this.image = image;
        this.difficulty = difficulty;
        this.rightChapter = rightChapter;
    }

    public String getImage() {
        return image;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getRightChapter() {
        return rightChapter;
    }
}
