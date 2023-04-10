package Model;

public class ToolsEraser implements ITools {

    public static ShapeTypes type = ShapeTypes.PEN;

    public boolean isTool(ToolsTypes type) {
        /**
         * Vérifie si le type de l'outil est celui de l'outil courant.
         * 
         * @param type Type de l'outil
         * @return true si le type de l'outil est celui de l'outil courant, false sinon
         */
        return type == ToolsTypes.ERASER;
    }
}
