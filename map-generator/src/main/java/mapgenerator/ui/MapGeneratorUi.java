package mapgenerator.ui;

import java.util.Arrays;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import mapgenerator.logic.CellularAutomata;
import mapgenerator.logic.RandomNumberGenerator;

public class MapGeneratorUi extends Application {
    
    private Stage stage;
    private Scene alkuvalikko;
    private Scene karttanakyma;
    private Pane tausta;
    private Pane pohja;
    
    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        //luoAlkuvalikko();
        luoKarttaNakyma();
        meneKarttanakymaan();
        //meneAlkuvalikkoon();
        stage.setTitle("Karttageneraattori");
        stage.setResizable(false);
        stage.show();
        
        CellularAutomata generaattori = new CellularAutomata(100, 100, 10, 0.5, 5);
        piirraTaulukko(generaattori.generoi());
        System.out.println("done");
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
        
        generoi.setOnAction(e->{
            meneKarttanakymaan();
        });

        lopeta.setOnAction(e->{
            Platform.exit();
            System.exit(0);
        });

        asettelu.getChildren().addAll(otsikko, generoi, lopeta);

        alkuvalikko = new Scene(asettelu, 800, 800);
    }
    
    private void luoKarttaNakyma() {
        tausta = new Pane();
        pohja = new Pane();
        tausta.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        tausta.getChildren().add(pohja);
        karttanakyma = new Scene(tausta, 800, 800);
    }
    
    private void meneAlkuvalikkoon() {
        stage.setScene(alkuvalikko);
    }
    
    private void meneKarttanakymaan() {
        stage.setScene(karttanakyma);
    }
    
    public void piirraTaulukko(boolean[][] taulukko) {
        for(int x = 0; x < taulukko.length; x++) {
            for(int y = 0; y < taulukko.length; y++) {
                if(taulukko[x][y]) {
                    lisaaLattia(x*10, y*10);
                }
            }
        }
        pohja.setScaleX(0.5);
        pohja.setScaleY(0.5);
        pohja.setTranslateX(400-(taulukko[0].length*10/2)*0.5);
        pohja.setTranslateY(400-(taulukko.length*10/2)*0.5);
    }
    
    public void lisaaLattia(int x, int y) {
        Rectangle lattia = new Rectangle(10, 10, Color.WHITE);
        pohja.getChildren().add(lattia);
        lattia.setTranslateX(x);
        lattia.setTranslateY(y);
    }
    
    public void kaynnista() {
        launch();
    }
}