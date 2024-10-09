package stocks;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static stocks.StockBuilder.makeStock;

/**
 * This class tests the Utils class that computes misc. operations for Stocks / Portfolios
 * such as testing if a stock is valid or testing if a date is valid.
 */
public class UtilsTest {
  Stock google;

  /**
   * Creates a Stock for google.
   *
   * @throws IOException if an exception is thrown.
   */
  @Before
  public void setGoogle() throws IOException {
    google = makeStock("GOOG");
  }

  /**
   * Tests to ensure validStock() correctly tests the validity of a stock.
   * Tests to ensure that a stock is considered valid if built correctly and exists.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testValidStock() throws IOException {
    assertEquals(true, Utils.isValidStock("GOOG"));
  }

  /**
   * Tests to ensure validStock() correctly tests the invalidity of a stock.
   * Tests to ensure that a stock is considered invalid if it doesn't exist.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testInvalidStock() throws IOException {
    assertEquals(false, Utils.isValidStock("AMZN"));
  }

  /**
   * Tests to ensure validDate() correctly tests the validity of a date.
   * Tests to ensure that a date is considered valid if it exists for the given Stock.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testValidDate() throws IOException {
    assertEquals(true, Utils.isValidDate(LocalDate.of(2024, 6, 4),
            "GOOG"));
  }

  /**
   * Tests to ensure validDate() correctly tests the invalidity of a date.
   * Tests to ensure that a date is considered invalid if it does not exist for the given Stock.
   *
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testInvalidDate() throws IOException {
    LocalDate dateToTest = LocalDate.of(1000, 3, 12);
    assertEquals(false, Utils.isValidDate(dateToTest, "GOOG"));
  }

  /**
   * Tests to ensure isValidTicker() correctly tests the invalidity of a stock ticker.
   * @throws IOException if an exception is thrown.
   */
  @Test
  public void testValidTicker() throws IOException {
    // tests a valid ticker, return true
    assertEquals(true, StockTicker.isValidTicker("GOOG"));
    // tests an invalid ticker, return false
    assertEquals(false, StockTicker.isValidTicker("no"));
  }


}
