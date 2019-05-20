package br.com.alice.calllistapi.repositories;

import br.com.alice.calllistapi.models.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMeetingRepository extends JpaRepository<Meeting, Long> {
    boolean existsById(Long id);
    List<Meeting> findByProfileId(Long profileId);
}
