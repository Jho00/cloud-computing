package ru.apotravnova.practice.Library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.apotravnova.practice.Library.model.PublishingOffice;

@Repository
public interface PublishingOfficeRepository extends JpaRepository<PublishingOffice, Integer> {
}
