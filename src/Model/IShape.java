package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * ShapeInterface
 */
public interface IShape {
    public void setPStart(Coord pStart);

    public void setPEnd(Coord pEnd);

    public void setColor(Color color);

    public void setBdSize(int size);

    public Coord[] getArea();

    public int getBdSize();

    public Color getColor();

    public Coord getPStart();

    public Coord getPEnd();

    public boolean isInArea(Coord coord);

    public boolean isShape(ShapeTypes type);

    public IShape copy();

    public void moveTo(Coord coord);

    public void draw(GraphicsContext gc);

    public void finish();

    public void zoomIn();

    public void zoomOut();
}
