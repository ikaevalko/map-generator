package mapgenerator.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import mapgenerator.logic.CellularAutomata;
import mapgenerator.logic.PerlinNoise;

public class MapGeneratorUi extends Application {
    
    private Stage stage;
    private Scene alkuvalikko;
    private Scene karttanakyma;
    private BorderPane ylaPalkki;
    private Pane pohja;
    private Label statusTeksti;
    private double skaala;
    
    private final double[] perlinNoiseTasot = new double[]{
        -0.1,
        0.0,
        0.2,
        0.4,
        0.8
    };
    
    public void kaynnista() {
        launch();
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        luoAlkuvalikko();
        luoKarttaNakyma(new Pane(), "Generoidaan...");
        meneAlkuvalikkoon();
        stage.setMinWidth(400);
        stage.setMinHeight(400);
        stage.setTitle("Karttageneraattori");
        stage.show();
    }
    
    private void luoAlkuvalikko() {
        Label otsikko = new Label("Karttageneraattori");
        otsikko.setStyle("-fx-text-fill: white; -fx-font-size: 16");
        
        ComboBox valinnat = new ComboBox();
        valinnat.setPadding(new Insets(10));
        valinnat.setPromptText("Valitse algoritmi");
        valinnat.getItems().addAll("Cellular Automata", "Perlin Noise");
        
        VBox cellularAutomataValikko = luoCAValikko();
        cellularAutomataValikko.setVisible(false);
        
        VBox perlinNoiseValikko = luoPNValikko();
        perlinNoiseValikko.setVisible(false);
        
        Pane valikot = new Pane();
        valikot.getChildren().addAll(cellularAutomataValikko, perlinNoiseValikko);
        
        GridPane asettelu = new GridPane();
        asettelu.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        asettelu.setAlignment(Pos.CENTER);
        asettelu.setVgap(20);
        
        valinnat.setOnAction((event) -> {
            int valinta = valinnat.getSelectionModel().getSelectedIndex();
            if(valinta == 0) {
                perlinNoiseValikko.setVisible(false);
                cellularAutomataValikko.setVisible(true);
            } else if(valinta == 1) {
                cellularAutomataValikko.setVisible(false);
                perlinNoiseValikko.setVisible(true);
            }
        });
        
        asettelu.add(otsikko, 0, 0);
        asettelu.add(valinnat, 0, 1);
        asettelu.add(valikot, 0, 2);
        GridPane.setHalignment(otsikko, HPos.CENTER);
        GridPane.setHalignment(valinnat, HPos.CENTER);
        alkuvalikko = new Scene(asettelu, 800, 800);
    }
    
    private VBox luoCAValikko() {
        IntegerField kartanLeveys = new IntegerField(100);
        IntegerField kartanKorkeus = new IntegerField(100);
        IntegerField seed = new IntegerField(10);
        TextField tayttoaste = new TextField("0.5");
        IntegerField tasoituskertoja = new IntegerField(5);
        IntegerField huoneidenMinimikoko = new IntegerField(5);
        IntegerField kaytavienKoko = new IntegerField(1);
        
        HBox leveysBox = luoIntegerParametri("Kartan leveys", kartanLeveys);
        HBox korkeusBox = luoIntegerParametri("Kartan korkeus", kartanKorkeus);
        HBox seedBox = luoIntegerParametri("Seed", seed);
        HBox tayttoasteBox = luoTekstiParametri("T\u00e4ytt\u00f6aste", tayttoaste);
        HBox tasoituskertojaBox = luoIntegerParametri("Tasoituskertoja", tasoituskertoja);
        HBox huoneidenMinimikokoBox = luoIntegerParametri("Huoneiden minimikoko", huoneidenMinimikoko);
        HBox kaytavienKokoBox = luoIntegerParametri("K\u00e4yt\u00e4vien koko", kaytavienKoko);
        
        Region vali = new Region();
        vali.setPadding(new Insets(10));
        
        Button generoi = new Button("Generoi");
        
        generoi.setOnAction(e -> {
            generoiCA(kartanLeveys.getArvo(), kartanKorkeus.getArvo(), seed.getArvo(), 
                      Double.parseDouble(tayttoaste.textProperty().getValue()), 
                      tasoituskertoja.getArvo(), huoneidenMinimikoko.getArvo(), kaytavienKoko.getArvo());
        });
        
        VBox asettelu = new VBox(5);
        asettelu.setAlignment(Pos.CENTER);
        asettelu.getChildren().addAll(leveysBox, korkeusBox, seedBox, tayttoasteBox,
                                      tasoituskertojaBox, huoneidenMinimikokoBox, kaytavienKokoBox, 
                                      vali, generoi);
        
        return asettelu;
    }
    
    private VBox luoPNValikko() {
        IntegerField kartanKoko = new IntegerField(100);
        IntegerField ristikonTiheys = new IntegerField(4);
        IntegerField seed = new IntegerField(10);
        
        HBox kokoBox = luoIntegerParametri("Kartan koko", kartanKoko);
        HBox tiheysBox = luoIntegerParametri("Ristikon tiheys", ristikonTiheys);
        HBox seedBox = luoIntegerParametri("Seed", seed);
        
        Region vali = new Region();
        vali.setPadding(new Insets(10));
        
        Button generoi = new Button("Generoi");
        
        generoi.setOnAction(e -> {
            generoiPN(kartanKoko.getArvo(), ristikonTiheys.getArvo(), seed.getArvo());
        });
        
        VBox asettelu = new VBox(5);
        asettelu.setAlignment(Pos.CENTER);
        asettelu.getChildren().addAll(kokoBox, tiheysBox, seedBox, 
                                      vali, generoi);
        
        return asettelu;
    }
    
    private void generoiCA(int kartanLeveys, int kartanKorkeus, int seed, double tayttoaste, 
                           int tasoituskertoja, int huoneidenMinimikoko, int kaytavienKoko) {
        statusTeksti.setText("Generoidaan...");
        meneKarttanakymaan();
        new Thread(() -> {
            CellularAutomata generaattori = new CellularAutomata(kartanLeveys, kartanKorkeus, seed, tayttoaste, 
                                                                 tasoituskertoja, huoneidenMinimikoko, kaytavienKoko);
            long alku = System.currentTimeMillis();
            boolean[][] kartta = generaattori.generoi();
            long loppu = System.currentTimeMillis();
            Pane uusiPohja = new Pane();
            piirraTaulukko(kartta, uusiPohja);
            
            Platform.runLater(() -> {
                this.pohja = uusiPohja;
                this.skaala = 1.0;
                luoKarttaNakyma(pohja, "Aika: " + (loppu - alku) + " ms");
                meneKarttanakymaan();
            });
        }).start();
    }
    
    private void generoiPN(int kartanKoko, int ristikonTiheys, int seed) {
        statusTeksti.setText("Generoidaan...");
        meneKarttanakymaan();
        new Thread(() -> {
            PerlinNoise generaattori = new PerlinNoise(kartanKoko, ristikonTiheys, seed);
            long alku = System.currentTimeMillis();
            double[][] kartta = generaattori.generoi();
            long loppu = System.currentTimeMillis();
            Pane uusiPohja = new Pane();
            piirraTaulukko(kartta, uusiPohja);
            
            Platform.runLater(() -> {
                this.pohja = uusiPohja;
                this.skaala = 1.0;
                luoKarttaNakyma(pohja, "Aika: " + (loppu - alku) + " ms");
                meneKarttanakymaan();
            });
        }).start();
    }
    
    private HBox luoIntegerParametri(String nimi, IntegerField kentta) {
        Label teksti = new Label(nimi);
        teksti.setStyle("-fx-text-fill: white");
        teksti.setPrefWidth(200);
        kentta.setPrefWidth(100);
        HBox asettelu = new HBox(40);
        asettelu.setAlignment(Pos.CENTER_RIGHT);
        asettelu.setPrefWidth(300);
        asettelu.getChildren().addAll(teksti, kentta);
        return asettelu;
    }
    
    private HBox luoTekstiParametri(String nimi, TextField kentta) {
        Label teksti = new Label(nimi);
        teksti.setStyle("-fx-text-fill: white");
        teksti.setPrefWidth(200);
        kentta.setPrefWidth(100);
        HBox asettelu = new HBox(40);
        asettelu.setAlignment(Pos.CENTER_RIGHT);
        asettelu.setPrefWidth(300);
        asettelu.getChildren().addAll(teksti, kentta);
        return asettelu;
    }
    
    private void luoKarttaNakyma(Pane pohja, String statusTeksti) {
        Button alkuvalikkoon = new Button("<");
        alkuvalikkoon.setStyle("-fx-font-size: 14");
        alkuvalikkoon.setPrefSize(40, 40);
        
        Button suurenna = new Button("+");
        suurenna.setStyle("-fx-font-size: 14");
        suurenna.setPrefSize(40, 40);
        
        Button pienenna = new Button("-");
        pienenna.setStyle("-fx-font-size: 14");
        pienenna.setPrefSize(40, 40);
        
        Label status = new Label(statusTeksti);
        status.setStyle("-fx-text-fill: white");
        status.setAlignment(Pos.CENTER);
        
        VBox vasenYla = new VBox(10);
        vasenYla.setPadding(new Insets(10));
        vasenYla.getChildren().addAll(alkuvalikkoon, status);
        
        VBox oikeaYla = new VBox(10);
        oikeaYla.setPadding(new Insets(10));
        oikeaYla.getChildren().addAll(suurenna, pienenna);
        
        ylaPalkki = new BorderPane();
        ylaPalkki.setLeft(vasenYla);
        ylaPalkki.setRight(oikeaYla);

        alkuvalikkoon.setOnAction(e -> {
            meneAlkuvalikkoon();
        });
        
        suurenna.setOnAction(e -> {
            if(skaala < 1.5) {
                skaalaaKarttaa(pohja, skaala*=2);
            }
        });
        
        pienenna.setOnAction(e -> {
            if(skaala > 0.05) {
                skaalaaKarttaa(pohja, skaala/=2);
            }
        });
        
        Pane ankkuri = new Pane();
        ankkuri.setTranslateX(100);
        ankkuri.getChildren().add(pohja);
        ankkuri.setMouseTransparent(true);
        
        BorderPane kartanAsettelu = new BorderPane();
        kartanAsettelu.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        kartanAsettelu.setTop(ylaPalkki);
        kartanAsettelu.setCenter(ankkuri);
        this.pohja = pohja;
        this.statusTeksti = status;
        karttanakyma = new Scene(kartanAsettelu, 800, 800);
    }
    
    private void meneAlkuvalikkoon() {
        ylaPalkki.setDisable(true);
        pohja.setVisible(false);
        pohja = null;
        stage.setScene(alkuvalikko);
    }
    
    private void meneKarttanakymaan() {
        stage.setScene(karttanakyma);
    }
    
    private void piirraTaulukko(boolean[][] taulukko, Pane pohja) {
        for(int x = 0; x < taulukko.length; x++) {
            for(int y = 0; y < taulukko[0].length; y++) {
                if(taulukko[x][y]) {
                    Rectangle lattia = new Rectangle(10, 10, Color.WHITE);
                    pohja.getChildren().add(lattia);
                    lattia.setTranslateX(x*10);
                    lattia.setTranslateY(y*10);
                }
            }
        }
    }
    
    private void piirraTaulukko(double[][] taulukko, Pane pohja) {
        for(int x = 0; x < taulukko.length; x++) {
            for(int y = 0; y < taulukko[0].length; y++) {
                double arvo = taulukko[x][y];
                Color c;
                if(arvo < perlinNoiseTasot[0]) {
                    c = Color.DEEPSKYBLUE;
                } else if(arvo < perlinNoiseTasot[1]) {
                    c = Color.NAVAJOWHITE;
                } else if(arvo < perlinNoiseTasot[2]) {
                    c = Color.GREEN;
                } else if(arvo < perlinNoiseTasot[3]) {
                    c = Color.FORESTGREEN;
                } else if(arvo < perlinNoiseTasot[4]) {
                    c = Color.LIGHTGREY;
                } else {
                    c = Color.YELLOW;
                }
                Rectangle lattia = new Rectangle(10, 10, c);
                pohja.getChildren().add(lattia);
                lattia.setTranslateX(x*10);
                lattia.setTranslateY(y*10);
            }
        }
    }
    
    private void skaalaaKarttaa(Pane pohja, double skaala) {
        pohja.setScaleX(skaala);
        pohja.setScaleY(skaala);
    }
}