package br.com.alice.calllistapi.repositories;

import br.com.alice.calllistapi.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByUserIdAndActiveTrue(Long userId);
    boolean existsByUserId(Long userId);

    @Modifying
    @Query("update Profile p set p.main = false where p.userId = ?1 and p.main = true")
    int unmarkAsMainByUserId(Long userId);
}
