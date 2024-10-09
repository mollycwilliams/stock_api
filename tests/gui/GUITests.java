package gui;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import stocks.Portfolio;
import stocks.swing.GUIModel;
import stocks.swing.GUIView;
import stocks.swing.GUIViewImpl;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the GUIController interface and the GUIControllerImpl class.
 */
public class GUITests {

  /**
   * Tests the functionality of buying a stock through the GUI model.
   *
   * @throws IOException if an I/O error occurs during the operation.
   */
  @Test
  public void testBuyStock() throws IOException {
    StringBuilder out = new StringBuilder();
    GUIModel mockmodel = new MockGUIModel(out);
    GUIView view = new GUIViewImpl();
    mockmodel.buyStock("GOOG", 3, LocalDate.parse("2024-05-21"));
    assertEquals("Ticker: GOOG Shares: 3.0 Date: 2024-05-21\n", out.toString());
  }

  /**
   * Tests the functionality of selling a stock through the GUI model.
   *
   * @throws IOException if an I/O error occurs during the operation.
   */
  @Test
  public void testSellStock() throws IOException {
    StringBuilder out = new StringBuilder();
    GUIModel mockmodel = new MockGUIModel(out);
    GUIView view = new GUIViewImpl();
    mockmodel.buyStock("GOOG", 3, LocalDate.parse("2024-05-21"));
    mockmodel.sellStock("GOOG", 2, LocalDate.parse("2024-05-22"));
    assertEquals("Ticker: GOOG Shares: 3.0 Date: 2024-05-21\n"
            + "Ticker: GOOG Shares: 2.0 Date: 2024-05-22\n", out.toString());
  }

  /**
   * Tests the functionality of saving a portfolio through the GUI model.
   *
   * @throws IOException if an I/O error occurs during the operation.
   */
  @Test
  public void testSavePortfolio() throws IOException {
    StringBuilder out = new StringBuilder();
    GUIModel mockmodel = new MockGUIModel(out);
    GUIView view = new GUIViewImpl();
    mockmodel.createPortfolio("a");
    mockmodel.savePortfolio("a");
    assertEquals("a\n" + "a\n", out.toString());
  }

  /**
   * Tests the functionality of retrieving a portfolio through the GUI model.
   *
   * @throws IOException if an I/O error occurs during the operation.
   */
  @Test
  public void testRetrievePortfolio() throws IOException {
    StringBuilder out = new StringBuilder();
    GUIModel mockmodel = new MockGUIModel(out);
    GUIView view = new GUIViewImpl();
    mockmodel.retrievePortfolio("a");
    assertEquals("a\n", out.toString());
  }

  /**
   * Tests the functionality of creating a portfolio through the GUI model.
   */
  @Test
  public void testCreatePortfolio() {
    StringBuilder out = new StringBuilder();
    GUIModel mockmodel = new MockGUIModel(out);
    GUIView view = new GUIViewImpl();
    mockmodel.createPortfolio("a");
    assertEquals("a\n", out.toString());
  }

  /**
   * Tests the functionality of attempting to create a portfolio when it already exists.
   */
  @Test
  public void testCreatePortfolioAlreadyExists() {
    StringBuilder out = new StringBuilder();
    GUIModel mockmodel = new MockGUIModel(out);
    GUIView view = new GUIViewImpl();
    mockmodel.createPortfolio("a");
    mockmodel.createPortfolio("a");
    assertEquals("a\n" + "a\n", out.toString());
  }

  /**
   * Tests the functionality of setting the current portfolio through the GUI model.
   */
  @Test
  public void setCurrentPortfolio() {
    StringBuilder out = new StringBuilder();
    GUIModel mockmodel = new MockGUIModel(out);
    GUIView view = new GUIViewImpl();
    mockmodel.setCurrentPortfolio("a");
    assertEquals("a\n", out.toString());
  }

  /**
   * A mock implementation of the {@link GUIModel} interface used for testing.
   */
  class MockGUIModel implements GUIModel {

    private StringBuilder log;

    /**
     * Constructs a mock GUI model with a specified log.
     *
     * @param sb the {@link StringBuilder} used to log method calls and parameters.
     */
    public MockGUIModel(StringBuilder sb) {
      this.log = sb;
    }

    /**
     * Creates a portfolio with the specified name.
     *
     * @param name the name of the portfolio.
     */
    @Override
    public void createPortfolio(String name) {
      log.append(name).append("\n");
    }

    /**
     * Returns an array of portfolio names.
     *
     * @return an empty array of strings.
     */
    @Override
    public String[] getPortfolioNames() {
      log.append(Arrays.toString(new String[0])).append("\n");
      return new String[0];
    }

    /**
     * Sets the current portfolio to the one specified by name.
     *
     * @param name the name of the portfolio to set as current.
     */
    @Override
    public void setCurrentPortfolio(String name) {
      log.append(name).append("\n");
    }

    /**
     * Returns the portfolio object.
     *
     * @return {@code null} in this mock implementation.
     */
    @Override
    public Portfolio getPortfolio() {
      return null;
    }

    /**
     * Buys a specified number of shares of a stock on a given date.
     *
     * @param ticker the stock ticker symbol.
     * @param shares the number of shares to buy.
     * @param date   the date of the transaction.
     */
    @Override
    public void buyStock(String ticker, double shares, LocalDate date) {
      log.append("Ticker: ").append(ticker)
              .append(" Shares: ").append(shares)
              .append(" Date: ").append(date).append("\n");
    }

    /**
     * Sells a specified number of shares of a stock on a given date.
     *
     * @param ticker the stock ticker symbol.
     * @param shares the number of shares to sell.
     * @param date   the date of the transaction.
     */
    @Override
    public void sellStock(String ticker, double shares, LocalDate date) {
      log.append("Ticker: ").append(ticker)
              .append(" Shares: ").append(shares)
              .append(" Date: ").append(date).append("\n");
    }

    /**
     * Saves the specified portfolio.
     *
     * @param name the name of the portfolio to save.
     * @throws IOException if an I/O error occurs during the operation.
     */
    @Override
    public void savePortfolio(String name) throws IOException {
      log.append(name).append("\n");
    }

    /**
     * Retrieves the specified portfolio.
     *
     * @param portfolioName the name of the portfolio to retrieve.
     * @throws IOException if an I/O error occurs during the operation.
     */
    @Override
    public void retrievePortfolio(String portfolioName) throws IOException {
      log.append(portfolioName).append("\n");
    }

  }

}
