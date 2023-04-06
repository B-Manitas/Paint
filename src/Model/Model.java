package Model;

public class Model {

  public int getSize(String strSize) {
    return Integer.parseInt(strSize.replace("px", ""));
  }
}
