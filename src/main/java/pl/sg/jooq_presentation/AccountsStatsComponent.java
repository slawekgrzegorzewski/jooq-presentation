package pl.sg.jooq_presentation;

import org.jooq.CommonTableExpression;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static pl.sg.jooq.Tables.TRANSACTIONS_FROM_USER;
import static pl.sg.jooq.tables.Accounts.ACCOUNTS;
import static pl.sg.jooq.tables.TransactionsFromOpenBankingApi.TRANSACTIONS_FROM_OPEN_BANKING_API;

@Component
public class AccountsStatsComponent {

    private final DSLContext dslContext;

    public AccountsStatsComponent(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public List<AccountStats> runAfterStartup() {
        CommonTableExpression<Record2<Long, BigDecimal>> creditsFromApi =
                DSL.name("creditsFromApi")
                        .fields("id", "amount")
                        .as(dslContext
                                .select(TRANSACTIONS_FROM_OPEN_BANKING_API.DESTINATION_ACCOUNT_ID.as("id"),
                                        DSL.sum(TRANSACTIONS_FROM_OPEN_BANKING_API.AMOUNT).as("amount"))
                                .from(TRANSACTIONS_FROM_OPEN_BANKING_API)
                                .groupBy(TRANSACTIONS_FROM_OPEN_BANKING_API.DESTINATION_ACCOUNT_ID));
        CommonTableExpression<Record2<Long, BigDecimal>> debitsFromApi =
                DSL.name("debitsFromApi")
                        .fields("id", "amount")
                        .as(dslContext
                                .select(TRANSACTIONS_FROM_OPEN_BANKING_API.SOURCE_ACCOUNT_ID.as("id"),
                                        DSL.sum(TRANSACTIONS_FROM_OPEN_BANKING_API.AMOUNT).as("amount"))
                                .from(TRANSACTIONS_FROM_OPEN_BANKING_API)
                                .groupBy(TRANSACTIONS_FROM_OPEN_BANKING_API.SOURCE_ACCOUNT_ID));
        CommonTableExpression<Record2<Long, BigDecimal>> creditsFromUser =
                DSL.name("creditsFromUser")
                        .fields("id", "amount")
                        .as(dslContext
                                .select(TRANSACTIONS_FROM_USER.DESTINATION_ACCOUNT_ID.as("id"),
                                        DSL.sum(TRANSACTIONS_FROM_USER.AMOUNT).as("amount"))
                                .from(TRANSACTIONS_FROM_USER)
                                .groupBy(TRANSACTIONS_FROM_USER.DESTINATION_ACCOUNT_ID));
        CommonTableExpression<Record2<Long, BigDecimal>> debitsFromUser =
                DSL.name("debitsFromUser")
                        .fields("id", "amount")
                        .as(dslContext
                                .select(TRANSACTIONS_FROM_USER.SOURCE_ACCOUNT_ID.as("id"),
                                        DSL.sum(TRANSACTIONS_FROM_USER.AMOUNT).as("amount"))
                                .from(TRANSACTIONS_FROM_USER)
                                .groupBy(TRANSACTIONS_FROM_USER.SOURCE_ACCOUNT_ID));
        //@formatter:off
        return dslContext.with(creditsFromApi)
                .with(debitsFromApi)
                .with(creditsFromUser)
                .with(debitsFromUser)
                .select(ACCOUNTS.ID,
                        ACCOUNTS.BALANCE,
                        DSL.sum(creditsFromApi.field(creditsFromApi.field(DSL.name("amount"), BigDecimal.class)))
                                .minus(DSL.sum(debitsFromApi.field(debitsFromApi.field(DSL.name("amount"), BigDecimal.class))))
                                .as("balanceFromApi"),
                        DSL.sum(creditsFromUser.field(creditsFromUser.field(DSL.name("amount"), BigDecimal.class)))
                                .minus(DSL.sum(debitsFromUser.field(debitsFromUser.field(DSL.name("amount"), BigDecimal.class))))
                                .as("balanceFromUser")
                )
                .from(ACCOUNTS)
                .leftJoin(creditsFromApi)
                .on(ACCOUNTS.ID.eq(creditsFromApi.field(DSL.name("id"), Long.class)))
                .leftJoin(debitsFromApi)
                .on(ACCOUNTS.ID.eq(debitsFromApi.field(DSL.name("id"), Long.class)))
                .leftJoin(creditsFromUser)
                .on(ACCOUNTS.ID.eq(creditsFromUser.field(DSL.name("id"), Long.class)))
                .leftJoin(debitsFromUser)
                .on(ACCOUNTS.ID.eq(debitsFromUser.field(DSL.name("id"), Long.class)))
                .groupBy(ACCOUNTS.ID)
                .fetch()
                .map(record -> new AccountStats(
                        record.get(ACCOUNTS.ID),
                        record.get(ACCOUNTS.BALANCE),
                        record.get(DSL.name("balanceFromApi"), BigDecimal.class),
                        record.get(DSL.name("balanceFromUser"), BigDecimal.class)
                ));

        //@formatter:on
    }

}