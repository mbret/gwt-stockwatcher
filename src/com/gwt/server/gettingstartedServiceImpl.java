package com.gwt.server;

import com.gwt.client.StockPrice;
import com.gwt.client.old.gettingstartedService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.Random;

public class gettingstartedServiceImpl extends RemoteServiceServlet implements gettingstartedService {

    private static final double MAX_PRICE = 100.0; // $100.00
    private static final double MAX_PRICE_CHANGE = 0.02; // +/- 2%

    // Implementation of sample interface method
    public String getMessage(String msg) {
        return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
    }

    public StockPrice[] getPrices(String[] symbols) {
        Random rnd = new Random();

        StockPrice[] prices = new StockPrice[symbols.length];
        for (int i=0; i<symbols.length; i++) {
            double price = rnd.nextDouble() * MAX_PRICE;
            double change = price * MAX_PRICE_CHANGE * (rnd.nextDouble() * 2f - 1f);

            prices[i] = new StockPrice(symbols[i], price, change);
        }

        return prices;
    }
}