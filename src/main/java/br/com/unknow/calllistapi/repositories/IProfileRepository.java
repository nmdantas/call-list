package br.com.unknow.calllistapi.repositories;

import br.com.unknow.calllistapi.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByUserIdAndActiveTrue(Long userId);
}
