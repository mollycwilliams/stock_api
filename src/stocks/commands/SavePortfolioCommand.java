package stocks.commands;

import java.io.IOException;

import stocks.CommandInfoImpl;
import stocks.Utils;

/**
 * This class represents a SavePortfolioCommand.
 * A SavePortfolioCommand saves a portfolio to the database given its name.
 * The portfolio will then appear in the database and be removed from the user's list of portfolios.
 */

public class SavePortfolioCommand implements Command {
  private CommandInfoImpl context;

  /**
   * Constructs a SavePortfolioCommand object.
   *
   * @param context CommandInfo object that contains Scanner, Appendable.
   */
  public SavePortfolioCommand(CommandInfoImpl context) {
    this.context = context;
  }

  /**
   * Runs the SavePortfolioCommand.
   *
   * @throws IOException if user input cannot be parsed.
   */
  @Override
  public void run() throws IOException {
    while (true) {
      String portfolioName = getPortfolioName();
      if (portfolioName == null) {
        break;
      }

      saveAndRemovePortfolio(portfolioName);

      break;
    }

    context.getView().writeMessage("Returning to main menu.\n");
  }

  /**
   * Prompts the user to enter the name of the portfolio to save.
   *
   * @return The portfolio name entered by the user or null if user quits.
   */
  private String getPortfolioName() {
    while (true) {
      context.getView().writeMessage("What portfolio do you want to save? "
              + "Or press quit to exit.\n");
      String portfolioName = context.getScanner().next();
      if (portfolioName.equals("quit")) {
        context.getView().writeMessage("Program quit successfully.\n");
        return null;
      }

      if (Utils.getPortfolios().get(portfolioName) != null) {
        return portfolioName;
      } else {
        context.getView().writeMessage("Portfolio does not exist yet, "
                + "please enter a different name:\n");
      }
    }
  }

  /**
   * Saves the portfolio to the database and removes it from the user's list of portfolios.
   *
   * @param portfolioName The name of the portfolio to save.
   * @throws IOException if there's an issue saving the portfolio.
   */
  private void saveAndRemovePortfolio(String portfolioName) {
    Utils.savePortfolioCSV(Utils.getPortfolios().get(portfolioName), portfolioName);
    Utils.getPortfolios().remove(portfolioName);
    context.getView().writeMessage("Saved " + portfolioName + "\n");
  }
}
