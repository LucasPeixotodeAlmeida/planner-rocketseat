package com.lucas.planner.activity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Interface de repositório para a entidade Activity.
 * Extende JpaRepository para fornecer métodos CRUD e de paginação.
 */
public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    /**
     * Método personalizado para encontrar atividades por tripId.
     *
     * parametro: tripId o UUID da viagem associada às atividades.
     * retorna: uma lista de atividades associadas à viagem especificada.
     */
    List<Activity> findByTripId(UUID tripId);
}