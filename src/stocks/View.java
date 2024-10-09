package stocks;

/**
 * This interface represents the view.
 * The view is for the user to see messages and interact with the program's console.
 */
public interface View {

  /**
   * Prints the welcome message to the console.
   * Appends to the view's appendable object.
   *
   * @throws IllegalStateException if an error is encountered.
   */
  void welcomeMessage() throws IllegalStateException;

  /**
   * Prints the quit message to the console.
   * Appends to the view's appendable object.
   *
   * @throws IllegalStateException if an error is encountered.
   */
  void farewellMessage() throws IllegalStateException;

  /**
   * Prints the menu message to the console.
   * Appends to the view's appendable object.
   *
   * @throws IllegalStateException if an error is encountered.
   */
  void printMenu() throws IllegalStateException;

  /**
   * Prints the given message to the console.
   * Appends to the view's appendable object.
   *
   * @param message The message to write.
   * @throws IllegalStateException if an error is encountered.
   */
  void writeMessage(String message) throws IllegalStateException;

  /**
   * Gets the appendable object that this view appends to.
   *
   * @return The Appendable to append to.
   */
  Appendable getAppendable();

}
