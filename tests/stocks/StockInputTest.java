package stocks;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

/**
 * This class tests different inputs for different Stock / Portfolio commands,
 * as well as View methods.
 */
public class StockInputTest {

  /**
   * Tests to ensure the correct message is displayed when an invalid ticker is inputted.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testInvalidTickerInput() throws IOException {
    Readable in = new StringReader("crossover wrong AMZN 2024-05-31 2024-06-04 3");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock,"
            + " given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)\n"
            + "Type instruction: Which stock would you like to use? Or type quit to exit.\n"
            + "Invalid ticker, try again: \n"
            + "Which stock would you like to use? Or type quit to exit.\n"
            + "This file is already contained in the program\n"
            + "Enter start date (yyyy-MM-dd):\n"
            + "Enter end date (yyyy-MM-dd):\n"
            + "Enter amount of days: Crossover dates:\n"
            + "2024-06-03\n"
            + "2024-06-04\n"
            + "Thank you for using this stock program! Happy gambling!", appendable.toString());
  }

  /**
   * Tests to ensure the correct message is displayed if an invalid date is entered.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testInvalidDate() throws IOException {
    Readable in = new StringReader("moving-average GOOG 1000-10-10 2020-06-02 10");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock,"
            + " given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)\n"
            + "Type instruction: Which stock would you like to use? Or press quit to exit.\n"
            + "This file is already contained in the program\n"
            + "For which date would you like to calculate the moving average? (yyyy-MM-dd)\n"
            + "Invalid date, please enter a valid date:\n"
            + "For which date would you like to calculate the moving average? (yyyy-MM-dd)\n"
            + "Please provide the number of days:\n"
            + "10.0-day moving average for GOOG is: 1421.9433333333334\n"
            + "Returning to main menu.\n"
            + "Thank you for using this stock program! Happy gambling!", appendable.toString());
  }

  /**
   * Tests to ensure that input is parsed correctly and calls
   * Crossover command.
   *
   * @throws IOException if the input is invalid.
   */
  @Test
  public void testCrossoverInput() throws IOException {
    Readable in = new StringReader("crossover AMZN 2024-05-31 2024-06-04 3");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock,"
            + " given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)\n"
            + "Type instruction: Which stock would you like to use? Or type quit to exit.\n"
            + "This file is already contained in the program\n"
            + "Enter start date (yyyy-MM-dd):\n"
            + "Enter end date (yyyy-MM-dd):\n"
            + "Enter amount of days: Crossover dates:\n"
            + "2024-06-03\n"
            + "2024-06-04\n"
            + "Thank you for using this stock program! Happy gambling!", appendable.toString());
  }

  /**
   * Tests to ensure that input is parsed correctly and calls
   * BuildPortfolio command.
   * Also tests to ensure that the Portfolio is created properly.
   *
   * @throws IOException if the input is invalid.
   */
  @Test
  public void testBuildPortfolioInput() throws IOException {
    Readable in = new StringReader("create-portfolio a add-stock GOOG 5 2024-05-21 add-stock "
            + "AMZN 3 2024-06-04 build");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    CommandInfoImpl context = new CommandInfoImpl();
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock,"
            + " given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)\n"
            + "Type instruction: What would you like to name this portfolio?\n"
            + "Or type quit to exit.\n"
            + "Type portfolio instruction: (add-stock, build, quit) \n"
            + "Which stock would you like to use?\n"
            + "This file is already contained in the program\n"
            + "How many shares would you like?\n"
            + "Which date (YYYY-MM-DD) are you purchasing this stock at?\n"
            + "Type portfolio instruction: (add-stock, build, quit) \n"
            + "Which stock would you like to use?\n"
            + "This file is already contained in the program\n"
            + "How many shares would you like?\n"
            + "Which date (YYYY-MM-DD) are you purchasing this stock at?\n"
            + "Type portfolio instruction: (add-stock, build, quit) \n"
            + "Portfolio was built, returned back to main menu.\n"
            + "Thank you for using this stock program! Happy gambling!", appendable.toString());
    assertEquals(true, Utils.getPortfolios().get("a") != null);
  }

  /**
   * Tests to ensure that input is parsed correctly and calls
   * PerformanceAll command.
   * Also tests to ensure that the right value is calculated and printed.
   *
   * @throws IOException if the input is invalid.
   */
  @Test
  public void testPerformanceAllInput() throws IOException {
    Readable in = new StringReader("retrieve-portfolio z performance-all z 2024-05-10");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock,"
            + " given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)\n"
            + "Type instruction: Enter name of portfolio you want to retrieve"
            + " or press quit to exit.\n"
            + "z has been added to your list of available portfolios!\n"
            + "Returning to main menu.\n"
            + "Type instruction: Which portfolio would you like to display?"
            + " Or press quit to exit.\n"
            + "Enter the date (yyyy-MM-dd) you would like to view the composition for:\n"
            + "Total of: z is: 4205.555\n"
            + "MSFT 9.4\n"
            + "GOOG 0.0\n"
            + "INTC 4.26\n"
            + "GME 10.3\n"
            + "Returning to menu.\n"
            + "Thank you for using this stock program! Happy gambling!", appendable.toString());
  }

  /**
   * Tests to ensure that input is parsed correctly and calls
   * Performance command.
   * Also tests to ensure that the right value is calculated and printed.
   *
   * @throws IOException if the input is invalid.
   */
  @Test
  public void testPerformanceInput() throws IOException {
    Readable in = new StringReader("performance GOOG 2024-05-02 2024-05-10");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock,"
            + " given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)\n"
            + "Type instruction: Which stock would you like to use? Or press quit to exit.\n"
            + "Enter start date (yyyy-MM-dd):\n"
            + "Enter end date (yyyy-MM-dd):\n"
            + "Stock gained value: 3.6200000000000045\n"
            + "Returning to main menu.\n"
            + "Thank you for using this stock program! Happy gambling!", appendable.toString());
  }

  /**
   * Tests to ensure that input is parsed correctly and calls
   * MovingAverageInput command.
   * Also tests to ensure that the right value is calculated and printed.
   *
   * @throws IOException if the input is invalid.
   */
  @Test
  public void testMovingAverageInput() throws IOException {
    Readable in = new StringReader("moving-average GOOG 2020-06-02 10");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock,"
            + " given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)\n"
            + "Type instruction: Which stock would you like to use? Or press quit to exit.\n"
            + "This file is already contained in the program\n"
            + "For which date would you like to calculate the moving average? (yyyy-MM-dd)\n"
            + "Please provide the number of days:\n"
            + "10.0-day moving average for GOOG is: 1421.9433333333334\n"
            + "Returning to main menu.\n"
            + "Thank you for using this stock program! Happy gambling!", appendable.toString());
  }


  /**
   * Test to ensure that the welcome message is displayed when
   * the user starts the program.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testWelcomeMessage() throws IOException {
    Readable in = new StringReader("");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    StringBuilder expected = new StringBuilder("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock, "
            + "given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)\n"
            + "Thank you for using this stock program! Happy gambling!");
    assertEquals(expected.toString(), appendable.toString());
  }

  /**
   * Test to ensure that the farewell message is displayed when
   * the user inputs "quit".
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testFarewellMessage() throws IOException {
    Readable in = new StringReader("quit");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    String[] array = new String[]{};
    array = appendable.toString().split("\n");
    String result = array[array.length - 1];
    assertEquals(true, result.contains("Thank you for using this stock program! "
            + "Happy gambling!"));
  }

  /**
   * Tests to ensure the correct message is displayed if an invalid command is entered.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testInvalidCommand() throws IOException {
    Readable in = new StringReader("blow-up-program");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock, "
            + "given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)\n"
            + "Type instruction: Unknown command\n"
            + "Thank you for using this stock program! Happy gambling!", out.toString());
  }

  /**
   * Tests to ensure the printMenu command prints the menu properly.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testPrintMenu() throws IOException {
    Readable in = new StringReader("menu");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock, "
            + "given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)\n"
            + "Type instruction: Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock, "
            + "given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)\n"
            + "Thank you for using this stock program! Happy gambling!", out.toString());
  }

  /**
   * Tests to ensure that the purchaseStockCommand displays the correct messages
   * in response to user input.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testPurchaseStockCommand() throws IOException {
    Readable in = new StringReader("retrieve-portfolio z purchase-stock z GOOG 2024-05-21 2");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock,"
            + " given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)\n"
            + "Type instruction: Enter name of portfolio you want to retrieve"
            + " or press quit to exit.\n"
            + "z has been added to your list of available portfolios!\n"
            + "Returning to main menu.\n"
            + "Type instruction: What portfolio would you like to add this stock into?"
            + " Or press quit to exit.\n"
            + "What stock would you like to add to z?\n"
            + "What date do you want to purchase this on (yyyy-MM-dd)?\n"
            + "How many shares would you like to add?\n"
            + "Adding 2.0 of GOOG to portfolio z purchased at 2024-05-21\n"
            + "Returning back to main menu.\n"
            + "Thank you for using this stock program! Happy gambling!", out.toString());
  }

  /**
   * Tests to ensure that the correct message is displayed / that users cannot purchase
   * fractional stocks shares.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testPurchaseFractionalStock() throws IOException {
    Readable in = new StringReader("retrieve-portfolio z purchase-stock z GOOG 2024-05-21 2.2 "
            + "2");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock,"
            + " given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)\n"
            + "Type instruction: Enter name of portfolio you want to"
            + " retrieve or press quit to exit.\n"
            + "z has been added to your list of available portfolios!\n"
            + "Returning to main menu.\n"
            + "Type instruction: What portfolio would you like to add this stock into?"
            + " Or press quit to exit.\n"
            + "What stock would you like to add to z?\n"
            + "What date do you want to purchase this on (yyyy-MM-dd)?\n"
            + "How many shares would you like to add?\n"
            + "Cannot purchase fractional shares. Enter a new value:\n"
            + "How many shares would you like to add?\n"
            + "Adding 2.0 of GOOG to portfolio z purchased at 2024-05-21\n"
            + "Returning back to main menu.\n"
            + "Thank you for using this stock program! Happy gambling!", out.toString());
  }

  /**
   * Tests to ensure that the correct message is displayed if a user tries to purchase a stock
   * at an invalid date.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testPurchaseStockInvalidDate() throws IOException {
    Readable in = new StringReader("retrieve-portfolio z purchase-stock z GOOG 1000 2024-05-21 2"
            + "2");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock,"
            + " given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n" + "q or quit (quit the program)\n"
            + "Type instruction: Enter name of portfolio you want to retrieve or"
            + " press quit to exit.\n" + "z has been added to your list of available portfolios!\n"
            + "Returning to main menu.\n"
            + "Type instruction: What portfolio would you like to add this stock into?"
            + " Or press quit to exit.\n" + "What stock would you like to add to z?\n"
            + "What date do you want to purchase this on (yyyy-MM-dd)?\n"
            + "Invalid date format, please try again.\n"
            + "What date do you want to purchase this on (yyyy-MM-dd)?\n"
            + "How many shares would you like to add?\n"
            + "Adding 22.0 of GOOG to portfolio z purchased at 2024-05-21\n"
            + "Returning back to main menu.\n"
            + "Thank you for using this stock program! Happy gambling!", out.toString());
  }

  /**
   * Tests to ensure that the correct message is displayed if a user tries to sell a stock
   * at an invalid date.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testSellStockInvalidDate() throws IOException {
    Readable in = new StringReader("retrieve-portfolio z sell-stock z GOOG 1000 2024-05-21 2.2 "
            + "2");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n" + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock,"
            + " given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)\n"
            + "Type instruction: Enter name of portfolio you want to retrieve or"
            + " press quit to exit.\n"
            + "z has been added to your list of available portfolios!\n"
            + "Returning to main menu.\n"
            + "Type instruction: What portfolio would you like to sell this stock in?"
            + " Or press quit to exit.\n" + "What stock would you like to sell in z?\n"
            + "What date do you want to sell this on (yyyy-MM-dd)?\n"
            + "Invalid date format, please try again.\n"
            + "What date do you want to sell this on (yyyy-MM-dd)?\n"
            + "Currently you have 4.5 shares of GOOG.\n"
            + "How many shares would you like to sell?\n"
            + "Removed 2.2 shares of GOOG from portfolio z sold at 2024-05-21\n"
            + "Returning to main menu.\n" + "Type instruction: Unknown command\n"
            + "Thank you for using this stock program! Happy gambling!", out.toString());
  }

  /**
   * Tests to ensure that the correct message is displayed if the user tries to sell more
   * stock shares than what they currently have.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testSellTooMuchStock() throws IOException {
    Readable in = new StringReader("retrieve-portfolio z sell-stock z GOOG 2024-05-21 10000 2");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n" + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock, given a date and"
            + " a number of days)\n" + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n" + "q or quit (quit the program)\n"
            + "Type instruction: Enter name of portfolio you want to retrieve or press"
            + " quit to exit.\n" + "z has been added to your list of available portfolios!\n"
            + "Returning to main menu.\n"
            + "Type instruction: What portfolio would you like to sell this stock in?"
            + " Or press quit to exit.\n" + "What stock would you like to sell in z?\n"
            + "What date do you want to sell this on (yyyy-MM-dd)?\n"
            + "Currently you have 4.5 shares of GOOG.\n"
            + "How many shares would you like to sell?\n"
            + "Cannot sell more than what you have.\n" + "How many shares would you like to sell?\n"
            + "Removed 2.0 shares of GOOG from portfolio z sold at 2024-05-21\n"
            + "Returning to main menu.\n"
            + "Thank you for using this stock program! Happy gambling!", out.toString());
  }

  /**
   * Tests to ensure the correct message is displayed when a user sells a stock.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testSellStockCommand() throws IOException {
    Readable in = new StringReader("retrieve-portfolio z sell-stock z GOOG 2024-05-21 2.2");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock,"
            + " given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n" + "q or quit (quit the program)\n"
            + "Type instruction: Enter name of portfolio you want to retrieve or"
            + " press quit to exit.\n" + "z has been added to your list of available portfolios!\n"
            + "Returning to main menu.\n"
            + "Type instruction: What portfolio would you like to sell this stock in?"
            + " Or press quit to exit.\n" + "What stock would you like to sell in z?\n"
            + "What date do you want to sell this on (yyyy-MM-dd)?\n"
            + "Currently you have 4.5 shares of GOOG.\n"
            + "How many shares would you like to sell?\n"
            + "Removed 2.2 shares of GOOG from portfolio z sold at 2024-05-21\n"
            + "Returning to main menu.\n"
            + "Thank you for using this stock program! Happy gambling!", out.toString());
  }

  /**
   * Tests to ensure the correct message/composition is displayed when a user tries to
   * display the composition for a portfolio.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testCompositionCommand() throws IOException {
    Readable in = new StringReader("retrieve-portfolio z display-composition z 2024-05-21");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock,"
            + " given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n" + "q or quit (quit the program)\n"
            + "Type instruction: Enter name of portfolio you want to retrieve or"

            + " press quit to exit.\n" + "z has been added to your list of available portfolios!\n"
            + "Returning to main menu.\n"
            + "Type instruction: Which portfolio would you like to display?"
            + " Or press quit to exit.\n"
            + "Enter the date (yyyy-MM-dd) you would like to view the composition for:\n"
            + "List of stocks in z:\n" + "MSFT : 9.4\n" + "GOOG : 4.5\n" + "INTC : 4.26\n"
            + "GME : 10.3\n" + "Returning to main menu.\n"
            + "Thank you for using this stock program! Happy gambling!", out.toString());
  }

  /**
   * Tests to ensure that the distributionDisplayCommand displays the correct distribution
   * for a user when they input the portfolio they want to display at the given date.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testDistributionDisplayCommand() throws IOException {
    Readable in = new StringReader("retrieve-portfolio z distribution-display z 2024-05-21");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n" + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock,"
            + " given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n" + "q or quit (quit the program)\n"
            + "Type instruction: Enter name of portfolio you want to retrieve or"
            + " press quit to exit.\n" + "z has been added to your list of available portfolios!\n"
            + "Returning to main menu.\n"
            + "Type instruction: Which portfolio do you want to get performance of?"
            + " Or press quit to exit.\n"
            + "Enter the date (yyyy-MM-dd) you would like to display the distribution for:\n"
            + "Values for: MSFT 4032.9760000000006: \n" + "Values for: GOOG 807.93: \n"
            + "Values for: INTC 135.21239999999997: \n" + "Values for: GME 227.836: \n"
            + "Total of: z is: 5203.954400000001\n" + "Returning back to main menu.\n"
            + "Thank you for using this stock program! Happy gambling!", out.toString());
  }


  /**
   * Tests to ensure the correct bar chart is displayed when calling performance over time.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testPerformanceOverTimeCommand() throws IOException {
    Readable in = new StringReader("retrieve-portfolio z performance-over-time z 2024-05-28 "
            + "2024-06-06");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n" +
            "Supported user instructions are: \n" +
            "create-portfolio (creates a new portfolio with the given name)\n" +
            "add-stock (adds given stock to a portfolio)\n" +
            "crossover (calculates the crossover dates for a given stock\n" +
            "moving-average (calculates the moving-average for a given stock, " +
            "given a date and a number of days)\n" +
            "performance (calculates the performance for a given stock\n" +
            "performance-all (calculates the performance for a given portfolio\n" +
            "display-stocks (displays all the stocks in the given portfolio)\n" +
            "menu (Print supported instruction list)\n" +
            "q or quit (quit the program)\n" +
            "Type instruction: Enter name of portfolio you want to retrieve or press" +
            " quit to exit.\n" +
            "z has been added to your list of available portfolios!\n" +
            "Returning to main menu.\n" +
            "Type instruction: Which portfolio would you like to see the performance of? " +
            "Or press quit to exit.\n" +
            "Portfolio was made at 2023-11-20. All dates before this will show up as no value.\n" +
            "Enter start date (yyyy-MM-dd):\n" +
            "Enter end date (yyyy-MM-dd):\n" +
            "Performance of portfolio z from 2024-05-28 to 2024-06-06.\n" +
            "\n" +
            "28 MAY 2024: **********\n" +
            "29 MAY 2024: *******\n" +
            "30 MAY 2024: *\n" +
            "31 MAY 2024: *\n" +
            "01 JUN 2024: *\n" +
            "02 JUN 2024: *\n" +
            "03 JUN 2024: **\n" +
            "04 JUN 2024: ***\n" +
            "06 JUN 2024: ***********\n" +
            "Scale: * = 18 more than minimum portfolio value : 5040\n" +
            "Thank you for using this stock program! Happy gambling!", out.toString());
  }

  /**
   * Tests to ensure th portfolio is correctly rebalanced.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testRebalancePortfolioCommand() throws IOException {
    Readable in = new StringReader("retrieve-portfolio z rebalance-portfolio z 2024-06-06 "
            + "10 20 50 20");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock, given"
            + " a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n" + "q or quit (quit the program)\n"
            + "Type instruction: Enter name of portfolio you want to retrieve or press"
            + " quit to exit.\n" + "z has been added to your list of available portfolios!\n"
            + "Returning to main menu.\n"
            + "Type instruction: Which portfolio would you like to rebalance? Or press"
            + " quit to exit.\n"
            + "Enter the date (yyyy-MM-dd) you would like to rebalance the portfolio at:\n"
            + "Make sure the date exists for all stocks.\n" + "Stocks in portfolio:\n"
            + "MSFT\n" + "GOOG\n" + "INTC\n" + "GME\n"
            + "You have 4 stocks so enter that amount of values for the"
            + " percentages (whole numbers)\n"
            + "Enter the percentages in order of how the stocks were displayed.\n"
            + "Readjusted: Sold 8.127476396871762 of MSFT\n"
            + "Readjusted: Bought 1.5578830389683214 of GOOG\n"
            + "Readjusted: Bought 84.53219592373439 of INTC\n"
            + "Readjusted: Bought 12.90995574650913 of GME\n" + "Portfolio z readjusted.\n"
            + "Returning to main menu.\n"
            + "Thank you for using this stock program! Happy gambling!", out.toString());
  }


  /**
   * Tests to ensure a portfolio can be retrieved.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testRetrievePortfolioCommand() throws IOException {
    Readable in = new StringReader("retrieve-portfolio z");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals(1, Utils.getPortfolios().size());
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock,"
            + " given a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n" + "q or quit (quit the program)\n"
            + "Type instruction: Enter name of portfolio you want to retrieve or"
            + " press quit to exit.\n" + "z has been added to your list of available portfolios!\n"
            + "Returning to main menu.\n"
            + "Thank you for using this stock program! Happy gambling!", out.toString());
  }


  /**
   * Tests to ensure if creating a portfolio and adding two stock, it will correctly be saved
   * and retrieved.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testSavePortfolioCommand() throws IOException {
    Readable in = new StringReader("create-portfolio saveplz add-stock GOOG 5 2024-05-21 "
            + "add-stock AMZN 2 2024-06-06 build save-portfolio saveplz retrieve-portfolio "
            + "saveplz");
    StringBuilder out = new StringBuilder();
    Appendable appendable = new MockModel(out);
    View view = new ViewImpl(appendable);
    StockController con = new StockControllerImpl(in, view);
    con.control();
    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock, given a "
            + "date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n" + "q or quit (quit the program)\n"
            + "Type instruction: What would you like to name this portfolio?\n"
            + "Or type quit to exit.\n"
            + "Type portfolio instruction: (add-stock, build, quit) \n"
            + "Which stock would you like to use?\n"
            + "This file is already contained in the program\n"
            + "How many shares would you like?\n"
            + "Which date (YYYY-MM-DD) are you purchasing this stock at?\n"
            + "Type portfolio instruction: (add-stock, build, quit) \n"
            + "Which stock would you like to use?\n"
            + "This file is already contained in the program\n"
            + "How many shares would you like?\n"
            + "Which date (YYYY-MM-DD) are you purchasing this stock at?\n"
            + "Type portfolio instruction: (add-stock, build, quit) \n"
            + "Portfolio was built, returned back to main menu.\n"
            + "Type instruction: What portfolio do you want to save? Or press quit to exit.\n"
            + "Saved saveplz\n" + "Returning to main menu.\n"
            + "Type instruction: Enter name of portfolio you want to retrieve or press quit"
            + " to exit.\n" + "saveplz has been added to your list of available portfolios!\n"
            + "Returning to main menu.\n"
            + "Thank you for using this stock program! Happy gambling!", out.toString());
  }


}
