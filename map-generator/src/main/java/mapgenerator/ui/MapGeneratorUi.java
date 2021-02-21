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
    private Scene karttanakyma;
    private GridPane paavalikko;
    private VBox skaalausNapit;
    private BorderPane kartanAsettelu;
    private Pane pohja;
    private Label statusTeksti;
    private double skaala;
    
    private final double[] perlinNoiseTasot = new double[5];
    
    public void kaynnista() {
        launch();
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        luoKarttaNakyma();
        meneKarttanakymaan();
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.setTitle("Karttageneraattori");
        stage.show();
    }
    
    private GridPane luoPaavalikko() {
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
        
        Label status = new Label();
        status.setStyle("-fx-text-fill: white");
        
        paavalikko = new GridPane();
        paavalikko.setAlignment(Pos.TOP_LEFT);
        paavalikko.setVgap(20);
        paavalikko.setPadding(new Insets(10));
        
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
        
        paavalikko.add(valinnat, 0, 0);
        paavalikko.add(valikot, 0, 1);
        paavalikko.add(status, 0, 2);
        GridPane.setHalignment(valinnat, HPos.LEFT);
        GridPane.setHalignment(status, HPos.CENTER);
        this.statusTeksti = status;
        return paavalikko;
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
            double validoituTayttoaste;
            try {
                validoituTayttoaste = Double.parseDouble(tayttoaste.getText());
            } catch(NumberFormatException ex) {
                statusTeksti.setText("Virheellinen täyttöaste");
                return;
            }
            generoiCA(kartanLeveys.getArvo(), kartanKorkeus.getArvo(), seed.getArvo(), 
                      validoituTayttoaste, 
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
        IntegerField ristikonTiheys = new IntegerField(6);
        IntegerField seed = new IntegerField(10);
        TextField taso1 = new TextField("-0.05");
        TextField taso2 = new TextField("0.0");
        TextField taso3 = new TextField("0.2");
        TextField taso4 = new TextField("0.45");
        TextField taso5 = new TextField("0.55");
        
        HBox kokoBox = luoIntegerParametri("Kartan koko", kartanKoko);
        HBox tiheysBox = luoIntegerParametri("Ristikon tiheys", ristikonTiheys);
        HBox seedBox = luoIntegerParametri("Seed", seed);
        HBox taso1Box = luoTekstiParametri("Taso 1", taso1);
        HBox taso2Box = luoTekstiParametri("Taso 2", taso2);
        HBox taso3Box = luoTekstiParametri("Taso 3", taso3);
        HBox taso4Box = luoTekstiParametri("Taso 4", taso4);
        HBox taso5Box = luoTekstiParametri("Taso 5", taso5);
        
        Region vali = new Region();
        vali.setPadding(new Insets(10));
        
        Button generoi = new Button("Generoi");
        
        generoi.setOnAction(e -> {
            try {
                perlinNoiseTasot[0] = Double.parseDouble(taso1.getText());
                perlinNoiseTasot[1] = Double.parseDouble(taso2.getText());
                perlinNoiseTasot[2] = Double.parseDouble(taso3.getText());
                perlinNoiseTasot[3] = Double.parseDouble(taso4.getText());
                perlinNoiseTasot[4] = Double.parseDouble(taso5.getText());
            } catch(NumberFormatException ex) {
                statusTeksti.setText("Virheelliset tasot");
                return;
            }
            generoiPN(kartanKoko.getArvo(), ristikonTiheys.getArvo(), seed.getArvo());
        });
        
        VBox asettelu = new VBox(5);
        asettelu.setAlignment(Pos.CENTER);
        asettelu.getChildren().addAll(kokoBox, tiheysBox, seedBox, taso1Box, taso2Box, 
                                      taso3Box, taso4Box, taso5Box, vali, generoi);
        
        return asettelu;
    }
    
    private void generoiCA(int kartanLeveys, int kartanKorkeus, int seed, double tayttoaste, 
                           int tasoituskertoja, int huoneidenMinimikoko, int kaytavienKoko) {
        statusTeksti.setText("Generoidaan...");
        new Thread(() -> {
            CellularAutomata generaattori = new CellularAutomata(kartanLeveys, kartanKorkeus, seed, tayttoaste, 
                                                                 tasoituskertoja, huoneidenMinimikoko, kaytavienKoko);
            long alku = System.currentTimeMillis();
            try {
                generaattori.generoi();
            } catch(Exception e) {
                Platform.runLater(() -> {
                    statusTeksti.setText("Virheelliset parametrit");
                });
                return;
            }
            long loppu = System.currentTimeMillis();
            boolean[][] kartta = generaattori.getKartta();
            Pane uusiPohja = new Pane();
            piirraTaulukko(kartta, uusiPohja);
            
            Platform.runLater(() -> {
                pohja = uusiPohja;
                Pane ankkuri = new Pane();
                ankkuri.getChildren().add(uusiPohja);
                ankkuri.setMouseTransparent(true);
                ankkuri.setTranslateX(20);
                ankkuri.setTranslateY(20);
                skaala = 1.0;
                statusTeksti.setText("Aika: " + (loppu - alku) + " ms");
                kartanAsettelu.setCenter(ankkuri);
                kartanAsettelu.getChildren().remove(skaalausNapit);
                kartanAsettelu.setRight(skaalausNapit);
            });
        }).start();
    }
    
    private void generoiPN(int kartanKoko, int ristikonTiheys, int seed) {
        statusTeksti.setText("Generoidaan...");
        new Thread(() -> {
            PerlinNoise generaattori = new PerlinNoise(kartanKoko, ristikonTiheys, seed);
            long alku = System.currentTimeMillis();
            try {
                generaattori.generoi();
            } catch(IllegalArgumentException e) {
                Platform.runLater(() -> {
                    statusTeksti.setText(e.getMessage());
                });
                return;
            }
            long loppu = System.currentTimeMillis();
            double[][] kartta = generaattori.getKartta();
            Pane uusiPohja = new Pane();
            piirraTaulukko(kartta, uusiPohja);
            
            Platform.runLater(() -> {
                pohja = uusiPohja;
                Pane ankkuri = new Pane();
                ankkuri.getChildren().add(uusiPohja);
                ankkuri.setMouseTransparent(true);
                ankkuri.setTranslateX(20);
                ankkuri.setTranslateY(20);
                skaala = 1.0;
                statusTeksti.setText("Aika: " + (loppu - alku) + " ms");
                kartanAsettelu.setCenter(ankkuri);
                kartanAsettelu.getChildren().remove(skaalausNapit);
                kartanAsettelu.setRight(skaalausNapit);
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
    
    private void luoKarttaNakyma() {
        paavalikko = luoPaavalikko();
        
        Button suurenna = new Button("+");
        suurenna.setStyle("-fx-font-size: 14");
        suurenna.setPrefSize(40, 40);
        
        Button pienenna = new Button("-");
        pienenna.setStyle("-fx-font-size: 14");
        pienenna.setPrefSize(40, 40);
        
        skaalausNapit = new VBox(10);
        skaalausNapit.setPadding(new Insets(10));
        skaalausNapit.getChildren().addAll(suurenna, pienenna);
        
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
        
        kartanAsettelu = new BorderPane();
        kartanAsettelu.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        kartanAsettelu.setLeft(paavalikko);
        kartanAsettelu.setRight(skaalausNapit);
        kartanAsettelu.setCenter(new Pane());
        karttanakyma = new Scene(kartanAsettelu, 800, 600);
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
                    c = Color.SILVER;
                } else {
                    c = Color.SNOW;
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