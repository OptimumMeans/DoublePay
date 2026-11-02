import java.util.Scanner;

public class DoublePay {
    // Federal tax rate
    private static final double FEDERAL_TAX_RATE = 0.0511;
    // Minnesota state tax rate
    private static final double MINNESOTA_STATE_TAX_RATE = 0.0358;
    // Social Security tax rate
    private static final double SOCIAL_SECURITY_TAX_RATE = 0.0622;
    // Medicare tax rate
    private static final double MEDICARE_TAX_RATE = 0.0145;
    // Overtime threshold
    private static final double OVERTIME_THRESHOLD = 40.0;
    // Overtime multiplier
    private static final double OVERTIME_MULTIPLIER = 1.5;
    // Store the user's input data in memory
    private static double[][] hoursWorked = new double[2][7];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Do you want to enter total hours worked (H) or specify start and end times (T)? ");
        String inputMethod = scanner.nextLine();

        double totalHoursWorked;

        if (inputMethod.equalsIgnoreCase("H")) {
            System.out.print("Enter the total hours worked for the two weeks combined: ");
            totalHoursWorked = scanner.nextDouble();
        } else if (inputMethod.equalsIgnoreCase("T")) {
            inputStartEndTimes(scanner);
            totalHoursWorked = calculateTotalHours();
        } else {
            System.out.println("Invalid input method. Exiting.");
            return;
        }

        // Calculate pay summary with hourly rate
        double hourlyRate = 21.0;
        calculateAndPrintPaySummary(hourlyRate, totalHoursWorked);

        scanner.close();
    }

    private static void inputStartEndTimes(Scanner scanner) {
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        // Input start and end times for each day of the two weeks
        for (int week = 0; week < 2; week++) {
            System.out.printf("Week %d:%n", week + 1);
            for (int day = 0; day < 7; day++) {
                System.out.printf("Enter start time for %s (HH:MM) or '0' if no work: ", daysOfWeek[day]);
                String startTime = scanner.next();
                if (startTime.equals("0")) {
                    hoursWorked[week][day] = 0;
                    continue;
                }
                System.out.printf("Enter end time for %s (HH:MM): ", daysOfWeek[day]);
                String endTime = scanner.next();
                hoursWorked[week][day] = calculateHours(startTime, endTime);
            }
        }
    }

    private static double calculateTotalHours() {
        double totalHours = 0;
        for (int week = 0; week < 2; week++) {
            for (int day = 0; day < 7; day++) {
                totalHours += hoursWorked[week][day];
            }
        }
        return totalHours;
    }

    private static void calculateAndPrintPaySummary(double hourlyRate, double totalHoursWorked) {
        double totalRegularHours = Math.min(totalHoursWorked, 2 * OVERTIME_THRESHOLD);
        double totalOvertimeHours = Math.max(0, totalHoursWorked - 2 * OVERTIME_THRESHOLD);

        double regularPay = totalRegularHours * hourlyRate;
        double overtimePay = totalOvertimeHours * hourlyRate * OVERTIME_MULTIPLIER;
        double grossPay = regularPay + overtimePay;
        double federalTax = grossPay * FEDERAL_TAX_RATE;
        double socialSecurityTax = grossPay * SOCIAL_SECURITY_TAX_RATE;
        double medicareTax = grossPay * MEDICARE_TAX_RATE;
        double stateTax = grossPay * MINNESOTA_STATE_TAX_RATE;
        double totalTaxWithheld = federalTax + socialSecurityTax + medicareTax + stateTax;
        double netPay = grossPay - totalTaxWithheld;

        System.out.println("\nBiweekly Pay Summary:");
        System.out.printf("Total Hours Worked: %.2f%n", totalHoursWorked);
        System.out.printf("Regular Hours Worked: %.2f%n", totalRegularHours);
        System.out.printf("Overtime Hours Worked: %.2f%n", totalOvertimeHours);
        System.out.printf("Hourly Rate: $%.2f%n", hourlyRate);
        System.out.printf("Gross Pay: $%.2f%n", grossPay);
        System.out.printf("Federal Income Tax: $%.2f%n", federalTax);
        System.out.printf("Social Security Tax: $%.2f%n", socialSecurityTax);
        System.out.printf("Medicare Tax: $%.2f%n", medicareTax);
        System.out.printf("Minnesota State Tax: $%.2f%n", stateTax);
        System.out.printf("Total Tax Withheld: $%.2f%n", totalTaxWithheld);
        System.out.printf("Net Pay: $%.2f%n", netPay);
    }

    private static double calculateHours(String startTime, String endTime) {
        String[] startParts = startTime.split(":");
        String[] endParts = endTime.split(":");

        int startHour = Integer.parseInt(startParts[0]);
        int startMinute = Integer.parseInt(startParts[1]);
        int endHour = Integer.parseInt(endParts[0]);
        int endMinute = Integer.parseInt(endParts[1]);

        int totalMinutes = (endHour - startHour) * 60 + (endMinute - startMinute);
        return totalMinutes / 60.0;
    }
}