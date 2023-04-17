package model.budget;

import resource.Constant;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private int budget;
    private List<Transaction> expenses;
    private List<Transaction> income;
    public Bank(){
        this.budget = Constant.Initial_BUDGET;
    }

    public boolean cost(String reason, int value) {
        // initiate Transaction() and add it to the expenses
        // subtract from the budget // check condition (budget minus is allowed)
        return false;
    }

    public boolean earn(String reason, int value) {
        // similar to cost();
        return false;
    }

    public List<Transaction> getExpenses() {
        return new ArrayList<>(expenses);
    }

}
