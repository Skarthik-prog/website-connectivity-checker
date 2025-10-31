import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WebsiteConnectivityChecker {

    // Method to check website connectivity
    public static Object[] checkWebsite(String url, int timeout) {
        // Add protocol if not present
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        long startTime = System.currentTimeMillis();
        try {
            // Create the URL object
            URL websiteURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) websiteURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(timeout * 1000);  // Convert timeout to milliseconds
            connection.setReadTimeout(timeout * 1000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

            int statusCode = connection.getResponseCode();
            long endTime = System.currentTimeMillis();
            double responseTime = (endTime - startTime) / 1000.0; // Response time in seconds

            return new Object[]{true, "✓ Connected successfully (Status: " + statusCode + ")", responseTime};
        } catch (SocketTimeoutException e) {
            return new Object[]{false, "✗ Connection timeout after " + timeout + " seconds", null};
        } catch (IOException e) {
            return new Object[]{false, "✗ Error: " + e.getMessage(), null};
        }
    }

    public static void main(String[] args) {
        System.out.println("=" + "=".repeat(58));
        System.out.println("         WEBSITE CONNECTIVITY CHECKER");
        System.out.println("=" + "=".repeat(58));
        System.out.println();

        // List of websites to check
        List<String> websites = List.of("google.com", "github.com", "python.org", "example.com");

        System.out.println("Checking websites...\n");

        List<Result> results = new ArrayList<>();

        for (String website : websites) {
            System.out.print("Checking: " + website + "... ");
            Object[] result = checkWebsite(website, 5); // Default timeout 5 seconds

            boolean success = (boolean) result[0];
            String message = (String) result[1];
            Double responseTime = (Double) result[2];

            if (responseTime != null) {
                System.out.println(message + " - Response time: " + String.format("%.2f", responseTime) + "s");
            } else {
                System.out.println(message);
            }

            results.add(new Result(website, success, message, responseTime));
        }

        // Summary
        System.out.println("\n" + "=" + "=".repeat(58));
        System.out.println("SUMMARY");
        System.out.println("=" + "=".repeat(58));

        long successful = results.stream().filter(r -> r.success).count();
        long failed = results.size() - successful;

        System.out.println("Total websites checked: " + results.size());
        System.out.println("Successful connections: " + successful);
        System.out.println("Failed connections: " + failed);

        // Option to check custom website
        System.out.println("\n" + "=" + "=".repeat(58));

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("\nEnter a website to check (or 'quit' to exit): ");
                String customUrl = scanner.nextLine().trim();

                if (customUrl.equalsIgnoreCase("quit") || customUrl.equalsIgnoreCase("exit") || customUrl.equalsIgnoreCase("q") || customUrl.isEmpty()) {
                    System.out.println("\nThank you for using Website Connectivity Checker!");
                    break;
                }

                Object[] customResult = checkWebsite(customUrl, 5);

                boolean successCustom = (boolean) customResult[0];
                String messageCustom = (String) customResult[1];
                Double responseTimeCustom = (Double) customResult[2];

                if (responseTimeCustom != null) {
                    System.out.println(messageCustom + " - Response time: " + String.format("%.2f", responseTimeCustom) + "s");
                } else {
                    System.out.println(messageCustom);
                }}
        }
    }

    // Result class to store website check results
    static class Result {
        String website;
        boolean success;
        String message;
        Double responseTime;

        public Result(String website, boolean success, String message, Double responseTime) {
            this.website = website;
            this.success = success;
            this.message = message;
            this.responseTime = responseTime;
        }}}
