package stocks;

import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Main Program that will be run for the stock program.
 * Uses a Readable object for user input, an Appendable for output,
 * and implements MVC design by passing a View into a StockController.
 */
public class StockMain {
  /**
   * Main method, runs the program.
   * @param args the body of the method.
   * @throws IOException if an exception is thrown.
   */
  public static void main(String[] args) throws IOException {
    Readable r = new InputStreamReader(System.in);
    Appendable a = System.out;
    View view = new ViewImpl(a);
    StockController controller = new StockControllerImpl(r, view);

    controller.control();
  }
}
