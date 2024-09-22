package org.example;

import java.util.*;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner (System.in);
        int[] electricityPrice = new int[24];
        String[] timeRanges;
        timeRanges = createTimespans();

        while (true) {
            printMenu();
            String userInput = scanner.nextLine();
            switch(userInput.toLowerCase()) {
            case "1" -> electricityPrice = setElectricityPrice(scanner);

            case "2" -> System.out.print(calculatingMinMaxAverage(electricityPrice, timeRanges));

            case "3" -> sortArray(electricityPrice, timeRanges);

            case "4" -> System.out.print(findLowestOfFour(electricityPrice));

            case "5" -> visualise(electricityPrice);

            case "e" -> {
            return;
            }

        }
        }
    }

    public static void printMenu(){

        System.out.print("""
                Elpriser
                ========
                1. Inmatning
                2. Min, Max och Medel
                3. Sortera
                4. Bästa Laddningstid (4h)
                5. Visualisering
                e. Avsluta
                """);
    }

    public static int[] setElectricityPrice(Scanner sc){

        int[] localPrice = new int[24];
        for (int i = 0; i < localPrice.length; i++) {
            if (i < 9) {
                System.out.print("0" + i + "-" + "0" + (i + 1));
            } else if (i == 9) {
                System.out.print( "0" + i + "-" + (i + 1));
            } else {
                System.out.print(i + "-" + (i + 1));
            }
            localPrice[i] = Integer.parseInt(sc.nextLine());
        }
        return localPrice;

    }

    public static String[] createTimespans(){

        String[] localTimeRanges = new String[24];
        for (int i = 0; i < 24; i++) {
            if (i < 9) {
                localTimeRanges[i] = "0" + i + "-0" + (i + 1);
            } else if (i == 9) {
                localTimeRanges[i] = "0" + i + "-" + (i + 1);
            } else {
                localTimeRanges[i] = i + "-" + (i + 1);
            }
        }
        return localTimeRanges;
    }

    public static String calculatingMinMaxAverage(int[] localPrice, String[] localTimeRanges) {

        int min = localPrice[0];
        int minIndex = 0;
        for (int i = 0; i < localPrice.length; i++) {
            if (localPrice[i] < min) {
                min = localPrice[i];
                minIndex = i;
            }
        }
        int maxIndex = 0;
        int max = localPrice[0];
        for (int i = 0; i < localPrice.length; i++) {
            if (localPrice[i] > max) {
                max = localPrice[i];
                maxIndex = i;
            }
        }
        float sum = 0;
        for (int i = 0; i < localPrice.length; i++) {
            sum =  sum + localPrice[i];
        }
        float average = sum/localPrice.length;
        String medelpris = String.format("%.2f", average).replace('.', ',');
        return ("Lägsta pris: " + localTimeRanges[minIndex] + ", " + min + " öre/kWh\nHögsta pris: " + localTimeRanges[maxIndex] + ", " + max + " öre/kWh" + "\nMedelpris: " + medelpris + " öre/kWh\n");
    }

    public static void sortArray(int[] localPrice, String[] localTimeRange ){

        int[] copyTimpris = new int[localPrice.length];
        for (int i = 0; i < localPrice.length; i++) {
            copyTimpris[i] = localPrice[i];
            }

        String[] copyTimmar = Arrays.copyOf(localTimeRange, localTimeRange.length);

        List<TimeRangeAndHourPrice> timprisList = new ArrayList<>();
        for (int i = 0; i < copyTimpris.length; i++) {
            timprisList.add(new TimeRangeAndHourPrice(copyTimpris[i], copyTimmar[i]));
    }
        timprisList.sort(Comparator.comparingInt(TimeRangeAndHourPrice::getTimpris).reversed());

        for (TimeRangeAndHourPrice entry : timprisList) {
            System.out.print("\n" + entry + " öre"); }
    }

    public static String findLowestOfFour(int[] localPrice) {
        int minSum = Integer.MAX_VALUE; //minsta sekvensen, denna variabeln ska talen jämföras med sen
        int[] lowestSequence = new int[4]; //här ska de fyra lägsta sammanhängande in
        int minIndex = 0;

        //söker igenom alla möjliga startpunkter för sekvenser av 4
        for (int i = 0; i <= localPrice.length - 4; i++) {
            //samlar sekvensen av 4 nummer och lägger det i currentSum
            int currentSum = 0;
            for (int j = 0; j < 4; j++) {
                currentSum += localPrice[i + j];
            }
            //uppdaterar den lägsta sekvensen om currentSum är lägre
            if (currentSum < minSum) {
                minSum = currentSum;
                minIndex = i;
            }
        }
        float average = (float) minSum / 4;
        String medelpris = String.format("%.1f", average).replace('.', ',');

        return ("Påbörja laddning klockan " + minIndex + "\nMedelpris 4h: " + medelpris + " öre/kWh\n");
    }

    public static int findMaxValue(int[] localPrice){

        int max = localPrice[0];
        for (int pris : localPrice) {
            if (max < pris) {
                max = pris;
            }
        }
        return max;
    }

    public static int findMinValue(int[] localPrice){

        int min = localPrice[0];
        for (int pris : localPrice) {
            if (min > pris) {
                min = pris;
            }
        }
        return min;
    }

    public static void visualise(int[] localPrice) {

        int hours = localPrice.length;
        int maxPris = findMaxValue(localPrice);
        int minPris = findMinValue(localPrice);
        int graphHeight = 5;

        //anpassa y-axeln (prisnivån) från högsta till lägsta
        for (int i = graphHeight; i >= 0; i--) {
            int priceLevel = minPris + (maxPris - minPris) * i / graphHeight;
            //Skriv ut prisnivån på y-axeln
            if (i == graphHeight || i == 0) {
                System.out.printf("%3d| ", priceLevel);
            } else {
                System.out.print("   | ");
            }

            //Skriv ut grafen för varje timme (x-axeln)
            for (int j = 0; j < hours; j++) {
                // Om priset för denna timme är högre än eller lika med den aktuella prisnivån. Här bestäms var x ska placeras.
                if (localPrice[j] >= priceLevel) {
                    if(j == hours - 1){
                        System.out.print(" x");
                    }else {
                        System.out.print(" x ");
                    }
                }
                else {
                    if(j == hours - 1){
                        System.out.print("  ");
                    }else {
                        System.out.print("   ");
                    }
                }
            }
            System.out.print("\n");

        }
        // Skriv ut x-axeln (timspannet)
        System.out.print("   |------------------------------------------------------------------------\n");
        System.out.print("   | 00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23\n");

    }
}
