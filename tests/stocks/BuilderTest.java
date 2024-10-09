package stocks;

import org.junit.Test;


import java.io.IOException;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static stocks.StockBuilder.makeStock;

/**
 * This is a test class that tests implementations for StockBuilder and PortfolioBuilder.
 */
public class BuilderTest {
  Stock google;
  Portfolio port;

  /**
   * Tests to ensure that the StockBuilder class creates a stock correctly.
   */
  @Test
  public void testStockBuilder() {
    google = new StockImpl.StockImplBuilder()
            .addTicker("GOOG")
            .addVolume(LocalDate.parse("2024-06-04"), 350)
            .addLowPrice(LocalDate.parse("2024-06-04"), 30)
            .addHighPrice(LocalDate.parse("2024-06-04"), 60)
            .addClosingPrice(LocalDate.parse("2024-06-04"), 50)
            .addOpeningPrice(LocalDate.parse("2024-06-04"), 35)
            .addShares(LocalDate.parse("2024-06-04"), 10)
            .build();

    assertEquals("GOOG", google.getTicker());
    assertEquals(new Double(50), google.getClosingPrice(LocalDate.parse("2024-06-04")));
    assertEquals(new Double(35), google.getOpeningPrice(LocalDate.parse("2024-06-04")));
    assertEquals(new Double(60), google.getHighPrice(LocalDate.parse("2024-06-04")));
    assertEquals(new Double(30), google.getLowPrice(LocalDate.parse("2024-06-04")));
    assertEquals(new Long(350), google.getVolume(LocalDate.parse("2024-06-04")));
  }

  /**
   * Tests to ensure that the PortfolioBuilder class creates a stock correctly.
   */
  @Test
  public void testPortfolioBuilder1() throws IOException {
    Stock s0 = makeStock("GOOG");
    Stock s1 = makeStock("AMZN");
    port = new PortfolioImpl.PortfolioImplBuilder()
            .addStock(s0)
            .addStock(s1)
            .build();

    assertEquals(2, port.getListOfStocks().size());
    assertEquals(s0, port.getListOfStocks().get("GOOG"));
    assertEquals(s1, port.getListOfStocks().get("AMZN"));
  }

  /**
   * Tests to ensure that the PortfolioBuilder class creates a stock correctly.
   */
  @Test
  public void testPortfolioBuilder2() throws IOException {
    Stock s0 = makeStock("GOOG");
    Stock s1 = makeStock("GOOG");
    Stock s2 = makeStock("AMZN");
    port = new PortfolioImpl.PortfolioImplBuilder()
            .addStock(s0)
            .addStock(s1)
            .addStock(s2)
            .build();

    // 2 stocks, not 2 shares **
    assertEquals(2, port.getListOfStocks().size());
  }

  /**
   * Tests to make sure that increaseShares increments the shares correctly.
   *
   * @throws IOException if a negative value is passed.
   */
  @Test
  public void testIncreaseShares() throws IOException {
    Stock s = makeStock("GOOG");
    s = s.increaseShares(LocalDate.parse("2024-05-21"), 5);
    s = s.increaseShares(LocalDate.parse("2024-05-22"), 6);
    s = s.increaseShares(LocalDate.parse("2024-05-23"), 10);
    assertEquals(new Double(5), s.getSharesAtDate(LocalDate.parse("2024-05-21")));
    assertEquals(new Double(11), s.getSharesAtDate(LocalDate.parse("2024-05-22")));
    assertEquals(new Double(21), s.getSharesAtDate(LocalDate.parse("2024-05-23")));
  }

  /**
   * Tests to make sure that decreaseShares decreases the shares correctly.
   *
   * @throws IOException if a negative value is passed.
   */
  @Test
  public void testDecreaseShares() throws IOException {
    Stock s = makeStock("GOOG");
    s = s.increaseShares(LocalDate.parse("2024-05-21"), 5);
    s = s.decreaseShares(LocalDate.parse("2024-05-21"), 3);
    assertEquals(new Double(2), s.getSharesAtDate(LocalDate.parse("2024-05-21")));
  }


}
