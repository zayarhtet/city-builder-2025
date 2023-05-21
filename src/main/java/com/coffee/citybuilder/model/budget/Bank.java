package com.coffee.citybuilder.model.budget;

import java.util.ArrayList;
import java.util.List;

import com.coffee.citybuilder.resource.Constant;

import static com.coffee.citybuilder.resource.Constant.Initial_BUDGET;

/**
 * The class that saves all transactions and keeps track of the players budget.
 * 
 * The budget is stored n te class, every transaction is given to this class,
 * recorded and budget modified.
 */
public class Bank {
    private int budget;
    private List<Transaction> expenses;
    private List<Transaction> income;

    /**
     * Contructor that initializes new bank class.
     */
    public Bank() {
        this.budget = Initial_BUDGET;
        this.expenses = new ArrayList<>();
        this.income = new ArrayList<>();
    }

    /**
     * Contructor when the player has already played the game and want to contnue.
     * 
     * @param bank a bank class left from the previuos sessions
     */
    public Bank(Bank bank) {
        this.budget = bank.budget;
        this.expenses = new ArrayList<>(bank.expenses);
        this.income = new ArrayList<>(bank.income);
    }

    /**
     * Subtract and amount from the budget.
     * 
     * Generates a transaction based on parameters subtracts the required amount
     * from the budget.
     * 
     * @param reason where the amount was spents (building
     *               roads/buildings/maintenance ect.)
     * @param value  the amuont spents
     * 
     * @return always true
     */
    public boolean cost(String reason, int value) {
        Transaction trans = new Transaction(reason, value);
        budget -= value;
        expenses.add(trans);
        return true;
    }

    /**
     * Add an amount to the budget.
     * 
     * Generates a transaction based on parameters adds the required amount to the
     * budget.
     * 
     * @param reason how the amount was earned (taxes)
     * @param value  the amuont earned
     * 
     * @return always true
     */
    public boolean earn(String reason, int value) {
        Transaction trans = new Transaction(reason, value);
        budget += value;
        income.add(trans);
        return true;
    }

    /**
     * Method to get all expense transactions.
     * 
     * @return array list of expenses
     */
    public List<Transaction> getExpenses() {
        return new ArrayList<>(expenses);
    }

    /**
     * Method to get all income transactions.
     * 
     * @return array list of income transactions
     */
    public List<Transaction> getIncomes() {
        return new ArrayList<>(income);
    }

    /**
     * Method to get the budget.
     * 
     * @return the current budget amount
     */
    public int getBudget() {
        return budget;
    }
}
