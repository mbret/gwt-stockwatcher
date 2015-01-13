package com.gwt.client;

import com.gwt.client.ui.InputDialogBox;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class StockWatcher implements EntryPoint {

  //private Grid grid = new Grid(2,2);
  public static final String JSON_URL = GWT.getModuleBaseURL() + "stockPrices?q=";
  private static final int REFRESH_INTERVAL = 5000; // ms
  private StockWatcherUI stockWatcherUI;
  public ArrayList<String> stocks = new ArrayList<String>();
  public StockPriceServiceAsync stockPriceSvc = GWT.create(StockPriceService.class);

  public ArrayList<StockPrice> stockPricePurchased = new ArrayList<StockPrice>();

  /**
   * Entry point method.
   */
  public void onModuleLoad() {

    // @todo get wallet (async)

    stockWatcherUI = new StockWatcherUI();
    stockWatcherUI.loadStocksList();
    stockWatcherUI.loadActionTable();

    // Setup timer to refresh list automatically.
    Timer refreshTimer = new Timer() {
      @Override
      public void run() {
        refreshWatchList();
      }
    };
    refreshTimer.scheduleRepeating(REFRESH_INTERVAL);

    // Listen for mouse events on the Add button.
    stockWatcherUI.addStockButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        addStock();
      }
    });

    // Listen for keyboard events in the input box.
    stockWatcherUI.newProductTextBox.addKeyPressHandler(new KeyPressHandler() {
      public void onKeyPress(KeyPressEvent event) {
        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
          addStock();
        }
      }
    });
  }


  /**
   * Add stock to FlexTable. Executed when the user clicks the addStockButton or
   * presses enter in the newSymbolTextBox.
   */
  private void addStock() {
    final String symbol = stockWatcherUI.newProductTextBox.getText().toUpperCase().trim();
    stockWatcherUI.newProductTextBox.setFocus(true);

    // Stock code must be between 1 and 10 chars that are numbers, letters, or dots.
    if (!symbol.matches("^[0-9a-zA-Z\\.]{1,10}$")) {
      Window.alert("'" + symbol + "' is not a valid symbol.");
      stockWatcherUI.newProductTextBox.selectAll();
      return;
    }

    stockWatcherUI.newProductTextBox.setText("");

    // Don't add the stock if it's already in the table.
    if (stocks.contains(symbol))
      return;

    // Add the action to the table.
    int row = stockWatcherUI.actionsFlexTable.getRowCount();
    stocks.add(symbol);
    stockWatcherUI.actionsFlexTable.setText(row, 0, symbol); // Name
    stockWatcherUI.actionsFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");

    stockWatcherUI.actionsFlexTable.setText(row, 1, "15"); // price
    stockWatcherUI.actionsFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");

    stockWatcherUI.actionsFlexTable.setText(row, 2, "A change"); // change
    stockWatcherUI.actionsFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");

    // Purchase button
    Button purchaseActionButton = new Button("Purchase");
    purchaseActionButton.addStyleDependentName("remove");
    purchaseActionButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        purchaseAction( stocks.indexOf(symbol) );
      }
    });
    stockWatcherUI.actionsFlexTable.setWidget(row, 3, purchaseActionButton);

    // Remove button
    Button removeStockButton = new Button("x");
    removeStockButton.addStyleDependentName("remove");
    removeStockButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        int removedIndex = stocks.indexOf(symbol);
        stocks.remove(removedIndex);
        stockWatcherUI.actionsFlexTable.removeRow(removedIndex + 1);
      }
    });
    stockWatcherUI.actionsFlexTable.setWidget(row, 4, removeStockButton);

    // Get the stock price.
    refreshWatchList();

  }

    /**
     * Purchase an action, add an action to the wallet
     * @param actionIndex
     */
    private void purchaseAction( int actionIndex ){
        // Show dialog
        InputDialogBox mDBox = new InputDialogBox( this, actionIndex );
        mDBox.center();
        mDBox.show();
        // Call purchaseActionCallback from dialog box
    }

    /**
     * continue the purchasing after the input dialog box
     * @param actionIndex
     * @param quantity
     */
    public void purchaseActionCallback( int actionIndex, int quantity ){
        int row = stockWatcherUI.walletActionFlexTable.getRowCount();
        final StockPrice newStockPrice = new StockPrice( stocks.get(actionIndex), new Double(1), new Double(10));
        this.stockPricePurchased.add( newStockPrice );

        // Name
        stockWatcherUI.walletActionFlexTable.setText(row, 0, newStockPrice.getSymbol() );
        stockWatcherUI.walletActionFlexTable.getCellFormatter().addStyleName(row, 0, "watchListNumericColumn");

        // price
        stockWatcherUI.walletActionFlexTable.setText(row, 1, String.valueOf(newStockPrice.getPrice()) );
        stockWatcherUI.walletActionFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");

        // Change
        stockWatcherUI.walletActionFlexTable.setText(row, 2, String.valueOf(newStockPrice.getChange()) );
        stockWatcherUI.walletActionFlexTable.getCellFormatter().addStyleName(row, 2, "watchListRemoveColumn");

        // Quantity
        stockWatcherUI.walletActionFlexTable.setText(row, 3, String.valueOf(quantity) );
        stockWatcherUI.walletActionFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");

        // Remove
        Button removeStockPriceButton = new Button("x");
        removeStockPriceButton.addStyleDependentName("remove");
        removeStockPriceButton.addClickHandler(new ClickHandler() {
          public void onClick(ClickEvent event) {
            removePurchasedAction(newStockPrice);
          }
        });
        stockWatcherUI.walletActionFlexTable.setWidget(row, 4, removeStockPriceButton);
    }

    private void removePurchasedAction( StockPrice stockPrice ){
        int removedIndex = stockPricePurchased.indexOf( stockPrice );
        stockPricePurchased.remove(removedIndex );
        stockWatcherUI.walletActionFlexTable.removeRow(removedIndex + 1);
        return;
    }

  /**
   * Generate random stock prices.
   */
  private void refreshWatchList() {
    if (stocks.size() == 0) {  return;  }

    String url = JSON_URL;

    // Append watch list stock symbols to query URL.
    Iterator<String> iter = stocks.iterator();
    while (iter.hasNext()) {
      url += iter.next();
      if (iter.hasNext()) {
        url += "+";
      }
    }

    url = URL.encode(url);

    // Send request to server and catch any errors.
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

    try {
      Request request = builder.sendRequest(null, new RequestCallback() {
        public void onError(Request request, Throwable exception) {
          displayError("Couldn't retrieve JSON");
        }

        public void onResponseReceived(Request request, Response response) {
          if (200 == response.getStatusCode()) {
            updateTable(JsonUtils.<JsArray<StockData>>safeEval(response.getText()));
          } else {
            displayError("Couldn't retrieve JSON (" + response.getStatusText()
                    + ")");
          }
        }
      });
    } catch (RequestException e) {
      displayError("Couldn't retrieve JSON");
    }
  }

  /**
   * Update the Price and Change fields all the rows in the stock table.
   *
   * @param prices Stock data for all rows.
   */
  private void updateTable(JsArray<StockData> prices) {
    for (int i = 0; i < prices.length(); i++) {
      updateTable(prices.get(i));
    }

    // Display timestamp showing last refresh.
    stockWatcherUI.lastUpdatedLabel.setText("Last update : "
            + DateTimeFormat.getMediumDateTimeFormat().format(new Date()));

    // Clear any errors.
    stockWatcherUI.errorMsgLabel.setVisible(false);
  }

  /**
   * Update a single row in the stock table.
   *
   * @param price Stock data for a single row.
   */
  private void updateTable(StockData price) {
    // Make sure the stock is still in the stock table.
    if (!stocks.contains(price.getSymbol())) {
      return;
    }

    int row = stocks.indexOf(price.getSymbol()) + 1;

    // Format the data in the Price and Change fields.
    String priceText = NumberFormat.getFormat("#,##0.00").format(
            price.getPrice());
    NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
    String changeText = changeFormat.format(price.getChange());
    String changePercentText = changeFormat.format(price.getChangePercent());

    // Populate the Price and Change fields with new data.
    stockWatcherUI.walletActionFlexTable.setText(row, 1, priceText);
    Label changeWidget = (Label)stockWatcherUI.walletActionFlexTable.getWidget(row, 2);
    changeWidget.setText(changeText + " (" + changePercentText + "%)");

    // Change the color of text in the Change field based on its value.
    String changeStyleName = "noChange";
    if (price.getChangePercent() < -0.1f) {
      changeStyleName = "negativeChange";
    }
    else if (price.getChangePercent() > 0.1f) {
      changeStyleName = "positiveChange";
    }

    changeWidget.setStyleName(changeStyleName);
  }

  /**  * If can't get JSON, display error message.  *
   * @param error  */
  private void displayError(String error) {
    stockWatcherUI.errorMsgLabel.setText("Error: " + error);
    stockWatcherUI.errorMsgLabel.setVisible(true);
  }
}
