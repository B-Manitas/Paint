package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * ShapeInterface
 */
public interface IShape {
    public boolean isShape(ShapeTypes type);

    public void initializeCoord(Coord c);

    public boolean isIn(Coord c);

    public IShape copy();

    public Coord[] getSelectedCoords();

    public void moveTo(Coord mouse);

    public int getToolSize();

    public Color getToolColor();

    public void setToolColor(Color color);

    public void setToolSize(int size);

    public Coord getStartCoord();

    public Coord getEndCoord();

    public void setEndCoord(Coord end);

    public void draw(GraphicsContext gc);

    public void finishShape();

    public void zoomIn();

    public void zoomOut();
}
