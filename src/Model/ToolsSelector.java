package Model;

public class ToolsSelector implements ITools {

	private static ToolsTypes type = ToolsTypes.SELECT;
	private Coord start, end;
	private double offset = 10;

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

	public boolean isResized(Coord mouse) {
		if (start == null || end == null)
			return false;

		else
			return distance(mouse.x, start.x) < offset ||
					distance(mouse.y, start.y) < offset ||
					distance(mouse.x, end.x) < offset ||
					distance(mouse.y, end.y) < offset;

	}

	public void setStart(Coord start) {
		this.start = start;
	}

	public void setEnd(Coord end) {
		this.end = end;
	}

	public void reset() {
		start = null;
		end = null;
	}
}
