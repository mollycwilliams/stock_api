package stocks.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import stocks.CommandInfoImpl;
import stocks.Stock;
import stocks.Utils;

/**
 * This class represents a PerformanceAllCommand.
 * A PerformanceAllCommand is a Command that calculates the
 * total performance for a given portfolio.
 */
public class PerformanceAllCommand implements Command {
  private CommandInfoImpl context;

  /**
   * Constructs a PerformanceAllCommand object.
   * Takes in a CommandInfo object that passes the Scanner and Appendable
   * from the controller to execute its function with.
   *
   * @param context CommandInfo object that contains Scanner, Appendable.
   */
  public PerformanceAllCommand(CommandInfoImpl context) {
    this.context = context;
  }

  /**
   * Runs the PerformanceAllCommand.
   * Processes user input to calculate the total performance of the portfolio.
   */
  @Override
  public void run() throws IOException {
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

      double total = calculatePortfolioPerformance(portfolioName, date);
      context.getView().writeMessage("Total of: "
              + portfolioName + " is: " + total + "\n");

      displayStockShares(portfolioName, date);
      context.getView().writeMessage("Returning to menu.\n");
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
      context.getView().writeMessage("Which portfolio would you like to display? "
              + "Or press quit to exit.\n");
      String name = context.getScanner().next();
      if (isQuit(name)) {
        return null;
      }
      if (Utils.getPortfolios().containsKey(name)) {
        return name;
      }
      context.getView().writeMessage("This portfolio does not exist.\n"
              + "Please enter a new name:\n");
    }
  }

  /**
   * Prompts the user to enter a date and validates it for the given portfolio.
   *
   * @param portfolioName The name of the portfolio.
   * @return A valid LocalDate or null if the user quits.
   */
  private LocalDate getValidDate(String portfolioName) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    while (true) {
      context.getView().writeMessage("Enter the date (yyyy-MM-dd) you would "
              + "like to view the composition for:\n");
      String dateInput = context.getScanner().next();
      if (isQuit(dateInput)) {
        return null;
      }
      try {
        LocalDate date = LocalDate.parse(dateInput, formatter);
        if (validatePortfolioDate(portfolioName, date)) {
          return date;
        }
      } catch (DateTimeParseException e) {
        context.getView().writeMessage("Invalid date entry, please try again:\n");
      }
    }
  }

  /**
   * Checks if the provided date is valid for the portfolio.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date to validate.
   * @return true if the date is valid, false otherwise.
   */
  private boolean validatePortfolioDate(String portfolioName, LocalDate date) {
    LocalDate purchaseDate = Utils.getPortfolios().get(portfolioName).getPurchaseDate();
    if (date.isBefore(purchaseDate) || !Utils.getPortfolios().get(portfolioName)
            .isValidDateForAll(date)) {
      context.getView().writeMessage("Portfolio does not exist at this date or some "
              + "stocks do not have this date. Enter a new date:\n");
      return false;
    }
    return true;
  }

  /**
   * Calculates the total performance of the portfolio on the given date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date to calculate the performance for.
   * @return The total performance value.
   */
  private double calculatePortfolioPerformance(String portfolioName, LocalDate date) {
    double total = 0.0;
    if (date.isAfter(Utils.getPortfolios().get(portfolioName).getPurchaseDate())) {
      for (Map.Entry<String, Stock> entry : Utils.getPortfolios().get(portfolioName)
              .getListOfStocks().entrySet()) {
        Double closingPrice = entry.getValue().getClosingPrice(date);
        double numShares = entry.getValue().getSharesAtDate(date);
        total += closingPrice * numShares;
      }
    }
    return total;
  }

  /**
   * Displays the number of shares for each stock in the portfolio up to the given date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date to display shares for.
   */
  private void displayStockShares(String portfolioName, LocalDate date) {
    for (Map.Entry<String, Stock> entry : Utils.getPortfolios().get(portfolioName)
            .getListOfStocks().entrySet()) {
      double totalShares = 0.0;
      for (Map.Entry<LocalDate, Double> shareEntry : entry.getValue().getShareDates().entrySet()) {
        if (shareEntry.getKey().isBefore(date) || shareEntry.getKey().equals(date)) {
          totalShares += shareEntry.getValue();
        } else {
          break;
        }
      }
      context.getView().writeMessage(entry.getValue().getTicker() + " " + totalShares + "\n");
    }
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
