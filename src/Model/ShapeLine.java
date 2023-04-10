package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShapeLine implements IShape {

    private ShapeTypes type = ShapeTypes.LINE;
    private double zoomFactor = Constant.DEFAULT_ZOOM;

    private int bdSize;
    private Color color;
    private Coord pStart, pEnd;

    public ShapeLine(Coord pStart, int size, Color color) {
        /**
         * Constructeur de la classe ShapeLine
         * 
         * @param posStart  : coordonnée de départ
         * @param toolSize  : taille de l'outil
         * @param toolColor : couleur de l'outil
         */
        this.bdSize = size;
        this.color = color;
        this.pStart = pStart;
    }

    private ShapeLine(Coord pStart, Coord pEnd, int size, Color color) {
        /**
         * Constructeur de la classe ShapeLine
         * 
         * @param pStart : coordonnée de départ
         * @param pEnd   : coordonnée de fin
         * @param size   : taille de la bordure
         * @param color  : couleur de la bordure
         */
        this.pStart = pStart;
        this.pEnd = pEnd;
        this.bdSize = size;
        this.color = color;
    }

    public void setColor(Color color) {
        /**
         * Initialise la couleur de la bordure
         * 
         * @param color : couleur de la bordure
         */
        this.color = color;
    }

    public void setBdSize(int size) {
        /**
         * Initialise la taille de la bordure
         * 
         * @param size : taille de la bordure
         */
        this.bdSize = size;
    }

    public void setPEnd(Coord end) {
        /**
         * Initialise la coordonnée de fin
         * 
         * @param end : coordonnée de fin
         */
        this.pEnd = end;
    }

    public void setPStart(Coord pStart) {
        /**
         * Initialise la coordonnée de départ
         * 
         * @param pStart : coordonnée de départ
         */
        this.pStart = pStart;
    }

    public Coord getPStart() {
        /**
         * Retourne la coordonnée de départ
         * 
         * @return pStart : coordonnée de départ
         */
        return this.pStart;
    }

    public Coord getPEnd() {
        /**
         * Retourne la coordonnée de fin
         * 
         * @return pEnd : coordonnée de fin
         */
        return this.pEnd;
    }

    public Color getColor() {
        /**
         * Retourne la couleur de la bordure
         * 
         * @return color : couleur de la bordure
         */
        return this.color;
    }

    public int getBdSize() {
        /**
         * Retourne la taille de la bordure
         * 
         * @return bdSize : taille de la bordure
         */
        return this.bdSize;
    }

    public Coord[] getArea() {
        /**
         * Retourne les coordonnées de la zone de sélection
         * 
         * @return Coord[] : coordonnées de la zone de sélection
         */
        Coord[] selectedCoords = new Coord[2];

        selectedCoords[0] = pStart.translate(-bdSize / 2);
        selectedCoords[1] = pEnd.translate(bdSize / 2);

        return selectedCoords;
    }

    public boolean isShape(ShapeTypes type) {
        /**
         * Retourne vrai si le type de l'objet courant est égal au type passé en
         * paramètre
         * 
         * @param type : type de l'objet
         * @return boolean : vrai si le type de l'objet courant est égal au type passé
         *         en
         *         paramètre
         */
        return this.type == type;
    }

    public boolean isInArea(Coord coord) {
        /**
         * Retourne vrai si la coordonnée passée en paramètre est dans la zone de
         * sélection
         * 
         * @param coord : coordonnée
         * @return boolean : vrai si la coordonnée passée en paramètre est dans la zone
         *         de
         *         sélection
         */
        double d1 = pStart.distance(coord) + pEnd.distance(coord);
        double d2 = pStart.distance(pEnd);

        return Math.abs(d1 - d2) <= 2 * this.bdSize;
    }

    public IShape copy() {
        /**
         * Retourne une copie de l'objet courant
         * 
         * @return ShapeLine : copie de l'objet courant
         */
        return new ShapeLine(pStart.copy(), pEnd.copy(), bdSize, color);
    }

    public void moveTo(Coord coord) {
        /**
         * Déplace l'objet courant
         * 
         * @param coord : coordonnée de la souris
         */
        double dx = coord.x - ((pStart.x + pEnd.x) / 2.0);
        double dy = coord.y - ((pStart.y + pEnd.y) / 2.0);

        pStart.moveTo(dx, dy);
        pEnd.moveTo(dx, dy);
    }

    public void draw(GraphicsContext gc) {
        /**
         * Dessine l'objet courant
         * 
         * @param gc : contexte graphique
         */
        gc.setLineDashes(0);
        gc.setLineWidth(bdSize * Math.exp(zoomFactor - 1));
        gc.setStroke(color);
        gc.strokeLine(pStart.x, pStart.y, pEnd.x, pEnd.y);
    }

    public void zoomIn() {
        /**
         * Zoom sur l'objet courant
         */
        this.zoomFactor = Math.max(Constant.MIN_ZOOM, this.zoomFactor - Constant.ZOOM_STEP);
        zoom();
    }

    public void zoomOut() {
        /**
         * Dézoom sur l'objet courant
         */
        this.zoomFactor = Math.min(Constant.MAX_ZOOM, this.zoomFactor + Constant.ZOOM_STEP);
        zoom();
    }

    public void zoom() {
        /**
         * Applique un zoom sur les coordonnées de l'objet courant
         */
        pStart.centerZoom(this.zoomFactor);
        pEnd.centerZoom(this.zoomFactor);
    }

    public void finish() {
    }

}
