package at.technikum.restapi.repository;

import at.technikum.restapi.model.CurrentPercentage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CurrentPercentageRepository extends JpaRepository<CurrentPercentage, LocalDateTime> {

    @Query("SELECT c FROM CurrentPercentage c ORDER BY c.hour DESC LIMIT 1")
    Optional<CurrentPercentage> findLatest();
}
