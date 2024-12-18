package pl.sg.jooq_presentation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sg.jooq_presentation.entities.Account;
import pl.sg.jooq_presentation.entities.TransactionFromOpenBankingApi;
import pl.sg.jooq_presentation.entities.TransactionFromUser;
import pl.sg.jooq_presentation.spring.AccountRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AccountsStatsTests {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountsStatsComponent accountsStatsComponent;

    @Test
    void contextLoads() {
        List<AccountStats> accountStats = accountsStatsComponent.runAfterStartup();
        List<Account> accounts = accountRepository.findAll();
        List<AccountStats> accountStatsJPA = accounts.stream().map(account -> {
            BigDecimal creditFromApi = sumAmounts(account.getApiCreditTransactions());
            BigDecimal debitFromApi = sumAmounts(account.getApiDebitTransactions());
            BigDecimal creditFromUser = sumAmounts2(account.getUserCreditTransactions());
            BigDecimal debitFromUser = sumAmounts2(account.getUserDebitTransactions());
            return new AccountStats(
                    account.getId(),
                    account.getBalance(),
                    creditFromApi.subtract(debitFromApi),
                    creditFromUser.subtract(debitFromUser)
            );
        }).toList();
        assertEquals(
                accountStatsJPA,
                accountStats
        );
    }

    private static BigDecimal sumAmounts(List<TransactionFromOpenBankingApi> transactions) {
        return transactions.stream().map(TransactionFromOpenBankingApi::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal sumAmounts2(List<TransactionFromUser> transactions) {
        return transactions.stream().map(TransactionFromUser::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
