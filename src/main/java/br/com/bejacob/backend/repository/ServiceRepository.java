package br.com.bejacob.backend.repository;

import br.com.bejacob.backend.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {

    Services save(Services service);

}