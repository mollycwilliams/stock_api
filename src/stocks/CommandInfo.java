package stocks;

import java.util.Scanner;

/**
 * This interface represents the context that will be passed to the command classes
 * used to obtain input from, append output of view.
 */
public interface CommandInfo {

  /**
   * Returns the scanner of the CommandInfo object.
   *
   * @return the scanner of the CommandInfo object.
   */
  Scanner getScanner();

  /**
   * Returns the view of the CommandInfo object.
   *
   * @return the view of the CommandInfo object.
   */
  View getView();


}
