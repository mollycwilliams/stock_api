package stocks.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import javax.swing.JOptionPane;

import stocks.Stock;
import stocks.Utils;

/**
 * This class represents a GUIController object.
 * This is the controller portion of the MVC design for the StockMarket GUI.
 * Processes user actions.
 */
public class GUIControllerImpl implements GUIController {
  private final GUIModel guimodel;
  private final GUIView guiview;

  /**
   * Constructs a GUIController object from a model and a view.
   *
   * @param guimodel the model to be used by the controller.
   * @param guiview  the view to be used by the controller.
   */
  public GUIControllerImpl(GUIModel guimodel, GUIView guiview) {
    this.guimodel = guimodel;
    this.guiview = guiview;

    guiview.addCreatePortfolioListener(new CreatePortfolioListener());
    guiview.addBuyStockListener(new BuyStockListener());
    guiview.addSellStockListener(new SellStockListener());
    guiview.addShowPortfolioListener(new ShowPortfolioListener());
    guiview.addSavePortfolioListener(new SavePortfolioListener());
    guiview.addRetrievePortfolioListener(new RetrievePortfolioListener());

    updatePortfolioList();
  }

  /**
   * Updates the current available portfolio list.
   */
  public void updatePortfolioList() {
    guiview.setPortfolioNames(guimodel.getPortfolioNames());
  }

  /**
   * Listener for the create portfolio button.
   * Creates a portfolio and adds it to the available portfolios list.
   */
  class CreatePortfolioListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String name = guiview.getUserInput("Enter portfolio name:");
      try {
        if (name != null && !name.isEmpty()) {
          guimodel.createPortfolio(name);
          updatePortfolioList();
          guiview.displayMessage("Created portfolio: " + name);
        }
      } catch (IllegalArgumentException ex) {
        guiview.displayMessage("Error occurred: " + ex.getMessage());
      }
    }
  }

  /**
   * Listener for the buy stock button.
   * Asks the user to enter which stock they would like to purchase, as well as the number
   * of shares and for whichever date they would like.
   */
  class BuyStockListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String portfolio = getPortfolioFromUser("Select portfolio for buying stock:");
      if (portfolio != null) {
        guimodel.setCurrentPortfolio(portfolio);
        String stockTicker = guiview.getUserInput("Enter stock symbol:");
        if (stockTicker != null) {
          try {
            double shares = Double.parseDouble(guiview
                    .getUserInput("Enter number of shares:"));
            LocalDate date = getDateFromUser("Enter date of purchase (YYYY-MM-DD):");
            while (shares % 1 != 0) {
              guiview.displayMessage("Please enter non-fractional shares:");
              shares = Double.parseDouble(guiview
                      .getUserInput("Enter number of shares:"));
            }
            if (date != null) {
              guimodel.buyStock(stockTicker, shares, date);
              guiview.displayMessage("Bought " + shares + " shares of "
                      + stockTicker + " on " + date + " in " + portfolio);
            }
          } catch (NumberFormatException exception) {
            guiview.displayMessage("Invalid number of shares.");
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        }
      }
    }
  }

  /**
   * Listener for the sell stock button.
   * Asks the user to enter which stock they would like to sell, as well as the number
   * of shares and for whichever date they would like.
   */
  class SellStockListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String portfolio = getPortfolioFromUser("Select portfolio for selling stock:");
      if (portfolio != null) {
        guimodel.setCurrentPortfolio(portfolio);
        String stockTicker = guiview.getUserInput("Enter stock symbol:");
        if (stockTicker != null) {
          try {
            double shares = Double.parseDouble(guiview
                    .getUserInput("Enter number of shares:"));

            LocalDate date = getDateFromUser("Enter date of sale (YYYY-MM-DD):");


            if (date != null && Utils.getPortfolios().get(portfolio)
                    .getListOfStocks().get(stockTicker)
                    .getSharesAtDate(date) - shares > 0) {
              try {
                guimodel.sellStock(stockTicker, shares, date);
                guiview.displayMessage("Sold " + shares + " shares of " + stockTicker + " on "
                        + date + " in " + portfolio);
              } catch (NullPointerException exception) {
                guiview.displayMessage(stockTicker + " does not exist in this portfolio.");
              }
            }

            if (Utils.getPortfolios().get(portfolio).getListOfStocks().get(stockTicker)
                    .getSharesAtDate(date) - shares < 0) {
              guiview.displayMessage("Was not able to sell shares because portfolio doesnt have"
                      + " enough of " + stockTicker + " shares.");
            }
          } catch (NumberFormatException ex) {
            guiview.displayMessage("Error that occurred: " + ex.getMessage());
          }
        }
      }
    }
  }

  /**
   * Listener for the show portfolio button.
   * Displays the chosen portfolio with its list of stocks with the values (closing price) for the
   * given date, as well as the total value of the portfolio for that date.
   */
  class ShowPortfolioListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String portfolio = getPortfolioFromUser("Select portfolio to show:");
      if (portfolio != null) {
        guimodel.setCurrentPortfolio(portfolio);
        LocalDate date = getDateFromUser("Enter date to view portfolio composition (YYYY-MM-DD):");
        if (date != null) {
          StringBuilder result = new StringBuilder();
          double totalValue = 0.0;

          for (Map.Entry<String, Stock> entry : guimodel.getPortfolio()
                  .getListOfStocks().entrySet()) {
            Stock stock = entry.getValue();
            double numShares = stock.getSharesAtDate(date);
            Double closingPrice = stock.getClosingPrice(date);
            if (closingPrice != null) {
              result.append(String.format("Ticker: %s, Shares: %.2f, Closing Price: %.2f\n",
                      entry.getKey(), numShares, closingPrice));
              totalValue += closingPrice * numShares;
            }
          }
          result.append(String.format("Total portfolio value on %s: %.2f", date, totalValue));
          guiview.displayMessage(result.toString());
        }
      }
    }
  }

  /**
   * Listener for the save portfolio button.
   * Saves a portfolio to the PortfolioData directory, to be retrieved at a later date.
   */
  class SavePortfolioListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String portfolio = getPortfolioFromUser("Select portfolio to save:");
      if (portfolio != null) {
        Utils.savePortfolioCSV(Utils.getPortfolios().get(portfolio), portfolio);
        guiview.displayMessage("Portfolio " + portfolio + " saved as " + portfolio);
      }
    }
  }

  /**
   * Listener for the retrieve portfolio button.
   * Retrieves a portfolio from the PortfolioData/ directory and adds it to the
   * current existing portfolio list.
   */
  class RetrievePortfolioListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String filename = guiview.getUserInput("Enter portfolio to retrieve:");
      if (filename != null && !filename.isEmpty()) {
        try {
          guimodel.retrievePortfolio(filename);
          updatePortfolioList();
          guiview.displayMessage("Portfolio " + filename + " retrieved.");
        } catch (IOException ex) {
          guiview.displayMessage("Error retrieving portfolio: " + ex.getMessage());
        }
      }
    }
  }

  /**
   * Prompts the user for the name of the portfolio they would like.
   *
   * @param message the message to display to the user.
   * @return the name of the portfolio inputted by the user.
   */
  private String getPortfolioFromUser(String message) {
    String[] portfolios = guimodel.getPortfolioNames();
    if (portfolios.length > 0) {
      return (String) JOptionPane.showInputDialog(null, message,
              "Select Portfolio", JOptionPane.QUESTION_MESSAGE, null,
              portfolios, portfolios[0]);
    } else {
      guiview.displayMessage("No portfolios available. Create one first.");
      return null;
    }
  }

  /**
   * Prompts the user for a valid date.
   *
   * @param message the message to display to the user.
   * @return the date inputted by the user.
   */
  private LocalDate getDateFromUser(String message) {
    String dateString = guiview.getUserInput(message);
    if (dateString != null) {
      try {
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
      } catch (DateTimeParseException ex) {
        guiview.displayMessage("Invalid date format. Please use YYYY-MM-DD.");
      }
    }
    return null;
  }

}
