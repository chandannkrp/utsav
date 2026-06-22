package dev.utsav.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SpringDataEventRepository extends JpaRepository<EventJpaEntity, UUID> {

    @Query("""
            SELECT e FROM EventJpaEntity e
            WHERE e.city = :city
            AND e.status = 'PUBLISHED'
            AND e.startTime > :now
            ORDER BY e.startTime ASC
            LIMIT :limit
            """
    )
    List<EventJpaEntity> findUpcomingByCity(@Param("city") String city,
                                            @Param("now") LocalDateTime now,
                                            @Param("limit") int limit);


}
