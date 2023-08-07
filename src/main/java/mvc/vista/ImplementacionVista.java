package mvc.vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import mvc.controlador.Controlador;
import mvc.controlador.ImplementacionControlador;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mvc.modelo.ImplementacionModelo;
import mvc.modelo.InterrogaModelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImplementacionVista implements InterrogaVista, InformaVista{
    private final Stage stage;
    private Controlador controlador;
    private InterrogaModelo modelo;

    private ImageView iv;
    private Button skipButton;
    private List<Spinner<Integer>> chapters = new ArrayList<>();
    private List<Label> scores = new ArrayList<>();
    private List<TextArea> feedbacks = new ArrayList<>();
    private Label feedbackGeneral = new Label("Juego sacado de RadioPirata y programado por Sancho.");


    private Font TITLE_FONT = new Font("Montserrat", 30);
    private Font TEXT_FONT = new Font("Montserrat", 20);
    private Font SUBTEXT_FONT = new Font("Montserrat", 16);
    public ImplementacionVista(Stage stage) {
        this.stage = stage;
    }


    // SETTERS
    public void setModelo(ImplementacionModelo modelo) {
        this.modelo = modelo;
    }

    public void setControlador(ImplementacionControlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void setFeedbackGeneral(String string) {
        feedbackGeneral.setText(string);
    }

    @Override
    public void setScore(int i, int newscore) {
        scores.get(i).setText(Integer.toString(newscore));
    }

    @Override
    public void setStatus(String status) {
        skipButton.setText(status);
    }

    @Override
    public void setNewPanel(String fileName) {
        iv.setImage(new Image(fileName));
    }


    // GETTERS
    @Override
    public String getStatus() {
        return skipButton.getText();
    }

    @Override
    public List<Integer> getChapters() {
        List<Integer> list = new ArrayList<>();
        for (Spinner<Integer> chapter : chapters) {
             list.add(chapter.getValue());
        }
        return list;
    }


    // GUI
    public void creaGUI() {
        menuGUI();

        Parent titleBox = titleGUI();
        Parent dataBox = dataGUI();
        Parent buttonBox = buttonGUI();
        Parent panelBox = panelGUI();
        Parent feedbackBox = feedbackGUI();

        VBox mainBox = new VBox(
                titleBox,
                dataBox,
                buttonBox,
                panelBox,
                feedbackBox);
        mainBox.setSpacing(5);

        Scene scene = new Scene(mainBox);
        stage.setFullScreen(true);
        stage.setTitle("The Right Chapter");
        stage.setScene(scene);
        stage.show();
    }

    private void menuGUI() {
        Alert menu = new Alert(Alert.AlertType.NONE);
        menu.setHeaderText("Bienvenido a 'El capítulo justo'!");
        menu.setTitle("Menú");

        Text text = new Text("Número de jugadores:");

        Slider slider = new Slider(1,5,2);
        slider.setPrefWidth(100);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);
        slider.setSnapToTicks(true);

        GridPane numPlayersPane = new GridPane();
        numPlayersPane.setPadding(new Insets(10,10,10,10));
        numPlayersPane.setVgap(5);
        numPlayersPane.setHgap(5);
        numPlayersPane.setAlignment(Pos.CENTER);
        numPlayersPane.add(text, 0, 0);
        numPlayersPane.add(slider, 0, 1);

        menu.getDialogPane().setGraphic(numPlayersPane);

        menu.setContentText("Desplegar para personalizar la elección de viñetas.");
        Label label = new Label("Aquí vienen los ajustes");
        Label label2 = new Label("Escoger franja de capítulos");
        Label label3 = new Label("0 al 1089");
        Label label4 = new Label("Escoger dificultad (easy, medium or hard)");
        GridPane options = new GridPane();
        options.setMaxWidth(Double.MAX_VALUE);
        options.add(label,0,0);
        options.add(label2,0,1);
        options.add(label3,1,1);
        options.add(label4,0,2);
        menu.getDialogPane().setExpandableContent(options);

// LIO LIO LIO LIO LIO LIO LIO LIO LIO LIO
        ButtonType next = new ButtonType("Siguiente");
        menu.getButtonTypes().setAll(next);
        Optional<ButtonType> numPlayers = menu.showAndWait();
        if (numPlayers.get() == next) {

            Alert namesMenu = new Alert(Alert.AlertType.NONE);
            namesMenu.setTitle("Escoger nombres");
            GridPane gridPaneNames = new GridPane();
            gridPaneNames.setPadding(new Insets(10, 10, 10, 10));
            gridPaneNames.setVgap(5);
            gridPaneNames.setHgap(5);
            gridPaneNames.setAlignment(Pos.CENTER);

            gridPaneNames.add(new Label("Escribir nombre de los jugadores."), 0, 0);
            List<TextField> textFieldList = new ArrayList<>((int) slider.getValue());
            for (int i = 1; i <= slider.getValue(); i++) {
                Text nameText = new Text("Jugador " + i + ":");
                TextField nameTF = new TextField("");
                textFieldList.add(nameTF);

                gridPaneNames.add(nameText, 0, i);
                gridPaneNames.add(nameTF, 1, i);
            }

            namesMenu.getDialogPane().setGraphic(gridPaneNames);

// Evitable si cambio de alerts a scenes
            ButtonType start = new ButtonType("Empezar");
            namesMenu.getButtonTypes().setAll(start);
            Optional<ButtonType> NEXT = namesMenu.showAndWait();
            if (NEXT.get() == start) {
                List<String> names = new ArrayList<>((int) slider.getValue());
                for (int i=0; i< slider.getValue(); i++){
                    names.add(textFieldList.get(i).getText());
                }
                controlador.sendNames(names);
            }
        }
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
        Image panel = new Image("rules.jpg");
        iv = new ImageView();
        iv.setImage(panel);
        iv.setFitHeight(500);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);

        StackPane stackPane = new StackPane(iv);
        stackPane.setAlignment(Pos.CENTER);
        return stackPane;
    }

    private Parent dataGUI() {
        GridPane dataBox = new GridPane();
        for (int i=0; i< modelo.getNumPlayers(); i++) {
            Label nameLabel = new Label(modelo.getPlayerName(i));
            nameLabel.setFont(TEXT_FONT);

            Spinner<Integer> chapterSpinner = new Spinner<>(0, 1089, 1);
            chapterSpinner.setEditable(true);
            chapters.add(chapterSpinner);

            Label scoreLabel = new Label("0");
            scoreLabel.setFont(new Font("Impact", 30));
            scoreLabel.setTextFill(Color.RED);
            scores.add(scoreLabel);

            dataBox.add(nameLabel, i, 0);
            dataBox.add(chapterSpinner, i, 1);
            dataBox.add(scoreLabel, i, 2);

            dataBox.setAlignment(Pos.CENTER);
            dataBox.setHgap(100);
        }

        return dataBox;
    }

    private Parent buttonGUI() {
        skipButton = new Button("Siguiente");
        skipButton.setOnAction(e-> controlador.goPressed());
        skipButton.setPrefSize(100,50);

        HBox buttonBox = new HBox(skipButton);
        buttonBox.setAlignment(Pos.CENTER);
        return buttonBox;
    }

    private Parent feedbackGUI() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5,10,5,10));
        gridPane.setHgap(50);
        gridPane.setAlignment(Pos.CENTER);
        feedbackGeneral.setFont(TEXT_FONT);

        for (int i=0; i< modelo.getNumPlayers(); i++){
            TextArea feedback = new TextArea();
            feedback.setMaxWidth(1920/modelo.getNumPlayers() - 100);
            feedback.setMaxHeight(100);
            feedback.setWrapText(true);
            feedback.setFont(SUBTEXT_FONT);
            feedbacks.add(feedback);

            gridPane.add(feedback, i,0) ;
        }

        VBox vbox = new VBox(feedbackGeneral, gridPane);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        return vbox;
    }


    // OTHERS
    @Override
    public void endGame() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Puntuación final");
        alert.setHeaderText(modelo.getFinalScore());

//        alert.getDialogPane().setExpandableContent();
        alert.setContentText(modelo.getScoreBoard());

        alert.showAndWait();
        stage.close();
    }

    @Override
    public void setFeedback(int i, String s) {
        feedbacks.get(i).setText(s);
    }

}
