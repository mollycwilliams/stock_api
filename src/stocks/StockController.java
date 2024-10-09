package stocks;

import java.io.IOException;
import java.util.Scanner;

/**
 * Interface for the Stock Controller. Its role is to connect take the user input, give it to
 * the model, and store the output for the view to display it.
 */
public interface StockController {

  /**
   * Executes the program with a feedback loop that allows the user to input and see results.
   *
   * @throws IllegalStateException if an error occurs when appending a message to the
   *     appendable field.
   * @throws IOException           if an error occurs with the operations performed.
   */
  void control() throws IllegalStateException, IOException;

  /**
   * Processes the user input and performs an operation depending on what the user asked.
   *
   * @param userInstruction The instruction the user asks.
   * @param s               the input the user inputted.
   * @throws IOException if an invalid input occurs.
   */
  void processStrategy(String userInstruction, Scanner s) throws IOException;

}
