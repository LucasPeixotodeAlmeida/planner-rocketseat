package com.lucas.planner.participant;

import com.lucas.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócios relacionada a participantes.
 */
@Service
public class ParticipantService {

    // Injeção de dependência do repositório de participantes
    @Autowired
    private ParticipantRepository participantRepository;

    /**
     * Registra uma lista de participantes para uma viagem.
     *
     * parametro participantsToInvite Lista de emails dos participantes a serem convidados.
     * parametro trip A viagem à qual os participantes serão associados.
     */
    public void registerParticipantsToTrip(List<String> participantsToInvite, Trip trip) {
        // Cria uma lista de participantes a partir dos emails fornecidos e da viagem
        List<Participant> participants = participantsToInvite.stream()
                .map(email -> new Participant(email, trip))
                .toList();

        // Salva todos os participantes no banco de dados
        this.participantRepository.saveAll(participants);

        // Imprime o ID do primeiro participante (para fins de depuração)
        System.out.println(participants.get(0).getId());
    }

    /**
     * Registra um novo participante para uma viagem.
     *
     * parametro email O email do novo participante.
     * parametro trip A viagem à qual o participante será associado.
     * returna A resposta contendo o identificador do novo participante.
     */
    public ParticipantCreateResponse registerParticipantToTrip(String email, Trip trip) {
        // Cria um novo participante a partir do email e da viagem
        Participant newParticipant = new Participant(email, trip);

        // Salva o novo participante no banco de dados
        this.participantRepository.save(newParticipant);

        // Retorna a resposta contendo o ID do novo participante
        return new ParticipantCreateResponse(newParticipant.getId());
    }

    /**
     * Envia um email de confirmação para todos os participantes de uma viagem.
     * Este método ainda não está implementado.
     *
     * @parametro tripId O UUID da viagem para a qual os emails de confirmação serão enviados.
     */
    public void triggerConfirmationEmailToParticipants(UUID tripId) {
        // Implementação pendente
    }

    /**
     * Envia um email de confirmação para um participante específico.
     * Este método ainda não está implementado.
     *
     * parametro email O email do participante que receberá o email de confirmação.
     */
    public void triggerConfirmationEmailToParticipant(String email) {
        // Implementação pendente
    }

    /**
     * Obtém todos os participantes associados a uma viagem.
     *
     * parametro tripId O UUID da viagem para a qual os participantes serão recuperados.
     * returna Uma lista de ParticipantData contendo os dados dos participantes.
     */
    public List<ParticipantData> getAllParticipantsFromTrip(UUID tripId) {
        // Busca os participantes pelo tripId e mapeia para ParticipantData
        return this.participantRepository.findByTripId(tripId).stream()
                .map(participant -> new ParticipantData(participant.getId(), participant.getName(), participant.getEmail(), participant.getIsConfirmed()))
                .toList();
    }
}
