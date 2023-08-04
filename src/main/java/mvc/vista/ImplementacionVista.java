package mvc.vista;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import mvc.controlador.Controlador;
import mvc.controlador.ImplementacionControlador;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mvc.modelo.ImplementacionModelo;
import mvc.modelo.InterrogaModelo;

public class ImplementacionVista implements InterrogaVista, InformaVista{
    private final Stage stage;
    private Controlador controlador;
    private InterrogaModelo modelo;

    private ImageView iv;
    private Button button;
    Spinner<Integer> chapter;
    Label score;
    Label feedback = new Label("Juego sacado de RadioPirata y programado por Sancho.");
    private Font TITLE_FONT = new Font("Montserrat", 30);
    private Font TEXT_FONT = new Font("Montserrat", 20);
    public ImplementacionVista(Stage stage) {
        this.stage = stage;
    }

    public void setModelo(ImplementacionModelo modelo) {
        this.modelo = modelo;
    }

    public void setControlador(ImplementacionControlador controlador) {
        this.controlador = controlador;
    }

    public void creaGUI() {
        Parent titleBox = titleGUI();
        Parent panelBox = panelGUI();
        Parent dataBox = dataGUI();
        Parent buttonBox = buttonGUI();

        VBox mainBox = new VBox(
                titleBox,
                dataBox,
                buttonBox,
                panelBox);
        mainBox.setSpacing(5);

        Scene scene = new Scene(mainBox);
        stage.setFullScreen(true);
        stage.setTitle("The Right Chapter");
        stage.setScene(scene);
        stage.show();
    }


    private Parent titleGUI() {
        Label saludo = new Label("El capítulo justo!");
        //saludo.setMnemonicParsing(true);
        saludo.setFont(TITLE_FONT);

        HBox title = new HBox(saludo);
        title.setAlignment(Pos.CENTER);

        return title;
    }


    private Parent panelGUI() {
        Image pannel = new Image("rules.jpg");
        iv = new ImageView();
        iv.setImage(pannel);
        feedback.setFont(TEXT_FONT);
        VBox panel = new VBox(iv, feedback);
        panel.setAlignment(Pos.CENTER);
        return panel;
    }
    @Override
    public void setFeedback(String string) {
        feedback.setText(string);
    }

    @Override
    public void setScore(int newscore) {
        score.setText(Integer.toString(newscore));
    }

    @Override
    public void setStatus(String status) {
        button.setText(status);
    }

    private Parent dataGUI() {
        Label nameLabel = new Label("¿Qué capítulo?");
        nameLabel.setFont(TEXT_FONT);

        chapter = new Spinner<> (0, 1089,1);
        chapter.setEditable(true);

        Label scoreLabel = new Label("Puntuación");
        scoreLabel.setFont(TEXT_FONT);

        score = new Label("0");
        score.setFont(new Font("Impact", 16));
        score.setTextFill(Color.RED);

        GridPane dataBox = new GridPane();
        dataBox.add(nameLabel, 0, 0);
        dataBox.add(chapter, 0, 1);
        dataBox.add(scoreLabel, 1, 0);
        dataBox.add(score, 1, 1);

        dataBox.setAlignment(Pos.CENTER);
        dataBox.setHgap(100);

        return dataBox;
    }

    private Parent buttonGUI() {
        button = new Button("Siguiente");
        button.setOnAction(e-> controlador.goPressed());
        button.setPrefSize(100,50);

        HBox buttonBox = new HBox(button);
        buttonBox.setAlignment(Pos.CENTER);
        return buttonBox;
    }

    public String getPanel(){
        return iv.getImage().getUrl();
    }

    @Override
    public String getStatus() {
        return button.getText();
    }

    @Override
    public int getChapter() {
        return chapter.getValue();
    }

    @Override
    public void newPanel(String fileName) {
        iv.setImage(new Image(fileName));
    }


}
