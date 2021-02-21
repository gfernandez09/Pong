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
import javafx.scene.image.Image;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.Random;

public class Pong_Gustavo_Mulet extends Application {
    /*Variables*/
    public static Circle cercle;
    public static Pane pong;
    public static Text pulsarTeclaSpace;
    public static Text textoContadorJug1;
    public static Text textoContadorJug2;
    public static Text GameOver;
    public static Text textoGanador;
    public static RectangleJugador rectanglejugador1;
    public static RectangleJugador rectanglejugador2;
    public static int contadorJugIzq = 0;
    public static int contadorJugDret = 0;
    public int alturapanel = 600;
    public int ampladapanel = 800;

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
        int velocitat=20;
        int ampladarectangle = 10;
        int alturarectangle = 50;
        Pane panell;
        Node Rectangle;
        /*Constructor para crear y situar los rectángulos en el panel*/
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
        /*Métodos de mover los rectángulos*/
        public void mouAmunt() {
            posicio.posY=posicio.posY-this.velocitat;
            this.repinta();
        }
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
        final Scene escena = new Scene(pong, ampladapanel, alturapanel);
        /*Añadimos los objetos de la Aplicación*/
        primaryStage.getIcons().add(new Image("https://silverballmuseum.com/wp-content/uploads/2016/05/pong-arcade-game-atari.jpg"));
        primaryStage.setTitle("Pong Game");
        primaryStage.setScene(escena);
        primaryStage.show();
        juego();
        pong.setStyle("-fx-background-color: black");
        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

            /*Formula en graus*/
            final double angle_en_radians = Math.toRadians(30);
            final int velocitat = 2;
            double deltaX = velocitat * Math.cos(angle_en_radians);
            double deltaY = velocitat * Math.sin(angle_en_radians);

            /*Simulació gravitatòria*/
            final Bounds limits = pong.getBoundsInLocal();
            @Override
            public void handle(final ActionEvent t) {
                /*Creamos los contadores para los rectangulos de los jugadores*/
                    if (contadorJugIzq < 15 && contadorJugDret < 15) {
                    cercle.setLayoutX(cercle.getLayoutX() + deltaX);
                    cercle.setLayoutY(cercle.getLayoutY() + deltaY);

                    /*Situamos los límites de nuestro programa*/
                    final boolean alLimitDret = cercle.getLayoutX() >= (limits.getMaxX() - cercle.getRadius());
                    final boolean alLimitEsquerra = cercle.getLayoutX() <= (limits.getMinX() + cercle.getRadius());
                    final boolean alLimitInferior = cercle.getLayoutY() >= (limits.getMaxY() - cercle.getRadius());
                    final boolean alLimitSuperior = cercle.getLayoutY() <= (limits.getMinY() + cercle.getRadius());
                    final boolean rectanglejugd1LimitInf = rectanglejugador1.Rectangle.getLayoutY() > limits.getMaxY()- rectanglejugador1.alturarectangle;
                    final boolean rectanglejugd2LimitInf = rectanglejugador2.Rectangle.getLayoutY() > limits.getMaxY()- rectanglejugador1.alturarectangle;
                    final boolean rectanglejugd1LimitSup = rectanglejugador1.Rectangle.getLayoutY() <= limits.getMinY()-10;
                    final boolean rectanglejugd2LimitSup = rectanglejugador2.Rectangle.getLayoutY() <= limits.getMinY()-10;

                    /* Condicional para hacer rebotar la bola cuando choquen con los rectangulos */
                    if (cercle.getBoundsInParent().intersects(rectanglejugador1.Rectangle.getBoundsInParent()) || cercle.getBoundsInParent().intersects(rectanglejugador2.Rectangle.getBoundsInParent())) {
                        deltaX *= -1;
                    }
                    if (alLimitInferior || alLimitSuperior) {
                        // Delta aleatori
                        // Multiplicam pel signe de deltaX per mantenir la trajectoria
                        deltaY *= -1;
                    }
                    /*
                    Limites para los rectangulos que al llegar a dicho límite, solo se permitirá hacer el movimiento
                    contrario al jugador.
                    */
                    if (rectanglejugd1LimitInf) {
                        rectanglejugador1.mouAmunt();
                    }
                    if (rectanglejugd2LimitInf) {
                        rectanglejugador2.mouAmunt();
                    }
                    if (rectanglejugd1LimitSup) {
                        rectanglejugador1.mouAbaix();
                    }
                    if (rectanglejugd2LimitSup) {
                        rectanglejugador2.mouAbaix();
                    }
                    /*
                    Condicionales cuando un jugador u otro meta un gol. Primero, recolocamos la pelota en el centro del panel
                    Acto seguido, aumentamos el valor de la variable del jugador correspondiente que haya metido gol
                    y actualizamos el marcador. También, "dormimos" el programa 200 milisegundos para que los jugadores
                    puedan tener la oportunidad de recolocarse.
                    */
                    if (alLimitDret) {
                        deltaX *= +1;
                        Random random = new Random();
                        int posicioRandom = random.nextInt(600);
                        cercle.relocate((alturapanel/2), posicioRandom);
                        contadorJugIzq++;
                        textoContadorJug1.setText(contadorJugIzq + "");
                        textoContadorJug2.setText(contadorJugDret + "");
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                    }
                    if (alLimitEsquerra) {
                        deltaX *= +1;
                        Random random = new Random();
                        int posicioRandom2 = random.nextInt(600);
                        cercle.relocate((alturapanel/2), posicioRandom2);
                        contadorJugDret++;
                        textoContadorJug1.setText(contadorJugIzq + "");
                        textoContadorJug2.setText(contadorJugDret + "");
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                    }
                }else {
                        /*
                        Al acabar la partida creamos un nuevo panel con un GAME OVER y el jugador que haya ganado
                        Generamos los textos de Game Over y el ganador de la partida, los situamos en el panel y asignamos
                        un color blanco para que el texto pueda ser leído por el jugador.
                        */
                    pong = new Pane();
                    final Scene escena = new Scene(pong, ampladapanel, alturapanel);
                    primaryStage.setTitle("Pong Game");
                    primaryStage.setScene(escena);
                    if(contadorJugIzq == 15){
                        pong.setStyle("-fx-background-color: black");
                        GameOver = new Text("Game Over");
                        GameOver.setFont(new Font(35));
                        GameOver.relocate((ampladapanel/2.5), (alturapanel/2));
                        GameOver.setFill(Color.WHITE);

                        textoGanador = new Text("El ganador ha sido el jugador 1!");
                        textoGanador.setFont(new Font(20));
                        textoGanador.relocate((ampladapanel/2.5), (alturapanel/1.50));
                        textoGanador.setFill(Color.WHITE);

                        pong.getChildren().add(GameOver);
                        pong.getChildren().add(textoGanador);
                    }else if(contadorJugDret == 15){
                        pong.setStyle("-fx-background-color: black");
                        GameOver = new Text("Game Over");
                        GameOver.setFont(new Font(35));
                        GameOver.relocate((ampladapanel/2), (alturapanel/2));
                        GameOver.setFill(Color.WHITE);

                        textoGanador = new Text("El ganador ha sido el jugador 2!");
                        textoGanador.setFont(new Font(20));
                        textoGanador.relocate(ampladapanel/2.5, alturapanel/1.5);
                        textoGanador.setFill(Color.WHITE);

                        pong.getChildren().add(GameOver);
                        pong.getChildren().add(textoGanador);
                        }
                    }
                }
        }));
        loop.setCycleCount(Timeline.INDEFINITE);
        pong.requestFocus();
        /*Al pulsar la tecla SPACE, se iniciará el juego*/
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
        cercle.relocate(ampladapanel/2, alturapanel/2);
        /*Creamos las rectangulos de los jugadore*/
        rectanglejugador1 = new RectangleJugador(pong, ((ampladapanel * 2) / 100), (alturapanel / 2));
        rectanglejugador2 = new RectangleJugador(pong, (int) (ampladapanel*1.95/2), (alturapanel / 2));
        /*Creamos el texto que llevará el marcador del partido*/
        textoContadorJug1=new Text(contadorJugIzq + "");
        textoContadorJug1.setFont(new Font(50));
        textoContadorJug1.relocate(ampladapanel*2/100, 20);
        textoContadorJug1.setFill(Color.WHITE);
        textoContadorJug2=new Text(contadorJugDret + "");
        textoContadorJug2.setFont(new Font(50));
        textoContadorJug2.relocate(ampladapanel*1.85/2, 20);
        textoContadorJug2.setFill(Color.WHITE);
        /*Creamos el texto que indique que pulse la tecla SPACE para empezar*/
        pulsarTeclaSpace=new Text("Pitja la tecla espai per començar");
        pulsarTeclaSpace.setFont(new Font("Times New Roman",20));
        pulsarTeclaSpace.relocate(ampladapanel/2.5, alturapanel/1.5);
        pulsarTeclaSpace.setFill(Color.WHITE);
        /*Asignamos los children para que aparezcan en el panel*/
        pong.getChildren().addAll(cercle);
        pong.getChildren().addAll(textoContadorJug1);
        pong.getChildren().addAll(textoContadorJug2);
        pong.getChildren().addAll(pulsarTeclaSpace);
        pong.getChildren().addAll(rectanglejugador1);
        pong.getChildren().addAll(rectanglejugador2);

    }
    private void moverRectangulos(){
        /*Asignamos las teclas a los movimientos de los rectangulos*/
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