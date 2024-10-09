package stocks;

import java.io.IOException;
import java.util.Scanner;

import stocks.commands.BuildPortfolioCommand;
import stocks.commands.CompositionCommand;
import stocks.commands.CrossoverCommand;
import stocks.commands.DistributionDisplayCommand;
import stocks.commands.MovingAverageCommand;
import stocks.commands.PerformanceCommand;
import stocks.commands.PerformanceAllCommand;
import stocks.commands.PerformanceOverTimeCommand;
import stocks.commands.PurchaseStockCommand;
import stocks.commands.RebalancePortfolioCommand;
import stocks.commands.RetrievePortfolioCommand;
import stocks.commands.SavePortfolioCommand;
import stocks.commands.SellStockCommand;


/**
 * Represents the StockControllerImpl which implements the StockController interface. Is responsible
 * for running the program, and making sure the model and view get the right inputs and are handled
 * at the appropriate times.
 */
public class StockControllerImpl implements StockController {
  private Readable readable;
  private View view;

  /**
   * StockControllerImpl constructor which assigns the readable and appendable to the object.
   *
   * @param readable the readable object.
   * @param view     the view object.
   * @throws IllegalArgumentException if readable or appendable is null.
   */
  public StockControllerImpl(Readable readable, View view) {
    if ((readable == null) || (view == null)) {
      throw new IllegalArgumentException("Readable, appendable, portfolio, stock, "
              + "or stock market is null");
    }
    this.readable = readable;
    this.view = view;
  }

  /**
   * Runs the feedback loop in stock program, first displaying welcome message passing operation
   * methods to processStrategy.
   *
   * @throws IllegalStateException if input is invalid.
   * @throws IOException           if input is invalid.
   */
  public void control() throws IllegalStateException, IOException {
    Scanner sc = new Scanner(readable);
    boolean quit = false;

    view.welcomeMessage();

    while (!quit && sc.hasNext()) {
      view.writeMessage("Type instruction: ");
      String userInstruction = sc.next();
      if (userInstruction.equals("quit") || userInstruction.equals("q")) {
        quit = true;
      } else if (userInstruction.equals("menu")) {
        view.printMenu();
      } else {
        processStrategy(userInstruction, sc);
      }
    }

    view.farewellMessage();
  }

  /**
   * Processes a command from the given user instruction.
   *
   * @param userInstruction The command to execute.
   * @param s               Scanner object from user input.
   * @throws IOException if input is invalid.
   */
  public void processStrategy(String userInstruction, Scanner s) throws IOException {
    CommandInfoImpl context = new CommandInfoImpl(s, this.view);
    switch (userInstruction) {
      case "performance-over-time":
        if (Utils.getPortfolios().isEmpty()) {
          context.getView().writeMessage("Create a portfolio or load one to look at "
                  + "performance.\n");
          break;
        }
        new PerformanceOverTimeCommand(context).run();
        break;
      case "save-portfolio":
        if (Utils.getPortfolios().isEmpty()) {
          context.getView().writeMessage("Create or load an existing portfolio to save it.\n"
                  + "Currently there are no portfolios available, returning to main.\n");
          break;
        }
        new SavePortfolioCommand(context).run();
        break;
      case "retrieve-portfolio":
        new RetrievePortfolioCommand(context).run();
        break;
      case "distribution-display":
        if (Utils.getPortfolios().isEmpty()) {
          context.getView().writeMessage("Create or load an existing portfolio to save it\n");
          break;
        }
        new DistributionDisplayCommand(context).run();
        break;
      case "sell-stock":
        if (Utils.getPortfolios().isEmpty()) {
          context.getView().writeMessage("Create or load an existing portfolio to sell stocks\n");
          break;
        }
        new SellStockCommand(context).run();
        break;
      case "purchase-stock":
        if (Utils.getPortfolios().isEmpty()) {
          context.getView().writeMessage("Create or load an existing portfolio to add stocks\n");
          break;
        }
        new PurchaseStockCommand(context).run();
        break;
      case "display-composition":
        if (Utils.getPortfolios().isEmpty()) {
          context.getView().writeMessage("There are no portfolios to display.\n");
          break;
        }
        new CompositionCommand(context).run();
        break;
      case "rebalance-portfolio":
        if (Utils.getPortfolios().isEmpty()) {
          context.getView().writeMessage("There are no portfolios to rebalance.\n");
          break;
        }
        new RebalancePortfolioCommand(context).run();
        break;
      case "create-portfolio":
        new BuildPortfolioCommand(context).run();
        break;
      case "performance-all":
        if (Utils.getPortfolios().isEmpty()) {
          context.getView().writeMessage("There are no portfolios to get performance of.\n");
          break;
        }
        new PerformanceAllCommand(context).run();
        break;
      case "performance":
        new PerformanceCommand(context).run();
        break;
      case "moving-average":
        new MovingAverageCommand(context).run();
        break;
      case "quit":
        return;
      case "crossover":
        new CrossoverCommand(context).run();
        break;
      default:
        view.writeMessage("Unknown command\n");
        break;
    }
  }
}
