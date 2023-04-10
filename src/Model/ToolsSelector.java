package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ToolsSelector implements ITools {

	private static ToolsTypes type = ToolsTypes.SELECT;
	private Coord start, end;
	private double offset = Constant.DEFAULT_OFFSET;

	public void setSelection(Coord pStart, Coord pEnd) {
		/**
		 * Définit la sélection.
		 * 
		 * @param pStart Coordonnées du point de départ
		 * @param pEnd   Coordonnées du point d'arrivée
		 */
		if (pStart.x > pEnd.x) {
			double tmp = pStart.x;
			pStart.x = pEnd.x;
			pEnd.x = tmp;
		}

		if (pStart.y > pEnd.y) {
			double tmp = pStart.y;
			pStart.y = pEnd.y;
			pEnd.y = tmp;
		}

		this.start = pStart;
		this.end = pEnd;
	}

	public boolean isTool(ToolsTypes type) {
		/**
		 * Vérifie si l'outil est de type sélection.
		 * 
		 * @param type Type de l'outil
		 * @return true si l'outil est de type sélection, false sinon
		 */
		return ToolsSelector.type == type;
	}

	public boolean isSelected() {
		/**
		 * Vérifie si une sélection est en cours.
		 * 
		 * @return true si une sélection est en cours, false sinon
		 */
		return start != null && end != null;
	}

	public boolean isOnSelectionX(Coord coord) {
		/**
		 * Vérifie si la souris est sur la sélection en X.
		 * 
		 * @param coord Coordonnées de la souris
		 * @return true si la souris est sur la sélection en X, false sinon
		 */
		return isSelected() && coord.x > start.x && coord.x < end.x;
	}

	public boolean isOnSelectionY(Coord coord) {
		/**
		 * Vérifie si la souris est sur la sélection en Y.
		 * 
		 * @param coord Coordonnées de la souris
		 * @return true si la souris est sur la sélection en Y, false sinon
		 */
		return isSelected() && coord.y > start.y && coord.y < end.y;
	}

	public boolean isOnShapeX(Coord coord) {
		/**
		 * Vérifie si la souris est sur la forme en X.
		 * 
		 * @param coord Coordonnées de la souris
		 * @return true si la souris est sur la forme en X, false sinon
		 */
		return isSelected() && coord.x > start.x + offset && coord.x < end.x - offset;
	}

	public boolean isOnShapeY(Coord coord) {
		/**
		 * Vérifie si la souris est sur la forme en Y.
		 * 
		 * @param coord Coordonnées de la souris
		 * @return true si la souris est sur la forme en Y, false sinon
		 */
		return isSelected() && coord.y > start.y + offset && coord.y < end.y - offset;
	}

	public boolean isOnShape(Coord coord) {
		/**
		 * Vérifie si la souris est sur la forme.
		 * 
		 * @param coord Coordonnées de la souris
		 * @return true si la souris est sur la forme, false sinon
		 */
		return isOnShapeX(coord) && isOnShapeY(coord);
	}

	public boolean canMove(Coord coord) {
		/**
		 * Vérifie si la souris est sur la forme et que la forme peut être déplacée.
		 * 
		 * @param coord Coordonnées de la souris
		 * @return true si la souris est sur la forme et que la forme peut être
		 *         déplacée, false sinon
		 */
		return isOnShapeX(coord) && isOnShapeY(coord) && !canResize(coord);
	}

	public boolean canResize(Coord coord) {
		/**
		 * Vérifie si la souris est sur la forme et que la forme peut être
		 * redimensionnée.
		 * 
		 * @param coord Coordonnées de la souris
		 * @return true si la souris est sur la forme et que la forme peut être
		 *         redimensionnée, false sinon
		 */
		return canResizeH(coord) || canResizeV(coord);
	}

	public boolean canResizeH(Coord coord) {
		/**
		 * Vérifie si la souris est sur la forme et que la forme peut être
		 * redimensionnée
		 * 
		 * @param coord Coordonnées de la souris
		 * @return true si la souris est sur la forme et que la forme peut être
		 */
		return isOnSelectionX(coord) && !isOnShapeX(coord);
	}

	public boolean canResizeV(Coord coord) {
		/**
		 * Vérifie si la souris est sur la forme et que la forme peut être
		 * redimensionnée
		 * 
		 * @param coord Coordonnées de la souris
		 * @return true si la souris est sur la forme et que la forme peut être
		 */
		return isOnSelectionY(coord) && !isOnShapeY(coord);
	}

	public void draw(GraphicsContext gc) {
		/**
		 * Dessine la sélection.
		 * 
		 * @param gc Contexte graphique
		 */
		double offset = 0;

		double width = Math.abs(start.x - end.x) - offset;
		double height = Math.abs(start.y - end.y) - offset;
		double x = Math.min(start.x, end.x) - offset;
		double y = Math.min(start.y, end.y) - offset;

		gc.setStroke(Color.BLUE);
		gc.setLineDashes(5);
		gc.setLineWidth(2);

		gc.strokeRect(x, y, width, height);
	}

	public void reset() {
		/**
		 * Réinitialise la sélection.
		 */
		this.start = null;
		this.end = null;
	}
}
