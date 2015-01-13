package com.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.*;

/**
 * Created by Maxime on 13/01/2015.
 */
public class StockWatcherUI {


  // Actions view
  public VerticalPanel actionsVerticalPanel = new VerticalPanel();
  public FlexTable actionsFlexTable = new FlexTable();
  public HorizontalPanel addActionPanel = new HorizontalPanel();
  public TextBox newProductTextBox = new TextBox();
  public Button addStockButton = new Button("Add a product");
  public Label lastUpdatedLabel = new Label();
  public Label errorMsgLabel = new Label();

  // Wallet view
  public FlexTable walletActionFlexTable = new FlexTable();
  public VerticalPanel verticalPurchasePanel = new VerticalPanel();
  public Button searchActionButton = new Button("Search");
  public Button purchaseButton = new Button("Purchase");
  public TextBox searchedSymbolTextBox = new TextBox();
  public HorizontalPanel searchPanel = new HorizontalPanel();

  public void loadStocksList(){
    // Create table for stock data.
    walletActionFlexTable.setText(0, 0, "Symbol");
    walletActionFlexTable.setText(0, 1, "Price");
    walletActionFlexTable.setText(0, 2, "Change");
    walletActionFlexTable.setText(0, 3, "Remove");

    // Add styles to elements in the stock list table.
    walletActionFlexTable.setCellPadding(6);
    walletActionFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
    walletActionFlexTable.addStyleName("watchList");
    walletActionFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
    walletActionFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
    walletActionFlexTable.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");

    // Assemble Add Stock panel.
    searchPanel.add(searchedSymbolTextBox);
    searchPanel.add(searchActionButton);
    searchPanel.addStyleName("addPanel");

    //verticalPurchasePanel.add(errorMsgLabel);
    verticalPurchasePanel.add(walletActionFlexTable);
    verticalPurchasePanel.add(searchPanel);

    // Associate the Main panel with the HTML host page.
    RootPanel.get("stockList").add(verticalPurchasePanel);

    // Move cursor focus to the input box.
    searchedSymbolTextBox.setFocus(true);

    // Listen for mouse events on the Add button.
    searchActionButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        //addStock();
      }
    });

    // Listen for keyboard events in the input box.
    searchedSymbolTextBox.addKeyPressHandler(new KeyPressHandler() {
      public void onKeyPress(KeyPressEvent event) {
                /*if (event.getCharCode() == KeyCodes.KEY_ENTER) {
                    addStock();
                }*/
      }
    });

  }

  /**
   * Load the UI relative to stock list
   * - Table that display list of available product
   * - Field and button to add a product
   */
  public void loadActionTable(){
    // Table
    actionsFlexTable.setText(0, 0, "Name");
    actionsFlexTable.setText(0, 1, "Price");
    actionsFlexTable.setText(0, 2, "Change");
    actionsFlexTable.setText(0, 3, "Purchase");
    actionsFlexTable.setText(0, 4, "Remove");
    actionsFlexTable.setCellPadding(6);
    actionsFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
    actionsFlexTable.addStyleName("watchList");
    actionsFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
    actionsFlexTable.getCellFormatter().addStyleName(0, 2, "watchListRemoveColumn");

    // Field + button to add product
    addActionPanel.add(newProductTextBox);
    addActionPanel.add(addStockButton);
    addActionPanel.addStyleName("addPanel");

    // Assemble Main panel.
    errorMsgLabel.setStyleName("errorMessage");
    errorMsgLabel.setVisible(false);

    // Main panel
    actionsVerticalPanel.add(errorMsgLabel);
    actionsVerticalPanel.add(actionsFlexTable);
    actionsVerticalPanel.add(addActionPanel);
    actionsVerticalPanel.add(lastUpdatedLabel);

    // Associate the Main panel with the HTML host page.
    RootPanel.get("actionInfos").add(actionsVerticalPanel);

    // Move cursor focus to the input box.
    newProductTextBox.setFocus(true);

  }

}
