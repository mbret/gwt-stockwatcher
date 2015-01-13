package com.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by Brian on 16/12/2014.
 */
public interface StockPriceServiceAsync {
  void getPrices(String[] symbols, AsyncCallback<StockPrice[]> callback);
}
