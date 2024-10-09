package stocks;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.nio.file.Paths;
import java.util.TreeMap;


/**
 * Abstract StockBuilder which builds Stock objects of type Stock.
 *
 * @param <T> A subclass of StockBuilder.
 */
public abstract class StockBuilder<T extends StockBuilder<T>> {
  protected String ticker;
  protected double numShares;
  protected ArrayList<LocalDate> dates = new ArrayList<>();
  protected Map<LocalDate, Long> volume = new HashMap<>();
  protected Map<LocalDate, Double> openingPrice = new HashMap<LocalDate, Double>();
  protected Map<LocalDate, Double> closingPrice = new HashMap<>();
  protected Map<LocalDate, Double> highPrice = new HashMap<LocalDate, Double>();
  protected Map<LocalDate, Double> lowPrice = new HashMap<LocalDate, Double>();
  protected Map<LocalDate, Double> shareDates = new TreeMap<>();

  /**
   * Builds the StockImpl with the specific fields in StockBuilder.
   *
   * @return A new Stock with the fields of StockBuilder.
   */
  public abstract Stock build();


  /**
   * Adds the ticker field to the StockBuilder.
   *
   * @param ticker the stock ticker.
   * @return A StockBuilder with the updated ticker field.
   */
  public T addTicker(String ticker) {
    this.ticker = ticker;
    return returnBuilder();
  }


  /**
   * Adds the specific closing price on the specific date to the closingPrice field in StockBuilder.
   *
   * @param date  the specific date
   * @param price the closing price for that date
   * @return A StockBuilder with it's closing price field updated to include
   *     the closing price field on the specific date.
   */
  public T addClosingPrice(LocalDate date, double price) {
    this.closingPrice.put(date, price);
    return returnBuilder();
  }

  /**
   * Adds the specific opening price on the specific date to the openingPrice field in StockBuilder.
   *
   * @param date  the specific date
   * @param price the opening price for that date
   * @return A StockBuilder with its opening price field updated to include the opening price field
   *     on the specific date.
   */
  public T addOpeningPrice(LocalDate date, double price) {
    this.openingPrice.put(date, price);
    return returnBuilder();
  }

  /**
   * Adds the specific high price on the specific date to the highPrice field in StockBuilder.
   *
   * @param date  the specific date
   * @param price the high price for that date
   * @return A StockBuilder with its high price field updated to include the high price field
   *     on the specific date.
   */
  public T addHighPrice(LocalDate date, double price) {
    this.highPrice.put(date, price);
    return returnBuilder();
  }

  /**
   * Adds the specific low price on the specific date to the lowPrice field in StockBuilder.
   *
   * @param date  the specific date
   * @param price the low price for that date
   * @return A StockBuilder with its low price field updated to include the low price field
   *     on the specific date.
   */
  public T addLowPrice(LocalDate date, double price) {
    this.lowPrice.put(date, price);
    return returnBuilder();
  }

  /**
   * Returns a subclass of StockBuilder with all its fields which is essential to building
   * a Stock object.
   *
   * @return a subclass of StockBuilder with all its fields which is essential to building
   *     a Stock object.
   */
  protected abstract T returnBuilder();

  /**
   * Returns a subclass of StockBuilder with the volume at a specific date put into the
   * volume field.
   *
   * @param date   the specific date.
   * @param volume the volume on that date.
   * @return a subclass of StockBuilder with the volume at a specific date put into the
   *     volume field.
   */
  public T addVolume(LocalDate date, long volume) {
    this.volume.put(date, volume);
    return returnBuilder();
  }

  /**
   * Return a subclass of StockBuilder with incremented numShares field.
   *
   * @param date the date to add shares at.
   * @param shares the amount of shares to be incremented.
   * @return a subclass of StockBuilder with incremented numShares field.
   */
  public T addShares(LocalDate date, double shares) {
    this.numShares += shares;
    this.shareDates.put(date, shares);
    return returnBuilder();
  }


  /**
   * Return a subclass of StockBuilder with decremented numShares field.
   *
   * @param shares the amount of shares to be decremented.
   * @param date  the date to add shares at.
   * @return a subclass of StockBuilder with decremented numShares field.
   * @throws IllegalArgumentException if the amount of shares is less than the amount
   *                                  decrementing by
   */
  public T decreaseShares(LocalDate date, double shares) {
    if (this.getNumShares() - shares <= 0) {
      throw new IllegalArgumentException("Cannot decrease number of shares by that much");
    }
    if (this.getShareDates().get(date) - shares <= 0) {
      throw new IllegalArgumentException("Cannot decrease number of share sby that much.");
    }
    this.numShares -= shares;
    this.shareDates.put(date, shares);
    return returnBuilder();
  }

  /**
   * Returns the number of shares of the StockBuilder.
   *
   * @return the number of shares of the StockBuilder.
   */
  private Double getNumShares() {
    return this.numShares;
  }

  private Map<LocalDate, Double> getShareDates() {
    return this.shareDates;
  }


  /**
   * Constructs a Stock object by parsing through a csv file and adding the date
   * to the StockBuilder and creating a Stock at the end with the StockBuilder Field.
   *
   * @param ticker the specific stock ticker for the respective stock being built.
   * @return A stock object with its fields that reflect the date of the underlying stock in the
   *     stock market.
   * @throws IOException if the csv file cannot be properly parsed.
   */
  public static Stock makeStock(String ticker) throws IOException {
    String filePath = "StockData/" + ticker + ".csv";
    StockImpl.StockImplBuilder stock = new StockImpl.StockImplBuilder();

    try {
      BufferedReader reader = Files.newBufferedReader(Paths.get(filePath));
      String line = reader.readLine();

      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        LocalDate stockDate = null;
        long volume = 0;
        double openPrice = 0;
        double highPrice = 0;
        double lowPrice = 0;
        double closePrice = 0;
        try {
          stockDate = LocalDate.parse(data[0]);
        } catch (DateTimeParseException e) {
          // empty
        }
        volume = Long.parseLong(data[5]);
        openPrice = Double.parseDouble(data[1]);
        highPrice = Double.parseDouble(data[2]);
        lowPrice = Double.parseDouble(data[3]);
        closePrice = Double.parseDouble(data[4]);

        stock.addOpeningPrice(stockDate, openPrice)
                .addVolume(stockDate, volume)
                .addHighPrice(stockDate, highPrice)
                .addLowPrice(stockDate, lowPrice)
                .addClosingPrice(stockDate, closePrice)
                .addTicker(ticker);
      }
    } catch (IOException e) {
      // empty
    }

    Stock entireStock = stock.build();
    Utils.getStocks().put(entireStock.getTicker(), entireStock);
    return entireStock;
  }


}
