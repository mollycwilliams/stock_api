package stocks.swing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import stocks.CommandInfoImpl;

/**
 * This class represents a GetDataCommand.
 * A GetDataCommand is a Command that creates a CSV file
 * for the Stock inputted buy the user.
 */
public class GetDataCommand {

  private CommandInfoImpl context;
  private static final String apiKey = "YGDOBLMWPW61EYXN";
  private static final String url = "https://www.alphavantage.co/query";

  /**
   * Constructs a GetDataCommand object.
   * Takes in a CommandInfo object that passes the Scanner and Appendable
   * from the controller to execute its function with.
   */
  public GetDataCommand() {
    // creates an empty command
  }

  /**
   * Runs the GetDataCommand.
   * Processes user input to get CSV data for the stock, given its ticker.
   *
   * @param ticker The ticker to run with.
   * @throws IOException if user input cannot be parsed.
   */
  public void run(String ticker) throws IOException {

    File file = new File("StockData/" + ticker + ".csv");
    if (!file.exists()) {
      String tempUrl = url + "?function=TIME_SERIES_DAILY" + "&symbol=" + ticker + "&apikey="
              + apiKey + "&outputsize=full" + "&datatype=csv";
      URL newUrl = new URL(tempUrl);


      // sets up connection to API
      HttpURLConnection connection = (HttpURLConnection) newUrl.openConnection();

      try (InputStream in = connection.getInputStream();
           // read response stream
           BufferedReader reader = new BufferedReader(new InputStreamReader(in));
           FileWriter writer = new FileWriter("StockData/" + ticker + ".csv")) {
        String line;
        while ((line = reader.readLine()) != null) {
          writer.write(line + "\n"); // writes each line to csv
        }
      } catch (IOException e) {
        context.getView().writeMessage("IOException from trying to get data, message: "
                + e.getMessage());
      } finally {
        // guarantees connection disconnects regardless of what happens
        connection.disconnect();
      }
    }
  }


}