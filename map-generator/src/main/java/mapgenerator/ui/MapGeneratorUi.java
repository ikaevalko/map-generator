package mapgenerator.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mapgenerator.logic.RandomNumberGenerator;

public class MapGeneratorUi extends Application {
    
    private Stage stage;
    private Scene alkuvalikko;
    
    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        luoAlkuvalikko();
        meneAlkuvalikkoon();
        stage.setTitle("Karttageneraattori");
        stage.setResizable(false);
        stage.show();
        RandomNumberGenerator rand = new RandomNumberGenerator(10);
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                double d = rand.seuraava();
                if(d < 0.5) {
                    System.out.print("0");
                } else {
                    System.out.print("X");
                }
            }
            System.out.println("");
        }
    }
    
    private void luoAlkuvalikko() {

        VBox asettelu = new VBox(20);
        asettelu.setAlignment(Pos.CENTER);
        asettelu.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        Label otsikko = new Label("Karttageneraattori");
        otsikko.textFillProperty().setValue(Color.WHITE);
        otsikko.setStyle("-fx-font-size: 20");
        Button generoi = new Button("Generoi");
        Button lopeta = new Button("Lopeta");

        otsikko.setPadding(new Insets(10, 10, 40, 10));
        generoi.setPadding(new Insets(10, 10, 10, 10));
        lopeta.setPadding(new Insets(10, 10, 10, 10));

        lopeta.setOnAction(e->{
            Platform.exit();
            System.exit(0);
        });

        asettelu.getChildren().addAll(otsikko, generoi, lopeta);

        alkuvalikko = new Scene(asettelu, 800, 800);
    }
        
    private void meneAlkuvalikkoon() {
        stage.setScene(alkuvalikko);
    }
    
    private void meneGenerointinakymaan() {
        
        Pane pohja = new Pane();
        pohja.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        Scene generointiNakyma = new Scene(pohja, 800, 800);
        stage.setScene(generointiNakyma);
    }
    
    public void kaynnista() {
        launch();
    }
}