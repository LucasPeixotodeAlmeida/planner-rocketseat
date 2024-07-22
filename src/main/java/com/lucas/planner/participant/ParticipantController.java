package com.lucas.planner.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

/**
 * Controlador REST para gerenciar operações relacionadas a participantes.
 */
@RestController
@RequestMapping("/participants")
public class ParticipantController {

    // Injeção de dependência do repositório de participantes
    @Autowired
    private ParticipantRepository participantRepository;

    /**
     * Endpoint para confirmar a participação de um participante.
     *
     * parametro id O UUID do participante a ser confirmado.
     * parametro payload Dados da solicitação, incluindo o nome do participante.
     * returna Resposta HTTP contendo o participante atualizado, ou um código de status 404 se não encontrado.
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {
        // Busca o participante pelo ID
        Optional<Participant> participant = this.participantRepository.findById(id);

        // Se o participante for encontrado
        if (participant.isPresent()) {
            // Obtém o participante
            Participant rawParticipant = participant.get();

            // Atualiza o status de confirmação e o nome
            rawParticipant.setIsConfirmed(true);
            rawParticipant.setName(payload.name());

            // Salva as alterações no banco de dados
            this.participantRepository.save(rawParticipant);

            // Retorna o participante atualizado com status 200 OK
            return ResponseEntity.ok(rawParticipant);
        }

        // Se o participante não for encontrado, retorna status 404 Not Found
        return ResponseEntity.notFound().build();
    }
}
