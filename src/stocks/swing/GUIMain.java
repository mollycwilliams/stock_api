package stocks.swing;

import java.io.IOException;
import java.io.InputStreamReader;

import stocks.StockController;
import stocks.StockControllerImpl;
import stocks.View;
import stocks.ViewImpl;

/**
 * Main class used to run the program. Creates a GUIModel and GUIView object to
 * pass into the controller.
 */
public class GUIMain {
  /**
   * Main method.
   * @param args args inputted by user.
   * @throws IOException if an exception is thrown.
   */
  public static void main(String[] args) throws IOException {
    if (args.length > 0) {
      Readable r = new InputStreamReader(System.in);
      Appendable a = System.out;
      View view = new ViewImpl(a);
      StockController controller = new StockControllerImpl(r, view);
      controller.control();
    } else {
      GUIModel model = new GUIModelImpl();
      GUIView view = new GUIViewImpl(); // Use GUIView
      GUIControllerImpl controller = new GUIControllerImpl(model, view);
    }
  }
}
