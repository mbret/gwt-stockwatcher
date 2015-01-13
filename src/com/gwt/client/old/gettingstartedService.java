package com.gwt.client.old;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("gettingstartedService")
public interface gettingstartedService extends RemoteService {
    // Sample interface method of remote interface
    String getMessage(String msg);

//    StockPrice[] getPrices(String[] symbols);

    /**
     * Utility/Convenience class.
     * Use gettingstartedService.App.getInstance() to access static instance of gettingstartedServiceAsync
     */
    public static class App {
        private static gettingstartedServiceAsync ourInstance = GWT.create(gettingstartedService.class);

        public static synchronized gettingstartedServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
