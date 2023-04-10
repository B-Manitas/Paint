package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShapeCircle implements IShape {

    private ShapeTypes type = ShapeTypes.CIRCLE;
    private double zoomFactor = Constant.DEFAULT_ZOOM;

    private Coord pCenter, pEnd;
    private Color bdColor;
    private int bdSize;

    public ShapeCircle(Coord center, int size) {
        /**
         * Constructeur de la classe ShapeCircle
         * 
         * @param c        : coordonnée
         * @param toolSize : taille de l'outil
         * 
         */
        this.bdSize = size;
        this.pCenter = center;
    }

    private ShapeCircle(Coord center, Coord end, int toolSize, Color toolColor) {
        /**
         * Constructeur de la classe ShapeCircle
         * 
         * @param center    : coordonnée du centre
         * @param end       : coordonnée de fin
         * @param toolSize  : taille de l'outil
         * @param toolColor : couleur de l'outil
         */
        this.pCenter = center;
        this.pEnd = end;
        this.bdSize = toolSize;
        this.bdColor = toolColor;
    }

    public void setColor(Color color) {
        /**
         * Initialise la couleur de la bordure
         * 
         * @param color : couleur de la bordure
         */
        this.bdColor = color;
    }

    public void setBdSize(int size) {
        /**
         * Initialise la taille de la bordure
         * 
         * @param size : taille de la bordure
         */
        this.bdSize = size;
    }

    public void setPStart(Coord pStart) {
        /**
         * Initialise les coordonnées de la forme
         * 
         * @param pStart : coordonnée
         */
        pCenter = pStart;
    }

    public void setPEnd(Coord pEnd) {
        /**
         * Initialise la coordonnée de fin
         * 
         * @param pEnd : coordonnée de fin
         */
        this.pEnd = pEnd;
    }

    public int getBdSize() {
        /**
         * Retourne la taille de la bordure
         * 
         * @return int : taille de la bordure
         */
        return this.bdSize;
    }

    public Color getColor() {
        /**
         * Retourne la couleur de la bordure
         * 
         * @return Color : couleur de la bordure
         */
        return this.bdColor;
    }

    public Coord getPStart() {
        return pCenter;
    }

    public Coord getPEnd() {
        return pEnd;
    }

    public double getRadius() {
        /**
         * Retourne le rayon du cercle
         * 
         * @return double : rayon du cercle
         */
        return pCenter.distance(pEnd);
    }

    public Coord[] getArea() {
        /**
         * Retourne les coordonnées de la zone de sélection de la forme.
         * 
         * @return Coord[] : tableau de coordonnées
         */
        Coord[] coords = new Coord[2];

        coords[0] = new Coord(pCenter.x - getRadius(), pCenter.y - getRadius());
        coords[1] = new Coord(pCenter.x + getRadius(), pCenter.y + getRadius());

        return coords;
    }

    public boolean isShape(ShapeTypes type) {
        /**
         * Retourne vrai si la forme est de type type
         * 
         * @param type : type de la forme
         * @return boolean : vrai si la forme est de type type
         */
        return this.type == type;
    }

    public boolean isInArea(Coord coord) {
        /**
         * Retourne vrai si la coordonnée est dans la forme
         * 
         * @param coord : coordonnée
         * @return boolean : vrai si la coordonnée est dans la forme
         */
        return pCenter.distance(coord) <= 1.5 * getRadius();
    }

    public IShape copy() {
        /**
         * Retourne une copie de la forme
         * 
         * @return IShape : copie de la forme
         */
        return new ShapeCircle(pCenter, pEnd, bdSize, bdColor);
    }

    public void moveTo(Coord coord) {
        /**
         * Déplace la forme
         * 
         * @param mouse : coordonnée de la souris
         */
        this.pCenter = coord;
    }

    public void draw(GraphicsContext gc) {
        /**
         * Dessine la forme
         * 
         * @param gc : GraphicsContext
         */
        gc.setLineWidth(bdSize * zoomFactor);
        gc.setStroke(bdColor);
        gc.setFill(bdColor);
        gc.fillOval(pCenter.x - getRadius(), pCenter.y - getRadius(), 2 * getRadius(), 2 * getRadius());
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
         * Recalcule les coordonnées de la forme après un zoom
         */
        pCenter.centerZoom(this.zoomFactor);
        pEnd.centerZoom(this.zoomFactor);
    }

    public void finish() {
        /**
         * Fin de la forme
         */
    }
}
