package br.com.anonymous.calllistapi.repositories;

import br.com.anonymous.calllistapi.models.AccessCode;
import br.com.anonymous.calllistapi.models.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAccessCodeRepository extends JpaRepository<AccessCode, Long> {
    List<AccessCode> findByMeeting(Meeting meeting);
}
