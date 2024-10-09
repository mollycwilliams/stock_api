package stocks.swing;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import stocks.PortfolioImpl;
import stocks.StockBuilder;

import stocks.Portfolio;
import stocks.Stock;
import stocks.Utils;

import static stocks.PortfolioBuilder.makePortfolio;

/**
 * This class is the overarching model for our GUI which represents the current user and the
 * current portfolios avaiable to them.
 */
public class GUIModelImpl implements GUIModel {
  private Portfolio currentPortfolio;

  /**
   * Constructs a new model object, i.e, a new user with a new Map of portfolios.
   */
  public GUIModelImpl() {
    // Default constructor.
  }

  /**
   * Creates a portfolio with the given portfolio name.
   *
   * @param name the name of the portfolio to create.
   */
  public void createPortfolio(String name) {
    if (Utils.getPortfolios().containsKey(name)) {
      throw new IllegalArgumentException("Portfolio already exists with that name.");
    } else {
      Utils.getPortfolios().put(name, new PortfolioImpl());
    }
  }

  /**
   * Gets an array of portfolio names.
   *
   * @return the names of all portfolios in an array.
   */
  public String[] getPortfolioNames() {
    return Utils.getPortfolios().keySet().toArray(new String[0]);
  }

  /**
   * Sets the current portfolio field to the given portfolio.
   *
   * @param name the name of the portfolio to be come the current portfolio.
   */
  public void setCurrentPortfolio(String name) {
    currentPortfolio = Utils.getPortfolios().get(name);
  }

  /**
   * Gets the current portfolio.
   *
   * @return the name of the current portfolio.
   */
  public Portfolio getPortfolio() {
    return currentPortfolio;
  }

  /**
   * Buys a specific amount of shares for the given stock on the given date.
   *
   * @param ticker the stock to purchase shares for.
   * @param shares the amount of shares to buy.
   * @param date   the date the shares are being bought on.
   */
  public void buyStock(String ticker, double shares, LocalDate date) throws IOException {
    if (currentPortfolio == null) {
      throw new IllegalStateException("No PortfolioImpl selected.");
    }

    Stock stock = currentPortfolio.getListOfStocks().get(ticker);
    if (stock == null) {
      GetDataCommand data = new GetDataCommand();
      data.run(ticker);
      try {
        stock = StockBuilder.makeStock(ticker);
        stock = stock.increaseShares(date, shares);
        currentPortfolio = currentPortfolio.addStockAfterCreation(stock);
      } catch (ArrayIndexOutOfBoundsException | IOException e) {
        throw new ArrayIndexOutOfBoundsException("Out of bounds error.");
      }
    } else {
      stock = stock.increaseShares(date, shares);
      currentPortfolio = currentPortfolio.addStockAfterCreation(stock);
    }
    for (Map.Entry<String, Portfolio> entry : Utils.getPortfolios().entrySet()) {
      if (entry.getValue() == currentPortfolio) {
        Utils.getPortfolios().put(entry.getKey(), currentPortfolio);
      }
    }
  }

  /**
   * Sells a specific amount of shares for the given stock on the given date.
   *
   * @param ticker the stock to sell shares for.
   * @param shares the amount of shares to sell.
   * @param date   the date the shares are being sold on.
   * @throws IOException if an exception is thrown.
   */
  public void sellStock(String ticker, double shares, LocalDate date) {
    if (currentPortfolio == null) {
      throw new IllegalStateException("No PortfolioImpl selected.");
    }

    Stock stock = currentPortfolio.getListOfStocks().get(ticker);
    try {
      stock = stock.decreaseShares(date, shares);
      currentPortfolio = currentPortfolio.addStockAfterCreation(stock);
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("Stock " + ticker + " not found in PortfolioImpl.");
    }
    String name = currentPortfolio.toString();
    for (Map.Entry<String, Portfolio> entry: Utils.getPortfolios().entrySet()) {
      if (entry.getValue() == currentPortfolio) {
        name = entry.getKey();
      }
    }
    Utils.getPortfolios().put(name, currentPortfolio);
  }

  /**
   * Saves the given portfolio to the PortfolioData directory.
   *
   * @param name the name of the portfolio to save.
   * @throws IOException if an exception is thrown.
   */
  public void savePortfolio(String name) throws IOException {
    Portfolio p = Utils.getPortfolios().get(name);
    if (p == null) {
      throw new IllegalArgumentException("PortfolioImpl " + name + " not found.");
    }
    Utils.savePortfolioCSV(p, name);
  }

  /**
   * Retrieves a portfolio from the PortfolioData directory.
   *
   * @param portfolioName the name of th   e portfolio to retrieve.
   * @throws IOException if an exception is thrown.
   */
  public void retrievePortfolio(String portfolioName) throws IOException {
    Portfolio madePortfolio = makePortfolio(portfolioName);
    Utils.getPortfolios().put(portfolioName, madePortfolio);
  }

}

