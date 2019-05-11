package br.com.unknow.calllistapi.repositories;

import br.com.unknow.calllistapi.models.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMeetingRepository extends JpaRepository<Meeting, Long> {
    long countById(Long id);
    List<Meeting> findByProfileId(Long profileId);
}
