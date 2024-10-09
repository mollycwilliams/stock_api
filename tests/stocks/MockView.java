package stocks;


import java.util.List;

/**
 * This class represents a Mock for a View.
 */
public class MockView implements View {
  public List<String> logs;

  /**
   * Constructor for a MockView has a list as a field.
   *
   * @param logs A list of String.
   */
  public MockView(List<String> logs) {
    this.logs = logs;

  }

  /**
   * Adds the welcome message and the print menu message to the log.
   *
   * @throws IllegalStateException if
   */
  @Override
  public void welcomeMessage() {
    logs.add("Welcome to the stock program!\n" + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n" +
            "add-stock (adds given stock to a portfolio)\n" +
            "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock, given"
            + " a date and a number of days)\n" +
            "performance (calculates the performance for a given stock\n" +
            "performance-all (calculates the performance for a given portfolio\n" +
            "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n" + "q or quit (quit the program)\n");
  }

  /**
   * Adds the farewell message to the log.
   */
  @Override
  public void farewellMessage() {
    logs.add("Thank you for using this stock program! Happy gambling!");
  }

  /**
   * Adds the print menu message to the log.
   */
  @Override
  public void printMenu() {
    logs.add("Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n" +
            "add-stock (adds given stock to a portfolio)\n" +
            "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock, given"
            + " a date and a number of days)\n" +
            "performance (calculates the performance for a given stock\n" +
            "performance-all (calculates the performance for a given portfolio\n" +
            "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n" + "q or quit (quit the program)\n");
  }

  /**
   * Appends the message to the log.
   *
   * @param message A string message.
   */
  @Override
  public void writeMessage(String message) {
    logs.add(message);
  }

  /**
   * Returns a string builder appendable object.
   *
   * @return an appendable object.
   */
  @Override
  public Appendable getAppendable() {
    return new StringBuilder();
  }


  /**
   * Returns the logs of the Mock view.
   *
   * @return the logs field of the Mock view.
   */
  public List<String> getLogs() {
    return this.logs;
  }


}
