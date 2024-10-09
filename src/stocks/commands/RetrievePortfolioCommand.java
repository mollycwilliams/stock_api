package stocks.commands;

import java.io.File;
import java.io.IOException;

import stocks.CommandInfoImpl;
import stocks.Portfolio;
import stocks.Utils;

import static stocks.PortfolioBuilder.makePortfolio;

/**
 * This class represents a RetrievePortfolioCommand.
 * A RetrievePortfolioCommand retrieves a portfolio from the database given its name.
 * The portfolio will then appear in the user's list of portfolios.
 */
public class RetrievePortfolioCommand implements Command {
  private CommandInfoImpl context;

  /**
   * Constructs a RetrievePortfolioCommand object.
   *
   * @param context CommandInfo object that contains Scanner, Appendable.
   */
  public RetrievePortfolioCommand(CommandInfoImpl context) {
    this.context = context;
  }

  /**
   * Runs the RetrievePortfolioCommand.
   *
   * @throws IOException if user input cannot be parsed.
   */
  @Override
  public void run() throws IOException {
    boolean quit = false;
    while (!quit) {
      File portfolioDirectory = new File("PortfolioData/");
      if (!checkPortfolioDirectory(portfolioDirectory)) {
        return;
      }

      String portfolioName = getPortfolioName();
      if (portfolioName == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      retrievePortfolio(portfolioName);

      break;
    }
    context.getView().writeMessage("Returning to main menu.\n");
  }

  /**
   * Checks if the portfolio directory exists and is valid.
   *
   * @param portfolioDirectory The directory to check.
   * @return true if the directory exists and is valid, false otherwise.
   */
  private boolean checkPortfolioDirectory(File portfolioDirectory) {
    if (!portfolioDirectory.exists() || !portfolioDirectory.isDirectory()) {
      context.getView().writeMessage("The directory 'PortfolioData/' "
              + "does not exist or is not a directory.\n");
      return false;
    }
    return true;
  }

  /**
   * Prompts the user to enter the name of the portfolio to retrieve.
   *
   * @return The portfolio name entered by the user or null if user quits.
   */
  private String getPortfolioName() {
    String[] files = new File("PortfolioData/").list();
    if (files == null || files.length == 0) {
      context.getView().writeMessage("Cannot retrieve because no portfolios are saved.\n");
      return null;
    }

    context.getView().writeMessage("Enter name of portfolio you want to retrieve or press "
            + "quit to exit.\n");
    String portfolioName = context.getScanner().next();
    if (portfolioName.equals("quit")) {
      context.getView().writeMessage("Program quit successfully.\n");
      return null;
    }
    return portfolioName;
  }

  /**
   * Retrieves the portfolio with the given name and adds it to the user's portfolios.
   *
   * @param portfolioName The name of the portfolio to retrieve.
   */
  private void retrievePortfolio(String portfolioName) {
    File portfolioFile = new File("PortfolioData/" + portfolioName + ".csv");
    while (!portfolioFile.exists()) {
      context.getView().writeMessage("No portfolio saved with that name, try again or type "
              + "'quit' to exit: \n");
      portfolioName = context.getScanner().next();
      if (portfolioName.equals("quit")) {
        context.getView().writeMessage("Program quit successfully.\n");
        return;
      }
      portfolioFile = new File("PortfolioData/" + portfolioName + ".csv");
    }

    Portfolio madePortfolio = makePortfolio(portfolioName);
    Utils.getPortfolios().put(portfolioName, madePortfolio);

    context.getView().writeMessage(portfolioName + " has been added to your list of available "
            + "portfolios!\n");
  }
}
