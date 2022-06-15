package cinema;

import java.util.Arrays;
import java.util.Scanner;

public class Cinema {

    static Scanner scanner = new Scanner(System.in);
    static boolean exit = false;
    static int purchasedTickets = 0;
    static int currentIncome = 0;

    public static void main(String[] args) {

        menu();

    }
    public static void printTicketPrice(int ticketPrice) {
       System.out.println();
       System.out.println("Ticket price: \n" + "$" + ticketPrice);
    }
    public static int ticketPrice(int finalRows, int finalSeats, int pickedRowNumber) {
        int ticketPrice;
        int frontRows;

        if ((finalRows * finalSeats) <=60) {
            ticketPrice =  10;
        } else {
            frontRows = finalRows /2;
            ticketPrice = pickedRowNumber < frontRows ? 10 : 8;
        }

        return ticketPrice;
    }
    public static int numberOfRows () {
        System.out.println("Enter the number of rows: ");
        return scanner.nextInt();
    }

    public static int numberOfSeats () {
        System.out.println("Enter the number of seats in each row: ");
        return scanner.nextInt();
    }

    public static int pickedRowNumber() {
        System.out.println();
        System.out.println("Enter a row number: ");
        return scanner.nextInt();
    }

    public static int pickedSeatNumber() {
        System.out.println("Enter a seat number in that row: ");
        return scanner.nextInt();
    }

    public static void printBookedTicketLayOut(String[][] arrayToPrint) {

        System.out.println();
        System.out.println("Cinema:");
        for (String[] strings : arrayToPrint) {
            for (String string : strings) {
                System.out.print(string);
            }
            System.out.println();
        }
    }

    public static int [] initialVerifyPickedSeat(int rows, int seats) {
        int[]verifiedPickedLocation = new int[2];
        int pickedRow = pickedRowNumber();
        int pickedSeat = pickedSeatNumber();
        boolean correct = false;
        while (!correct) {
            if ((pickedRow >= rows || pickedRow <=0) ) {
                System.out.println("Wrong input!");
                pickedRow = pickedRowNumber();
                pickedSeat = pickedSeatNumber();
            } else if (pickedSeat >= seats || pickedSeat <=0) {
                pickedRow = pickedRowNumber();
                pickedSeat = pickedSeatNumber();
                System.out.println("Wrong input!");
            } else {
                verifiedPickedLocation[0] = pickedRow;
                verifiedPickedLocation[1] = pickedSeat;
                correct = true;
            }
        }
        return verifiedPickedLocation;
    }

    public static String[][] verifiedSeats(int rows, int seats, String[][] initialArray) {
        int[] arrayOfSeats = initialVerifyPickedSeat(rows, seats);
        int pickedRow = arrayOfSeats[0];
        int pickedSeat = arrayOfSeats[1];
        String seatToVerify = initialArray[pickedRow][pickedSeat];

        if (seatToVerify.contains(" B")) {
            System.out.println("That ticket has already been purchased!");
            verifiedSeats(rows, seats, initialArray);

        }else {
            saveBookedTickets(initialArray, pickedRow, pickedSeat);
            printTicketPrice(ticketPrice(rows, seats, pickedRow));
            purchasedTickets = purchasedTickets + 1;
            currentIncome = currentIncome + ticketPrice(rows, seats, pickedRow);

        }
        return initialArray;
    }
    public static String[][] saveBookedTickets(String[][]array, int pickedRow, int pickedSeat) {
        array[pickedRow][pickedSeat] = " B";
        return array;
    }

    public static String[][] initialArray(int rows, int seats) {
        String [][] array = new String[rows][seats];
        for (int row =0; row < rows; row++){
            for (int seat = 0; seat < seats; seat++ ) {
                if ((row ==0) && (seat == 0)) {
                    array[row][seat] = "  ";
                } else if (row == 0) {
                    array[row][seat] = (seat) + " ";
                } else if (seat == 0) {
                    array[row][seat] = String.valueOf(row);
                } else {
                    array[row][seat] = " S";
                }
            }
        }
        return array;
    }

    public static void statistics(int rows, int seats) {
        rows = rows -1;
        seats = seats -1;
        int totalIncome = 0;
        int frontRows = 0;
        int backRows = 0;
        int totalNumberOfTickets = rows * seats;
        double percentageOfOccupiedSeats = ((double) purchasedTickets/ totalNumberOfTickets) *100;

        if (totalNumberOfTickets <= 60) {
            totalIncome = totalNumberOfTickets * 10;
        } else {
            frontRows = (rows - 1) / 2;
            backRows = rows - frontRows;
            totalIncome = (frontRows * seats * 10) + (backRows * seats * 8);
        }

        System.out.println("Number of purchased tickets: " + purchasedTickets);
        System.out.printf("Percentage: " + "%.2f%%\n", percentageOfOccupiedSeats);
        System.out.println("Current income: $" + currentIncome);
        System.out.println("Total income: $" + totalIncome);

    }

    public static void menu() {
        int rows = numberOfRows() + 1;
        int seats = numberOfSeats() + 1;
        String[][] initialArray = initialArray(rows, seats);
        String[][] arrayToPrint = new String[rows][seats];


        while (!exit) {
            System.out.println();
            System.out.println("" +
                    "1. Show the seats\n" +
                    "2. Buy a ticket\n" +
                    "3. Statistics \n" +
                    "0. Exit");
            int inputCase = scanner.nextInt();
            switch (inputCase) {
                case 2:
                    arrayToPrint = verifiedSeats(rows, seats, initialArray);

                    break;
                case 1:
                    String checking = Arrays.deepToString(arrayToPrint);
                    if (checking.contains("B") ) {
                    printBookedTicketLayOut((arrayToPrint));
                    } else {
                        printBookedTicketLayOut(initialArray);
                    }
                break;
                case 3:
                    statistics(rows, seats);
                    break;
                case 0:
                    exit = true;
                    break;
            }
        }
    }



}