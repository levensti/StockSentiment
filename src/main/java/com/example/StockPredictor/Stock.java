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
    private double latestPrice;
    private String industry;
    private String companyDescription;

    // API access details for IEX Real-Time Stock Price API
    private String iexAPIKey = "pk_9b9e15de011c4e97872f2aab206d63df";
    private String iexRootURL = "https://cloud.iexapis.com/stable/stock/";
    private String iexTemplateEnding = "?token=";

    public static void main(String[] args) throws IOException {
        Stock tesla = new Stock("tsla");
        System.out.println(tesla.getCompanyDescription());
    }

    // Constructors
    public Stock() {}

    private Stock(String stockTicker) {
        this.stockTicker = stockTicker;
    }

    private Stock(String companyName, String stockTicker, double latestPrice, String industry, String companyDescription) throws IOException {
        this.companyName = companyName;
        this.stockTicker = stockTicker;
        this.latestPrice = latestPrice;
        this.industry = industry;
        this.companyDescription = companyDescription;
    }

    // Getters and setters
    public String getCompanyName() throws IOException {
        return getPriceJson(stockTicker).get("companyName").getAsString();
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStockTicker() {
        return this.stockTicker;
    }

    public void setStockTicker(String stockTicker) {
        this.stockTicker = stockTicker;
    }

    public double latestPrice(String stockTicker) throws IOException {
        return getPriceJson(stockTicker).get("latestPrice").getAsDouble();
    }

    public String getIndustry() throws IOException {
        return getMetaJson(stockTicker).get("industry").getAsString();
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCompanyDescription() throws IOException {
        return getMetaJson(stockTicker).get("description").getAsString();
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    // Pricing endpoint
    public JsonObject getPriceJson(String stockTicker) throws IOException {
        try {
            URL url = new URL(iexRootURL + stockTicker + "/quote" + iexTemplateEnding + iexAPIKey);
            return getJsonHelper(url);
        } catch(IOException ioException) {
            throw new IOException("IO Exception in Service", ioException);
        }
    }

    // Company background data endpoint
    public JsonObject getMetaJson(String stockTicker) throws IOException {
        try {
            URL url = new URL(iexRootURL + stockTicker + "/company" + iexTemplateEnding + iexAPIKey);
            return getJsonHelper(url);
        } catch(IOException ioException) {
            throw new IOException("IO Exception in Service", ioException);
        }
    }

    // Helping function to make it easy to parse Json from an endpoint
    public JsonObject getJsonHelper(URL url) throws IOException {
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

            return jsonObject;
        } catch(IOException ioException) {
            throw new IOException("IO Exception in Service", ioException);
        }
    }
}