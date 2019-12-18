package com.utfpr.ativadi.repositories;

import com.utfpr.ativadi.entities.AulaConcrete;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AulaRepository extends CrudRepository<AulaConcrete, Long> {
    @Query(value = "SELECT * FROM ativadi.aula", nativeQuery = true)
    public List<AulaConcrete> findAll();

    @Query(value = "SELECT * FROM ativadi.aula a WHERE a.id = :id", nativeQuery = true)
    public Optional<AulaConcrete> findById(@Param("id") Long id);

    @Query(value = "SELECT MAX(COALESCE(id, 0)) + 1 FROM ativadi.aula", nativeQuery = true)
    public long getNewID();
}
