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

public class ImplementacionVista implements InterrogaVista, InformaVista{
    private final Stage stage;
    private Controlador controlador;
    private InterrogaModelo modelo;

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


    // GUI
    private void creaGameGUI() {
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

        Scene scene2 = new Scene(mainBox);
        stage.setResizable(true);
        stage.setFullScreen(true);
        stage.setScene(scene2);
        stage.show();
    }

    public void creaMenuGUI() {
        stage.setTitle("¡El capítulo justo!");

        Parent startPane = startGUI();
        Tab page1 = new Tab("Menú principal", startPane);
        Parent customPane = customGUI();
        Tab page2 = new Tab("Personalización", customPane);
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(page1);
        tabPane.getTabs().add(page2);

        Scene scene1 = new Scene(tabPane);
        stage.setResizable(false);
        stage.setScene(scene1);
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

        updateNamesGUI((int) slider.getValue(), textList, textFieldList);
        slider.setOnMouseReleased( e->
                    updateNamesGUI((int) slider.getValue(), textList, textFieldList)
        );

        Button nextButton = new Button("Comenzar");
        nextButton.setOnAction(e -> {
            List<String> names = new ArrayList<>((int) slider.getValue());
            for (int i=0; i< slider.getValue(); i++) {
                names.add(textFieldList.get(i).getText());
            }
            controlador.sendNames(names);
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

    private void updateNamesGUI(int value, List<Text> textList, List<TextField> textFieldList) {
        boolean visible = true;
        for (int i=0; i<5; i++){
            if (i==value) visible = false;
            textList.get(i).setVisible(visible);
            textFieldList.get(i).setVisible(visible);
        }
    }

    private Parent customGUI() {
        GridPane customPane = new GridPane();
        Label label = new Label("Aquí vienen los ajustes");
        Label label2 = new Label("Escoger franja de capítulos");
        Label label3 = new Label("0 al 1089");
        Label label4 = new Label("Escoger dificultad (easy, medium or hard)");
        customPane.setMaxWidth(Double.MAX_VALUE);
        customPane.add(label,0,0);
        customPane.add(label2,0,1);
        customPane.add(label3,1,1);
        customPane.add(label4,0,2);

        return customPane;
    }
    private Parent titleGUI() {
        Label saludo = new Label("Intenta adivinar los capítulos.");
        saludo.setFont(TITLE_FONT);

        HBox title = new HBox(saludo);
        title.setSpacing(10);
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
            feedback.setMaxWidth( (float) 1920/modelo.getNumPlayers() - 100);
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

    @Override
    public void finalScoreGUI() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Puntuación final");
        alert.setHeaderText(modelo.getFinalScore());

        alert.setContentText("Desplegar para ver los mejores puntajes.");
        TextArea textArea = new TextArea(modelo.getScoreBoard());
        textArea.setMaxWidth(250);
        VBox vbox = new VBox(textArea);
        alert.getDialogPane().setExpandableContent(vbox);

        alert.showAndWait();
        stage.close();
    }


}
