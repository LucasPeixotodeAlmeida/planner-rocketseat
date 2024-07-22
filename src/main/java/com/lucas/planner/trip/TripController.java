package com.lucas.planner.trip;

import com.lucas.planner.activity.ActivityData;
import com.lucas.planner.activity.ActivityRequestPayload;
import com.lucas.planner.activity.ActivityResponse;
import com.lucas.planner.activity.ActivityService;
import com.lucas.planner.link.LinkData;
import com.lucas.planner.link.LinkRequestPayload;
import com.lucas.planner.link.LinkResponse;
import com.lucas.planner.link.LinkService;
import com.lucas.planner.participant.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {


    // Injeção de dependências dos serviços e repositórios necessários
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;

    /**
     * Cria uma nova viagem e registra participantes para a viagem.
     *
     * parametro payload Dados da solicitação para criar uma nova viagem.
     * returna Resposta HTTP contendo o ID da viagem criada.
     */
    @PostMapping
    public ResponseEntity<TripCreatedResponse> createTrip(@RequestBody TripRequestPayload payload){
        // Cria uma nova instância de Trip a partir dos dados do payload
        Trip newTrip = new Trip(payload);

        // Salva a nova viagem no banco de dados
        this.tripRepository.save(newTrip);

        // Registra os participantes na nova viagem
        this.participantService.registerParticipantsToTrip(payload.emails_to_invite(), newTrip);

        // Retorna a resposta com o ID da nova viagem
        return ResponseEntity.ok(new TripCreatedResponse(newTrip.getId()));
    }

    /**
     * Obtém os detalhes de uma viagem específica.
     *
     * parametro id O UUID da viagem a ser recuperada.
     * returna Resposta HTTP contendo os detalhes da viagem ou um código de status 404 se não encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id){
        // Busca a viagem pelo ID
        Optional<Trip> trip = this.tripRepository.findById(id);

        // Retorna os detalhes da viagem se encontrada, ou um código 404 se não encontrada
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Atualiza os detalhes de uma viagem existente.
     *
     * parametro id O UUID da viagem a ser atualizada.
     * parametro payload Dados da solicitação para atualizar a viagem.
     * returna Resposta HTTP contendo a viagem atualizada ou um código de status 404 se não encontrada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload){
        // Busca a viagem pelo ID
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()){
            // Obtém a viagem encontrada
            Trip rawTrip = trip.get();

            // Atualiza os detalhes da viagem com os dados do payload
            rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination(payload.destination());

            // Salva a viagem atualizada no banco de dados
            this.tripRepository.save(rawTrip);

            // Retorna a viagem atualizada
            return ResponseEntity.ok(rawTrip);
        }

        // Retorna um código de status 404 se a viagem não for encontrada
        return ResponseEntity.notFound().build();
    }

    /**
     * Confirma uma viagem e envia emails de confirmação para os participantes.
     *
     * parametro id O UUID da viagem a ser confirmada.
     * returna Resposta HTTP contendo a viagem confirmada ou um código de status 404 se não encontrada.
     */
    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id){
        // Busca a viagem pelo ID
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()){
            // Obtém a viagem encontrada
            Trip rawTrip = trip.get();

            // Define o status de confirmação da viagem como verdadeiro
            rawTrip.setIsConfirmed(true);

            // Salva a viagem confirmada no banco de dados
            this.tripRepository.save(rawTrip);

            // Envia email de confirmação para todos os participantes da viagem
            this.participantService.triggerConfirmationEmailToParticipants(id);

            // Retorna a viagem confirmada
            return ResponseEntity.ok(rawTrip);
        }

        // Retorna um código de status 404 se a viagem não for encontrada
        return ResponseEntity.notFound().build();
    }

    /**
     * Convida um novo participante para uma viagem.
     *
     * parametro id O UUID da viagem para a qual o participante será convidado.
     * parametro payload Dados da solicitação para convidar um participante.
     * returna Resposta HTTP contendo o ID do participante criado ou um código de status 404 se a viagem não for encontrada.
     */
    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {
        // Busca a viagem pelo ID
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()){
            // Obtém a viagem encontrada
            Trip rawTrip = trip.get();

            // Registra o novo participante na viagem
            ParticipantCreateResponse participantResponse = this.participantService.registerParticipantToTrip(payload.email(), rawTrip);

            // Se a viagem já estiver confirmada, envia um email de confirmação para o participante
            if(rawTrip.getIsConfirmed()) {
                this.participantService.triggerConfirmationEmailToParticipant(payload.email());
            }

            // Retorna a resposta com o ID do participante criado
            return ResponseEntity.ok(participantResponse);
        }

        // Retorna um código de status 404 se a viagem não for encontrada
        return ResponseEntity.notFound().build();
    }

    /**
     * Obtém todos os participantes associados a uma viagem específica.
     *
     * parametro id O UUID da viagem para a qual os participantes serão recuperados.
     * returna Resposta HTTP contendo a lista de participantes da viagem.
     */
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id){
        // Obtém a lista de participantes associados à viagem
        List<ParticipantData> participantList = this.participantService.getAllParticipantsFromTrip(id);

        // Retorna a lista de participantes
        return ResponseEntity.ok(participantList);
    }

    /**
     * Adiciona uma nova atividade a uma viagem específica.
     *
     * parametro id O UUID da viagem para a qual a atividade será adicionada.
     * parametro payload Dados da solicitação para adicionar uma nova atividade.
     * returna Resposta HTTP contendo o ID da atividade criada ou um código de status 404 se a viagem não for encontrada.
     */
    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> addActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayload payload) {
        // Busca a viagem pelo ID
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()){
            // Obtém a viagem encontrada
            Trip rawTrip = trip.get();

            // Registra a nova atividade na viagem
            ActivityResponse activityResponse = this.activityService.registerActivity(payload, rawTrip);

            // Retorna a resposta com o ID da nova atividade
            return ResponseEntity.ok(activityResponse);
        }

        // Retorna um código de status 404 se a viagem não for encontrada
        return ResponseEntity.notFound().build();
    }

    /**
     * Obtém todas as atividades associadas a uma viagem específica.
     *
     * parametro id O UUID da viagem para a qual as atividades serão recuperadas.
     * returna Resposta HTTP contendo a lista de atividades da viagem.
     */
    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id){
        // Obtém a lista de atividades associadas à viagem
        List<ActivityData> activityDataList = this.activityService.getAllActivitiesFromId(id);

        // Retorna a lista de atividades
        return ResponseEntity.ok(activityDataList);
    }

    /**
     * Registra um novo link associado a uma viagem específica.
     *
     * parametro id O UUID da viagem para a qual o link será registrado.
     * parametro payload Dados da solicitação para registrar um novo link.
     * returna Resposta HTTP contendo o ID do link criado ou um código de status 404 se a viagem não for encontrada.
     */
    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayload payload) {
        // Busca a viagem pelo ID
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()){
            // Obtém a viagem encontrada
            Trip rawTrip = trip.get();

            // Registra o novo link na viagem
            LinkResponse linkResponse = this.linkService.registerLink(payload, rawTrip);

            // Retorna a resposta com o ID do novo link
            return ResponseEntity.ok(linkResponse);
        }

        // Retorna um código de status 404 se a viagem não for encontrada
        return ResponseEntity.notFound().build();
    }

    /**
     * Obtém todos os links associados a uma viagem específica.
     *
     * parametro id O UUID da viagem para a qual os links serão recuperados.
     * returna Resposta HTTP contendo a lista de links da viagem.
     */
    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id){
        // Obtém a lista de links associados à viagem
        List<LinkData> linkDataList = this.linkService.getAllLinksFromId(id);

        // Retorna a lista de links
        return ResponseEntity.ok(linkDataList);
    }
}
