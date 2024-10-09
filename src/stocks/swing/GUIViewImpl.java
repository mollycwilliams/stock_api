package stocks.swing;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

/**
 * A GUI implementation of the stock portfolio management view.
 * Provides a user interface to interact with portfolios, stocks, and various actions.
 */
public class GUIViewImpl extends JFrame implements GUIView {
  private JTextArea textArea;
  private JButton createPortfolioButton;
  private JButton buyStockButton;
  private JButton sellStockButton;
  private JButton showPortfolioButton;
  private JButton savePortfolioButton;
  private JButton retrievePortfolioButton;
  private JComboBox<String> portfolioComboBox;
  private DefaultListModel<String> portfolioListModel;

  /**
   * Constructs the GUIViewImpl and initializes the user interface components.
   * Sets up the main frame, buttons, text area, combo box, and list for portfolio management.
   */
  public GUIViewImpl() {
    setTitle("Stock Portfolio Management");
    setSize(800, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    textArea = new JTextArea();
    textArea.setEditable(false);
    add(new JScrollPane(textArea), BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(2, 3));

    createPortfolioButton = new JButton("Create Portfolio");
    buyStockButton = new JButton("Buy Stock");
    sellStockButton = new JButton("Sell Stock");
    showPortfolioButton = new JButton("View Portfolio Composition");
    savePortfolioButton = new JButton("Save Portfolio");
    retrievePortfolioButton = new JButton("Retrieve Portfolio");

    buttonPanel.add(createPortfolioButton);
    buttonPanel.add(buyStockButton);
    buttonPanel.add(sellStockButton);
    buttonPanel.add(showPortfolioButton);
    buttonPanel.add(savePortfolioButton);
    buttonPanel.add(retrievePortfolioButton);

    add(buttonPanel, BorderLayout.SOUTH);

    portfolioComboBox = new JComboBox<>();
    add(portfolioComboBox, BorderLayout.NORTH);

    portfolioListModel = new DefaultListModel<>();
    JList<String> portfolioList = new JList<>(portfolioListModel);
    JScrollPane portfolioScrollPane = new JScrollPane(portfolioList);
    portfolioScrollPane.setPreferredSize(new Dimension(200, getHeight()));
    portfolioScrollPane.setBorder(BorderFactory.createTitledBorder("Available Portfolios"));
    add(portfolioScrollPane, BorderLayout.EAST);

    setVisible(true);
  }

  /**
   * Displays a message to the user in the text area.
   *
   * @param message the message to display
   */
  @Override
  public void displayMessage(String message) {
    textArea.append(message + "\n");
  }

  /**
   * Prompts the user with a dialog to input a value.
   *
   * @param prompt the message to display in the dialog
   * @return the user input as a string
   */
  @Override
  public String getUserInput(String prompt) {

    return JOptionPane.showInputDialog(this, prompt);
  }

  /**
   * Sets the available portfolio names in the combo box and list.
   *
   * @param names an array of portfolio names
   */
  @Override
  public void setPortfolioNames(String[] names) {
    portfolioComboBox.removeAllItems();
    portfolioListModel.clear();
    for (String name : names) {
      portfolioComboBox.addItem(name);
      portfolioListModel.addElement(name);
    }
  }

  /**
   * Gets the currently selected portfolio from the combo box.
   *
   * @return the selected portfolio name
   */
  @Override
  public String getSelectedPortfolio() {
    return portfolioComboBox.getSelectedItem().toString();
  }

  /**
   * Adds an ActionListener to the "Create Portfolio" button.
   *
   * @param listener the ActionListener to handle button actions
   */
  @Override
  public void addCreatePortfolioListener(ActionListener listener) {
    createPortfolioButton.addActionListener(listener);
  }

  /**
   * Adds an ActionListener to the "Buy Stock" button.
   *
   * @param listener the ActionListener to handle button actions
   */
  @Override
  public void addBuyStockListener(ActionListener listener) {
    buyStockButton.addActionListener(listener);
  }

  /**
   * Adds an ActionListener to the "Sell Stock" button.
   *
   * @param listener the ActionListener to handle button actions
   */
  @Override
  public void addSellStockListener(ActionListener listener) {
    sellStockButton.addActionListener(listener);
  }

  /**
   * Adds an ActionListener to the "View Portfolio Composition" button.
   *
   * @param listener the ActionListener to handle button actions
   */
  @Override
  public void addShowPortfolioListener(ActionListener listener) {
    showPortfolioButton.addActionListener(listener);
  }

  /**
   * Adds an ActionListener to the "Save Portfolio" button.
   *
   * @param listener the ActionListener to handle button actions
   */
  @Override
  public void addSavePortfolioListener(ActionListener listener) {
    savePortfolioButton.addActionListener(listener);
  }

  /**
   * Adds an ActionListener to the "Retrieve Portfolio" button.
   *
   * @param listener the ActionListener to handle button actions
   */
  @Override
  public void addRetrievePortfolioListener(ActionListener listener) {
    retrievePortfolioButton.addActionListener(listener);
  }

}

