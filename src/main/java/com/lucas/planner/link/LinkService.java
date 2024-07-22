package com.lucas.planner.link;

import com.lucas.planner.activity.ActivityData;
import com.lucas.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LinkService {

    // Injeção de dependência do repositório de links
    @Autowired
    private LinkRepository linkRepository;

    /**
     * Registra um novo link com base nos dados fornecidos no payload e na viagem associada.
     *
     * parametro payload Dados da solicitação de criação do link.
     * parametro trip A viagem à qual o link está associado.
     * returna A resposta contendo o identificador do novo link.
     */
    public LinkResponse registerLink(LinkRequestPayload payload, Trip trip) {
        // Cria uma nova instância de Link com os dados do payload e da trip
        Link newLink = new Link(payload.title(), payload.url(), trip);

        // Salva o novo link no banco de dados
        this.linkRepository.save(newLink);

        // Retorna a resposta contendo o ID do novo link
        return new LinkResponse(newLink.getId());
    }

    /**
     * Obtém todos os links associados a um determinado tripId.
     *
     * parametro tripId O UUID da viagem para a qual os links serão recuperados.
     * returna Uma lista de LinkData contendo os dados dos links.
     */
    public List<LinkData> getAllLinksFromId(UUID tripId) {
        // Busca os links pelo tripId e mapeia para LinkData
        return this.linkRepository.findByTripId(tripId).stream()
                .map(link -> new LinkData(link.getId(), link.getTitle(), link.getUrl()))
                .toList();
    }
}
