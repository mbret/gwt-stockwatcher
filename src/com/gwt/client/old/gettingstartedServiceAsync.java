package com.gwt.client.old;

import com.gwt.client.StockPrice;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface gettingstartedServiceAsync {

    void getMessage(String msg, AsyncCallback<String> async);

    void getPrices(String[] symbols, AsyncCallback<StockPrice[]> callback);
}
