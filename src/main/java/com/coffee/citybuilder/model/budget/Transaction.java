package com.coffee.citybuilder.model.budget;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class to keep track of a transaction.
 * 
 * Transaction stores data about the cause of the transaction,
 * the amount and the time the tranaction took place.
 */
public class Transaction {
    private String cause;
    private int amount;
    private String datetime;

    public Transaction(String cause, int amount, String datetime) {
        this.cause = cause;
        this.amount = amount;
        this.datetime = datetime;
    }

    public Transaction(String cause, int amount) {
        this(cause, amount, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
    }

    /**
     * Gets transaction datetime.
     * 
     * @return String value of the datetime
     */
    public String getDatetime() {
        return datetime;
    }

    /**
     * Gets the amount that was recorded in this transaction.
     * 
     * @return Int value
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Gets the reason for the transaction.
     * 
     * @return string which means the reason
     */
    public String getReason() {
        return cause;
    }
}
