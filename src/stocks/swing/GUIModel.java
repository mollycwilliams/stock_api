package stocks.swing;

import java.io.IOException;
import java.time.LocalDate;

import stocks.Portfolio;

/**
 * Interface for the GUI Model. Used to perform calculations to effect how the user
 * interacts with the GUI.
 */
public interface GUIModel {

  /**
   * Creates a portfolio with the given portfolio name.
   *
   * @param name the name of the portfolio to create.
   */
  void createPortfolio(String name);

  /**
   * Gets an array of portfolio names.
   *
   * @return the names of all portfolios in an array.
   */
  String[] getPortfolioNames();

  /**
   * Sets the current portfolio field to the given portfolio.
   *
   * @param name the name of the portfolio to be come the current portfolio.
   */
  void setCurrentPortfolio(String name);

  /**
   * Gets the current portfolio.
   *
   * @return the name of the current portfolio.
   */
  Portfolio getPortfolio();

  /**
   * Buys a specific amount of shares for the given stock on the given date.
   *
   * @param ticker the stock to purchase shares for.
   * @param shares the amount of shares to buy.
   * @param date   the date the shares are being bought on.
   * @throws IOException if an exception is thrown.
   */
  void buyStock(String ticker, double shares, LocalDate date) throws IOException;

  /**
   * Sells a specific amount of shares for the given stock on the given date.
   *
   * @param ticker the stock to sell shares for.
   * @param shares the amount of shares to sell.
   * @param date   the date the shares are being sold on.
   * @throws IOException if an exception is thrown.
   */
  void sellStock(String ticker, double shares, LocalDate date);

  /**
   * Saves the given portfolio to the PortfolioData directory.
   *
   * @param name the name of the portfolio to save.
   * @throws IOException if an exception is thrown.
   */
  void savePortfolio(String name) throws IOException;

  /**
   * Retrieves a portfolio from the PortfolioData directory.
   *
   * @param portfolioName the name of th   e portfolio to retrieve.
   * @throws IOException if an exception is thrown.
   */
  void retrievePortfolio(String portfolioName) throws IOException;

}
