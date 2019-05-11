package br.com.unknow.calllistapi.repositories;

import br.com.unknow.calllistapi.models.AccessCode;
import br.com.unknow.calllistapi.models.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAccessCodeRepository extends JpaRepository<AccessCode, Long> {
    List<AccessCode> findByMeeting(Meeting meeting);
}
