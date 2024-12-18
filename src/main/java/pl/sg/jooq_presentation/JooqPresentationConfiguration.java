package pl.sg.jooq_presentation;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JooqPresentationConfiguration {

    private final DataSource dataSource;

    public JooqPresentationConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public DSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }

    public org.jooq.Configuration configuration() {
        return new DefaultConfiguration().set(dataSource).set(SQLDialect.H2);
    }
}