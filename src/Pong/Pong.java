package Pong;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class Pong extends Application {

    public static Circle cercle;
    public static Pane pong;
    public static Text pulsarTeclaSpace;
    public static Text textoContador;
    public static Text GameOver;
    public static RectangleJugador rectanglejugador1;
    public static RectangleJugador rectanglejugador2;
    public static int contadorJugIzq = 0;
    public static int contadorJugDret = 0;


    class RectangleJugador extends Rectangle{
        class Posicio {
            int posX;
            int posY;
            public Posicio(int x,int y) {
                this.posX=x;
                this.posY=y;
            }
        }
        RectangleJugador.Posicio posicio;
        int velocitat=10;
        int ampladarectangle = 20;
        int alturarectangle = 90;
        Pane panell;
        Node Rectangle;

        public RectangleJugador(Pane panell,int posX,int posY) {
            posicio = new Posicio(posX,posY);
            this.panell = panell;
            this.Rectangle = new javafx.scene.shape.Rectangle(ampladarectangle,alturarectangle,Color.WHITE);
            this.posicio.posX = posX;
            this.posicio.posY = posY;
            this.Rectangle.setLayoutX(posicio.posX);
            this.Rectangle.setLayoutY(posicio.posY);
            this.panell.getChildren().add(this.Rectangle);

        }

        public void mouAmunt() {
            posicio.posY=posicio.posY-this.velocitat;
            this.repinta();
        }
        /**
         * Mou bolla cap abaix
         */
        public void mouAbaix() {
            posicio.posY=posicio.posY+this.velocitat;
            this.repinta();
        }
        private void repinta() {
            this.Rectangle.setLayoutY(posicio.posY);
        }
    }

    @Override
    public void start(final Stage primaryStage) throws Exception{

        pong = new Pane();
        final Scene escena = new Scene(pong, 800, 600);

        primaryStage.setTitle("Pong Game");
        primaryStage.setScene(escena);
        primaryStage.show();
        juego();
        pong.setStyle("-fx-background-color: black");
        //Pulsar tecla para SPACE
        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {


            // Formula en radians
            //double deltaX = 3*Math.cos(Math.PI/3);
            //double deltaY = 3*Math.sin(Math.PI/3);

            // Formula en graus
            final double angle_en_radians = Math.toRadians(30);
            final int velocitat = 2;
            double deltaX = velocitat * Math.cos(angle_en_radians);
            double deltaY = velocitat * Math.sin(angle_en_radians);
            int rebotaBalon = 0;

            // Simulació gravitatòria
            final double temps = 0;
            final Bounds limits = pong.getBoundsInLocal();

            @Override
            public void handle(final ActionEvent t) {
                if (contadorJugIzq < 15 && contadorJugDret < 15) {
                    //cercle.setLayoutX(cercle.getLayoutX() + deltaX/2);
                    cercle.setLayoutX(cercle.getLayoutX() + deltaX);
                    //cercle.setLayoutY(cercle.getLayoutY() + deltaY/3);
                    cercle.setLayoutY(cercle.getLayoutY() + deltaY);
                    //System.out.println(cercle.getLayoutX()+":"+cercle.getLayoutY());


                    final boolean alLimitDret = cercle.getLayoutX() >= (limits.getMaxX() - cercle.getRadius());
                    final boolean alLimitEsquerra = cercle.getLayoutX() <= (limits.getMinX() + cercle.getRadius());
                    final boolean alLimitInferior = cercle.getLayoutY() >= (limits.getMaxY() - cercle.getRadius());
                    final boolean alLimitSuperior = cercle.getLayoutY() <= (limits.getMinY() + cercle.getRadius());
                    final boolean rectangleIzqLimitInf = rectanglejugador1.Rectangle.getLayoutY() > limits.getMaxY()- rectanglejugador1.alturarectangle;
                    final boolean rectangleDretLimitInf = rectanglejugador2.Rectangle.getLayoutY() > limits.getMaxY()- rectanglejugador1.alturarectangle;
                    final boolean rectangleIzqLimitSup = rectanglejugador1.Rectangle.getLayoutY() <= limits.getMinY()-10;
                    final boolean rectangleDretLimitSup = rectanglejugador2.Rectangle.getLayoutY() <= limits.getMinY()-10;

                    /* Aumentamos la velocidad del balon cada 5 golpes */
                    if (cercle.getBoundsInParent().intersects(rectanglejugador1.Rectangle.getBoundsInParent()) || cercle.getBoundsInParent().intersects(rectanglejugador2.Rectangle.getBoundsInParent())) {
                        if (rebotaBalon == 5) {
                            deltaX += 1 * Math.signum(deltaX);
                            rebotaBalon = 0;
                        } else {
                            rebotaBalon++;
                        }
                        deltaX *= -1;
                    }
                    if (alLimitInferior || alLimitSuperior) {
                        // Delta aleatori
                        // Multiplicam pel signe de deltaX per mantenir la trajectoria
                        deltaY *= -1;
                    }
                    if (rectangleIzqLimitInf) {
                        rectanglejugador1.mouAmunt();
                    }
                    if (rectangleDretLimitInf) {
                        rectanglejugador2.mouAmunt();
                    }
                    if (rectangleIzqLimitSup) {
                        rectanglejugador1.mouAbaix();
                    }
                    if (rectangleDretLimitSup) {
                        rectanglejugador2.mouAbaix();
                    }
                    if (alLimitDret) {
                        deltaX *= -1;
                        cercle.relocate(600, 400);
                        contadorJugIzq++;
                        textoContador.setText(contadorJugIzq + "-" + contadorJugDret);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (alLimitEsquerra) {
                        deltaX *= -1;
                        cercle.relocate(600, 400);
                        contadorJugDret++;
                        textoContador.setText(contadorJugIzq + "-" + contadorJugDret);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }else {
                        GameOver = new Text("Game Over");
                        GameOver.setFont(new Font(20));
                        GameOver.relocate(400, 200);
                        GameOver.setFill(Color.WHITE);
                        pong.getChildren().add(GameOver);
                    }
                }
        }));

        loop.setCycleCount(Timeline.INDEFINITE);
        pong.requestFocus();
        pong.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.SPACE)){
                moverRectangulos();
                loop.play();
                pong.getChildren().remove(pulsarTeclaSpace);
            }
        });

    }

    private void juego() {
        /* Creamos la Bola y la asignamos al centro del panel*/
        int radi=8;
        cercle = new Circle(radi, Color.WHITE);
        cercle.relocate(400, 300);
        /*Creamos las rectangulos de los jugadore*/
        rectanglejugador1 = new RectangleJugador(pong, 2, 250);
        rectanglejugador2 = new RectangleJugador(pong,780,250);
        /*Creamos el texto que llevará el marcador del partido*/
        textoContador=new Text(contadorJugIzq + "-" + contadorJugDret);
        textoContador.setFont(new Font(20));
        textoContador.relocate(350, 10);
        textoContador.setFill(Color.WHITE);
        /*Creamos el texto que indique que pulse la tecla SPACE para empezar*/
        pulsarTeclaSpace=new Text("Pulsa espacio para comenzar");
        pulsarTeclaSpace.setFont(new Font("Times New Roman",20));
        pulsarTeclaSpace.relocate(350, 200);
        pulsarTeclaSpace.setFill(Color.WHITE);
        /*Seleccionamos panel como foco de la escena*/
        pong.getChildren().addAll(cercle);
        pong.getChildren().addAll(textoContador);
        pong.getChildren().addAll(pulsarTeclaSpace);
        pong.getChildren().addAll(rectanglejugador1);
        pong.getChildren().addAll(rectanglejugador2);
    }

    private void moverRectangulos(){
        /*Asignamos las letras a los movimientos de los rectangulos*/
        pong.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W -> rectanglejugador1.mouAmunt();
                case S -> rectanglejugador1.mouAbaix();
                case UP -> rectanglejugador2.mouAmunt();
                case DOWN -> rectanglejugador2.mouAbaix();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    }


