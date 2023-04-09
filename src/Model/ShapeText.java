package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ShapeText implements IShape {
    private ShapeTypes type = ShapeTypes.TEXT;
    private int toolSize;
    private Color toolColor;
    private Coord start, end;
    private double offset = 5;
    private double zoomFactor = 1.0;
    private Text text = new Text("Un texte");

    public ShapeText(Coord posStart, int toolSize, Color toolColor) {
        this.toolSize = toolSize;
        this.toolColor = toolColor;
        this.start = posStart;
    }

    private ShapeText(
            Coord posStart,
            Coord posEnd,
            int toolSize,
            Color toolColor, String text) {
        this.toolSize = toolSize;
        this.toolColor = toolColor;
        this.start = posStart;
        this.end = posEnd;
        this.text.setText(text);
    }

    public Color getToolColor() {
        return this.toolColor;
    }

    public void setToolColor(Color color) {
        this.toolColor = color;
    }

    public void setToolSize(int size) {
        this.toolSize = size;
    }

    public IShape copy() {
        return new ShapeText(start.copy(), end.copy(), toolSize, toolColor, text.getText());
    }

    public Coord getStartCoord() {
        return this.start;
    }

    public Coord getEndCoord() {
        return this.end;
    }

    public void setEndCoord(Coord mouse) {
        this.start = new Coord(mouse.x - getHeight(), mouse.y);
    }

    public int getToolSize() {
        return this.toolSize;
    }
    
    public boolean isShape(ShapeTypes type) {
        return this.type == type;
    }
    
    public void initializeCoord(Coord mouse) {
        this.end = new Coord(mouse.x +  getWidth(), mouse.y + getHeight());
    }
    
    public void setText(){
        TextInputDialog dialog = new TextInputDialog(text.getText());
        dialog.setTitle("Modifier le texte");
        dialog.setContentText("Veuillez entrer le texte :");
        dialog.showAndWait();
        this.text.setText(dialog.getResult());
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(toolColor);
        gc.setFont(new Font("Arial", toolSize * zoomFactor));
        gc.fillText(text.getText(), start.x, start.y + getHeight());
    }

    public double getWidth() {
        return text.getLayoutBounds().getWidth() + toolSize;
    }

    public double getHeight() {
        return text.getLayoutBounds().getHeight();
    }

    public boolean isIn(Coord c) {
        double xMin = Math.min(start.x, end.x) - offset;
        double xMax = Math.max(start.x, end.x) + offset;
        double yMin = Math.min(start.y, end.y) - offset;
        double yMax = Math.max(start.y, end.y) + offset;

        return c.x >= xMin && c.x <= xMax && c.y >= yMin && c.y <= yMax;
    }

    public void moveTo(Coord mouse) {
        double dx = mouse.x - ((start.x + end.x) / 2.0);
        double dy = mouse.y - ((start.y + end.y) / 2.0);

        start.moveTo(dx, dy);
        end.moveTo(dx, dy);
    }

    public void zoomIn() {
        this.zoomFactor = Math.max(.1, this.zoomFactor - .1);
        zoom();
    }

    public void zoomOut() {
        this.zoomFactor = Math.min(2.1, this.zoomFactor + .1);
        zoom();
    }

    public void zoom() {
        start.centerZoom(this.zoomFactor);
        end.centerZoom(this.zoomFactor);
    }

    public Coord[] getSelectedCoords() {
        Coord[] coords = new Coord[2];

        coords[0] = start.translate(-2 * offset);
        coords[1] = end.translate(2 * offset);

        return coords;
    }

    public void finishShape() {
        setText();
    }
}
