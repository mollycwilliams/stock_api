package stocks.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import stocks.CommandInfo;
import stocks.Stock;
import stocks.Utils;

/**
 * This class represents a CompositionCommand.
 * A CompositionCommand is a Command that displays the composition of a portfolio
 * at a specific date. The composition includes (a) the list of stocks and (b) the
 * number of shares of each stock.
 */
public class CompositionCommand implements Command {
  private CommandInfo context;

  /**
   * Constructs a CompositionCommand object.
   * Takes in a CommandInfo object that passes the Scanner and Appendable
   * from the controller to execute its function with.
   *
   * @param context CommandInfo object that contains Scanner, Appendable.
   */
  public CompositionCommand(CommandInfo context) {
    this.context = context;
  }

  /**
   * Runs the CompositionCommand.
   * Processes user input to display the composition of a given portfolio.
   *
   * @throws IOException if user input cannot be parsed.
   */
  @Override
  public void run() throws IOException {
    boolean quit = false;
    while (!quit) {
      String name = getPortfolioName();
      if (name == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      LocalDate date = getPortfolioDate(name);
      if (date == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      displayPortfolioComposition(name, date);
      context.getView().writeMessage("Returning to main menu.\n");
      break;
    }
  }

  /**
   * Asks for and verifies the name of the portfolio the user wants the composition of.
   *
   * @return The name of the portfolio if valid, null if invalid.
   * @throws IOException if an exception is thrown.
   */
  private String getPortfolioName() throws IOException {
    while (true) {
      context.getView().writeMessage("Which portfolio would you like to display? "
              + "Or press quit to exit.\n");
      String name = context.getScanner().next();
      if (isQuit(name)) {
        return null;
      }
      if (Utils.getPortfolios().containsKey(name)) {
        return name;
      }
      context.getView().writeMessage("This portfolio does not exist.\n");
    }
  }

  /**
   * Asks and verifies that the date the user inputted is valid to calculate the composition
   * for the portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @return the date if valid, null if invalid.
   * @throws IOException if an exception is thrown.
   */
  private LocalDate getPortfolioDate(String portfolioName) throws IOException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    while (true) {
      context.getView().writeMessage("Enter the date (yyyy-MM-dd) you would"
              + " like to view the composition for:\n");
      try {
        String dateInput = context.getScanner().next();
        if (isQuit(dateInput)) {
          return null;
        }
        LocalDate date = LocalDate.parse(dateInput, formatter);
        if (!date.isBefore(Utils.getPortfolios().get(portfolioName).getPurchaseDate())) {
          return date;
        }
        context.getView().writeMessage("Portfolio does not exist at this date. "
                + "Enter a new date:\n");
      } catch (DateTimeParseException e) {
        context.getView().writeMessage("Invalid date entry, please try again:\n");
      }
    }
  }

  /**
   * Displays the composition of the given portfolio.
   *
   * @param portfolioName the name of the portfolio to display.
   * @param date          the date to display the composition for.
   * @throws IOException if an exception is thrown.
   */
  private void displayPortfolioComposition(String portfolioName, LocalDate date)
          throws IOException {
    context.getView().writeMessage("List of stocks in " + portfolioName + ":\n");
    for (Map.Entry<String, Stock> entry : Utils.getPortfolios()
            .get(portfolioName).getListOfStocks().entrySet()) {
      double totalShares = entry.getValue().getSharesAtDate(date);
      context.getView().writeMessage(entry.getKey() + " : " + totalShares + "\n");
    }
  }

  /**
   * Checks if the user inputted quit, and quits if true.
   *
   * @param s the input to check.
   * @return true if quit, false if not.
   * @throws IOException if an exception is thrown.
   */
  private boolean isQuit(String s) throws IOException {
    if (s.equalsIgnoreCase("quit")) {
      context.getView().writeMessage("Program quit successfully.\n");
      return true;
    }
    return false;
  }
}
