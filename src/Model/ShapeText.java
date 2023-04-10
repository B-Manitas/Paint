package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ShapeText implements IShape {
    private ShapeTypes type = ShapeTypes.TEXT;
    private double zoomFactor = Constant.DEFAULT_ZOOM;
    private double offset = Constant.DEFAULT_OFFSET;
    private Text text = new Text(Constant.DEFAULT_TEXT);

    private int bdSize;
    private Color color;
    private Coord pStart, pEnd;

    public ShapeText(Coord pStart, int size, Color color) {
        /**
         * Constructeur de la classe ShapeText
         * 
         * @param posStart  : coordonnée de départ
         * @param toolSize  : taille de l'outil
         * @param toolColor : couleur de l'outil
         */
        this.pStart = pStart;
        this.bdSize = size;
        this.color = color;
    }

    private ShapeText(Coord pStart, Coord pEnd, int size, Color color, String text) {
        /**
         * Constructeur de la classe ShapeText
         * 
         * @param pStart : coordonnée de départ
         * @param pEnd   : coordonnée de fin
         * @param size   : taille de la bordure
         * @param color  : couleur de la bordure
         * @param text   : texte à afficher
         */
        this.pStart = pStart;
        this.pEnd = pEnd;
        this.bdSize = size;
        this.color = color;
        this.text.setText(text);
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

    public void setPEnd(Coord pEnd) {
        /**
         * Initialise la coordonnée de fin
         * 
         * @param pEnd : coordonnée de fin
         */
        this.pStart = new Coord(pEnd.x - getHeight(), pEnd.y);
    }

    public void setPStart(Coord pStart) {
        /**
         * Initialise la coordonnée de départ
         * 
         * @param pStart : coordonnée de départ
         */
        this.pEnd = new Coord(pStart.x + getWidth(), pStart.y + getHeight());
    }

    public void setText() {
        /**
         * Modifie le texte de l'objet courant
         */
        TextInputDialog dialog = new TextInputDialog(text.getText());
        dialog.setTitle("Modifier le texte");
        dialog.setContentText("Veuillez entrer le texte :");
        dialog.showAndWait();
        this.text.setText(dialog.getResult());
    }

    public Color getColor() {
        /**
         * Retourne la couleur de la bordure
         * 
         * @return color : couleur de la bordure
         */
        return this.color;
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

    public int getBdSize() {
        /**
         * Retourne la taille de la bordure
         * 
         * @return bdSize : taille de la bordure
         */
        return this.bdSize;
    }

    public double getWidth() {
        /**
         * Retourne la largeur du texte
         * 
         * @return double : largeur du texte
         */
        return text.getLayoutBounds().getWidth() + bdSize;
    }

    public double getHeight() {
        /**
         * Retourne la hauteur du texte
         * 
         * @return double : hauteur du texte
         */
        return text.getLayoutBounds().getHeight();
    }

    public Coord[] getArea() {
        /**
         * Retourne les coordonnées de la zone de l'objet courant
         * 
         * @return Coord[] : coordonnées de la zone de l'objet courant
         */
        Coord[] coords = new Coord[2];

        coords[0] = pStart.translate(-2. * offset);
        coords[1] = pEnd.translate(2. * offset);

        return coords;
    }

    public IShape copy() {
        /**
         * Retourne une copie de l'objet courant
         * 
         * @return ShapeText : copie de l'objet courant
         */
        return new ShapeText(pStart.copy(), pEnd.copy(), bdSize, color, text.getText());
    }

    public boolean isShape(ShapeTypes type) {
        /**
         * Retourne vrai si le type de l'objet courant est égal au type passé en
         * paramètre
         * 
         * @param type : type de l'objet
         * @return boolean : vrai si le type de l'objet courant est égal au type passé
         *         en
         */
        return this.type == type;
    }

    public boolean isInArea(Coord coord) {
        /**
         * Retourne vrai si la coordonnée passée en paramètre est dans la zone de
         * l'objet
         * courant
         * 
         * @param coord : coordonnée
         * @return boolean : vrai si la coordonnée passée en paramètre est dans la zone
         *         de l'objet courant
         */
        double xMin = Math.min(pStart.x, pEnd.x) - offset;
        double xMax = Math.max(pStart.x, pEnd.x) + offset;
        double yMin = Math.min(pStart.y, pEnd.y) - offset;
        double yMax = Math.max(pStart.y, pEnd.y) + offset;

        return coord.x >= xMin && coord.x <= xMax && coord.y >= yMin && coord.y <= yMax;
    }

    public void draw(GraphicsContext gc) {
        /**
         * Dessine l'objet courant
         * 
         * @param gc : contexte graphique
         */
        gc.setFill(color);
        gc.setFont(new Font("Arial", bdSize * zoomFactor));
        gc.fillText(text.getText(), pStart.x, pStart.y + getHeight());
    }

    public void moveTo(Coord coord) {
        /**
         * Déplace l'objet courant
         * 
         * @param coord : coordonnée
         */
        double dx = coord.x - ((pStart.x + pEnd.x) / 2.0);
        double dy = coord.y - ((pStart.y + pEnd.y) / 2.0);

        pStart.moveTo(dx, dy);
        pEnd.moveTo(dx, dy);
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
        /**
         * Termine l'objet courant
         */
        setText();
    }
}
