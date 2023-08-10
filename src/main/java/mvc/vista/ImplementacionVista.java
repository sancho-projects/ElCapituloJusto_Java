package mvc.vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import mvc.controlador.Controlador;
import mvc.controlador.ImplementacionControlador;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mvc.modelo.ImplementacionModelo;
import mvc.modelo.InterrogaModelo;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

public class ImplementacionVista implements InterrogaVista, InformaVista{
    private final Stage stage;
    private Controlador controlador;
    private InterrogaModelo modelo;

    private CheckBox easy;
    private CheckBox medium;
    private CheckBox hard;
    private Spinner<Integer> lastChapter;
    private ImageView iv;
    private Button skipButton;
    private List<Spinner<Integer>> chapters = new ArrayList<>();
    private List<Label> scores = new ArrayList<>();
    private List<TextArea> feedbacks = new ArrayList<>();
    private Label feedbackGeneral = new Label("Idea de RadioPirata y código de Sancho.");

    private final Font TITLE_FONT = new Font("Montserrat", 30);
    private final Font TEXT_FONT = new Font("Montserrat", 20);
    private final Font SUBTEXT_FONT = new Font("Montserrat", 16);

    public ImplementacionVista(Stage stage) {
        this.stage = stage;
    }

    // GUI
    public void creaMenuGUI() {
        stage.setTitle("¡El capítulo justo!");
        stage.getIcons().add(new Image("icon.png"));

        Tab page1 = new Tab("Menú principal", startGUI());
        Tab page2 = new Tab("Personalización", customGUI());
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().add(page1); 
        tabPane.getTabs().add(page2);

        Scene scene1 = new Scene(tabPane);
        stage.setResizable(false);
        stage.setScene(scene1);
        stage.show();
    }
    private void creaGameGUI() {
        Parent titleBox = titleGUI();
        Parent dataBox = userGUI();
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

        Scene scene2 = new Scene(mainBox);
        stage.setResizable(true);
        stage.setScene(scene2);
        stage.setFullScreen(true);
        stage.show();
    }

    private Parent startGUI() {
        Text text = new Text("Escoge el número de jugadores:");

        Slider slider = new Slider(1,5,2);
        slider.setPrefWidth(100);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);
        slider.setSnapToTicks(true);

        GridPane namesPane = new GridPane();
        namesPane.setPadding(new Insets(10, 10, 10, 10));
        namesPane.setVgap(5);
        namesPane.setHgap(5);
        namesPane.setAlignment(Pos.CENTER);
        namesPane.add(new Label("Escribir nombre de los jugadores."), 0, 0);

        List<Text> textList = new ArrayList<>(5);
        List<TextField> textFieldList = new ArrayList<>(5);
        for (int i=1; i<=5; i++){
            Text nameText = new Text("Jugador " + i + ":");
            namesPane.add(nameText, 0, i);
            textList.add(nameText);

            TextField nameTF = new TextField("");
            namesPane.add(nameTF, 1, i);
            textFieldList.add(nameTF);
        }

        updateNames((int) slider.getValue(), textList, textFieldList);
        slider.setOnMouseReleased( e->
                    updateNames((int) slider.getValue(), textList, textFieldList)
        );

        Button nextButton = new Button("Comenzar");
        nextButton.setOnAction(e -> {
            List<String> names = new ArrayList<>((int) slider.getValue());
            for (int i=0; i< slider.getValue(); i++) {
                names.add(textFieldList.get(i).getText());
            }
            controlador.initializeGame(names);
            creaGameGUI();
        });

        GridPane startPane = new GridPane();
        startPane.setMinSize(300,300);
        startPane.setPadding(new Insets(10,10,10,10));
        startPane.setVgap(5);
        startPane.setHgap(5);
        startPane.setAlignment(Pos.CENTER);
        startPane.add(text, 0, 0);
        startPane.add(slider, 0, 1);
        startPane.add(namesPane, 0, 2);
        startPane.add(nextButton,0,3);
        return startPane;
    }

    private Parent customGUI() {
        Label label = new Label("Aquí puedes personalizar las viñetas que te saldrán.");

        Label limitLabel = new Label("Del capítulo 1 al :");
        limitLabel.setFont(SUBTEXT_FONT);
        lastChapter = new Spinner<>(100, 1089, 1);
        lastChapter.setEditable(true);
        lastChapter.getEditor().setTextFormatter(numberFormatter(1089));

        Label difficultyLabel = new Label("Dificultad de las viñetas: ");
        difficultyLabel.setFont(SUBTEXT_FONT);
        easy = new CheckBox("Fácil");
        easy.fire();
        medium = new CheckBox("Medio");
        medium.fire();
        hard = new CheckBox("Difícil");
        hard.fire();

        AtomicInteger checkedButtons = new AtomicInteger(3);
        easy.setOnAction(e-> checkedButtons.set(notDisableAll(easy, checkedButtons.get())));
        medium.setOnAction(e-> checkedButtons.set(notDisableAll(medium, checkedButtons.get())));
        hard.setOnAction(e-> checkedButtons.set(notDisableAll(hard, checkedButtons.get())));

        HBox difficultyPane = new HBox(easy, medium, hard);
        difficultyPane.setSpacing(20);

        GridPane customPane = new GridPane();
        customPane.setAlignment(Pos.TOP_CENTER);
        customPane.setPadding(new Insets(10,10,10,10));
        customPane.setVgap(20);

        customPane.add(limitLabel,0,0);
        customPane.add(lastChapter,1,0);
        customPane.add(difficultyLabel,0,1);
        customPane.add(difficultyPane,1,1);

        VBox vbox = new VBox(label, customPane);
        vbox.setAlignment(Pos.TOP_CENTER);
        return vbox;
    }

    private Parent titleGUI() {
        Label saludo = new Label("↓ Introduce aquí abajo el capítulo por el que apuestas ↓");
        saludo.setFont(TITLE_FONT);

        HBox title = new HBox(saludo);
        title.setSpacing(10);
        title.setAlignment(Pos.CENTER);

        return title;
    }

    private Parent userGUI() {
        GridPane dataBox = new GridPane();
        for (int i=0; i< modelo.getNumPlayers(); i++) {
            Label nameLabel = new Label(modelo.getPlayerName(i));
            nameLabel.setFont(TEXT_FONT);

            Spinner<Integer> chapterSpinner = new Spinner<>(1, 1089, 1);
            chapterSpinner.setEditable(true);
            chapterSpinner.setMinHeight(50);
            chapterSpinner.setMaxWidth(75);
            chapterSpinner.getEditor().setTextFormatter(numberFormatter(1));
            chapters.add(chapterSpinner);

            Label scoreLabel = new Label("0");
            scoreLabel.setFont(new Font("Impact", 30));
            scoreLabel.setTextFill(Color.RED);
            scores.add(scoreLabel);

            VBox vbox = new VBox(nameLabel, chapterSpinner, scoreLabel);
            vbox.setAlignment(Pos.CENTER);
            dataBox.add(vbox, i, 0);

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

    private Parent panelGUI() {
        Image panel = new Image("rules.png");
        iv = new ImageView();
        iv.setImage(panel);
        iv.setFitHeight(400);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);

        StackPane stackPane = new StackPane(iv);
        stackPane.setAlignment(Pos.CENTER);
        return stackPane;
    }

    private Parent feedbackGUI() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5,10,5,10));
        gridPane.setHgap(50);
        gridPane.setAlignment(Pos.CENTER);
        feedbackGeneral.setFont(TITLE_FONT);
        feedbackGeneral.setStyle("-fx-font-weight: bold");

        for (int i=0; i< modelo.getNumPlayers(); i++){
            TextArea feedback = new TextArea();
            feedback.setMaxWidth( (float) 1920/modelo.getNumPlayers() - 100);
            feedback.setMaxHeight(60);
            feedback.setWrapText(true);
            feedback.setEditable(false);
            feedback.setFont(SUBTEXT_FONT);
            feedbacks.add(feedback);

            gridPane.add(feedback, i,0) ;
        }

        VBox vbox = new VBox(feedbackGeneral, gridPane);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        return vbox;
    }


    // MODELO INFORMA a VISTA
    @Override
    public void finalScore() {
        setFeedbackGeneral("FIN DEL JUEGO.");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Puntuación final");
        alert.setHeaderText(modelo.getFinalScore());

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab highscorePage = new Tab("Highscore", highscoreGUI());
        tabPane.getTabs().add(highscorePage);

        for (int i=0; i<modelo.getNumPlayers(); i++){
            Tab playerPage = new Tab("Registro de " + modelo.getPlayerName(i), registerGUI(i));
            tabPane.getTabs().add(playerPage);
        }

        alert.getDialogPane().setExpandableContent(tabPane);

        ButtonType buttonTryAgain = new ButtonType("Volver a jugar");
        ButtonType buttonClose = new ButtonType("Cerrar");
        alert.getButtonTypes().setAll(buttonTryAgain,buttonClose);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == buttonTryAgain) {
            controlador.tryAgain();
        } else if (option.get() == buttonClose) {
            stage.close();
        }
    }

    private Parent registerGUI(int playerIndex) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);

        Label subtitle = new Label("Registro de todos los turnos de "+ modelo.getPlayerName(playerIndex));
        subtitle.setFont(SUBTEXT_FONT);
        gridPane.addRow(0, subtitle);
        for (int i = 0; i<modelo.getTURNS(); i++){
            gridPane.addRow(i+1, new Label(modelo.getRegisterAtTurn(playerIndex, i)));
        }

        Label minLabel = new Label(modelo.getMinRecord(playerIndex));
        Label maxLabel = new Label(modelo.getMaxRecord(playerIndex));
        minLabel.setFont(SUBTEXT_FONT);
        maxLabel.setFont(SUBTEXT_FONT);
        gridPane.addRow(modelo.getTURNS()+1, minLabel);
        gridPane.addRow(modelo.getTURNS()+2, maxLabel);

        return gridPane;
    }

    private Parent highscoreGUI() {
        Label title = new Label("HIGH SCORE");
        title.setFont(new Font("Impact", 30));
        title.setStyle("-fx-font-weight: bold");
        TextArea textArea = new TextArea(modelo.getHighScoreBoard());
        textArea.setMaxWidth(250);
        textArea.setEditable(false);
        VBox vbox = new VBox(title, textArea);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    @Override
    public void outOfPanels() {
        setFeedbackGeneral("No hay más viñetas.");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fin del juego.");
        alert.setHeaderText("Parece que hemos llegado al límite.");
        alert.setContentText("No disponemos de más viñetas que cumplan con los requisitos exigidos," +
                "por lo que ponemos fin al juego. Vuelva a jugar con otras opciones o espere nuevas" +
                " actualizaciones.");
        alert.showAndWait();
        stage.close();
    }


    // SETTERS
    public void setModelo(ImplementacionModelo modelo) {
        this.modelo = modelo;
    }
    public void setControlador(ImplementacionControlador controlador) {
        this.controlador = controlador;
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
    @Override
    public void setFeedbackGeneral(String string) {
        feedbackGeneral.setText(string);
    }
    @Override
    public void setFeedback(int i, String s) {
        feedbacks.get(i).setText(s);
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

    @Override
    public boolean getEasyStatus() {
        return easy.isSelected();
    }

    @Override
    public boolean getMediumStatus() {
        return medium.isSelected();
    }

    @Override
    public boolean getHardStatus() {
        return hard.isSelected();
    }

    @Override
    public int getLastChapter() {
        return lastChapter.getValue();
    }

    // AUXILIAR
    private void updateNames(int value, List<Text> textList, List<TextField> textFieldList) {
        boolean visible = true;
        for (int i=0; i<5; i++){
            if (i==value) visible = false;
            textList.get(i).setVisible(visible);
            textFieldList.get(i).setVisible(visible);
        }
    }

    private int notDisableAll(CheckBox button, int checkedButtons) {
        {
            if (checkedButtons == 1) {
                if (!button.isSelected()){
                    button.fire();
                } else {
                    checkedButtons++;
                }
            }
            else {
                if (!button.isSelected()) {
                    checkedButtons--;
                } else {
                    checkedButtons++;
                }
            }
        }
        return checkedButtons;
    }

    // kleopatra, stackoverflow (2015)
    private TextFormatter<?> numberFormatter(int startNumber) {
        NumberFormat format = NumberFormat.getIntegerInstance();
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (c.isContentChange()) {
                ParsePosition parsePosition = new ParsePosition(0);
                // NumberFormat evaluates the beginning of the text
                format.parse(c.getControlNewText(), parsePosition);
                if (parsePosition.getIndex() == 0 ||
                        parsePosition.getIndex() < c.getControlNewText().length()) {
                    // reject parsing the complete text failed
                    return null;
                }
            }
            return c;
        };
        return new TextFormatter<>(
                new IntegerStringConverter(), startNumber, filter);
    }
}
