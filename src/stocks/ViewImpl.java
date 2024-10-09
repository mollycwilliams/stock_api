package stocks;

import java.io.IOException;

/**
 * This class represents the View.
 * The view handles printing messages to the console, by appending to its appendable object.
 * Is used in the controller to print messages.
 */
public class ViewImpl implements View {

  private Appendable appendable;

  /**
   * Constructs a view with the given Appendable object.
   *
   * @param appendable the appendable to append messages to.
   */
  public ViewImpl(Appendable appendable) {
    this.appendable = appendable;
  }

  /**
   * Gets the appendable object that this view appends to.
   *
   * @return The Appendable to append to.
   */
  public Appendable getAppendable() {
    return this.appendable;
  }

  /**
   * Appends string message to appendable field which in turn updates the output of the program.
   *
   * @param message the message that will be appended to appendable field.
   * @throws IllegalStateException if unable to append message.
   */
  public void writeMessage(String message) throws IllegalStateException {
    try {
      appendable.append(message);

    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  /**
   * Displays info for the user to view by calling writeMessage with specific menu message with
   * all the possible instructions.
   *
   * @throws IllegalStateException if unable to append message.
   */
  public void printMenu() throws IllegalStateException {
    writeMessage("Supported user instructions are: \n");
    writeMessage("create-portfolio (creates a new portfolio with the given name)\n");
    writeMessage("crossover (calculates the crossover dates for a given stock\n");
    writeMessage("moving-average (calculates the moving-average for a given stock, given"
            + " a date and a number of days)\n");
    writeMessage("performance (calculates the performance for a given stock\n");
    writeMessage("performance-all (calculates the performance for a given portfolio\n");
    writeMessage("purchase-stock (buys the given number of shares for a stock)\n");
    writeMessage("sell-stock (sells the given number of shares for a stock)\n");
    writeMessage("display-composition (displays all the stocks in the given portfolio)\n");
    writeMessage("rebalance-portfolio (rebalances a portfolio by buying/selling shares "
            + "for each stock to match the users inputted percentages)\n");
    writeMessage("distribution-display (displays the distribution of a portfolio\n"
            + " * at a specific date)\n");
    writeMessage("performance-over-time (shows a bar chart representation of the portfolio's"
            + "performance over time)\n");
    writeMessage("save-portfolio (saves the portfolio locally so that it can be retrieved in"
            + " the future)\n");
    writeMessage("retrieve-portfolio (retrieves the portfolio from the previously"
            + " saved portfolios)\n");
    writeMessage("menu (Print supported instruction list)\n");
    writeMessage("q or quit (quit the program)\n");
  }

  /**
   * Display welcome message for the user by calling the writeMessage with a specific message.
   *
   * @throws IllegalStateException if unable to append message.
   */
  public void welcomeMessage() throws IllegalStateException {
    writeMessage("Welcome to the stock program!\n");
    printMenu();
  }

  /**
   * Display farewell message for the user by calling the writeMessage with a specific message.
   *
   * @throws IllegalStateException if unable to append message.
   */
  public void farewellMessage() throws IllegalStateException {
    writeMessage("Thank you for using this stock program! Happy gambling!");
  }

}
