package com.gwt.server;

import com.gwt.client.DelistedException;
import com.gwt.client.StockPrice;
import com.gwt.client.StockPriceService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.Random;

/**
 * Created by Brian on 16/12/2014.
 */
public class StockPriceServiceImpl extends RemoteServiceServlet implements StockPriceService {
  private static final double MAX_PRICE = 100.0; // $100.00
  private static final double MAX_PRICE_CHANGE = 0.02; // +/- 2%

  public StockPrice[] getPrices(String[] symbols) throws DelistedException {
    Random rnd = new Random();

    StockPrice[] prices = new StockPrice[symbols.length];
    for (int i=0; i<symbols.length; i++) {
      if (symbols[i].equals("ERR")) {
        throw new DelistedException("ERR");
      }

      double price = rnd.nextDouble() * MAX_PRICE;
      double change = price * MAX_PRICE_CHANGE * (rnd.nextDouble() * 2f - 1f);

      prices[i] = new StockPrice(symbols[i], price, change);
    }

    return prices;
  }
}
