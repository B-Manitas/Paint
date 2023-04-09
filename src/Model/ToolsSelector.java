package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ToolsSelector implements ITools {

	private static ToolsTypes type = ToolsTypes.SELECT;
	private Coord start, end;
	private double offset = 7;

	public boolean isTool(ToolsTypes type) {
		/**
		 * Retourne vrai si la forme est de type `type`.
		 *
		 * @param type Le type de forme à comparer
		 */
		return ToolsSelector.type == type;
	}

	public double distance(double x1, double x2) {
		return Math.abs(x1 - x2);
	}

	public boolean isSelected() {
		return start != null && end != null;
	}

	public boolean isResized(Coord mouse) {
		return isResizedH(mouse) || isResizedV(mouse);
	}

	public boolean isResizedV(Coord mouse) {
		return isSelected() && (distance(mouse.y, start.y) < offset ||
				distance(mouse.y, end.y) < offset);
	}

	public boolean isResizedH(Coord mouse) {
		return isSelected() && (distance(mouse.x, start.x) < offset ||
				distance(mouse.x, end.x) < offset);
	}

	public void setSelection(Coord startPos, Coord endPos) {
		this.start = startPos;
		this.end = endPos;
	}

	public void reset() {
		this.start = null;
		this.end = null;
	}

	public void draw(GraphicsContext gc) {
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
}
