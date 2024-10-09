package gui;

import org.junit.Test;


import stocks.Utils;
import stocks.swing.GUIModelImpl;
import stocks.swing.GUIModel;

import static org.junit.Assert.assertTrue;

/**
 * This class tests the GUIModel interface and GUIModelImpl class.
 */
public class GUIModelTest {

  /**
   * Tests the functionality of creating a portfolio in the GUI model.
   * Verifies that the portfolio is created and is initially empty.
   */
  @Test
  public void testCreatePortfolio() {
    assertTrue(Utils.getPortfolios().get("TestPortfolio") == null);

    GUIModel guiModel = new GUIModelImpl();
    guiModel.createPortfolio("TestPortfolio");

    assertTrue(Utils.getPortfolios().get("TestPortfolio") != null);
    assertTrue(Utils.getPortfolios().get("TestPortfolio").getListOfStocks().isEmpty());
  }

}
