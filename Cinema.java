import java.util.Scanner;

enum States {
    CHOOSING_ACTION,
    SHOWING_SEATS,
    BUYING_TICKET,
    SHOWING_STATISTICS,
    EXIT,
}
public class Cinema {
    static final Scanner scanner = new Scanner(System.in);
    private States currentState = States.CHOOSING_ACTION;
    private int noOfRows;
    private int noOfSeatsEachRow; //columns
    private int ticketSold;
    private int currentIncome;
    private int selectedRow;
    private int selectedSeat;
    private String[][] cinemaSeats;
    String getUserStringInput() {
        return scanner.next();
    }
    void setRowsAndColumnsOfCinema() {
        //validation for entering number of rows
        do {
            System.out.print("Enter the number of rows:\n");
            while (!scanner.hasNextInt()) {
                String input = scanner.next();
                System.out.println("Wrong Input\n");
                System.out.print("Enter the number of rows:\n");
            }
            noOfRows = scanner.nextInt();
        } while (noOfRows < 0 || noOfRows > 9);

        //validation for entering number of seats each row
        do {
            System.out.print("Enter the number of seats in each row:\n");
            while (!scanner.hasNextInt()) {
                String input = scanner.next();
                System.out.println("Wrong Input\n");
                System.out.print("Enter the number of seats in each row:\n");
            }
            noOfSeatsEachRow = scanner.nextInt();
        } while (noOfSeatsEachRow < 0 || noOfSeatsEachRow > 9);
    }
    void arrangeTheSeats(){
        int number = 0;
        int number2 = 1;

        String [][] cinemaNewSeats = new String[noOfRows + 1][noOfSeatsEachRow + 1];
        //inserting elements
        for (int i = 0; i < cinemaNewSeats.length; i++) {
            for (int j = 0; j < cinemaNewSeats[i].length; j++) {
                cinemaNewSeats[i][j] = String.valueOf(number);
                cinemaNewSeats[0][0] = " ";

                if (number == cinemaNewSeats[i].length - 1) {
                    number = 0;
                    // topNumbersImplemented = true;
                }
                if (i > 0) {
                    if (j == 0) {
                        cinemaNewSeats[i][0] = String.valueOf(number2);
                        number2++;
                    } else {
                        cinemaNewSeats[i][j]= "S";
                    }
                }
                number++;
            }
        }
        cinemaSeats = cinemaNewSeats;
    }
    boolean isRowWrong(int tempRow) {
        return tempRow < 0 || tempRow > noOfRows;
    }
    boolean isSeatWrong(int tempSeat) {
        return tempSeat < 0 || tempSeat > noOfSeatsEachRow;
    }
    void setRow() {
        System.out.println("\nEnter a row number:");
        while (!scanner.hasNextInt()) {
            String input = scanner.next();
            System.out.println("Wrong input\n");
            System.out.print("Enter a row number:\n");
        }
        int tempRow = scanner.nextInt();
        if (isRowWrong(tempRow)) {
            System.out.println("Wrong input\n");
            setRow();
        } else {
            selectedRow = tempRow;
        }
    }
    void setSeat() {
        System.out.println("\nEnter a seat number in that row:");
        while (!scanner.hasNextInt()) {
            String input = scanner.next();
            System.out.println("Wrong input\n");
            System.out.println("Enter a seat number in that row:");
        }
        int tempSeat = scanner.nextInt();
        if (isSeatWrong(tempSeat)) {
            System.out.println("Wrong input\n");
            setSeat();
        } else {
            selectedSeat = tempSeat;
        }
    }
    void buyTicket() {
        setRow();
        setSeat();
        if (isSeatTaken(selectedRow, selectedSeat)) {
            System.out.println("\nThat ticket has already been purchased!");
            buyTicket();
        } else {
            ticketSold++;
            addCurrentIncome(calculatePriceOfSeat(selectedRow));
            cinemaSeats[selectedRow][selectedSeat] = "B";
            currentState = States.SHOWING_SEATS;
            showSeats();
        }
    }
    void menuCinema() {
        String choices = """
                \n1. Show the seats
                2. Buy a ticket
                3. Statistics
                0. Exit
                """;
        while (currentState.equals(States.CHOOSING_ACTION)) {
            System.out.println(choices);
            String userAction = getUserStringInput();
            switch (userAction) {
                case "1" -> {
                    currentState = States.SHOWING_SEATS;
                    showSeats();
                }
                case "2" -> {
                    currentState = States.BUYING_TICKET;
                    buyTicket();
                }
                case "3" -> {
                    currentState = States.SHOWING_STATISTICS;
                    displayStats();
                }
                case "0" -> currentState = States.EXIT;
                default -> System.out.println("Error!");
            }
        }
    }
    void showSeats() {
        //printing out elements
        System.out.println("\nCinema:");
        for (String[] strings : cinemaSeats) {
            for (String string : strings) {
                System.out.print(string + " ");
            }
            System.out.println();
        }
        currentState = States.CHOOSING_ACTION;
    }
    boolean isSeatTaken (int row, int seat) {
        return "B".equals(cinemaSeats[row][seat]);
    }
    int calculatePriceOfSeat(int row) {
        int totalNoOfSeats = noOfRows * noOfSeatsEachRow;
        int firstHalfRows = noOfRows / 2;

        if (totalNoOfSeats < 60) {
            System.out.printf("\nTicket price: $%d\n",10);
            return 10;
        } else if (row <= firstHalfRows) {
            System.out.printf("\nTicket price: $%d\n",10);
            return 10;
        } else {
            System.out.printf("\nTicket price: $%d\n",8);
            return 8;
        }
    }
    int calculateTotalIncome() {
        int totalNoOfSeats = noOfRows * noOfSeatsEachRow;

        if (totalNoOfSeats < 60) {
            int price = 10;

            return noOfRows * noOfSeatsEachRow * price;
        } else {
            int fPrice = 10;
            int sPrice = 8;

            int firstHalfRows = noOfRows / 2;
            int secondHalfRows = noOfRows - firstHalfRows;

            int firstHalfIncome = firstHalfRows * noOfSeatsEachRow * fPrice;
            int secondHalfIncome = secondHalfRows * noOfSeatsEachRow * sPrice;

            return firstHalfIncome + secondHalfIncome;
        }
    }
    void displayStats() {
        char p = '%';
        System.out.printf("\nNumber of purchased tickets: %d\n", ticketSold);
        System.out.printf("Percentage: %,.2f%s\n",getTicketPercentage(), p);
        System.out.printf("Current income: $%d\n", currentIncome);
        System.out.printf("Total income: $%d\n", calculateTotalIncome());
        currentState = States.CHOOSING_ACTION;
    }
    float getTicketPercentage() {
        float totalNoOfSeats = (noOfRows) * (noOfSeatsEachRow);
        //Percentage formula = (Value/Total value) × 100
        float fTickets = (float) ticketSold;
        return fTickets / totalNoOfSeats * 100;
    }
    void addCurrentIncome (int price) { currentIncome += price; }

    public static void main(String[] args) {
        Cinema cinema = new Cinema();
        cinema.setRowsAndColumnsOfCinema();
        cinema.arrangeTheSeats();
        cinema.menuCinema();
    }
}
