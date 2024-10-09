package stocks.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Map;

import stocks.CommandInfoImpl;
import stocks.Stock;
import stocks.Utils;

/**
 * This class represents a RebalancePortfolioCommand.
 * A RebalancePortfolioCommand rebalances the number of stocks to the proper
 * ratios, given by the user. After balancing, the distribution of value of the portfolio will
 * match the intended weights.
 */
public class RebalancePortfolioCommand implements Command {
  private CommandInfoImpl context;

  /**
   * Constructs a RebalancePortfolioCommand object.
   *
   * @param context CommandInfo object that contains Scanner, Appendable.
   */
  public RebalancePortfolioCommand(CommandInfoImpl context) {
    this.context = context;
  }

  /**
   * Runs the RebalancePortfolioCommand.
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

      LocalDate date = getValidDate(portfolioName);
      if (date == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      ArrayList<Integer> percentages = getValidPercentages(portfolioName);
      if (percentages == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      rebalancePortfolio(portfolioName, percentages, date);

      break;
    }
    context.getView().writeMessage("Returning to main menu.\n");
  }

  /**
   * Prompts the user to enter a valid portfolio name.
   *
   * @return A valid portfolio name or null if the user quits.
   */
  private String getValidPortfolioName() {
    while (true) {
      context.getView().writeMessage("Which portfolio would you like to rebalance? "
              + "Or press quit to exit.\n");
      String portfolioName = context.getScanner().next();
      if (isQuit(portfolioName)) {
        return null;
      }
      if (Utils.getPortfolios().containsKey(portfolioName)) {
        return portfolioName;
      }
      context.getView().writeMessage("This portfolio does not exist.\n");
    }
  }

  /**
   * Prompts the user to enter a valid rebalance date for the specified portfolio.
   *
   * @param portfolioName The portfolio name to validate the date against.
   * @return A valid LocalDate or null if the user quits.
   */
  private LocalDate getValidDate(String portfolioName) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate date = null;
    LocalDate latestDate = Utils.getPortfolios().get(portfolioName).getLatestDate();
    while (true) {
      context.getView().writeMessage("Enter the date (yyyy-MM-dd) you would like "
              + "to rebalance the portfolio at:\n"
              + "Make sure the date exists for all stocks.\n");
      try {
        String s = context.getScanner().next();
        if (isQuit(s)) {
          return null;
        }

        date = LocalDate.parse(s, formatter);
        if (date.isBefore(latestDate) || !Utils.getPortfolios().get(portfolioName)
                .isValidDateForAll(latestDate)) {
          context.getView().writeMessage("Cannot alter past transactions or not a valid"
                  + " date for all stocks in portfolio,\n"
                  + "please enter a new date:\n");
          continue;
        }
        if (!Utils.dateExists(date, portfolioName)) {
          context.getView().writeMessage("This date does not exist for one or more stocks."
                  + " Please enter a new date.\n");
          continue;
        }
        return date;
      } catch (DateTimeParseException e) {
        context.getView().writeMessage("Invalid date entry, please try again:\n");
      }
    }
  }

  /**
   * Prompts the user to enter valid percentages for rebalancing.
   *
   * @param portfolioName The portfolio name for which percentages are entered.
   * @return A list of valid percentages or null if the user quits.
   */
  private ArrayList<Integer> getValidPercentages(String portfolioName) {
    ArrayList<String> tickers = new ArrayList<>();

    for (Map.Entry<String, Stock> entry : Utils.getPortfolios().get(portfolioName)
            .getListOfStocks().entrySet()) {
      tickers.add(entry.getValue().getTicker());
    }

    context.getView().writeMessage("Stocks in portfolio:\n");

    for (String ticker : tickers) {
      context.getView().writeMessage(ticker + "\n");
    }

    int numPercentages = tickers.size();
    context.getView().writeMessage("You have " + numPercentages
            + " stocks so enter that amount of values for the percentages (whole numbers)\n");
    context.getView().writeMessage("Enter the percentages in order of how the"
            + " stocks were displayed.\n");

    ArrayList<Integer> percentages = new ArrayList<>();
    while (true) {
      int sum = 0;
      percentages.clear();

      for (int i = 0; i < numPercentages; i++) {
        try {
          String s = context.getScanner().next();
          if (isQuit(s)) {
            return null;
          }
          int percentage = Integer.parseInt(s);
          if (percentage < 0) {
            context.getView().writeMessage("Percentages cannot be negative. "
                    + "Please reenter your value:\n");
            continue;
          }
          percentages.add(percentage);
          sum += percentage;
        } catch (NumberFormatException e) {
          context.getView().writeMessage("Invalid input, please enter a number.\n");
          continue;
        }
      }

      if (sum != 100) {
        context.getView().writeMessage("Total percentages do not add up to 100, try again.\n");
      } else {
        break;
      }
    }

    return percentages;
  }

  /**
   * Rebalances the portfolio based on the specified percentages.
   *
   * @param portfolioName The portfolio name to rebalance.
   * @param percentages   The list of percentages for rebalancing.
   * @param date          The date to perform rebalancing.
   */
  private void rebalancePortfolio(String portfolioName, ArrayList<Integer> percentages,
                                  LocalDate date) {
    ArrayList<String> tickers = new ArrayList<>();
    ArrayList<Double> stockTotals = new ArrayList<>();

    double total = 0.0;
    for (Map.Entry<String, Stock> entry : Utils.getPortfolios().get(portfolioName)
            .getListOfStocks().entrySet()) {
      double stockValue = 0.0;
      Double closingPrice = entry.getValue().getClosingPrice(date);
      if (closingPrice != null) {
        double numShares = entry.getValue().getNumShares();
        stockValue = closingPrice * numShares;
        total += stockValue;
      }
      stockTotals.add(stockValue);
      tickers.add(entry.getValue().getTicker());
    }

    for (int i = 0; i < percentages.size(); i++) {
      double newAmount = total / 100 * percentages.get(i);
      double difference = stockTotals.get(i) - newAmount;
      double newShares = difference / Utils.getPortfolios().get(portfolioName)
              .getListOfStocks().get(tickers.get(i)).getClosingPrice(date);

      if (difference > 0) {
        Stock replacement = Utils.getPortfolios().get(portfolioName).getListOfStocks()
                .get(tickers.get(i)).decreaseShares(date, newShares);
        context.getView().writeMessage("Readjusted: Sold " + newShares + " of "
                + tickers.get(i) + "\n");
        Utils.getPortfolios().get(portfolioName).getListOfStocks()
                .replace(tickers.get(i), replacement);
      } else {
        newShares = Math.abs(newShares);
        Stock replacement = Utils.getPortfolios().get(portfolioName).getListOfStocks()
                .get(tickers.get(i)).increaseShares(date, newShares);
        context.getView().writeMessage("Readjusted: Bought " + newShares + " of "
                + tickers.get(i) + "\n");
        Utils.getPortfolios().get(portfolioName).getListOfStocks()
                .replace(tickers.get(i), replacement);
      }
    }

    context.getView().writeMessage("Portfolio " + portfolioName + " readjusted.\n");
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
