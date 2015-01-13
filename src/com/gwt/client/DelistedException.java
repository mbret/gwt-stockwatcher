package com.gwt.client;

import java.io.Serializable;

/**
 * Created by Brian on 16/12/2014.
 */
public class DelistedException extends Exception implements Serializable {
  private String symbol;

  public DelistedException() {
  }

  public DelistedException(String symbol) {
    this.symbol = symbol;
  }

  public String getSymbol() {
    return this.symbol;
  }
}
