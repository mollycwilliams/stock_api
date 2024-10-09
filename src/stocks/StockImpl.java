package stocks;

import java.time.LocalDate;
import java.util.Map;


/**
 * A StockImpl class which implements Stock. It represents a stock which contains important
 * information about its prices, volume, ticker, and how many shares it has.
 */
public class StockImpl implements Stock {

  protected final String ticker;  // stock symbol
  protected final double numShares;  // number of shares of that stock
  protected final Map<LocalDate, Long> volume;   // Key: date, Value: Volume at that date
  protected final Map<LocalDate, Double> openingPrice; // Key: date, Value: opening price
  // at that date
  protected final Map<LocalDate, Double> closingPrice; // Key: date, Value: closing price
  // at that date
  protected final Map<LocalDate, Double> highPrice; // Key: date, Value: high price at that date
  protected final Map<LocalDate, Double> lowPrice; // Key: date, Value low price at that date
  protected final Map<LocalDate, Double> shareDates; // Key: date, Value number of shares
  // purchased at that date

  /**
   * The constructor for StockImpl.
   *
   * @param ticker       the stock symbol
   * @param numShares    number of shares of that stock
   * @param volume       Key: date, Value: Volume at that date
   * @param openingPrice Key: date, Value: opening price at that date
   * @param closingPrice Key: date, Value: closing price at that date
   * @param highPrice    Key: date, Value: high price at that date
   * @param lowPrice     Key: date, Value: low price at that date
   * @param shareDates   Key: date, Value: number of shares purchased at that date
   */
  protected StockImpl(String ticker, double numShares, Map<LocalDate, Long> volume, Map<LocalDate,
          Double> openingPrice, Map<LocalDate, Double> closingPrice, Map<LocalDate,
          Double> highPrice, Map<LocalDate, Double> lowPrice, Map<LocalDate, Double> shareDates) {
    this.ticker = ticker;
    this.numShares = numShares;
    this.volume = volume;
    this.openingPrice = openingPrice;
    this.closingPrice = closingPrice;
    this.highPrice = highPrice;
    this.lowPrice = lowPrice;
    this.shareDates = shareDates;
  }


  /**
   * A StockImplBuilder class which extends the StockBuilder. Used to build the StockImpl.
   */
  public static class StockImplBuilder extends StockBuilder<StockImplBuilder> {

    /**
     * StockImplBuilder constructor which creates a StockBuilder.
     */
    public StockImplBuilder() {
      super();
    }

    /**
     * Builds a StockImpl with its fields.
     *
     * @return A StockImpl object.
     */
    @Override
    public Stock build() {

      return new StockImpl(this.ticker, this.numShares, this.volume, this.openingPrice,
              this.closingPrice, this.highPrice, this.lowPrice, this.shareDates);
    }

    /**
     * Used to building A StockBuilder.
     *
     * @return this object.
     */
    @Override
    protected StockImplBuilder returnBuilder() {
      return this;
    }
  }

  @Override
  public Double getNumShares() {
    return this.numShares;
  }

  @Override
  public Stock increaseShares(LocalDate date, double num) {
    Map<LocalDate, Double> temp = this.shareDates;
    if (this.shareDates.get(date) != null) {
      double newShares = this.shareDates.get(date) + num;
      temp.replace(date, newShares);
    } else {
      temp.put(date, num);
    }

    return new StockImpl(this.ticker, this.numShares + num,
            this.volume, this.openingPrice, this.closingPrice, this.highPrice, this.lowPrice,
            temp);
  }

  @Override
  public Stock decreaseShares(LocalDate date, double num) throws IllegalArgumentException {

    Map<LocalDate, Double> temp = this.shareDates;
    if (this.shareDates.get(date) != null) {
      double newShares = this.shareDates.get(date) - num;
      temp.replace(date, newShares);
    } else {
      temp.put(date, -1 * num);
    }

    return new StockImpl(this.ticker, this.numShares - num,
            this.volume, this.openingPrice, this.closingPrice, this.highPrice, this.lowPrice,
            temp);
  }


  @Override
  public Stock addShareDates(Map<LocalDate, Double> shareDates) {
    return new StockImpl(this.ticker, this.numShares,
            this.volume, this.openingPrice, this.closingPrice, this.highPrice, this.lowPrice,
            shareDates);
  }

  @Override
  public Stock setNumShares(Double shares) {
    return new StockImpl(this.ticker, shares,
            this.volume, this.openingPrice, this.closingPrice, this.highPrice, this.lowPrice,
            this.shareDates);
  }


  @Override
  public Double getSharesAtDate(LocalDate date) {
    Double count = 0.0;
    for (Map.Entry<LocalDate, Double> entry : shareDates.entrySet()) {
      if (date.isAfter(entry.getKey()) || date.equals(entry.getKey())) {
        count += entry.getValue();
      }
    }
    return count;
  }


  @Override
  public LocalDate lastDate() {
    LocalDate last = LocalDate.MIN;
    for (Map.Entry<LocalDate, Double> entry : shareDates.entrySet()) {
      if (last.isBefore(entry.getKey())) {
        last = entry.getKey();
      }
    }
    return last;
  }

  @Override
  public String getTicker() {
    return this.ticker;
  }

  @Override
  public Long getVolume(LocalDate date) {
    return this.volume.get(date);
  }

  @Override
  public Double getOpeningPrice(LocalDate date) {
    return this.openingPrice.get(date);
  }

  @Override
  public Double getClosingPrice(LocalDate date) {
    return this.closingPrice.get(date);
  }

  @Override
  public Double getHighPrice(LocalDate date) {
    return this.highPrice.get(date);
  }

  @Override
  public Double getLowPrice(LocalDate date) {
    return this.lowPrice.get(date);
  }

  @Override
  public Map<LocalDate, Double> getShareDates() {
    return this.shareDates;
  }

}
