package stocks;

import java.time.LocalDate;
import java.util.Map;

/**
 * Represents a stock in the program. A Stock is a share of the ownership of a company.
 * This will be immutable.
 */
public interface Stock {

  /**
   * Gets the ticker of the Stock.
   *
   * @return the ticker of the Stock as a String.
   */
  String getTicker();

  /**
   * Gets the number of shares of the stock.
   *
   * @return the number of shares of the stock as an integer.
   */
  Double getNumShares();

  /**
   * Gets the Volume of the stock at a specific date.
   *
   * @param date A LocalDate object representing a specific date.
   * @return A long representing the volume of the stock on the date.
   */
  Long getVolume(LocalDate date);

  /**
   * Gets the opening price of the stock at a specific date.
   *
   * @param date A LocalDate object representing a specific date.
   * @return A double representing the opening price of the stock on the date.
   */
  Double getOpeningPrice(LocalDate date);

  /**
   * Gets the closing price of the stock at a specific date.
   *
   * @param date A LocalDate object representing a specific date.
   * @return A double representing the closing price of the stock on the date.
   */
  Double getClosingPrice(LocalDate date);

  /**
   * Gets the high price of the stock at a specific date.
   *
   * @param date A LocalDate object representing a specific date.
   * @return A double representing the high price of the stock on the date.
   */
  Double getHighPrice(LocalDate date);

  /**
   * Gets the low price of the stock at a specific date.
   *
   * @param date A LocalDate object representing a specific date.
   * @return A double representing the low price of the stock on the date.
   */
  Double getLowPrice(LocalDate date);

  /**
   * Returns a new stock object with the same fields but its number of shares incremented by
   * a specific amount.
   *
   * @param num An integer representing the number of shares the stock will be incremented by.
   * @param date the date to increase shares at.
   * @return A new stock object with the same fields but its number of shares incremented by
   *     a specific amount.
   */
  Stock increaseShares(LocalDate date, double num);

  /**
   * Returns a new stock object with the same fields but its number of shares decremented by
   * a specific amount.
   *
   * @param num An integer representing the number of shares the stock will be decremented by.
   * @param date the date to decrease shares at.
   * @return A new stock object with the same fields but its number of shares decremented by
   *     a specific amount.
   * @throws IllegalArgumentException if num is greater than the amount of shares it currently has.
   */
  Stock decreaseShares(LocalDate date, double num) throws IllegalArgumentException;

  /**
   * Returns the list of shareDates for this stock.
   * @return a Map that maps the date purchased to the amount of shares bought.
   */
  Map<LocalDate, Double> getShareDates();

  /**
   * Determines the latest date existing for this stock through its transactions.
   * @return the latest valid date for a stock through its transactions.
   */
  LocalDate lastDate();

  /**
   * Gets the number of shares purchased at the inputted date.
   * @param date the date purchased.
   * @return the number of shares bought at that date.
   */
  Double getSharesAtDate(LocalDate date);

  /**
   * Adds a map of shareDates to this stock.
   * @param shareDates the map to be added.
   * @return a clone stock with the added dates.
   */
  Stock addShareDates(Map<LocalDate, Double> shareDates);

  /**
   * Sets the number of shares for a given stock to the inputted value.
   * @param shares the amount of shares to be set.
   * @return a clone stock with the added numShares.
   */
  Stock setNumShares(Double shares);

}

