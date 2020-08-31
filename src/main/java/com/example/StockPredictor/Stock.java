package com.example.StockPredictor;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Stock {
    private String companyName;
    private String stockTicker;
    private int currentPrice;
    private String industry;
    private String companyDescription;

    private String apiKey = "pk_9b9e15de011c4e97872f2aab206d63df";
    private String rootURL = "https://cloud.iexapis.com/stable/stock/";
    private String templateEnding = "/quote?token=";

    public static void main(String[] args) throws IOException {
        Stock apple = new Stock("aapl");
        System.out.println(apple.latestPrice("aapl"));
    }
    public Stock() {}

    private Stock(String companyName, String stockTicker, int currentPrice, String industry, String companyDescription) {
        this.companyName = companyName;
        this.stockTicker = stockTicker;
        this.currentPrice = currentPrice;
        this.industry = industry;
        this.companyDescription = companyDescription;
    }

    private Stock(String stockTicker) {
        this.stockTicker = stockTicker;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStockTicker() {
        return stockTicker;
    }

    public void setStockTicker(String stockTicker) {
        this.stockTicker = stockTicker;
    }

    public double latestPrice(String stockTicker) throws IOException {

        URL url = new URL(rootURL + stockTicker + templateEnding + apiKey);

        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            String stream = "";
            Scanner sc = new Scanner(url.openStream());
            while(sc.hasNext()) {
                stream += sc.nextLine();
            }
            JsonElement jsonElement = new JsonParser().parse(stream);

            JsonObject jsonObject = jsonElement.getAsJsonObject();

            double price = jsonObject.get("latestPrice").getAsDouble();
            return price;

        } catch(IOException ioException) {
            throw new IOException("IO Exception in Service", ioException);
        }
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }
}
