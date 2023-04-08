package Model;

public class ToolsEraser implements ITools {

  public static ShapeTypes type = ShapeTypes.PEN;

  public boolean isTool(ToolsTypes type) {
    return type == ToolsTypes.ERASER;
  }
}
