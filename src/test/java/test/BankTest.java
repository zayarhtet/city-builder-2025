package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Target;
import java.util.List;

import org.junit.Test;

import com.coffee.citybuilder.model.budget.Bank;
import com.coffee.citybuilder.model.budget.Transaction;

// earn
// cost
// get expenses
public class BankTest {

    private Bank getNewBank() {
        return new Bank();
    }

    @Test
    public void test_Earn() {
        Bank bank = getNewBank();
        bank.earn("test", 100);
        assertEquals(bank.getBudget(), 460);
    }

    @Test
    public void test_Cost() {
        Bank bank = getNewBank();
        bank.cost("test", 100);
        assertEquals(bank.getBudget(), 260);
    }
    // TODO: since transaction does not return string
    // @Test
    // public void test_GetExpenses() {
    // List<Transaction> transactionList = bank.getExpenses();
    // for (Transaction transaction : transactionList) {
    // if(Transaction.class)
    // }
    // }
}
