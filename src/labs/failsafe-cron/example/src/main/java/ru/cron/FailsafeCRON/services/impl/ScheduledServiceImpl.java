package ru.cron.FailsafeCRON.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import ru.cron.FailsafeCRON.services.ImportantDataService;
import ru.cron.FailsafeCRON.services.ScheduledService;

import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;
import static ru.cron.FailsafeCRON.utils.StringUtil.*;

@Component
@RequiredArgsConstructor
public class ScheduledServiceImpl implements ScheduledService {
    private final ImportantDataService service;
    private final StringRedisTemplate redisTemplate;

    @Override
    public void script() {
        printNumberOfEntriesBefore(service.numberOfEntries());
//
        Object lock = redisTemplate.opsForValue().getAndSet("lock", "lock");
        redisTemplate.expire("lock", 15, TimeUnit.SECONDS);
        if (isNull(lock)) {
            printLock();
            service.save();
        } else printSkip();
//
        printNumberOfEntriesAfter(service.numberOfEntries());
    }
}