package at.technikum.restapi.repository;

import at.technikum.restapi.model.HourlyUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HourlyUsageRepository extends JpaRepository<HourlyUsage, LocalDateTime> {
    List<HourlyUsage> findByHourBetweenOrderByHourAsc(LocalDateTime start, LocalDateTime end);
}
