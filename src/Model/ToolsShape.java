package Model;

public class ToolsShape implements ITools {

  private static ToolsTypes type = ToolsTypes.SHAPE;

  public boolean isTool(ToolsTypes type) {
    /**
     * Retourne vrai si la forme est de type `type`.
     *
     * @param type Le type de forme à comparer
     */
    return ToolsShape.type == type;
  }
}
