package stocks.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import stocks.CommandInfoImpl;
import stocks.Stock;
import stocks.StockBuilder;
import stocks.StockTicker;
import stocks.Utils;

/**
 * This class represents a CrossoverCommand.
 * A CrossoverCommand is a Command that returns a list of
 * crossover dates.
 * An x-day crossover happens when the closing price for a day is
 * greater than the x-day moving average for that day.
 */
public class CrossoverCommand implements Command {
  private CommandInfoImpl context;

  /**
   * Constructs a CrossoverCommand object.
   * Takes in a CommandInfo object that passes the Scanner and Appendable
   * from the controller to execute its function with.
   *
   * @param context CommandInfo object that contains Scanner, Appendable.
   */
  public CrossoverCommand(CommandInfoImpl context) {
    this.context = context;
  }

  /**
   * Runs the CrossoverCommand.
   * Processes user input to compute the crossover dates and
   * prints them back to the user.
   *
   * @throws IOException if user input cannot be parsed.
   */
  @Override
  public void run() throws IOException {
    boolean quit = false;
    while (!quit) {
      List<LocalDate> crossovers = new ArrayList<>();

      String ticker = getStockTicker();
      if (ticker == null) {
        break;
      }

      GetDataCommand data = new GetDataCommand(this.context);
      data.run(ticker);
      Stock stock = StockBuilder.makeStock(ticker);

      LocalDate start = getDate("Enter start date (yyyy-MM-dd):\n", ticker);
      if (start == null) {
        break;
      }

      LocalDate end = getDate("Enter end date (yyyy-MM-dd):\n", ticker);
      if (end == null) {
        break;
      }

      if (!validateDateRange(start, end)) {
        continue;
      }

      double days = getDays();
      if (days < 0) {
        break;
      }

      List<LocalDate> dates = generateValidDates(start, end, ticker);
      crossovers = calculateCrossoverDates(dates, stock, days, ticker);

      displayCrossoverDates(crossovers);
      break;
    }
  }

  /**
   * Prompts and checks that the stock the user wants to use is valid.
   *
   * @return the stock if valid, null if the user quit.
   * @throws IOException if an exception is thrown.
   */
  private String getStockTicker() throws IOException {
    while (true) {
      context.getView().writeMessage("Which stock would you like to use? Or type quit to exit.\n");
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
   * Asks and verifies that the date the user inputted is valid.
   *
   * @param prompt the prompt to ask the user, start or end date.
   * @param ticker the ticker for the stock to check the dates of.
   * @return the date if valid, null if invalid.
   * @throws IOException if an exception is thrown.
   */
  private LocalDate getDate(String prompt, String ticker) throws IOException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    while (true) {
      context.getView().writeMessage(prompt);
      String s = context.getScanner().next();
      if (isQuit(s)) {
        return null;
      }
      try {
        LocalDate date = LocalDate.parse(s, formatter);
        if (Utils.isValidDate(date, ticker)) {
          return date;
        }
        context.getView().writeMessage("Invalid date. Please enter again:\n");
      } catch (DateTimeParseException e) {
        context.getView().writeMessage("Invalid date format. Please use yyyy-MM-dd. Try again:\n");
      }
    }
  }

  /**
   * Validates that the range between the start and end dates is valid.
   *
   * @param start the start date to check.
   * @param end   the end date to check.
   * @return true if the range is valid, false if invalid.
   * @throws IOException if an exception is thrown.
   */
  private boolean validateDateRange(LocalDate start, LocalDate end) throws IOException {
    if (start.isAfter(end)) {
      context.getView().writeMessage("Start date cannot be after the end date. "
              + "Please enter the dates again and make sure the first "
              + "one comes before the next:\n");
      return false;
    }
    return true;
  }

  /**
   * Asks and verifies that the number of days inputted by the user is valid.
   *
   * @return the days if valid, -1 if invalid.
   * @throws IOException if an exception is thrown.
   */
  private double getDays() throws IOException {
    while (true) {
      context.getView().writeMessage("Enter amount of days: ");
      String s = context.getScanner().next();
      if (isQuit(s)) {
        return -1;
      }
      try {
        double days = Double.parseDouble(s);
        if (days >= 0) {
          return days;
        }
        context.getView().writeMessage("Days cannot be negative, please input a new amount:\n");
      } catch (NumberFormatException e) {
        context.getView().writeMessage("Invalid input. Please enter a numeric value.\n");
      }
    }
  }

  /**
   * Generates valid dates given a start and end date.
   *
   * @param start  the start date.
   * @param end    the end date.
   * @param ticker the ticker of the stock to check with.
   * @return the list of valid dates between the start and end date.
   */
  private List<LocalDate> generateValidDates(LocalDate start, LocalDate end, String ticker) {
    List<LocalDate> dates = new ArrayList<>();
    LocalDate currentDate = start;
    while (!currentDate.isAfter(end)) {
      if (Utils.isValidDate(currentDate, ticker)) {
        dates.add(currentDate);
      }
      currentDate = currentDate.plusDays(1);
    }
    return dates;
  }

  /**
   * Calculates the crossover dates given the list of dates to check. An x-day crossover happens
   * when the closing price for a day is greater than the x-day moving average for that day.
   *
   * @param dates  the dates to check.
   * @param stock  the stock to compare to.
   * @param days   the amount of days to check.
   * @param ticker the ticker of the stock.
   * @return the calculated list of crossover dates.
   */
  private List<LocalDate> calculateCrossoverDates(List<LocalDate> dates, Stock stock,
                                                  double days, String ticker) {
    List<LocalDate> crossovers = new ArrayList<>();
    for (LocalDate date : dates) {
      double sum = 0;
      double counter = 0;
      LocalDate tempDate = date;
      for (double i = 0; i < days; i++) {
        tempDate = tempDate.minusDays(1);
        if (Utils.isValidDate(tempDate, ticker)) {
          double average = (stock.getHighPrice(tempDate) + stock.getLowPrice(tempDate)) / 2;
          sum += average;
          counter++;
        }
      }
      double movingAverage = 0;  // Initialize with default value
      if (counter != 0) {
        movingAverage = sum / counter;
      }
      Double closingPrice = stock.getClosingPrice(date);
      if (closingPrice == null) {
        throw new IllegalStateException("Closing price for date " + date + " is null.");
      }
      if (closingPrice > movingAverage) {
        crossovers.add(date);
      }
    }
    return crossovers;
  }

  /**
   * Displays the given list of crossover dates.
   *
   * @param crossovers the list of dates to display.
   * @throws IOException if an exception is thrown.
   */
  private void displayCrossoverDates(List<LocalDate> crossovers) throws IOException {
    context.getView().writeMessage("Crossover dates:\n");
    for (LocalDate date : crossovers) {
      context.getView().writeMessage(date.toString() + "\n");
    }
  }

  /**
   * Checks if the user inputted quit, and quits if true.
   *
   * @param input the input to check.
   * @return true if quit, false if not.
   * @throws IOException if an exception is thrown.
   */
  private boolean isQuit(String input) throws IOException {
    if (input.equalsIgnoreCase("quit")) {
      context.getView().writeMessage("Program quit successfully.\n");
      return true;
    }
    return false;
  }
}
