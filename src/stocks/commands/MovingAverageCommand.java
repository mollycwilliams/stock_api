package stocks.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import stocks.CommandInfoImpl;
import stocks.Stock;
import stocks.StockBuilder;
import stocks.StockTicker;
import stocks.Utils;

/**
 * This class represents a MovingAverageCommand.
 * A MovingAverageCommand is a Command that returns the x-day moving
 * average of a stock for a specified date and a specified value of x.
 * The x-day moving average is the average of the last x days (starting
 * from the given date). Note that this corresponds to the last x days
 * when the stock prices are available.
 */
public class MovingAverageCommand implements Command {
  private CommandInfoImpl context;

  /**
   * Constructs a MovingAverageCommand object.
   * Takes in a CommandInfo object that passes the Scanner and Appendable
   * from the controller to execute its function with.
   *
   * @param context CommandInfo object that contains Scanner, Appendable.
   */
  public MovingAverageCommand(CommandInfoImpl context) {
    this.context = context;
  }

  /**
   * Runs the MovingAverageCommand.
   * Processes user input to calculate the average of the last x days (starting
   * from the given date).
   *
   * @throws IOException if user input cannot be parsed.
   */
  @Override
  public void run() throws IOException {
    boolean quit = false;
    while (!quit) {
      String ticker = getStockTicker();
      if (ticker == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      Stock stock = getStockData(ticker);
      if (stock == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      LocalDate date = getValidDate(ticker);
      if (date == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      double days = getValidDays();
      if (days < 0) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      double movingAverage = calculateMovingAverage(stock, date, days);
      context.getView().writeMessage(days + "-day moving average for " + stock.getTicker()
              + " is: " + movingAverage + "\n");
      context.getView().writeMessage("Returning to main menu.\n");
      break;
    }
  }

  /**
   * Prompts the user to enter a stock ticker and validates it.
   *
   * @return A valid stock ticker or null if the user quits.
   */
  private String getStockTicker() {
    while (true) {
      context.getView().writeMessage("Which stock would you like to use? "
              + "Or press quit to exit.\n");
      String ticker = this.context.getScanner().next();
      if (isQuit(ticker)) {
        return null;
      }
      if (StockTicker.isValidTicker(ticker)) {
        return ticker;
      }
      context.getView().writeMessage("Invalid ticker, try again: \n");
    }
  }

  /**
   * Fetches and verifies the stock data.
   *
   * @param ticker The ticker of the stock.
   * @return A Stock object or null if the stock is not valid or user quits.
   */
  private Stock getStockData(String ticker) throws IOException {
    GetDataCommand data = new GetDataCommand(this.context);
    try {
      data.run(ticker);
    } catch (IOException e) {
      context.getView().writeMessage("Could not get data for the stock.\n");
      return null;
    }
    return StockBuilder.makeStock(ticker);
  }

  /**
   * Prompts the user to enter a date and validates it for the given ticker.
   *
   * @param ticker The ticker of the stock.
   * @return A valid LocalDate or null if the user quits.
   */
  private LocalDate getValidDate(String ticker) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    while (true) {
      context.getView().writeMessage("For which date would you like to calculate "
              + "the moving average? (yyyy-MM-dd)\n");
      String dateInput = this.context.getScanner().next();
      if (isQuit(dateInput)) {
        return null;
      }
      try {
        LocalDate date = LocalDate.parse(dateInput, formatter);
        if (Utils.isValidDate(date, ticker)) {
          return date;
        }
        context.getView().writeMessage("Invalid date, please enter a valid date:\n");
      } catch (DateTimeParseException e) {
        context.getView().writeMessage("Invalid date format. Please use yyyy-MM-dd. Try again:\n");
      }
    }
  }

  /**
   * Prompts the user to enter a valid number of days for the moving average calculation.
   *
   * @return A positive number of days or -1 if the user quits.
   */
  private double getValidDays() {
    while (true) {
      context.getView().writeMessage("Please provide the number of days:\n");
      String input = this.context.getScanner().next();
      if (isQuit(input)) {
        return -1;
      }
      try {
        double days = Double.parseDouble(input);
        if (days >= 0) {
          return days;
        }
        context.getView().writeMessage("Days cannot be negative, please enter a new value:\n");
      } catch (NumberFormatException e) {
        context.getView().writeMessage("Invalid number format. Please enter "
                + "a valid number of days:\n");
      }
    }
  }

  /**
   * Calculates the moving average of the stock over the given number of days.
   *
   * @param stock The stock object.
   * @param date  The starting date for the moving average calculation.
   * @param days  The number of days for the moving average.
   * @return The calculated moving average.
   */
  private double calculateMovingAverage(Stock stock, LocalDate date, double days) {
    double sum = 0;
    int counter = 0;
    for (int i = 0; i < days; i++) {
      LocalDate currentDate = date.minusDays(i);
      if (Utils.isValidDate(currentDate, stock.getTicker())) {
        double average = (stock.getHighPrice(currentDate) + stock.getLowPrice(currentDate)) / 2;
        sum += average;
        counter++;
      }
    }
    if (counter > 0) {
      return sum / counter;
    }
    return 0;
  }

  /**
   * Checks if the user input is a quit command.
   *
   * @param input The user input to check.
   * @return true if the input is "quit", false otherwise.
   */
  private boolean isQuit(String input) {
    if (input.equalsIgnoreCase("quit")) {
      context.getView().writeMessage("Program quit successfully.\n");
      return true;
    }
    return false;
  }
}
