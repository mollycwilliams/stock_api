package stocks;

/**
 * The enum StockTicker represents the values for DEFINED valid tickers in this class.
 */
public enum StockTicker {
  GME("GME"),
  MSFT("MSFT"),
  AAPL("AAPL"),
  AMZN("AMZN"),
  GOOG("GOOG"),
  INTC("INTC");


  private final String ticker;

  /**
   * Constructs a StockTicker with the given ticker.
   *
   * @param ticker the ticker to become a StockTicker.
   */
  StockTicker(String ticker) {
    this.ticker = ticker;
  }

  /**
   * Determines if the given ticker is a ticker for a valid Stock.
   *
   * @param ticker the ticker to check.
   * @return true if valid, false if invalid.
   */
  public static boolean isValidTicker(String ticker) {
    for (StockTicker st : StockTicker.values()) {
      if (st.getTicker().equalsIgnoreCase(ticker)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the ticker.
   *
   * @return the ticker as a String.
   */
  public String getTicker() {
    return this.ticker;
  }
}
