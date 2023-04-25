package com.coffee.citybuilder.model.budget;

import java.util.ArrayList;
import java.util.List;

import com.coffee.citybuilder.resource.Constant;

import static com.coffee.citybuilder.resource.Constant.Initial_BUDGET;

public class Bank {
    private int budget;
    private List<Transaction> expenses;
    private List<Transaction> income;
    public Bank(){
        this.budget = Initial_BUDGET;
        this.expenses = new ArrayList<>();
        this.income = new ArrayList<>();
    }

    public boolean cost(String reason, int value) {
        Transaction trans = new Transaction(reason, value);
        budget -= value;
        expenses.add(trans);
        return true;
    }

    public boolean earn(String reason, int value) {
        Transaction trans = new Transaction(reason, value);
        budget += value;
        income.add(trans);
        return true;
    }

    public List<Transaction> getExpenses() {
        return new ArrayList<>(expenses);
    }
    public int getBudget() { return budget; }
}
