package br.com.anonymous.calllistapi.repositories;

import br.com.anonymous.calllistapi.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByUserIdAndActiveTrue(Long userId);
}
