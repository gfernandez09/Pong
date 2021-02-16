package Pong;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

class RectangleIzq {
    static class Posicio {
        int posX;
        int posY;
        public Posicio(int x,int y) {
            this.posX=x;
            this.posY=y;
        }
    }
    RectangleIzq.Posicio posicio;
    int velocitat=10;
    Pane panell;
    Node Rectangle;

    public RectangleIzq(Pane panell,int posX,int posY, Color color) {
        posicio = new Posicio(posX, posY);
        this.panell = panell;
        this.Rectangle = new Rectangle(posicio.posX,posicio.posY,color);
        posicio.posX = 0;
        posicio.posY = 0;
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
        this.Rectangle.setLayoutX(posicio.posX);
        this.Rectangle.setLayoutY(posicio.posY);
    }
}