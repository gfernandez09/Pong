package Pong;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class Pong extends Application {

    public static Circle cercle;
    public static Pane pong;
    public static Text rectanguloIzqContador;
    public static Text rectanguloDretContador;
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
        Pane panell;
        Node Rectangle;

        public RectangleJugador(Pane panell,int posX,int posY) {
            posicio = new Posicio(posX,posY);
            this.panell = panell;
            this.Rectangle = new javafx.scene.shape.Rectangle(20,90,Color.RED);
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
        pong.requestFocus();
        pong.setStyle("-fx-background-color: black");
        /* Creamos la Bola y la asignamos al centro del panel*/
        int radi=15;
        cercle = new Circle(radi, Color.BLUE);
        cercle.relocate(200-radi, 200-radi);
        /*Creamos las rectangulos de los jugadore*/
        rectanglejugador1 = new RectangleJugador(pong, 2, 2);
        rectanglejugador2 = new RectangleJugador(pong,780,2);
        /*Seleccionamos panel como foco de la escena*/
        pong.getChildren().addAll(cercle);
        pong.getChildren().addAll(rectanglejugador1);
        pong.getChildren().addAll(rectanglejugador2);
        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {


            // Formula en radians
            //double deltaX = 3*Math.cos(Math.PI/3);
            //double deltaY = 3*Math.sin(Math.PI/3);

            // Formula en graus
            final double angle_en_radians = Math.toRadians(30);
            final int velocitat = 2;
            double deltaX = velocitat * Math.cos(angle_en_radians);
            double deltaY = velocitat * Math.sin(angle_en_radians);

            // Simulació gravitatòria
            final double temps = 0;
            final Bounds limits = pong.getBoundsInLocal();

            @Override
            public void handle(final ActionEvent t) {
                //cercle.setLayoutX(cercle.getLayoutX() + deltaX/2);

                cercle.setLayoutX(cercle.getLayoutX() + deltaX);
                //cercle.setLayoutY(cercle.getLayoutY() + deltaY/3);
                cercle.setLayoutY(cercle.getLayoutY() + deltaY);
                //System.out.println(cercle.getLayoutX()+":"+cercle.getLayoutY());


                final boolean alLimitDret = cercle.getLayoutX() >= (limits.getMaxX() - cercle.getRadius());
                final boolean alLimitEsquerra = cercle.getLayoutX() <= (limits.getMinX() + cercle.getRadius());
                final boolean alLimitInferior = cercle.getLayoutY() >= (limits.getMaxY() - cercle.getRadius());
                final boolean alLimitSuperior = cercle.getLayoutY() <= (limits.getMinY() + cercle.getRadius());
                final boolean rectangleIzqLimitInf= rectanglejugador1.Rectangle.getLayoutY()> limits.getMaxY()-80;
                final boolean rectangleDretLimitInf= rectanglejugador2.Rectangle.getLayoutY()> limits.getMaxY()-80;
                final boolean rectangleIzqLimitSup=rectanglejugador1.Rectangle.getLayoutY()<=limits.getMinY();
                final boolean rectangleDretLimitSup=rectanglejugador2.Rectangle.getLayoutY()<=limits.getMinY();
                if(cercle.getBoundsInParent().intersects(rectanglejugador1.Rectangle.getBoundsInParent())) {
                    deltaX *= -1;
                }

                if(cercle.getBoundsInParent().intersects(rectanglejugador2.Rectangle.getBoundsInParent())) {
                    deltaX *= -1;
                }
                if (alLimitDret || alLimitEsquerra) {
                    // Delta aleatoriaA
                    // Multiplicam pel signe de deltaX per mantenir la trajectoria
                    cercle.relocate(200, 200);
                    deltaY *= -1;
                }
                if (alLimitInferior || alLimitSuperior) {
                    // Delta aleatori
                    // Multiplicam pel signe de deltaX per mantenir la trajectoria
                    deltaY *= -1;
                }
                if(rectangleIzqLimitInf){
                    rectanglejugador1.mouAmunt();
                }
                if (rectangleDretLimitInf){
                    rectanglejugador2.mouAmunt();
                }
                if (rectangleIzqLimitSup){
                    rectanglejugador1.mouAbaix();
                }
                if (rectangleDretLimitSup){
                    rectanglejugador2.mouAbaix();
                }
            }
        }));

        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
        pong.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W -> rectanglejugador1.mouAmunt();
                case S -> rectanglejugador1.mouAbaix();
                case UP -> rectanglejugador2.mouAmunt();
                case DOWN -> rectanglejugador2.mouAbaix();
            }
        });
    }

    private void resetPong() {
        if (contadorJugIzq==15||contadorJugDret==15){
            contadorJugIzq=0;
            contadorJugDret=0;
            rectanguloIzqContador.setText(contadorJugIzq+"");
            rectanguloDretContador.setText(contadorJugDret+"");
        }else {
            return;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    }


