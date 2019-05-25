package br.com.alice.calllistapi.repositories;

import br.com.alice.calllistapi.models.ViewContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IViewContactRepository extends JpaRepository<ViewContact, Long> {
    @Query(value = "select distinct u.ID as USER_ID, u.NAME as USER_NAME, pr.COMPANY as COMPANY, pr.ROLE as ROLE, pr.EMAIL as EMAIL, pr.PHONE as PHONE\n" +
            "from participant p\n" +
            "inner join profile pr on pr.ID = p.PROFILE_ID\n" +
            "inner join user u on u.ID =  pr.USER_ID\n" +
            "where p.MEETING_ID in (\n" +
            "\tselect m.ID\n" +
            "\tfrom meeting m\n" +
            "\tinner join participant p on p.MEETING_ID = m.ID\n" +
            "\tinner join profile pr on pr.ID = p.PROFILE_ID\n" +
            "\twhere pr.USER_ID = ?1\n" +
            ") and pr.USER_ID <> ?1", nativeQuery = true)
    List<ViewContact> findByUser(long userId);
}
