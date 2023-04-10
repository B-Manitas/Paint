package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShapeTriangle implements IShape {

	public ShapeTypes type = ShapeTypes.TRIANGLE;
	private double offset = Constant.DEFAULT_OFFSET;
	private double zoomFactor = Constant.DEFAULT_ZOOM;

	private Coord pStart, pEnd, pOpposite = new Coord();
	private int bdSize;
	private Color color;

	public ShapeTriangle(Coord pStart, int size) {
		/**
		 * Crée une forme de type triangle.
		 * 
		 * @param start Coordonnées du point de départ
		 * @param size  Taille de la bordure
		 */
		this.pStart = pStart;
		this.bdSize = size;
	}

	private ShapeTriangle(Coord pStart, Coord pEnd, int size, Color color) {
		/**
		 * Crée une forme de type triangle.
		 * 
		 * @param pStart Coordonnées du point de départ
		 * @param pEnd   Coordonnées du point de fin
		 * @param size   Taille de la bordure
		 * @param color  Couleur de la bordure
		 */
		this.pStart = pStart;
		this.pEnd = pEnd;
		this.pOpposite = pStart.opposite(pEnd);

		this.bdSize = size;
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
		this.pOpposite = this.pStart.opposite(this.pEnd);
	}

	public Coord getPStart() {
		/**
		 * Retourne les coordonnées du point de départ.
		 * 
		 * @return Coordonnées du point de départ
		 */
		return this.pStart;
	}

	public Coord getPEnd() {
		/**
		 * Retourne les coordonnées du point de fin.
		 * 
		 * @return Coordonnées du point de fin
		 */
		return this.pEnd;
	}

	public int getBdSize() {
		/**
		 * Retourne la taille de la bordure.
		 * 
		 * @return Taille de la bordure
		 */
		return this.bdSize;
	}

	public Color getColor() {
		/**
		 * Retourne la couleur de la bordure.
		 * 
		 * @return Couleur de la bordure
		 */
		return this.color;
	}

	public Coord[] getArea() {
		/**
		 * Retourne les coordonnées du rectangle englobant la forme.
		 * 
		 * @return Coordonnées du rectangle englobant la forme
		 */
		double minX = Math.min(Math.min(pStart.x, pEnd.x), pOpposite.x);
		double minY = Math.min(Math.min(pStart.y, pEnd.y), pOpposite.y);
		double maxX = Math.max(Math.max(pStart.x, pEnd.x), pOpposite.x);
		double maxY = Math.max(Math.max(pStart.y, pEnd.y), pOpposite.y);

		return new Coord[] { new Coord(minX, minY), new Coord(maxX, maxY) };
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
		double alpha = ((pOpposite.y - pEnd.y) * (coord.x - pEnd.x) + (pEnd.x - pOpposite.x) * (coord.y - pEnd.y)) /
				((pOpposite.y - pEnd.y) * (pStart.x - pEnd.x) + (pEnd.x - pOpposite.x) * (pStart.y - pEnd.y));

		double beta = ((pEnd.y - pStart.y) * (coord.x - pEnd.x) + (pStart.x - pEnd.x) * (coord.y - pEnd.y)) /
				((pOpposite.y - pEnd.y) * (pStart.x - pEnd.x) + (pEnd.x - pOpposite.x) * (pStart.y - pEnd.y));

		return alpha + offset >= 0 && beta + offset >= 0 && alpha + beta - offset <= 1;
	}

	public IShape copy() {
		/**
		 * Retourne une copie de la forme.
		 * 
		 * @return Copie de la forme
		 */
		return new ShapeTriangle(pStart, pEnd, bdSize, color);
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
		pOpposite.moveTo(dx, dy);
	}

	public void draw(GraphicsContext gc) {
		/**
		 * Dessine la forme.
		 * 
		 * @param gc Contexte graphique
		 */
		double[] xPoints = new double[] { pStart.x, pEnd.x, pOpposite.x };
		double[] yPoints = new double[] { pStart.y, pEnd.y, pOpposite.y };

		gc.setStroke(this.color);
		gc.setLineWidth(this.bdSize);
		gc.setFill(color);
		gc.fillPolygon(xPoints, yPoints, 3);
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
		pOpposite.centerZoom(this.zoomFactor);
	}

	public void finish() {
	}
}
