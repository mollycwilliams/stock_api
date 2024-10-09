package stocks.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import stocks.CommandInfoImpl;
import stocks.Portfolio;
import stocks.Stock;
import stocks.StockTicker;
import stocks.Utils;

/**
 * This class represents a SellStockCommand.
 * A SellStockCommand sells a given amount of shares for a given stock.
 * The stock is then removed from the portfolio if it reaches 0 shares, or its number of shares
 * decreases.
 */
public class SellStockCommand implements Command {
  private CommandInfoImpl context;

  /**
   * Constructs a SellStockCommand object.
   *
   * @param context CommandInfo object that contains Scanner, Appendable.
   */
  public SellStockCommand(CommandInfoImpl context) {
    this.context = context;
  }

  /**
   * Runs the SellStockCommand.
   *
   * @throws IOException if user input cannot be parsed.
   */
  @Override
  public void run() throws IOException {
    while (true) {
      String portfolioName = getPortfolioName();
      if (portfolioName == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      String ticker = getStockTicker(portfolioName);
      if (ticker == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      LocalDate date = getValidDate(portfolioName, ticker);
      if (date == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      double amountShares = getShares(portfolioName, ticker, date);
      if (amountShares <= 0) {
        break;
      }

      sell(portfolioName, ticker, date, amountShares);

      break;
    }
    context.getView().writeMessage("Returning to main menu.\n");
  }

  /**
   * Prompts the user for the portfolio name.
   *
   * @return The portfolio name entered by the user or null if user quits.
   */
  private String getPortfolioName() {
    context.getView().writeMessage("What portfolio would you like to sell this stock in? "
            + "Or press quit to exit.\n");
    String portfolioName = context.getScanner().next();
    if (portfolioName.equals("quit")) {
      context.getView().writeMessage("Program quit successfully.\n");
      return null;
    }

    while (Utils.getPortfolios().get(portfolioName) == null ||
            Utils.getPortfolios().get(portfolioName).getListOfStocks().isEmpty()) {
      context.getView().writeMessage("Portfolio does not exist or has no stocks, please enter "
              + "a valid name or quit:\n");
      portfolioName = context.getScanner().next();
      if (portfolioName.equals("quit")) {
        context.getView().writeMessage("Program quit successfully.\n");
        return null;
      }
    }

    return portfolioName;
  }

  /**
   * Prompts the user for the stock ticker.
   *
   * @param portfolioName The name of the portfolio.
   * @return The stock ticker entered by the user or null if user quits.
   */
  private String getStockTicker(String portfolioName) {
    context.getView().writeMessage("What stock would you like to sell in " + portfolioName + "?\n");
    String ticker = context.getScanner().next();
    if (ticker.equals("quit")) {
      context.getView().writeMessage("Program quit successfully.\n");
      return null;
    }

    while (!StockTicker.isValidTicker(ticker) &&
            Utils.getPortfolios().get(portfolioName).getListOfStocks().get(ticker)
                    .getNumShares() == 0) {
      context.getView().writeMessage("Invalid ticker or no shares available, try again or type "
              + "'quit' to exit:\n");
      ticker = context.getScanner().next();
      if (ticker.equals("quit")) {
        context.getView().writeMessage("Program quit successfully.\n");
        return null;
      }
    }

    return ticker;
  }

  /**
   * Prompts the user for the date of the sale.
   *
   * @param portfolioName The name of the portfolio.
   * @param ticker        The stock ticker.
   * @return The sale date entered by the user or null if user quits.
   */
  private LocalDate getValidDate(String portfolioName, String ticker) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate date = null;

    while (true) {
      context.getView().writeMessage("What date do you want to sell this on (yyyy-MM-dd)?\n");
      String inputDate = context.getScanner().next();
      if (inputDate.equals("quit")) {
        context.getView().writeMessage("Program quit successfully.\n");
        return null;
      }

      try {
        date = LocalDate.parse(inputDate, formatter);
        if (!Utils.isValidDate(date, ticker) ||
                date.isBefore(Utils.getPortfolios().get(portfolioName).getListOfStocks()
                        .get(ticker).lastDate())) {
          context.getView().writeMessage("Invalid date or not chronological, try again.\n");
          continue;
        }
        break;
      } catch (DateTimeParseException e) {
        context.getView().writeMessage("Invalid date format, please try again.\n");
      }
    }

    return date;
  }

  /**
   * Prompts the user for the amount of shares to sell.
   *
   * @param portfolioName The name of the portfolio.
   * @param ticker        The stock ticker.
   * @param date          The date of the sale.
   * @return The amount of shares entered by the user.
   */
  private double getShares(String portfolioName, String ticker, LocalDate date) {
    context.getView().writeMessage("Currently you have " + Utils.getPortfolios()
            .get(portfolioName).getListOfStocks().get(ticker).getSharesAtDate(date) + " shares of "
            + ticker + ".\n");

    double amountShares = 0;
    while (true) {
      context.getView().writeMessage("How many shares would you like to sell?\n");
      String inputShares = context.getScanner().next();
      if (inputShares.equals("quit")) {
        context.getView().writeMessage("Program quit successfully.\n");
        return 0;
      }

      try {
        amountShares = Double.parseDouble(inputShares);
        if (amountShares <= 0) {
          context.getView().writeMessage("Cannot sell 0 or negative shares.\n");
          continue;
        } else if (Utils.getPortfolios().get(portfolioName).getListOfStocks().get(ticker)
                .getSharesAtDate(date) - amountShares < 0) {
          context.getView().writeMessage("Cannot sell more than what you have.\n");
          continue;
        }
        break;
      } catch (NumberFormatException e) {
        context.getView().writeMessage("Invalid input, please enter a number.\n");
      }
    }

    return amountShares;
  }

  /**
   * Processes the stock sale based on user input.
   *
   * @param portfolioName The name of the portfolio.
   * @param ticker        The stock ticker.
   * @param date          The date of the sale.
   * @param amountShares  The amount of shares to sell.
   */
  private void sell(String portfolioName, String ticker, LocalDate date, double amountShares) {
    Stock stock = Utils.getPortfolios().get(portfolioName).getListOfStocks().get(ticker);
    Stock replacement = stock.decreaseShares(date, amountShares);

    if (replacement.getNumShares() == 0) {
      context.getView().writeMessage("Sold entire share, removing from portfolio.\n");
      Portfolio newPort = Utils.getPortfolios().get(portfolioName)
              .removeStockAfterCreation(replacement);
      Utils.getPortfolios().replace(portfolioName, newPort);
    } else {
      context.getView().writeMessage("Removed " + amountShares + " shares of "
              + ticker + " from portfolio " + portfolioName + " sold at " + date + "\n");
      Portfolio newPort = Utils.getPortfolios().get(portfolioName)
              .removeStockAfterCreation(replacement);
      Utils.getPortfolios().replace(portfolioName, newPort);
    }
  }
}
