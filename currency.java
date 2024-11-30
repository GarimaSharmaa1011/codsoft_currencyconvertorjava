import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class CurrencyConverter {

    
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        
        System.out.println("Enter the base currency (e.g., USD, EUR, INR): ");
        String baseCurrency = scanner.nextLine().toUpperCase();
        
        System.out.println("Enter the target currency (e.g., USD, EUR, INR): ");
        String targetCurrency = scanner.nextLine().toUpperCase();
        
        System.out.println("Enter the amount to convert: ");
        double amount = scanner.nextDouble();

     
        double conversionRate = getConversionRate(baseCurrency, targetCurrency);
        if (conversionRate == -1) {
            System.out.println("Unable to fetch exchange rates.");
        } else {
            double convertedAmount = amount * conversionRate;
            System.out.printf("%.2f %s = %.2f %s\n", amount, baseCurrency, convertedAmount, targetCurrency);
        }

        scanner.close();
    }

   
    public static double getConversionRate(String baseCurrency, String targetCurrency) {
        try {
            URL url = new URL(API_URL + baseCurrency);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject rates = jsonResponse.getJSONObject("conversion_rates");

            if (rates.has(targetCurrency)) {
                return rates.getDouble(targetCurrency);
            } else {
                System.out.println("Currency not supported.");
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
