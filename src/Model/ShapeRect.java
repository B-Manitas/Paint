package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class ShapeRect extends Shape implements IShape {

    private ShapeTypes type = ShapeTypes.RECTANGLE;
    private double offset = Constant.DEFAULT_OFFSET;
    private double zoomFactor = Constant.DEFAULT_ZOOM;

    private Coord pStart, pEnd;
    private int bdSize;
    private Color color;

    public ShapeRect(Coord pStart, int size) {
        /**
         * Crée une forme de type rectangle.
         *
         * @param start Coordonnées du point de départ
         * @param size  Taille de la bordure
         */
        this.pStart = pStart;
        this.bdSize = size;
    }

    private ShapeRect(Coord pStart, Coord pEnd, int bdSize, Color color) {
        /**
         * Crée une forme de type rectangle.
         *
         * @param pStart Coordonnées du point de départ
         * @param pEnd   Coordonnées du point de fin
         * @param bdSize Taille de la bordure
         * @param color  Couleur de la bordure
         */
        this.pStart = pStart;
        this.pEnd = pEnd;
        this.bdSize = bdSize;
        this.color = color;
    }

    public void setColor(Color color) {
        /**
         * Initialise la couleur de la bordure.
         * 
         * @param color Couleur de la bordure
         */
        this.color = color;
    }

    public void setBdSize(int size) {
        /**
         * Initialise la taille de la bordure.
         * 
         * @param size Taille de la bordure
         */
        this.bdSize = size;
    }

    public void setPStart(Coord pStart) {
        /**
         * Initialise les coordonnées du point de départ.
         * 
         * @param pStart Coordonnées du point de départ
         */
        this.pStart = pStart;
    }

    public void setPEnd(Coord pEnd) {
        /**
         * Initialise les coordonnées du point de fin.
         * 
         * @param pEnd Coordonnées du point de fin
         */
        this.pEnd = pEnd;
    }

    public Coord getPStart() {
        /**
         * Retourne les coordonnées du point de départ.
         * 
         * @return Les coordonnées du point de départ
         */
        return this.pStart;
    }

    public Coord getPEnd() {
        /**
         * Retourne les coordonnées du point de fin.
         * 
         * @return Les coordonnées du point de fin
         */
        return this.pEnd;
    }

    public int getBdSize() {
        /**
         * Retourne la taille de la bordure.
         * 
         * @return La taille de la bordure
         */
        return this.bdSize;
    }

    public Color getColor() {
        /**
         * Retourne la couleur de la bordure.
         * 
         * @return La couleur de la bordure
         */
        return this.color;
    }

    public Coord[] getArea() {
        /**
         * Retourne les coordonnées des points de la zone de la forme.
         * 
         * @return Les coordonnées des points de la zone de la forme
         */
        Coord[] coords = new Coord[2];

        coords[0] = pStart.translate(-offset);
        coords[1] = pEnd.translate(offset);

        return coords;
    }

    public boolean isShape(ShapeTypes type) {
        /**
         * Vérifie si la forme est de type type.
         * 
         * @param type Type de la forme
         * @return Vrai si la forme est de type type, faux sinon
         */
        return this.type == type;
    }

    public boolean isInArea(Coord coord) {
        /**
         * Vérifie si les coordonnées coord sont dans la zone de la forme.
         * 
         * @param coord Coordonnées à vérifier
         * @return Vrai si les coordonnées coord sont dans la zone de la forme, faux
         *         sinon
         */
        double xMin = Math.min(pStart.x, pEnd.x) - offset;
        double xMax = Math.max(pStart.x, pEnd.x) + offset;
        double yMin = Math.min(pStart.y, pEnd.y) - offset;
        double yMax = Math.max(pStart.y, pEnd.y) + offset;

        return coord.x >= xMin && coord.x <= xMax && coord.y >= yMin && coord.y <= yMax;
    }

    public IShape copy() {
        /**
         * Copie la forme.
         * 
         * @return La copie de la forme
         */
        return new ShapeRect(pStart, pEnd, bdSize, color);
    }

    public void moveTo(Coord coord) {
        /**
         * Déplace la forme aux coordonnées coord.
         * 
         * @param coord Coordonnées de destination
         */
        double dx = coord.x - ((pStart.x + pEnd.x) / 2.0);
        double dy = coord.y - ((pStart.y + pEnd.y) / 2.0);

        pStart.moveTo(dx, dy);
        pEnd.moveTo(dx, dy);
    }

    public void draw(GraphicsContext gc) {
        /**
         * Dessine la forme.
         * 
         * @param gc Contexte graphique
         */
        double width = Math.abs(pStart.x - pEnd.x);
        double height = Math.abs(pStart.y - pEnd.y);
        double x = Math.min(pStart.x, pEnd.x);
        double y = Math.min(pStart.y, pEnd.y);

        gc.setStroke(color);
        gc.setLineWidth(bdSize * Math.exp(zoomFactor - 1));
        gc.setFill(color);
        gc.fillRect(x, y, width, height);
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
        pStart.centerZoom(this.zoomFactor);
        pEnd.centerZoom(this.zoomFactor);
    }

    public void finish() {
    }
}
