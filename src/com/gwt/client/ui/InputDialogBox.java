package com.gwt.client.ui;

import com.gwt.client.StockWatcher;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class InputDialogBox extends DialogBox {
    private TextBox textBox = new TextBox();
    private Button okButton = new Button("Ok");

    public InputDialogBox(final StockWatcher stockWatcher, final int stockIndex) {
        super();
        // Enable glass background.
        setGlassEnabled(true);
        setText("Input dialog box");
        okButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
                stockWatcher.purchaseActionCallback(stockIndex, Integer.parseInt(textBox.getText()) );
            }
        });
        VerticalPanel vPanel = new VerticalPanel();
        vPanel.add(textBox);
        vPanel.add(okButton);
        setWidget(vPanel);
    }
}