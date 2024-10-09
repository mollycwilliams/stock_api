package stocks.commands;

import stocks.CommandInfoImpl;
import stocks.Stock;
import stocks.Utils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;


/**
 * This class represents a PerformanceOverTimeCommand.
 * A PerformanceOverTimeCommand is a Command that displays the performance of a given
 * portfolio overtime, as a bar graph. The bar graph is a horizontal-text graph.
 * The scale is $1000 per *.
 */
public class PerformanceOverTimeCommand implements Command {
  private CommandInfoImpl context;

  /**
   * Constructs a PerformanceOverTimeCommand object.
   * Takes in a PerformanceOverTimeCommand object that passes the Scanner and Appendable
   * from the controller to execute its function with.
   *
   * @param context CommandInfo object that contains Scanner, Appendable.
   */
  public PerformanceOverTimeCommand(CommandInfoImpl context) {
    this.context = context;
  }

  /**
   * Runs the PerformanceOverTimeCommand.
   * Processes user input to display the performance of a portfolio over time.
   *
   * @throws IOException if user input cannot be parsed.
   */
  @Override
  public void run() throws IOException {
    boolean quit = false;
    while (!quit) {
      String name = getPortfolioName();
      if (name == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

      LocalDate[] dates = getValidatedDates(name);
      if (dates == null) {
        context.getView().writeMessage("Quit successful, returning back to menu.\n");
        break;
      }

      LocalDate start = dates[0];
      LocalDate end = dates[1];

      long daysBetween = ChronoUnit.DAYS.between(start, end);

      double numYears = (double) daysBetween / 365.0;
      double numMonths = (double) daysBetween / 30.0;
      while (daysBetween <= 0 || Math.floor(numYears) > 30) {
        context.getView().writeMessage("Time frame either less than 1 days or greater than 30 "
                + "years. Enter a better amount.\n");
        if (quit) {
          break;
        }
        daysBetween = Long.parseLong(context.getScanner().next());
      }

      ArrayList<Double> ticks;
      ticks = computeTimeFrameValues(numYears, numMonths, daysBetween);

      context.getView().writeMessage("Performance of portfolio " + name + " from " + start
              + " to " + end + ".\n\n");

      int minPortVal = getMinAmount(start, end, ticks.get(0), name);
      int maxPortVal = getMaxAmount(start, end, ticks.get(0), name);

      int starVal = (maxPortVal - minPortVal) / 20;

      if (starVal == 0) {
        starVal = 1;
      }

      makeBarChart(ticks, name, starVal, minPortVal, start, end);

      break;
    }
  }

  /**
   * Adds a zero to the beginning of a day if it is a single digit, to adjust for the bar chart.
   *
   * @param date the day to check for adjustment.
   * @return the resulting day.
   */
  private String adjustDateToZero(double date) {
    if (Integer.toString((int) date).length() == 1) {
      return "0" + (int) date;
    }
    return Integer.toString((int) date);
  }

  /**
   * Calculates the performance of a portfolio on a specific date. To be used for displaying info
   * on the bar chart.
   *
   * @param date the date to check the performance on.
   * @param name the name of the portfolio.
   * @return the performance of the portfolio at that date.
   */
  private double calculatePerformance(LocalDate date, String name) {
    double total = 0.0;
    if (date.isAfter(Utils.getPortfolios().get(name).getPurchaseDate())) {
      for (Map.Entry<String, Stock> entry : Utils.getPortfolios()
              .get(name).getListOfStocks().entrySet()) {

        try {
          Double closingPrice = entry.getValue().getClosingPrice(date);
          double numShares = entry.getValue().getSharesAtDate(date);
          total += closingPrice * numShares;
        } catch (NullPointerException e) {
          // empty
        }

      }
    } else {
      return 0;
    }
    if (total == 0) {
      return calculatePerformance(date.minusDays(1), name);
    } else {
      return total;
    }
  }


  /**
   * Gets the minimum performance value to use as a base value.
   *
   * @param start     the start date.
   * @param end       the end date.
   * @param tickValue the tickvalue to check.
   * @param name      the name of the portfolio.
   * @return the minimum performance value.
   */
  private int getMinAmount(LocalDate start, LocalDate end, double tickValue, String name) {
    int smallest = Integer.MAX_VALUE;
    LocalDate tempDate = start;
    while (tempDate.isBefore(end) || tempDate.equals(end)) {
      double performance = calculatePerformance(tempDate, name);
      if (performance != 0) {
        int temp = (int) performance;
        if (temp < smallest) {
          smallest = temp;
        }
      }
      tempDate = tempDate.plusDays((long) tickValue);
    }
    return smallest;
  }

  /**
   * Gets the maximum performance value of a portfolio.
   *
   * @param start     the start date.
   * @param end       the end date.
   * @param tickValue the tickvalue to check.
   * @param name      the name of the portfolio.
   * @return the maximum performance value.
   */
  private int getMaxAmount(LocalDate start, LocalDate end, double tickValue, String name) {
    int max = Integer.MIN_VALUE;
    LocalDate tempDate = start;
    while (tempDate.isBefore(end) || tempDate.equals(end)) {
      double performance = calculatePerformance(tempDate, name);
      int temp = (int) performance;
      if (temp > max) {
        max = temp;
      }
      tempDate = tempDate.plusDays((long) tickValue);
    }
    return max;
  }


  /**
   * Prints out the stars for the bra chart given the data for the chart.
   *
   * @param date    the date to print for.
   * @param name    the name of the portfolio.
   * @param starVal the value for how much money each star is worth.
   * @param base    the base performance to be subtracted by each performance.
   */
  private void makeStars(LocalDate date, String name, int starVal, int base) {
    try {
      int currentPortVal = (int) (calculatePerformance(date, name) - base) / starVal;
      if (currentPortVal == 0) {
        context.getView().writeMessage("*");
      } else {
        for (int i = 0; i < currentPortVal; i++) {
          context.getView().writeMessage("*");
        }
      }
    } catch (NullPointerException e) {
      // empty
    }
  }

  private ArrayList<Double> computeTimeFrameValues(double numYears, double numMonths,
                                                   long daysBetween) {
    ArrayList<Double> ticks = new ArrayList<>();
    if (numYears >= 5) {
      ticks.add(365.0);
      ticks.add(Math.floor(numYears));
    } else if (numMonths > 30) {
      ticks.add(60.0);
      ticks.add(Math.floor(numMonths / 2));
    } else if (numMonths >= 5.0) {
      ticks.add(30.0);
      ticks.add(Math.floor(numMonths));
    } else if (daysBetween < 30) {
      ticks.add(1.0);
      ticks.add((double) daysBetween);
    } else {
      ticks.add(5.0);
      ticks.add(Math.floor((double) daysBetween / 5));
    }

    return ticks;
  }

  /**
   * Gets the portfolio name from the user.
   *
   * @return the name of the portfolio, or null if the user quits.
   * @throws IOException if user input cannot be parsed.
   */
  private String getPortfolioName() throws IOException {
    while (true) {
      context.getView().writeMessage("Which portfolio would you like to see the performance of? "
              + "Or press quit to exit.\n");
      String name = context.getScanner().next();
      if (name.equals("quit")) {
        context.getView().writeMessage("Program quit successfully.\n");
        return null;
      }
      if (Utils.getPortfolios().containsKey(name)) {
        return name;
      }
      context.getView().writeMessage("This portfolio does not exist.\nPlease enter a new name: \n");
    }
  }


  /**
   * Creates the bar chart visualization for the given time frame, tick values, and grpah.
   *
   * @param ticks      the value of the time ticks.
   * @param name       the name of the portfolio.
   * @param starVal    the value of each star.
   * @param minPortVal the minimum value of the portfolio.
   * @param start      the start date to calculate the performance with.
   * @param end        the end date to calculate the performance with.
   */
  private void makeBarChart(ArrayList<Double> ticks, String name, int starVal,
                            int minPortVal, LocalDate start, LocalDate end) {
    double counter = ticks.get(1);
    LocalDate temp = start;
    while (counter > 1) {
      if (ticks.get(0) == 365.0) {
        context.getView().writeMessage(temp.getYear() + ": ");
        makeStars(temp, name, starVal, minPortVal);
        temp = temp.plusYears(1);


      } else if (ticks.get(0) == 60.0) {
        context.getView().writeMessage(temp.getMonth().toString().substring(0, 3)
                + " " + temp.getYear() + ": ");
        makeStars(temp, name, starVal, minPortVal);
        temp = temp.plusMonths(2);


      } else if (ticks.get(0) == 30.0) {
        context.getView().writeMessage(temp.getMonth().toString().substring(0, 3)
                + " " + temp.getYear() + ": ");
        makeStars(temp, name, starVal, minPortVal);
        temp = temp.plusMonths(1);


      } else if (ticks.get(0) == 1.0) {
        context.getView().writeMessage(adjustDateToZero(temp.getDayOfMonth()) + " "
                + temp.getMonth().toString().substring(0, 3) + " "
                + temp.getYear() + ": ");
        makeStars(temp, name, starVal, minPortVal);
        temp = temp.plusDays(1);


      } else {
        context.getView().writeMessage(adjustDateToZero(temp.getDayOfMonth()) + " "
                + temp.getMonth().toString().substring(0, 3) + " "
                + temp.getYear() + ": ");
        makeStars(temp, name, starVal, minPortVal);
        temp = temp.plusDays(5);
      }
      context.getView().writeMessage("\n");
      counter -= 1;
    }


    // for last day
    if (ticks.get(0) == 365.0) {
      context.getView().writeMessage(end.getYear() + ": ");
      makeStars(temp, name, starVal, minPortVal);

    } else if (ticks.get(0) == 60.0) {
      context.getView().writeMessage(end.getMonth().toString().substring(0, 3)
              + " " + end.getYear() + ": ");

      makeStars(temp, name, starVal, minPortVal);

    } else if (ticks.get(0) == 30.0) {
      context.getView().writeMessage(end.getMonth().toString().substring(0, 3) + " "
              + end.getYear() + ": ");

      makeStars(temp, name, starVal, minPortVal);

    } else if (ticks.get(0) == 1.0) {
      context.getView().writeMessage(adjustDateToZero(end.getDayOfMonth()) + " " + end.getMonth()
              .toString().substring(0, 3) + " "
              + temp.getYear() + ": ");

      makeStars(temp, name, starVal, minPortVal);

    } else {
      context.getView().writeMessage(adjustDateToZero(end.getDayOfMonth()) + " " + temp.getMonth()
              .toString().substring(0, 3) + " "
              + temp.getYear() + ": ");

      makeStars(temp, name, starVal, minPortVal);
    }

    context.getView().writeMessage("\nScale: * = " + starVal +
            " more than minimum portfolio value : " + minPortVal + "\n");
  }


  /**
   * Returns an arraylist of the start and end date which are valid.
   *
   * @param name the name of the portfolio.
   * @return an arraylist of the start and end date which are valid.
   * @throws IOException if an invalid date is processed.
   */
  private LocalDate[] getValidatedDates(String name) throws IOException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate start = null;
    LocalDate end = null;

    while (true) {
      context.getView().writeMessage("Portfolio was made at "
              + Utils.getPortfolios().get(name).getPurchaseDate()
              + ". All dates before this will show up as no value.\n");
      context.getView().writeMessage("Enter start date (yyyy-MM-dd):\n");
      String startInput = context.getScanner().next();
      if (startInput.equalsIgnoreCase("quit")) {
        context.getView().writeMessage("Exiting...\n");
        return null;
      }
      try {
        start = LocalDate.parse(startInput, formatter);
      } catch (DateTimeParseException e) {
        context.getView().writeMessage("Invalid date format. Please use yyyy-MM-dd. Try again:\n");
        continue;
      }

      context.getView().writeMessage("Enter end date (yyyy-MM-dd):\n");
      String endInput = context.getScanner().next();
      if (endInput.equalsIgnoreCase("quit")) {
        context.getView().writeMessage("Exiting...\n");
        return null;
      }
      try {
        end = LocalDate.parse(endInput, formatter);
        if (!start.isAfter(end)) {
          if (validateDateRange(start, end, name)) {
            return new LocalDate[]{start, end};
          } else {
            context.getView().writeMessage("One or both dates are outside the valid " +
                    "range or do not contain data.\n");
          }
        } else {
          context.getView().writeMessage("Start date must be before end date.\n");
        }
      } catch (DateTimeParseException e) {
        context.getView().writeMessage("Invalid date format. Please use yyyy-MM-dd. Try again:\n");
      }
    }
  }

  /**
   * Checks if the start and end date for the portfolio exists.
   *
   * @param start the start date.
   * @param end   the end date.
   * @param name  the name of the portfolio.
   * @return if the start and end date for the portfolio exists.
   */
  private boolean validateDateRange(LocalDate start, LocalDate end, String name) {
    return Utils.dateExists(start, name) && Utils.dateExists(end, name);
  }


}

