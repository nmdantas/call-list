package br.com.alice.calllistapi.repositories;

import br.com.alice.calllistapi.models.ViewMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IViewMeetingRepository extends JpaRepository<ViewMeeting, Long> {
    @Query(value = "select m.ID as MEETING_ID, m.NAME as MEETING_NAME, m.DATE as MEETING_DATE, m.PROFILE_ID as PROFILE_ID, pr.COMPANY as COMPANY\n" +
            "from meeting m\n" +
            "inner join participant p on p.MEETING_ID = m.ID\n" +
            "inner join profile pr on pr.ID = p.PROFILE_ID\n" +
            "where pr.ACTIVE = 1 and pr.USER_ID = ?1\n" +
            "order by m.DATE", nativeQuery = true)
    List<ViewMeeting> findByUser(long userId);
}
