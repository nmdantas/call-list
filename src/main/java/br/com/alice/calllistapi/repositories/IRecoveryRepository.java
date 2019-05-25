package br.com.alice.calllistapi.repositories;

import br.com.alice.calllistapi.models.Recovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRecoveryRepository extends JpaRepository<Recovery, String> {
}
