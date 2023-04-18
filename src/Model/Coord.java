package Model;

import javafx.scene.input.MouseEvent;

public class Coord {
    // Déclare les attributs
    public double x, y;
    private double xInit, yInit;
    public static double centerX, centerY = 0;

    public Coord(double posX, double posY) {
        /**
         * Constructeur de la classe Coord
         * 
         * @param posX : position en x
         * @param posY : position en y
         */
        x = posX;
        y = posY;
        xInit = x;
        yInit = y;
    }

    public Coord() {
        /**
         * Constructeur de la classe Coord
         */
        x = 0;
        y = 0;
        xInit = 0;
        yInit = 0;
    }

    public void setInit(Coord c) {
        /**
         * Initialise les coordonnées
         * 
         * @param c : coordonnée
         */
        this.xInit = c.x;
        this.yInit = c.y;
    }

    public static void setCenter(double centerX, double centerY) {
        /**
         * Initialise le centre
         * 
         * @param centerX : centre en x
         * @param centerY : centre en y
         */
        Coord.centerX = centerX;
        Coord.centerY = centerY;
    }

    public static Coord getCoordMouse(MouseEvent e) {
        /**
         * Retourne la position de la souris
         * 
         * @param e : MouseEvent
         * @return Coord : position de la souris
         */
        double posX = e.getX();
        double posY = e.getY();

        return new Coord(posX, posY);
    }

    public Coord copy() {
        /**
         * Retourne une copie de l'objet
         * 
         * @return Coord : copie de l'objet
         */
        return new Coord(x, y);
    }

    public static Coord getCoordMouse(MouseEvent e, int penSize) {
        /**
         * Retourne la position de la souris
         * 
         * @param e : MouseEvent
         * @return Coord : position de la souris
         */
        double posX = e.getX() - penSize / 2;
        double posY = e.getY() - penSize / 2;

        return new Coord(posX, posY);
    }

    public void printCoord() {
        /**
         * Affiche les coordonnées
         */
        System.out.println("(" + this.x + ", " + this.y + ")");
    }

    public double distance(Coord c) {
        /**
         * Retourne la distance entre deux coordonnées
         * 
         * @param c : coordonnée
         * @return double : distance entre les deux coordonnées
         */
        return Math.sqrt(Math.pow(this.x - c.x, 2) + Math.pow(this.y - c.y, 2));
    }

    public void moveTo(double translateX, double translateY) {
        /**
         * Déplace les coordonnées
         * 
         * @param translateX : déplacement en x
         * @param translateY : déplacement en y
         */
        this.x += translateX;
        this.y += translateY;

        this.xInit += translateX;
        this.yInit += translateY;
    }

    public Coord translate(double translate) {
        /**
         * Retourne une coordonnée déplacée
         * 
         * @param translate : déplacement
         * @return Coord : coordonnée déplacée
         */
        return new Coord(x + translate, y + translate);
    }

    public Coord opposite(Coord c) {
        /**
         * Retourne une coordonnée opposée
         * 
         * @param c : coordonnée
         * @return Coord : coordonnée opposée
         */
        return new Coord(c.x - distance(c), c.y);
    }

    public void centerZoom(double factor) {
        /**
         * Centre la coordonnée lors du zoom
         * 
         * @param factor : facteur de zoom
         * @return Coord : coordonnée centrée
         */
        double deltaCenterX = (this.xInit - centerX) * (1 - factor);
        double deltaCenterY = (this.yInit - centerY) * (1 - factor);

        this.x = this.xInit - deltaCenterX;
        this.y = this.yInit - deltaCenterY;
    }

    @Override
    public boolean equals(Object o) {
        /**
         * Retourne si deux coordonnées sont égales
         * 
         * @param o : coordonnée
         * @return boolean : true si les coordonnées sont égales, false sinon
         */
        if (o instanceof Coord)
            return (((Coord) o).x == this.x && ((Coord) o).y == this.y);
        else
            return false;
    }

}
