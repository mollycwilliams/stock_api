package stocks;

import java.time.LocalDate;
import java.util.Map;

/**
 * Represents the Portfolio interface for the stock program. A Portfolio lets users have a list
 * of stocks that they can add or remove from, and perform insightable actions on such as getting
 * the performance of the entire portfolio.
 */
public interface Portfolio {

  /**
   * Returns a hashmap with the Key: ticker, Value: stock of the entire stocks that comprise the
   *     portfolio.
   *
   * @return a hashmap with the Key: ticker, Value: stock of the entire stocks that comprise the
   *     portfolio.
   */
  Map<String, Stock> getListOfStocks();

  /**
   * Returns the earliest date available for a given portfolio.
   * @return the creation date of the portfolio.
   */
  LocalDate getPurchaseDate();

  /**
   * Gets the latest possible date from the list of stocks in a portfolio.
   * @return the latest existing date.
   */
  LocalDate getLatestDate();

  /**
   * Determines if the inputted date is valid for all Stocks within this portfolio.
   * @param date the date to check amongst the stocks.
   * @return true if its valid for all, false if not.
   */
  boolean isValidDateForAll(LocalDate date);

  /**
   * Returns a new portfolio with a list of stocks that contains the stock being added.
   * @param stock the stock to be added to the portfolio.
   * @return a new portfolio with a list of stocks that contains the stock being added.
   */
  Portfolio addStockAfterCreation(Stock stock);

  /**
   * Returns a new portfolio with a list of stocks that removes the stock.
   * @param stock the stock to be removed from the portfolio.
   * @return a new portfolio with a list of stocks that removes the stock.
   */
  Portfolio removeStockAfterCreation(Stock stock);
}
