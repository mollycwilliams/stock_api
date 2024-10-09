package stocks.swing;

/**
 * Interface for the GUIController. Its role is to connect take the actions performed in
 * the view to the model, and update the GUI with the new changes that occur from the model.
 */

public interface GUIController {

  /**
   * Updates the current available portfolio list.
   */
  void updatePortfolioList();


}
