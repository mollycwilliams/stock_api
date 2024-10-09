package stocks;

import java.util.Scanner;

/**
 * CommandInfo class representing the context that will be passed to the command classes
 * used to obtain input from, append output of view.
 */
public class CommandInfoImpl implements CommandInfo {
  private Scanner scanner;
  private View view;

  /**
   * Constructor for CommandInfo which takes in a Scanner and Appendable and sets its
   * fields to those.
   */
  public CommandInfoImpl() {
    this.scanner = new Scanner(System.in);
    this.view = new ViewImpl(System.out);
  }

  /**
   * Constructor for CommandInfo which takes in a Scanner and Appendable and sets its
   * fields to those.
   *
   * @param scanner The scanner  object.
   * @param view    the view object.
   */
  public CommandInfoImpl(Scanner scanner, View view) {
    this.scanner = scanner;
    this.view = view;
  }

  /**
   * Returns the scanner of the CommandInfo object.
   *
   * @return the scanner of the CommandInfo object.
   */
  public Scanner getScanner() {
    return this.scanner;
  }

  /**
   * Returns the view of the CommandInfo object.
   *
   * @return the view of the CommandInfo object.
   */
  public View getView() {
    return this.view;
  }


}
