package coffee.model.budget;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    private String cause;
    private int amount;
    private String datetime;
    public Transaction (String cause, int amount, String datetime) {
        this.cause = cause;
        this.amount = amount;
        this.datetime = datetime;
    }

    public Transaction (String cause, int amount) {
        this.cause = cause;
        this.amount = amount;
        this.datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
    }
}
