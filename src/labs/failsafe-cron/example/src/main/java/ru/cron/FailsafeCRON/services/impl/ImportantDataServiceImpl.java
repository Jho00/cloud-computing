package ru.cron.FailsafeCRON.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cron.FailsafeCRON.entities.ImportantData;
import ru.cron.FailsafeCRON.repositories.ImportantDataRepository;
import ru.cron.FailsafeCRON.services.ImportantDataService;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ImportantDataServiceImpl implements ImportantDataService {
    private final ImportantDataRepository repository;

    @Override
    public void save() {
        ImportantData data = new ImportantData();
        data.setTime(LocalTime.now());
        repository.save(data);
    }

    @Override
    public Integer numberOfEntries() {
        return repository.findAll().size();
    }
}
