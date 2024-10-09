    DESIGN EXPLANATION


(06/20/2024)
For the design of our gui, we basically had to reimplement all of our code. This is because for the
past two parts of the assignment, we used the Command Design pattern which relied heavily on the
view. For the third part of the assignment however, we created a new View interface which directly
interacts with the user by displaying information, and then by delivering information to our
controller. We also created a new controller class that uses data from the model and view to
perform actions in correspondence to the user. We reused our Stock/StockImpl and
Portfolio/PortfolioImpl interfaces/classes from the previous assignments to perform calculations,
and instead of utilizing a static map like our previous assignments we created an overarching
main model class to represent the portfolios accessed in the program / user.
---------------------------------------------------------------------------------------------------
(06/14/2024) For part 2 of the assignment, we continued to utilize the Command Design pattern as we
implemented new functionality by creating classes for each new command, (CompositionCommand,
DistributionDisplayCommand, PerformanceOverTimeCommand, PurchaseStockCommand, SellStockCommand,
RebalancePortfolioCommand, RetrievePortfolioCommand, SavePortfolioCommand). Instead of creating a
new controller/model that extends our old ones, we directly edited our previous code (as we feel
the code hadn't reached its maturity / finality yet) by adding a new field, a protected final
Map<LocalDate, Double> shareDates, for a StockImpl object that stores the dates it bought shares on,
as well as the amount of shares purchased for the stock. Also we moved our static stocks and
portfolios to Utils from CommandInfo to decrease coupling.

---------------------------------------------------------------------------------------------------
(06/06/2024) For the design of our program, we decided to implement both the Command Design Pattern,
Builders, and MVC (as expected). Our builder classes are PortfolioBuilder and StockBuilder,
our models are interface Stock / implementation StockImpl, and Portfolio / PortfolioImpl.
Our view is the interface View and implementation ViewImpl. Our controller consists of
interface StockController and its implementation StockControllerImpl.

The commands are contained in a package stocks.commands, and are called in the controller,
according to the user input, and perform each task: (performance / PerformanceCommand,
performance-all / PerformanceAllCommand, crossover / CrossoverCommand, moving-average /
MovingAverageCommand, display-stocks / DisplayStocks). We also implemented a command that builds a
portfolio based on user input (create-portfolio / BuildPortfolioCommand) and a command that gets
the data for a stock from the API key (GetDataCommand).

Additionally, each Command class took in a field which was initialized to a CommandInfo object. This
serves as a central environment object which passes in the Scanner, View, and our static stocks and
portfolios. By passing the CommandInfo, this reduces coupling as the individual command objects
don't need to manage how the data is stored or maintained. Also, the CommandInfo has scalability
in mind as new features can be added to a central place which integrates easily throughout the
whole program. This can reduce the risk of breaking existing functionality.

For our models, we implemented a model for a Stock, (Stock / StockImpl) and a model for a Portfolio
(Portfolio / PortfolioImpl). We decided on using a static Map<String, Stock> that is contained
within CommandInfo for all the stocks the user gets data for / adds to a portfolio. We created a
Utils class for miscellaneous functions such as isValidDate, isValidStock, and isValidChecker which
are used to check validity of fields used throughout many of our classes. StockMain.java is our
main file that contains the main method, to run the program.

--------------------------------------------------------------------------------------------------
