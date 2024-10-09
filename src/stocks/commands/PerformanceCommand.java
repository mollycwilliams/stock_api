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
 * This class represents a PerformanceCommand.
 * A PerformanceCommand is a Command that calculates the
 * total performance for a given stock.
 */
public class PerformanceCommand implements Command {
  private CommandInfoImpl context;

  /**
   * Constructs a PerformanceCommand object.
   * Takes in a CommandInfo object that passes the Scanner and Appendable
   * from the controller to execute its function with.
   *
   * @param context CommandInfo object that contains Scanner, Appendable.
   */
  public PerformanceCommand(CommandInfoImpl context) {
    this.context = context;
  }

  @Override
  public void run() throws IOException {
    while (true) {
      String ticker = getValidTicker();
      if (ticker == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      Stock stock = StockBuilder.makeStock(ticker);
      LocalDate startDate = getValidDate("Enter start date (yyyy-MM-dd):\n", ticker);
      if (startDate == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      LocalDate endDate = getValidDate("Enter end date (yyyy-MM-dd):\n", ticker);
      if (endDate == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      validateDateOrder(startDate, endDate, ticker);

      calculatePerformance(stock, startDate, endDate);
      context.getView().writeMessage("Returning to main menu.\n");
      break;
    }
  }

  /**
   * Asks and verifies that the user inputs a valid ticker.
   * @return the ticker if valid, null if the user quit.
   */
  private String getValidTicker() {
    while (true) {
      context.getView().writeMessage("Which stock would you like to use? Or press quit to exit.\n");
      String ticker = context.getScanner().next();
      if (isQuit(ticker)) {
        return null;
      }
      if (StockTicker.isValidTicker(ticker)) {
        return ticker;
      }
      context.getView().writeMessage("Invalid ticker, try again: ");
    }
  }

  /**
   * Asks and verifies that the user inputs valid dates.
   * @param prompt the prompt to ask (start/end date).
   * @param ticker the stock to check the dates for.
   * @return the date if valid, null if the user quit.
   */
  private LocalDate getValidDate(String prompt, String ticker) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    while (true) {
      context.getView().writeMessage(prompt);
      String dateInput = context.getScanner().next();
      if (isQuit(dateInput)) {
        return null;
      }
      try {
        LocalDate date = LocalDate.parse(dateInput, formatter);
        if (Utils.isValidDate(date, ticker)) {
          return date;
        } else {
          context.getView().writeMessage("Invalid date for the stock, please try again.\n");
        }
      } catch (DateTimeParseException e) {
        context.getView().writeMessage("Invalid date format, please try again.\n");
      }
    }
  }

  /**
   * Validates that the date range inputted by the user is valid.
   * @param startDate the start date to check.
   * @param endDate the end date to check.
   * @param ticker the stock to check the dates for.
   */
  private void validateDateOrder(LocalDate startDate, LocalDate endDate, String ticker) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    while (startDate.isAfter(endDate)) {
      context.getView().writeMessage("Start date cannot be after the end date. "
              + "Please enter a new start date:\n");
      try {
        String newStartDate = context.getScanner().next();
        if (isQuit(newStartDate)) {
          return;
        }
        startDate = LocalDate.parse(newStartDate, formatter);
        if (!Utils.isValidDate(startDate, ticker)) {
          context.getView().writeMessage("Invalid start date for the stock, please try again.\n");
        }
      } catch (DateTimeParseException e) {
        context.getView().writeMessage("Invalid date format, please try again.\n");
      }
    }
  }

  /**
   * Calculates the performance for the stock, given the start and end date.
   * @param stock the stock to calculate the performance of.
   * @param startDate the start date.
   * @param endDate the end date.
   */
  private void calculatePerformance(Stock stock, LocalDate startDate, LocalDate endDate) {
    Double openingPrice = stock.getOpeningPrice(startDate);
    Double closingPrice = stock.getClosingPrice(endDate);

    if (openingPrice == null || closingPrice == null) {
      context.getView().writeMessage("No data available for the provided date range.\n");
      return;
    }

    double difference = closingPrice - openingPrice;
    if (difference > 0) {
      context.getView().writeMessage("Stock gained value: " + difference + "\n");
    } else {
      context.getView().writeMessage("Stock lost value: " + difference + "\n");
    }
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
