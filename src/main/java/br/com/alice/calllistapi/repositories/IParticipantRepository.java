package br.com.alice.calllistapi.repositories;

import br.com.alice.calllistapi.models.Meeting;
import br.com.alice.calllistapi.models.Participant;
import br.com.alice.calllistapi.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByMeeting(Meeting meeting);
    Participant findByProfileAndMeeting(Profile profile, Meeting meeting);
}
