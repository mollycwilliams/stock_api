package stocks.swing;

import java.awt.event.ActionListener;

/**
 * This interface represents a GUI view for stock portfolio management.
 * It provides methods for interacting with the user interface, handling user input, and
 * managing portfolio-related actions.
 */
public interface GUIView {

  /**
   * Displays a message to the user.
   *
   * @param message the message to be displayed
   */
  void displayMessage(String message);

  /**
   * Prompts the user with a dialog to input a value.
   *
   * @param prompt the message to display in the dialog
   * @return the user input as a string
   */
  String getUserInput(String prompt);

  /**
   * Sets the available portfolio names in the GUI components (e.g., combo box, list).
   *
   * @param names an array of portfolio names
   */
  void setPortfolioNames(String[] names);

  /**
   * Gets the currently selected portfolio from the GUI components.
   *
   * @return the name of the selected portfolio
   */
  String getSelectedPortfolio();

  /**
   * Adds an ActionListener to handle actions for the "Create Portfolio" button.
   *
   * @param listener the ActionListener to handle button actions
   */
  void addCreatePortfolioListener(ActionListener listener);

  /**
   * Adds an ActionListener to handle actions for the "Buy Stock" button.
   *
   * @param listener the ActionListener to handle button actions
   */
  void addBuyStockListener(ActionListener listener);

  /**
   * Adds an ActionListener to handle actions for the "Sell Stock" button.
   *
   * @param listener the ActionListener to handle button actions
   */
  void addSellStockListener(ActionListener listener);

  /**
   * Adds an ActionListener to handle actions for the "View Portfolio Composition" button.
   *
   * @param listener the ActionListener to handle button actions
   */
  void addShowPortfolioListener(ActionListener listener);

  /**
   * Adds an ActionListener to handle actions for the "Save Portfolio" button.
   *
   * @param listener the ActionListener to handle button actions
   */
  void addSavePortfolioListener(ActionListener listener);

  /**
   * Adds an ActionListener to handle actions for the "Retrieve Portfolio" button.
   *
   * @param listener the ActionListener to handle button actions
   */
  void addRetrievePortfolioListener(ActionListener listener);
}
