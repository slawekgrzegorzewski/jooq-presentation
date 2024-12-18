package pl.sg.jooq_presentation.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions_from_user")
public class TransactionFromUser {
    @Id
    Long id;
    @ManyToOne
    Account sourceAccount;
    @ManyToOne
    Account destinationAccount;
    BigDecimal amount;
    String description;

    public Long getId() {
        return id;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
