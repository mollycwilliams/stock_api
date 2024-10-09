package stocks.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import stocks.CommandInfoImpl;
import stocks.PortfolioImpl;
import stocks.Stock;
import stocks.StockBuilder;
import stocks.StockTicker;
import stocks.Utils;

/**
 * This class represents a BuildPortfolioCommand.
 * A BuildPortfolioCommand is a Command that builds a portfolio from
 * user input, adding stocks until the user finishes.
 */
public class BuildPortfolioCommand implements Command {
  private CommandInfoImpl context;

  /**
   * Constructs a BuildPortfolioCommand object.
   * Takes in a CommandInfo object that passes the Scanner and Appendable
   * from the controller to execute its function with.
   *
   * @param context CommandInfo object that contains Scanner, Appendable.
   */
  public BuildPortfolioCommand(CommandInfoImpl context) {
    this.context = context;
  }

  /**
   * Runs the BuildPortfolioCommand.
   * Processes user input to add appropriate stocks and build when complete.
   *
   * @throws IOException if user input cannot be parsed.
   */
  @Override
  public void run() throws IOException {
    context.getView().writeMessage("What would you like to name this portfolio?\n"
            + "Or type quit to exit.\n");
    String name = context.getScanner().next();
    if (name.equals("quit")) {
      context.getView().writeMessage("Program quit, returning to menu.\n");
      return;
    }
    PortfolioImpl.PortfolioImplBuilder port = new PortfolioImpl.PortfolioImplBuilder();
    boolean quit = false;
    while (!quit) { // continue until the user quits
      context.getView().writeMessage("Type portfolio instruction: (add-stock, build, quit) \n");
      String userInstruction = context.getScanner().next();
      switch (userInstruction) {
        case "quit":
        case "q":
          context.getView().writeMessage("Pressed quit, returning back to menu.");
          quit = true;
          break;
        case "add-stock":
          String ticker = validateTicker();
          GetDataCommand data = new GetDataCommand(this.context);
          data.run(ticker);
          Stock stock = StockBuilder.makeStock(ticker);
          double shares = validateShares();

          LocalDate date = validateDate(ticker);

          stock = stock.increaseShares(date, shares);
          port.addStock(stock);
          Utils.getStocks().put(ticker, stock);
          break;
        case "build":
          context.getView().writeMessage("Portfolio was built, returned back to main menu.\n");
          PortfolioImpl newPort = port.build();
          Utils.getPortfolios().put(name, newPort);
          quit = true;
          break;
        default:
          context.getView().writeMessage("Invalid input, returned back to main menu.\n");
          quit = true;
          break;
      }
    }

  }

  /**
   * Validates that the user's inputted ticker is a valid Stock.
   *
   * @return true if valid, false if not.
   * @throws IOException if an exception is thrown.
   */
  private String validateTicker() throws IOException {
    context.getView().writeMessage("Which stock would you like to use?\n");
    String ticker = this.context.getScanner().next();
    while (!StockTicker.isValidTicker(ticker)) {
      context.getView().writeMessage("Invalid ticker, try again: ");
      ticker = this.context.getScanner().next();
    }
    return ticker;
  }

  /**
   * Validates that the user's inputted shares will pass for the stocks the user is trying to build.
   *
   * @return true if valid, false if not.
   * @throws IOException if an exception is thrown.
   */
  private double validateShares() {
    context.getView().writeMessage("How many shares would you like?\n");
    while (true) {
      try {
        double shares = Double.parseDouble(context.getScanner().next());
        if (shares < 0) {
          context.getView().writeMessage("Cannot buy negative shares, please enter a new value.\n");
        } else if (shares % 1 != 0) {
          context.getView().writeMessage("Cannot enter fractional shares, try again.\n");
        } else {
          return shares;
        }
      } catch (IllegalArgumentException e) {
        context.getView().writeMessage("Please enter a valid number of shares.\n");
      }
    }
  }

  /**
   * Validates that the user's inputted date is a valid date to purchase a stock on.
   *
   * @return true if valid, false if not.
   * @throws IOException if an exception is thrown.
   */
  private LocalDate validateDate(String ticker) {
    context.getView().writeMessage("Which date (YYYY-MM-DD) are you purchasing this stock at?\n");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    while (true) {
      try {
        LocalDate date = LocalDate.parse(context.getScanner().next(), formatter);
        if (Utils.isValidDate(date, ticker)) {
          return date;
        }
        context.getView().writeMessage(ticker + " does not have data for this date, try again.\n");
      } catch (DateTimeParseException e) {
        context.getView().writeMessage("Invalid date format, please use yyyy-MM-dd.\n");
      }
    }
  }

}
