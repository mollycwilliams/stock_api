package stocks;

import java.io.File;

import java.time.LocalDate;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utils for the stock program used to perform misc. tasks for both stocks and portfolios,
 * using operations with the stored static maps 'stocks' and 'portfolios'. These include
 * but are not limited to saving a portfolio to a csv, checking if a stock is valid, a date
 * exists, etc.
 */
public class Utils {
  private static Map<String, Stock> stocks = new HashMap<>();
  private static Map<String, Portfolio> portfolios = new HashMap<>();

  /**
   * Returns the list of portfolios stored in this Utils class.
   *
   * @return global list of portfolios.
   */
  public static Map<String, Portfolio> getPortfolios() {
    return portfolios;
  }

  /**
   * Returns the list of stocks stored in this Utils class.
   *
   * @return global list of stocks.
   */
  public static Map<String, Stock> getStocks() {
    return stocks;
  }

  /**
   * Saves the given portfolio to a csv with the given portfolioName.
   * Constructs a "portfolioName.csv" file by parsing its data into a csv file.
   * Saves the file to our database of portfolios, "PortfolioData/".
   *
   * @param portfolio     the portfolio to save.
   * @param portfolioName the portfolio name.
   */
  public static void savePortfolioCSV(Portfolio portfolio, String portfolioName) {
    File file = new File("PortfolioData/" + portfolioName + ".csv");

    try (FileWriter writer = new FileWriter(file)) {
      // header
      writer.append("ticker,numShares,shareDates\n");

      StringBuilder resultString = new StringBuilder();
      for (Map.Entry<String, Stock> stockEntry : portfolio.getListOfStocks().entrySet()) {
        if (stockEntry.getValue() == null) {
          throw new IllegalArgumentException("Stock is null.\n");
        }
        Stock stock = stockEntry.getValue();
        StringBuilder s = new StringBuilder();
        s.append(stock.getTicker() + ",");
        s.append(stock.getNumShares() + ",");
        for (Map.Entry<LocalDate, Double> entry : stock.getShareDates().entrySet()) {
          s.append(entry.getKey() + ",");
          s.append(entry.getValue() + ",");
          resultString.append(s);
        }
        resultString.append("\n");
      }

      writer.append(resultString.toString());
    } catch (IOException e) {
      // CSV not saved.
    }
  }


  /**
   * Static method returning if the ticker is a valid stock in the stock market.
   *
   * @param ticker the ticker symbol for the stock that is being checked.
   * @return if the ticker is a valid stock in the stock market.
   */
  public static boolean isValidStock(String ticker) {
    return getStocks().get(ticker) != null;
  }


  /**
   * Static method returning if the date for the ticker is a valid date.
   *
   * @param date   the LocalDate object of the specific date.
   * @param ticker the ticker symbol for the stock that is being checked.
   * @return if the date for the ticker is a valid date.
   */
  public static boolean isValidDate(LocalDate date, String ticker) {
    if (date.isBefore(LocalDate.MIN) || date.isAfter(LocalDate.MAX)) {
      return false;
    }
    Stock toCheck = getStocks().get(ticker);
    return toCheck.getOpeningPrice(date) != null;
  }

  /**
   * Determines if the given date exists for all stocks within a given portfolio.
   *
   * @param date          the date to check amongst the stocks.
   * @param portfolioName the name of the portfolio to check.
   * @return true if it exists in all stocks, false if not.
   */
  public static boolean dateExists(LocalDate date, String portfolioName) {
    for (Map.Entry<String, Stock> entry :
            Utils.getPortfolios().get(portfolioName).getListOfStocks().entrySet()) {
      if (!isValidDate(date, entry.getKey())) {
        return false;
      }
    }
    return true;
  }

}
