package Model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ToolsShape implements ITools {

    private static ToolsTypes type = ToolsTypes.SHAPE;

    public boolean isTool(ToolsTypes type) {
        /**
         * Vérifie si le type de l'outil est celui de l'outil courant.
         *
         * @param type Type de l'outil
         * @return true si le type de l'outil est celui de l'outil courant, false sinon
         */
        return ToolsShape.type == type;
    }

    public void draw(Canvas canvas, Image prevImage, Coord pCurrent, IShape shape) {
        /**
         * Dessine la forme.
         *
         * @param canvas    Canvas sur lequel dessiner
         * @param prevImage Image précédente
         * @param pCurrent  Coordonnées de la souris
         * @param shape     Forme à dessiner
         */
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(prevImage, 0, 0);

        shape.setPEnd(pCurrent);
        shape.draw(gc);
    }
}
