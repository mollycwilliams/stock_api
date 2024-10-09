package stocks;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * A PortfolioImpl that implements the Portfolio interface. Is an immutable object that contains
 * a list of stocks that will be built with a builder class.
 */
public class PortfolioImpl implements Portfolio {

  private final Map<String, Stock> listOfStocks; // immutable Key: Ticker,
  // Value: Stock of that ticker


  /**
   * PortfolioImpl constructor that initializes the list of stocks to an empty hashmap to prevent
   * null exception errors when trying to put items.
   */
  public PortfolioImpl() {
    this.listOfStocks = new HashMap<>();
  }


  /**
   * PortfolioImpl constructor that is protected which takes in a listOfStocks, and creates a
   * portfolioimpl which contains that hashmap.
   *
   * @param listOfStocks immutable Key: Ticker, Value: Stock of that ticker
   */
  protected PortfolioImpl(Map<String, Stock> listOfStocks) {
    this.listOfStocks = listOfStocks;
  }


  /**
   * PortfolioImplBuilder extends PortfolioBuilder with the purpose of building the PortfolioImpl
   * object.
   */
  public static class PortfolioImplBuilder extends PortfolioBuilder<PortfolioImplBuilder> {

    /**
     * Constructs a PortfolioImplBuilder which calls its super class.
     */
    public PortfolioImplBuilder() {
      super();
    }

    /**
     * Creates a new PortfolioImpl with a specific listOfStocks.
     *
     * @return creates a new PortfolioImpl with a specific listOfStocks.
     */
    public PortfolioImpl build() {

      return new PortfolioImpl(this.listOfStocks);
    }

    /**
     * Returns the PortfolioImplBuilder which is required during the building process.
     *
     * @return the PortfolioImplBuilder.
     */
    protected PortfolioImpl.PortfolioImplBuilder returnBuilder() {
      return this;
    }
  }

  /**
   * Returns a hashmap with the Key: ticker, Value: stock of the entire stocks that comprise the
   * portfolio.
   *
   * @return a hashmap with the Key: ticker, Value: stock of the entire stocks that comprise the
   *     portfolio.
   */
  @Override
  public Map<String, Stock> getListOfStocks() {
    return new HashMap<>(this.listOfStocks);
  }

  /**
   * Returns the earliest date available for a given portfolio.
   *
   * @return the creation date of the portfolio.
   */
  @Override
  public LocalDate getPurchaseDate() {
    if (this.listOfStocks.isEmpty()) {
      throw new IllegalStateException("No stocks purchased.\n");
    }
    LocalDate date = LocalDate.MAX;
    for (Stock stock : listOfStocks.values()) {
      for (LocalDate tempDate : stock.getShareDates().keySet()) {
        if (tempDate.isBefore(date)) {
          date = tempDate;
        }
      }
    }
    return date;
  }

  /**
   * Gets the latest possible date from the list of stocks in a portfolio.
   *
   * @return the latest existing date.
   */
  @Override
  public LocalDate getLatestDate() {
    if (this.listOfStocks.isEmpty()) {
      throw new IllegalStateException("No stocks purchased.\n");
    }
    LocalDate date = LocalDate.MIN;
    for (Stock stock : listOfStocks.values()) {
      for (LocalDate tempDate : stock.getShareDates().keySet()) {
        if (tempDate.isAfter(date)) {
          date = tempDate;
        }
      }
    }
    return date;
  }

  /**
   * Determines if the inputted date is valid for all Stocks within this portfolio.
   *
   * @param date the date to check amongst the stocks.
   * @return true if its valid for all, false if not.
   */
  @Override
  public boolean isValidDateForAll(LocalDate date) {
    for (String ticker : listOfStocks.keySet()) {
      if (!Utils.isValidDate(date, ticker)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Portfolio addStockAfterCreation(Stock stock) {
    Map<String, Stock> tempListOfStocks = this.listOfStocks;
    tempListOfStocks.put(stock.getTicker(), stock);
    return new PortfolioImpl(tempListOfStocks);
  }

  @Override
  public Portfolio removeStockAfterCreation(Stock stock) {
    Map<String, Stock> tempListOfStocks = this.listOfStocks;
    tempListOfStocks.remove(stock.getTicker());
    return new PortfolioImpl(tempListOfStocks);
  }



}
