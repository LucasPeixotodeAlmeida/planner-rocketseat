package com.lucas.planner.activity;

import com.lucas.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócios relacionada a atividades.
 */
@Service
public class ActivityService {

    // Injeção de dependência do repositório de atividades
    @Autowired
    private ActivityRepository activityRepository;

    /**
     * Registra uma nova atividade com base nos dados fornecidos no payload e na viagem associada.
     *
     * parametro payload Dados da solicitação de criação da atividade.
     * parametro trip A viagem à qual a atividade está associada.
     * returna a resposta contendo o identificador da nova atividade.
     */
    public ActivityResponse registerActivity(ActivityRequestPayload payload, Trip trip) {
        // Cria uma nova instância de Activity com os dados do payload e da trip
        Activity newActivity = new Activity(payload.title(), payload.occurs_at(), trip);

        // Salva a nova atividade no banco de dados
        this.activityRepository.save(newActivity);

        // Retorna a resposta contendo o ID da nova atividade
        return new ActivityResponse(newActivity.getId());
    }

    /**
     * Obtém todas as atividades associadas a um determinado tripId.
     *
     * parametro tripId O UUID da viagem para a qual as atividades serão recuperadas.
     * returna uma lista de ActivityData contendo os dados das atividades.
     */
    public List<ActivityData> getAllActivitiesFromId(UUID tripId) {
        // Busca as atividades pelo tripId e mapeia para ActivityData
        return this.activityRepository.findByTripId(tripId).stream()
                .map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt()))
                .toList();
    }
}
