package ru.cron.FailsafeCRON.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cron.FailsafeCRON.entities.ImportantData;

@Repository
public interface ImportantDataRepository extends JpaRepository<ImportantData, Long> {
}
