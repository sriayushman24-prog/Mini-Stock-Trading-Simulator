/*
 * Name: Ayushman Sri
 * Date: Jan 16, 2026
 * Culminating Project: Mini-Stock Trading Simulator
 */
import java.text.DecimalFormat;
import java.util.*;
public class TradingSimulator {
    public static void main(String[] args) {
        Scanner input=new Scanner(System.in);
        Random random=new Random();
        //Create 3 parallel arrays for Symbols, Prices and Quantity and initialize them
        String[] symbols = {"AAPL", "AMZN", "GOOG", "META", "MSFT"};
        double[] prices = {275.00, 222.00, 306.00, 657.00, 476.00};
        int[] quantities={0,0,0,0,0};
        double cashBalance=10000.00;
        double currCashBal=10000.00;
        System.out.println("Welcome to Mini Stock Trading Simulator");
        System.out.println("---------------------------------------");
        int menuOption;
        boolean runSimulator = true;
        //Display the Menu to the user until user choose to exit
        do {
            System.out.println("\n1 - View Portfolio");
            System.out.println("2 - Buy Stock");
            System.out.println("3 - Sell Stock");
            System.out.println("4 - Exit");
            System.out.println("\nPlease enter an option: ");
            cashBalance=currCashBal;
            //check if user has entered a character input for the Menu option and display error message
            if (!input.hasNextDouble()) {
                System.out.println("Invalid Menu Option. Please enter a valid menu(1-4) option.");
                input.next(); // discard the character input
                continue;
            }
            menuOption = input.nextInt();
            //perform View Portfolio, Buy Stock, Sell Stock and Exit based on user choice
            switch (menuOption) {
                case 1:
                    viewPortfolio(symbols, prices, quantities, cashBalance);
                    break;
                case 2:
                    currCashBal=buyStock(symbols, prices, quantities, cashBalance, input, random);
                    break;
                case 3:
                    currCashBal=sellStock(symbols, prices, quantities, cashBalance, input, random);
                    break;
                case 4:
                    System.out.println("Exit");
                    runSimulator=false;
                    viewPortfolio(symbols, prices, quantities, cashBalance);
                    System.out.println("Thank you for using the Mini Stock Trading Simulator");
                    break;
                default:
                    System.out.println("Invalid Menu Option. Please enter a valid menu(1-4) option.");
            }
        } while(runSimulator);
    }
    //View Portfolio method where the current holding of owned stocks, their current prices and total value
    //displayed to the user
    public static void viewPortfolio(String[] sym, double[] prc, int[] qty, double cashBal) {
        //print the header for the portfolio summary
        System.out.println("\n       Your Portfolio Summary");
        System.out.println("------------------------------------");
        System.out.println("Symbol  Price($)  Quantity  Value($)");
        System.out.println("======  =======   =======   ========");
        double totalValue=0;
        DecimalFormat df = new DecimalFormat("0.00");
        //calculate the total value of each stock owned and round it to 2 decimal places and print it
        for (int i = 0; i < sym.length; i++) {
            double value = prc[i] * qty[i];
            totalValue += value;
            String roundedPrice= df.format(prc[i]);
            String roundedValue=df.format(value);
            System.out.println(sym[i] + "\t" + roundedPrice+ "\t\t" + qty[i] + "\t\t" + roundedValue);
        }
        //calculate the current net worth, print cash balance and new net worth
        double netWorth= cashBal + totalValue;
        String roundedCashBal= df.format(cashBal);
        String roundedNetWorth=df.format(netWorth);
        System.out.println("\nCash Balance: $" +roundedCashBal );
        System.out.println("Net Worth: $" + roundedNetWorth);
        System.out.println("------------------------------------");
    }
    //perform the Buy Stock and return the updated cash balance
    public static double buyStock(String[] sym,double[] prc,int[] qty, double cashBal, Scanner input, Random num) {
       //print the header for available stocks to buy
        System.out.println("Available Stocks to Buy:");
        System.out.println("Symbol   Price($)");
        System.out.println("======   ========");
        DecimalFormat df = new DecimalFormat("0.00");
        //Print the available stocks and new prices in 2 decimal places
        for (int i = 0; i <sym.length; i++) {
            String roundedPrice =df.format(prc[i]);
            System.out.println(sym[i] + "      $" + roundedPrice);
        }
        int index;
        //Ask the user to enter a stock symbol until they enter a valid symbol
        do {
            System.out.print("\nEnter a stock symbol to buy: ");
            String symbol = input.next().toUpperCase();
            index = doesSymbolExist(sym,symbol);
            if (index == -1) {
                System.out.println("Invalid stock symbol. Please try again.");
            }
        }while(index == -1);
        int quantity=0;
        double purchaseCost=0;
        //Ask the user to enter a Quantity until they enter a valid quantity
        do {
            System.out.print("Enter a quantity: ");

            if (!input.hasNextInt()) {
                //check if a character quantity is entered and print an error message
                System.out.println("Invalid input. Please enter an integer quantity.");
                input.next(); // discard the character input
                quantity = 0;
                continue;
            }
            quantity=input.nextInt();
            purchaseCost=prc[index] * quantity;
            //if quantity entered is negative ask for quantity again
            if (quantity <= 0) {
                System.out.println("Invalid quantity. Try again!");
            }
            //Check if the purchase cost is greater than the available cash balance
            else if (purchaseCost > cashBal) {

                if (prc[index] > cashBal) {
                    int count = 0;
                    String stocksCanBuy = "";
                    //Find what stocks can be bought with the available cash balance
                    for (int i = 0; i < prc.length; i++) {
                        if (cashBal < prc[i]) {
                            count++;
                        } else {
                            if (!stocksCanBuy.isEmpty()) {
                                stocksCanBuy += ", ";
                            }
                            stocksCanBuy += sym[i];
                        }
                    }
                    //Check if Cash balance is not enough to buy any stock and print an error message
                    if (count == prc.length) {
                        System.out.println("Cash balance is not sufficient to buy any stocks.");
                        return cashBal;
                        //Print the stocks that can be bought with the cash balance available
                    } else {
                        System.out.println("You can only buy any of " + stocksCanBuy + " stocks.");
                        return cashBal;
                    }
                    //If the cash balance is not enough to buy a stock print an error message
                } else {
                    System.out.println("Insufficient funds to buy " + quantity + " " + sym[index] + " stocks. Enter a lower quantity to proceed with the Buy.");

                }
            }
        }while(quantity <=0 ||purchaseCost > cashBal );
        //if the Cash balance is enough proceed with the buy and update the cash balance and quantity
        qty[index] += quantity;
        cashBal -= purchaseCost;
        //display the confirmation message and print the updated portfolio and print the updated market price
        System.out.println("You have successfully bought " + quantity + " " + sym[index] + " stock(s).");
        viewPortfolio(sym,prc,qty,cashBal);
        updateMarketPrices(sym, prc, num);
        return cashBal;
    }
    //Method to check if an entered symbol by the user exist. If it exists it returns the index of the symbol
    public static int doesSymbolExist(String[] sym, String symInput) {
        for (int i = 0; i < sym.length; i++) {
            if (sym[i].equals(symInput)) {
                return i;
            }
        }
        return -1;
    }
    //Method to perform a Sell Stock and it returns the cash balance after a successful Sell
    public static double sellStock(String[] sym,double[] prc,int[] qty, double cashBal, Scanner input, Random num) {
        //display the current portfolio to assist choosing the stock to sell
        viewPortfolio(sym, prc, qty, cashBal);
        int index;
        //Ask for a symbol to sell until a valid symbol is entered by the user
        do {
            System.out.print("\nEnter a stock symbol to sell: ");
            String symbol = input.next().toUpperCase();
            index = doesSymbolExist(sym,symbol);
            if (index == -1) {
                System.out.println("Invalid stock symbol. Please try again.");
            }
        }while(index == -1);
        //If valid symbol is entered check if the user holds that security
        if (qty[index] == 0) {
            System.out.println("\nYou do not hold any " + sym[index] + " stocks to sell. Cannot perform the sell.");
            return cashBal;
        }
        //if the user holds the security ask the user enter the quantity
        else {

            int quantity = 0;
            double sellAmount = 0;
            //Ask the user to enter the quantity until a valid quantity is entered
            do {
                System.out.print("Enter a quantity: ");
                //Check if a character input is entered and display an error message
                if (!input.hasNextInt()) {
                    System.out.println("Invalid input. Please enter an integer quantity.");
                    input.next(); // discard the character input
                    quantity = 0;
                    continue;
                }
                //if Valid quantity entered calculate the Sell Amount
                quantity = input.nextInt();
                sellAmount = prc[index] * quantity;

                //if quantity entered is negative print an error message
                if (quantity < 0) {
                    System.out.println("Invalid quantity. Try again!");
                    //if quantity is greater than the number of stocks owned print error message
                } else if (quantity > qty[index]) {
                    System.out.println("You don't have enough quantity to sell. Please enter a value less than or equal to " + qty[index]);
                }

            } while (quantity < 0 || quantity > qty[index]);
            //if Valid Symbol and Quantity is entered update the quantity and cash balance and print a confirmation message
            qty[index] -= quantity;
            cashBal += sellAmount;
            System.out.println("You have successfully sold " + quantity + " " + sym[index] + " stock(s).");
            //print the updated portfolio
            viewPortfolio(sym, prc, qty, cashBal);
            //print updated market price
            updateMarketPrices(sym, prc, num);
            return cashBal;
        }
    }
    //Method to update the market price using random generator
    public static void updateMarketPrices(String[] sym, double[] prc, Random random) {
        //print header for updated market price
        System.out.println("Updated New Market Prices:");
        System.out.println("Symbol   Price($)");
        System.out.println("======   ========");
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i=0; i < prc.length; i++) {
            //finding a random factor to update the stock price by +/- 5%
            double randomFactor = (random.nextDouble()*0.1)-0.05;
            //update the new market price with the random factor
            prc[i]= prc[i] * (1 + randomFactor);
            String roundedPrice = df.format(prc[i]);
            //print the updated market price
            System.out.println(sym[i] + "      $" + roundedPrice);
        }
    }
}