package stocks;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static stocks.StockBuilder.makeStock;


/**
 * The abstract PortfolioBuilder class used to build a portfolio.
 *
 * @param <T> Is a subclass of PortfolioBuilder.
 */
public abstract class PortfolioBuilder<T extends PortfolioBuilder<T>> {
  protected Map<String, Stock> listOfStocks = new HashMap<String, Stock>();

  /**
   * Default constructor for PortfolioBuilder.
   */
  public PortfolioBuilder() {
    //Empty: constructs an empty builder.
  }

  /**
   * Builds the PortfolioImpl with the specific listOfStocks in PortfolioBuilder.
   *
   * @return a new PortfolioImpl with the specific listOfStocks in PortfolioBuilder.
   * @throws IllegalArgumentException if listOfStocks is null.
   */
  public PortfolioImpl build() throws IllegalArgumentException {
    if (this.listOfStocks == null) {
      throw new IllegalArgumentException("List of stocks cannot be null.");
    }
    return new PortfolioImpl(this.listOfStocks);
  }


  /**
   * Adds a given stock to a portfolio builder listOfStocks field.
   *
   * @param stock The stock of type Stock which will be added to the portfolio.
   * @return A subclass of PortfolioBuilder used to allow chaining the and the building process
   *     for creating a PortfolioImpl.
   * @throws IllegalArgumentException if the stock is not a publicly available stock in the stock
   *                                  market.
   */
  public T addStock(Stock stock) {
    if (Utils.isValidStock(stock.getTicker())) {
      this.listOfStocks.put(stock.getTicker(), stock);
    } else {
      throw new IllegalArgumentException("Not a valid ticker");
    }
    return returnBuilder();
  }


  /**
   * Returns a subclass of PortfolioBuilder with an listOfStocks field essential to building
   * a Portfolio object.
   *
   * @return a subclass of PortfolioBuilder with an listOfStocks field essential to building
   *     a Portfolio object.
   */
  protected abstract T returnBuilder();


  /**
   * Constructs a portfolio with the given portfolio name, from the DataBase of portfolios.
   * Retrieves the data for the portfolio from the PortfolioData/ folder, and constructs a
   * new portfolio with its identical data.
   *
   * @param portfolioName the name of the portfolio to be reconstructed.
   * @return the completed Portfolio.
   */
  public static Portfolio makePortfolio(String portfolioName) {

    String filePath = "PortfolioData/" + portfolioName + ".csv";
    PortfolioImpl.PortfolioImplBuilder portfolio = new PortfolioImpl.PortfolioImplBuilder();
    Map<String, Stock> portStocks = new HashMap<>();
    try {
      BufferedReader reader = Files.newBufferedReader(Paths.get(filePath));
      String line = reader.readLine();


      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");

        Stock tempStock;

        String tempTicker = "";
        double tempNumShares = 0.0;
        ArrayList<LocalDate> tempDates = new ArrayList<LocalDate>();
        ArrayList<Double> tempShares = new ArrayList<Double>();
        Map<LocalDate, Double> tempShareDates = new HashMap<LocalDate, Double>();

        tempTicker = String.valueOf(data[0]);
        tempNumShares = Double.parseDouble(data[1]);
        for (int i = 2; i < data.length; i++) {
          try {
            tempDates.add(LocalDate.parse(data[2]));
          } catch (DateTimeParseException e) {
            System.out.println("API call limit reached. Program quit. \n");
          }
          tempShares.add(Double.parseDouble(data[3]));
        }

        while (!tempDates.isEmpty()) {
          tempShareDates.put(tempDates.get(0), tempShares.get(0));
          tempDates.remove(0);
          tempShares.remove(0);
        }

        Stock stockToMake = makeStock(tempTicker);
        Stock stockMade = stockToMake.addShareDates(tempShareDates).setNumShares(tempNumShares);

        portfolio.addStock(stockMade);
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }

    return portfolio.build();
  }
}


