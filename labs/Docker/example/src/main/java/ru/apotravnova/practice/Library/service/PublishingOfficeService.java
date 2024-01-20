package ru.apotravnova.practice.Library.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) //уровень транзакции - только чтение
public class PublishingOfficeService {
}
