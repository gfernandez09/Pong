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

public class Pong extends Application {

    public static Circle cercle;
    public static Pane canvas1;
    public static RectangleIzq rectangleIzq;
    public static RectangleDret rectangleDret;



    @Override
    public void start(final Stage primaryStage) throws Exception{

        canvas1 = new Pane();
        final Scene escena = new Scene(canvas1, 400, 400);

        primaryStage.setTitle("Pong Game");
        primaryStage.setScene(escena);
        primaryStage.show();
        canvas1.requestFocus();
        rectangleIzq = new RectangleIzq(canvas1,20,50,Color.BLACK);
        rectangleIzq.Rectangle.setLayoutX(0);
        rectangleIzq.Rectangle.setLayoutY(0);
        rectangleDret = new RectangleDret(canvas1,20,50,Color.RED);
        rectangleDret.Rectangle.setLayoutX(380);
        rectangleDret.Rectangle.setLayoutY(0);
        canvas1.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W: rectangleIzq.mouAmunt(); break;
                case S: rectangleIzq.mouAbaix(); break;
                case UP: rectangleDret.mouAmunt(); break;
                case DOWN: rectangleDret.mouAbaix(); break;
            }
        });

        int radi=15;
        cercle = new Circle(radi, Color.BLUE);
        cercle.relocate(200-radi, 200-radi);

        canvas1.getChildren().addAll(cercle);
        //canvas1.getChildren().addAll(rectangleIzq.Rectangle);
        //canvas1.getChildren().addAll(rectangleDret.Rectangle);


        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {


            // Formula en radians
            //double deltaX = 3*Math.cos(Math.PI/3);
            //double deltaY = 3*Math.sin(Math.PI/3);

            // Formula en graus
            final double angle_en_radians = Math.toRadians(30);
            final int velocitat = 1;
            double deltaX = velocitat * Math.cos(angle_en_radians);
            double deltaY = velocitat * Math.sin(angle_en_radians);

            // Simulació gravitatòria
            final double temps = 0;
            final Bounds limits = canvas1.getBoundsInLocal();

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
                final boolean rectangleIzqLimitInf= rectangleIzq.Rectangle.getLayoutY()> limits.getMaxY()-50;
                final boolean rectangleDretLimitInf= rectangleDret.Rectangle.getLayoutY()> limits.getMaxY()-50;
                final boolean rectangleIzqLimitSup=rectangleIzq.Rectangle.getLayoutY()<=limits.getMinY()-10;
                final boolean rectangleDretLimitSup=rectangleDret.Rectangle.getLayoutY()<=limits.getMinY()-10;



                if(cercle.getBoundsInParent().intersects(rectangleIzq.Rectangle.getBoundsInParent())) {
                    deltaX *= -1;
                }

                if(cercle.getBoundsInParent().intersects(rectangleDret.Rectangle.getBoundsInParent())) {
                    deltaX *= -1;
                }
                if (alLimitDret || alLimitEsquerra) {
                    // Delta aleatoriaA
                    // Multiplicam pel signe de deltaX per mantenir la trajectoria
                    cercle.relocate(200, 200);
                }
                if (alLimitInferior || alLimitSuperior) {
                    // Delta aleatori
                    // Multiplicam pel signe de deltaX per mantenir la trajectoria
                    deltaY *= -1;
                }
                if(rectangleIzqLimitInf){
                    rectangleIzq.mouAmunt();
                }
                if (rectangleDretLimitInf){
                    rectangleDret.mouAmunt();
                }
                if (rectangleIzqLimitSup){
                    rectangleIzq.mouAbaix();
                }
                if (rectangleDretLimitSup){
                    rectangleDret.mouAbaix();
                }
            }
        }));

        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
    }