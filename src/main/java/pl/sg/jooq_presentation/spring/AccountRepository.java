package pl.sg.jooq_presentation.spring;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.jooq_presentation.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
