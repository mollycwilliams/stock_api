package stocks.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import stocks.CommandInfoImpl;
import stocks.Portfolio;
import stocks.Stock;
import stocks.StockBuilder;
import stocks.StockTicker;
import stocks.Utils;

import static stocks.StockBuilder.makeStock;

/**
 * This class represents a PurchaseStockCommand.
 * A PurchaseStockCommand is a Command that purchases a given amount of shares
 * for a given stock. The stock is then added to the proper portfolio if it doesn't exist,
 * or its number of shares increases by the given amount.
 */
public class PurchaseStockCommand implements Command {
  private CommandInfoImpl context;

  /**
   * Constructs a PurchaseStockCommand object.
   * Takes in a CommandInfo object that contains Scanner, Appendable.
   *
   * @param context CommandInfo object that contains Scanner, Appendable.
   */
  public PurchaseStockCommand(CommandInfoImpl context) {
    this.context = context;
  }

  /**
   * Runs the PurchaseStockCommand.
   * Processes user input to purchase a given stock with the inputted number of shares.
   *
   * @throws IOException if user input cannot be parsed.
   */
  @Override
  public void run() throws IOException {
    boolean quit = false;
    while (!quit) {
      String portfolioName = getValidPortfolioName();
      if (portfolioName == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      String ticker = getValidTicker(portfolioName);
      if (ticker == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      LocalDate date = getValidPurchaseDate(ticker, portfolioName);
      if (date == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      double amountShares = getValidAmountShares();
      if (amountShares == -1) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      purchase(portfolioName, ticker, date, amountShares);
      break;
    }
    context.getView().writeMessage("Returning back to main menu.\n");
  }

  /**
   * Prompts the user to enter a valid portfolio name.
   *
   * @return A valid portfolio name or null if the user quits.
   */
  private String getValidPortfolioName() {
    while (true) {
      context.getView().writeMessage("What portfolio would you like to add "
              + "this stock into? Or press quit to exit.\n");
      String portfolioName = context.getScanner().next();
      if (isQuit(portfolioName)) {
        return null;
      }
      if (Utils.getPortfolios().containsKey(portfolioName)) {
        return portfolioName;
      }
      context.getView().writeMessage("Portfolio does not exist yet, please enter a "
              + "different name or quit:\n");
    }
  }

  /**
   * Prompts the user to enter a valid stock ticker.
   *
   * @return A valid stock ticker or null if the user quits.
   */
  private String getValidTicker(String name) {
    while (true) {
      context.getView().writeMessage("What stock would you like to add to " + name + "?\n");
      String ticker = context.getScanner().next();
      if (isQuit(ticker)) {
        return null;
      }
      if (StockTicker.isValidTicker(ticker)) {
        return ticker;
      }
      context.getView().writeMessage(ticker + " is not a valid ticker, try again.\n");
    }
  }

  /**
   * Prompts the user to enter a valid purchase date for the specified stock.
   *
   * @param ticker The stock ticker to validate the date against.
   * @return A valid LocalDate or null if the user quits.
   */
  private LocalDate getValidPurchaseDate(String ticker, String portfolioName) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    while (true) {
      context.getView().writeMessage("What date do you want to purchase this on (yyyy-MM-dd)?\n");
      String dateInput = context.getScanner().next();
      if (isQuit(dateInput)) {
        return null;
      }
      try {
        LocalDate date = LocalDate.parse(dateInput, formatter);
        Stock toCheck = StockBuilder.makeStock(ticker);
        Utils.getStocks().putIfAbsent(ticker, toCheck);
        if (!Utils.isValidDate(date, ticker) || date.isBefore(Utils.getPortfolios()
                .get(portfolioName).getPurchaseDate())) {
          context.getView().writeMessage("Invalid date or not chronological, try again.\n");
          continue;
        }
        return date;
      } catch (DateTimeParseException | IOException e) {
        Utils.getStocks().remove(ticker);
        context.getView().writeMessage("Invalid date format, please try again.\n");
      }
    }
  }

  /**
   * Prompts the user to enter a valid amount of shares to purchase.
   *
   * @return A valid amount of shares or -1 if the user quits.
   */
  private double getValidAmountShares() {
    while (true) {
      context.getView().writeMessage("How many shares would you like to add?\n");
      String amountInput = context.getScanner().next();
      if (isQuit(amountInput)) {
        return -1;
      }
      try {
        double amountShares = Double.parseDouble(amountInput);
        if (amountShares % 1 != 0) {
          context.getView().writeMessage("Cannot purchase fractional shares. Enter a new value:\n");
          continue;
        }
        if (amountShares <= 0.0) {
          context.getView().writeMessage("Need to add at least 1 share. Enter a new value:\n");
          continue;
        }
        return amountShares;
      } catch (NumberFormatException e) {
        context.getView().writeMessage("Invalid input, please enter a number.\n");
      }
    }
  }

  /**
   * Processes the purchase of the stock.
   *
   * @param portfolioName The portfolio name to add the stock into.
   * @param ticker        The stock ticker to purchase.
   * @param date          The purchase date.
   * @param amountShares  The number of shares to purchase.
   */
  private void purchase(String portfolioName, String ticker, LocalDate date, double amountShares)
          throws IOException {
    context.getView().writeMessage("Adding " + amountShares + " of " + ticker + " to portfolio "
            + portfolioName + " purchased at " + date + "\n");
    Portfolio newPort = null;
    Stock existingStock = Utils.getPortfolios().get(portfolioName).getListOfStocks().get(ticker);
    if (existingStock != null) {
      Stock updatedStock = existingStock.increaseShares(date, amountShares);
      newPort = Utils.getPortfolios().get(portfolioName)
              .addStockAfterCreation(updatedStock);
    } else {
      Stock newStock = makeStock(ticker);
      newStock = newStock.increaseShares(date, amountShares);
      newPort = Utils.getPortfolios().get(portfolioName).addStockAfterCreation(newStock);
    }
    Utils.getPortfolios().replace(portfolioName, newPort);
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
