package mapgenerator.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mapgenerator.logic.CellularAutomata;
import mapgenerator.logic.PerlinNoise;

public class MapGeneratorUi extends Application {
    
    private Stage stage;
    private GridPane paavalikko;
    private VBox skaalausNapit;
    private Pane ankkuri;
    private Pane pohja;
    private ImageView kuva;
    private Label statusTeksti;
    private double skaala;
    
    private final double[] perlinNoiseTasot = new double[5];
    
    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        
        luoKarttaNakyma();
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
            if(kartanLeveys.getArvo() > 2000 || kartanKorkeus.getArvo() > 2000) {
                statusTeksti.setText("Leveys ja korkeus saavat olla enintään 2000");
                return;
            }
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
            if(kartanKoko.getArvo() > 2000) {
                statusTeksti.setText("Kartan koko saa olla enintään 2000");
                return;
            }
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
        paavalikko.setDisable(true);
        kuva.setImage(null);
        new Thread(() -> {
            CellularAutomata generaattori = new CellularAutomata(kartanLeveys, kartanKorkeus, seed, tayttoaste, 
                                                                 tasoituskertoja, huoneidenMinimikoko, kaytavienKoko);
            long alku = System.currentTimeMillis();
            try {
                generaattori.generoi();
            } catch(Exception e) {
                Platform.runLater(() -> {
                    statusTeksti.setText("Virheelliset parametrit");
                    paavalikko.setDisable(false);
                });
                return;
            }
            long loppu = System.currentTimeMillis();
            boolean[][] kartta = generaattori.getKartta();
            piirraTaulukko(kartta);
            
            Platform.runLater(() -> {
                skaalaaKarttaa(1.0);
                skaala = 1.0;
                statusTeksti.setText("Aika: " + (loppu - alku) + " ms");
                paavalikko.setDisable(false);
            });
        }).start();
    }
    
    private void generoiPN(int kartanKoko, int ristikonTiheys, int seed) {
        statusTeksti.setText("Generoidaan...");
        paavalikko.setDisable(true);
        kuva.setImage(null);
        new Thread(() -> {
            PerlinNoise generaattori = new PerlinNoise(kartanKoko, ristikonTiheys, seed);
            long alku = System.currentTimeMillis();
            try {
                generaattori.generoi();
            } catch(IllegalArgumentException e) {
                Platform.runLater(() -> {
                    statusTeksti.setText(e.getMessage());
                    paavalikko.setDisable(false);
                });
                return;
            }
            long loppu = System.currentTimeMillis();
            double[][] kartta = generaattori.getKartta();
            piirraTaulukko(kartta);
            
            Platform.runLater(() -> {
                skaalaaKarttaa(1.0);
                skaala = 1.0;
                statusTeksti.setText("Aika: " + (loppu - alku) + " ms");
                paavalikko.setDisable(false);
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
        kuva = new ImageView();
        
        pohja = new Pane(kuva);
        
        ankkuri = new Pane(pohja);
        ankkuri.setTranslateX(20);
        ankkuri.setTranslateY(20);
        ankkuri.setMouseTransparent(true);
        
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
                skaalaaKarttaa(skaala*=2);
            }
        });
        
        pienenna.setOnAction(e -> {
            if(skaala > 0.05) {
                skaalaaKarttaa(skaala/=2);
            }
        });
        
        BorderPane kartanAsettelu = new BorderPane();
        kartanAsettelu.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        kartanAsettelu.setLeft(paavalikko);
        kartanAsettelu.setCenter(ankkuri);
        kartanAsettelu.setRight(skaalausNapit);
        Scene karttanakyma = new Scene(kartanAsettelu, 800, 600);
        stage.setScene(karttanakyma);
    }
    
    private void piirraTaulukko(boolean[][] taulukko) {
        BufferedImage puskuroituKuva = new BufferedImage(taulukko.length*5, taulukko[0].length*5, BufferedImage.TYPE_INT_RGB);
        Graphics g = puskuroituKuva.createGraphics();
        for(int x = 0; x < taulukko.length; x++) {
            for(int y = 0; y < taulukko[0].length; y++) {
                if(taulukko[x][y]) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(x*5, y*5, 5, 5);
            }
        }
        kuva.setImage(SwingFXUtils.toFXImage(puskuroituKuva, null));
        kuva.setSmooth(false);
        kuva.setTranslateX((taulukko.length*5)*0.5);
        kuva.setTranslateY((taulukko[0].length*5)*0.5);
        pohja.setTranslateX((taulukko.length*5)*-0.5);
        pohja.setTranslateY((taulukko[0].length*5)*-0.5);
    }
    
    private void piirraTaulukko(double[][] taulukko) {
        BufferedImage puskuroituKuva = new BufferedImage(taulukko.length*5, taulukko[0].length*5, BufferedImage.TYPE_INT_RGB);
        Graphics g = puskuroituKuva.createGraphics();
        for(int x = 0; x < taulukko.length; x++) {
            for(int y = 0; y < taulukko[0].length; y++) {
                double arvo = taulukko[x][y];
                if(arvo < perlinNoiseTasot[0]) {
                    g.setColor(new Color(3, 141, 187));
                } else if(arvo < perlinNoiseTasot[1]) {
                    g.setColor(new Color(255, 220, 133));
                } else if(arvo < perlinNoiseTasot[2]) {
                    g.setColor(new Color(68, 126, 43));
                } else if(arvo < perlinNoiseTasot[3]) {
                    g.setColor(new Color(83, 141, 58));
                } else if(arvo < perlinNoiseTasot[4]) {
                    g.setColor(new Color(169, 169, 169));
                } else {
                    g.setColor(new Color(211, 212, 211));
                }
                g.fillRect(x*5, y*5, 5, 5);
            }
        }
        kuva.setImage(SwingFXUtils.toFXImage(puskuroituKuva, null));
        kuva.setSmooth(false);
        kuva.setTranslateX((taulukko.length*5)*0.5);
        kuva.setTranslateY((taulukko[0].length*5)*0.5);
        pohja.setTranslateX((taulukko.length*5)*-0.5);
        pohja.setTranslateY((taulukko[0].length*5)*-0.5);
    }
    
    private void skaalaaKarttaa(double skaala) {
        pohja.setScaleX(skaala);
        pohja.setScaleY(skaala);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}