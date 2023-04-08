package Model;

public class ToolsSelector implements ITools {

  private static ToolsTypes type = ToolsTypes.SELECT;

  public boolean isTool(ToolsTypes type) {
    /**
     * Retourne vrai si la forme est de type `type`.
     *
     * @param type Le type de forme à comparer
     */
    return ToolsSelector.type == type;
  }
}
