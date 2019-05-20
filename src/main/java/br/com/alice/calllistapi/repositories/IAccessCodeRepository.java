package br.com.alice.calllistapi.repositories;

import br.com.alice.calllistapi.models.AccessCode;
import br.com.alice.calllistapi.models.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAccessCodeRepository extends JpaRepository<AccessCode, Long> {
    List<AccessCode> findByMeeting(Meeting meeting);
    AccessCode findByCode(Integer code);
    boolean existsByCode(Integer code);
    long deleteByMeeting(Meeting meeting);
}
