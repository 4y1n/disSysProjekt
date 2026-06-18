package at.technikum.percentageservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface HourlyUsageRepository extends JpaRepository<HourlyUsage, LocalDateTime> {
    Optional<HourlyUsage> findByHour(LocalDateTime hour);
}
