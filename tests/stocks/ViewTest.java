package stocks;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;


/**
 * Class for testing the View.
 */
public class ViewTest {

  /**
   * Tests to ensure that the printMenu() command prints the menu properly.
   */
  @Test
  public void testPrintMenu() {

    ArrayList<String> logs = new ArrayList<>();
    View v = new MockView(logs);
    v.printMenu();

    assertEquals("Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock, given"
            + " a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n" + "q or quit (quit the program)\n",
            logs.get(0));

  }

  /**
   * Tests to ensure the welcomeMessage() command prints the welcome message properly.
   */
  @Test
  public void testWelcomeMessage() {
    ArrayList<String> logs = new ArrayList<>();
    View v = new MockView(logs);
    v.welcomeMessage();

    assertEquals("Welcome to the stock program!\n"
            + "Supported user instructions are: \n"
            + "create-portfolio (creates a new portfolio with the given name)\n"
            + "add-stock (adds given stock to a portfolio)\n"
            + "crossover (calculates the crossover dates for a given stock\n"
            + "moving-average (calculates the moving-average for a given stock, given"
            + " a date and a number of days)\n"
            + "performance (calculates the performance for a given stock\n"
            + "performance-all (calculates the performance for a given portfolio\n"
            + "display-stocks (displays all the stocks in the given portfolio)\n"
            + "menu (Print supported instruction list)\n" + "q or quit (quit the program)\n",
            logs.get(0));

  }

  /**
   * Tests to ensure the farewellMessage() command prints the farewell message properly.
   */
  @Test
  public void testFarewellMessage() {

    ArrayList<String> logs = new ArrayList<>();
    View v = new MockView(logs);
    v.farewellMessage();

    assertEquals("Thank you for using this stock program! Happy gambling!", logs.get(0));

  }

  /**
   * Tests to ensure the correct message is appended and dislayed.
   *
   */
  @Test
  public void testWriteMessage1() {
    ArrayList<String> logs = new ArrayList<>();
    View v = new MockView(logs);
    v.writeMessage("hiiiii");

    assertEquals("hiiiii", logs.get(0));

    v.writeMessage("byeeeeeioqntoqerigjerig398943944t3");

    assertEquals("byeeeeeioqntoqerigjerig398943944t3", logs.get(1));


  }

}
