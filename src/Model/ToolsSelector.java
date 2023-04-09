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

	public boolean isSelected() {
		return start != null && end != null;
	}

	public boolean isOnSelectionX(Coord mouse) {
		return isSelected() && mouse.x > start.x && mouse.x < end.x;
	}

	public boolean isOnSelectionY(Coord mouse) {
		return isSelected() && mouse.y > start.y && mouse.y < end.y;
	}

	public boolean isOnShapeX(Coord mouse) {
		return isSelected() && mouse.x > start.x + offset && mouse.x < end.x - offset;
	}

	public boolean isOnShapeY(Coord mouse) {
		return isSelected() && mouse.y > start.y + offset && mouse.y < end.y - offset;
	}

	public boolean isOnShape(Coord mouse) {
		return isOnShapeX(mouse) && isOnShapeY(mouse);
	}

	public boolean canMove(Coord mouse) {
		return isOnShapeX(mouse) && isOnShapeY(mouse) && !canResize(mouse);
	}

	public boolean canResize(Coord mouse) {
		return canResizeH(mouse) || canResizeV(mouse);
	}

	public boolean canResizeH(Coord mouse) {
		return isOnSelectionX(mouse) && !isOnShapeX(mouse);
	}

	public boolean canResizeV(Coord mouse) {
		return isOnSelectionY(mouse) && !isOnShapeY(mouse);
	}

	public void setSelection(Coord startPos, Coord endPos) {
		if(startPos.x > endPos.x) {
			double tmp = startPos.x;
			startPos.x = endPos.x;
			endPos.x = tmp;
		}
		else if(startPos.y > endPos.y) {
			double tmp = startPos.y;
			startPos.y = endPos.y;
			endPos.y = tmp;
		}

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
