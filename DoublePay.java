import java.util.Scanner;

public class DoublePay {
    // Federal tax rate
    private static final double FEDERAL_TAX_RATE = 0.12;

    // Minnesota state tax rate
    private static final double MINNESOTA_STATE_TAX_RATE = 0.0535;

    // Overtime threshold
    private static final double OVERTIME_THRESHOLD = 40.0;

    // Overtime multiplier
    private static final double OVERTIME_MULTIPLIER = 1.5;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your hourly rate: $");
        double hourlyRate = scanner.nextDouble();

        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        double totalRegularHours = 0;
        double totalOvertimeHours = 0;

        for (int week = 1; week <= 2; week++) {
            System.out.printf("Week %d:%n", week);
            double weeklyHours = 0;

            for (String day : daysOfWeek) {
                System.out.printf("Enter start time for %s (HH:MM) or '0' if no work: ", day);
                String startTime = scanner.next();

                if (startTime.equals("0")) {
                    continue;
                }

                System.out.printf("Enter end time for %s (HH:MM): ", day);
                String endTime = scanner.next();

                double hours = calculateHours(startTime, endTime);
                weeklyHours += hours;
            }

            if (weeklyHours > OVERTIME_THRESHOLD) {
                double overtimeHours = weeklyHours - OVERTIME_THRESHOLD;
                totalRegularHours += OVERTIME_THRESHOLD;
                totalOvertimeHours += overtimeHours;
            } else {
                totalRegularHours += weeklyHours;
            }
        }

        double regularPay = totalRegularHours * hourlyRate;
        double overtimePay = totalOvertimeHours * hourlyRate * OVERTIME_MULTIPLIER;
        double grossPay = regularPay + overtimePay;
        double federalTax = grossPay * FEDERAL_TAX_RATE;
        double stateTax = grossPay * MINNESOTA_STATE_TAX_RATE;
        double netPay = grossPay - federalTax - stateTax;

        System.out.println("\nBiweekly Pay Summary:");
        System.out.printf("Regular Hours Worked: %.2f%n", totalRegularHours);
        System.out.printf("Overtime Hours Worked: %.2f%n", totalOvertimeHours);
        System.out.printf("Regular Pay: $%.2f%n", regularPay);
        System.out.printf("Overtime Pay: $%.2f%n", overtimePay);
        System.out.printf("Gross Pay: $%.2f%n", grossPay);
        System.out.printf("Federal Tax (%.2f%%): $%.2f%n", FEDERAL_TAX_RATE * 100, federalTax);
        System.out.printf("Minnesota State Tax (%.2f%%): $%.2f%n", MINNESOTA_STATE_TAX_RATE * 100, stateTax);
        System.out.printf("Net Pay: $%.2f%n", netPay);

        scanner.close();
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