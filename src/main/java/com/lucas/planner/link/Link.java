package com.lucas.planner.link;


import com.lucas.planner.trip.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "links")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    /**
     * Construtor personalizado para criar um 'Link' a partir de um título, URL e uma viagem.
     *
     * parametro title Título do link.
     * parametro url URL do link.
     * parametro trip A viagem associada ao link.
     */
    public Link(String title, String url, Trip trip) {
        this.title = title;
        this.url = url;
        this.trip = trip;
    }
}
