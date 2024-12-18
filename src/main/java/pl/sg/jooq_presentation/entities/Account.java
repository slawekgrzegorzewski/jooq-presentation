package pl.sg.jooq_presentation.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    Long id;
    String name;
    BigDecimal balance;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sourceAccount")
    List<TransactionFromOpenBankingApi> apiDebitTransactions;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "destinationAccount")
    List<TransactionFromOpenBankingApi> apiCreditTransactions;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sourceAccount")
    List<TransactionFromUser> userDebitTransactions;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "destinationAccount")
    List<TransactionFromUser> userCreditTransactions;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<TransactionFromOpenBankingApi> getApiDebitTransactions() {
        return apiDebitTransactions;
    }

    public List<TransactionFromOpenBankingApi> getApiCreditTransactions() {
        return apiCreditTransactions;
    }

    public List<TransactionFromUser> getUserDebitTransactions() {
        return userDebitTransactions;
    }

    public List<TransactionFromUser> getUserCreditTransactions() {
        return userCreditTransactions;
    }
}
