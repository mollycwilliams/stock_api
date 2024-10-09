package stocks.commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import stocks.CommandInfoImpl;
import stocks.Stock;
import stocks.Utils;

/**
 * This class represents a DistributionDisplayCommand.
 * A DistributionDisplayCommand is a Command that displays the distribution of a portfolio
 * at a specific date. The distribution includes (a) the stock itself (b) the
 * value of each individual stock in the portfolio. The sum of the values in (b) are equal to the
 * value of that portfolio on that date.
 */
public class DistributionDisplayCommand implements Command {
  private CommandInfoImpl context;

  /**
   * Constructs a DistributionDisplayCommand object.
   * Takes in a DistributionDisplayCommand object that passes the Scanner and Appendable
   * from the controller to execute its function with.
   *
   * @param context CommandInfo object that contains Scanner, Appendable.
   */
  public DistributionDisplayCommand(CommandInfoImpl context) {
    this.context = context;
  }

  /**
   * Runs the DistributionDisplayCommand.
   * Processes user input to display the distribution of a given portfolio.
   */
  @Override
  public void run() {
    boolean quit = false;
    while (!quit) {
      String portfolioName = getPortfolioName();
      if (portfolioName == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      LocalDate date = getValidDate(portfolioName);
      if (date == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      displayDistribution(portfolioName, date);
      context.getView().writeMessage("Returning back to main menu.\n");
      break;
    }
  }

  /**
   * Prompts the user to enter a portfolio name and validates it.
   *
   * @return A valid portfolio name or null if the user quits.
   */
  private String getPortfolioName() {
    while (true) {
      context.getView().writeMessage("Which portfolio do you want to get "
              + "performance of? Or press quit to exit.\n");
      String portfolioName = this.context.getScanner().next();
      if (isQuit(portfolioName)) {
        return null;
      }
      if (Utils.getPortfolios().containsKey(portfolioName)) {
        return portfolioName;
      }
      context.getView().writeMessage("Portfolio does not exist yet, "
              + "please enter a different name or quit:\n");
    }
  }

  /**
   * Asks and checks that the date the user provided is valid or not.
   *
   * @param portfolioName the portfolio to check the dates for.
   * @return the date if valid, null if the user quit.
   */
  private LocalDate getValidDate(String portfolioName) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    while (true) {
      context.getView().writeMessage("Enter the date (yyyy-MM-dd) you would like to display the"
              + " distribution for:\n");
      String dateInput = context.getScanner().next();
      if (isQuit(dateInput)) {
        return null;
      }
      try {
        LocalDate date = LocalDate.parse(dateInput, formatter);
        if (Utils.getPortfolios().get(portfolioName).isValidDateForAll(date)) {
          return date;
        }
        context.getView().writeMessage("Invalid date for portfolio, please try again:\n");
      } catch (DateTimeParseException e) {
        context.getView().writeMessage("Invalid date entry, please try again:\n");
      }
    }
  }

  /**
   * Displays the distribution of the portfolio on the given date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date for which the distribution is displayed.
   */
  private void displayDistribution(String portfolioName, LocalDate date) {
    double total = 0.0;
    for (Map.Entry<String, Stock> entry : Utils.getPortfolios().get(portfolioName)
            .getListOfStocks().entrySet()) {
      double stockValue = calculateStockValue(entry.getValue(), date);
      total += stockValue;
      context.getView().writeMessage("Values for: " + entry.getKey() + " " + stockValue + ": \n");
    }
    context.getView().writeMessage("Total of: " + portfolioName
            + " is: " + total + "\n");
  }

  /**
   * Calculates the value of the stock on the given date.
   *
   * @param stock The stock object.
   * @param date  The date for which the value is calculated.
   * @return The value of the stock.
   */
  private double calculateStockValue(Stock stock, LocalDate date) {
    Double closingPrice = stock.getClosingPrice(date);
    double numShares = stock.getSharesAtDate(date);
    return closingPrice * numShares;
  }

  /**
   * Checks if the user input is a quit command.
   *
   * @param input The user input to check.
   * @return true if the input is "quit", false otherwise.
   */
  private boolean isQuit(String input) {
    if (input.equalsIgnoreCase("quit")) {
      context.getView().writeMessage("Program quit successfully.\n");
      return true;
    }
    return false;
  }
}
