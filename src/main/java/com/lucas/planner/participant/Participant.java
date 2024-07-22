package com.lucas.planner.participant;

import com.lucas.planner.trip.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "participants")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String Email;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    /**
     * Construtor personalizado para criar um 'Participant' a partir de um email e uma viagem.
     * A confirmação é definida como falsa e o nome é definido como uma string vazia.
     *
     * parametro email Email do participante.
     * parametro trip A viagem associada ao participante.
     */
    public Participant(String email, Trip trip) {
        this.Email = email;
        this.trip = trip;
        this.isConfirmed = false;
        this.name = "";
    }
}
