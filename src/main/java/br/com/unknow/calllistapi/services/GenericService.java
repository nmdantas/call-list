package br.com.unknow.calllistapi.services;

import br.com.unknow.calllistapi.payloads.PayloadGenerator;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class GenericService<TRepository extends JpaRepository, TEntity, TPayload> {

    protected TRepository repository;

    public GenericService(TRepository repository) {
        super();

        this.repository = repository;
    }

    @Transactional
    public Page<TPayload> find(TEntity filter, Pageable pagination) {
        Example<TEntity> example = Example.of(filter);

        return repository.findAll(example, pagination).map(entity -> PayloadGenerator.generate(entity));
    }

    @Transactional
    public TPayload findById(Long id) {
        Optional<TEntity> optional = repository.findById(id);

        if (optional.isPresent()) {
            return PayloadGenerator.generate(optional.get());
        } else {
            return (TPayload) null;
        }
    }
}
