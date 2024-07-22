package com.lucas.planner.activity;

import com.lucas.planner.trip.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "activities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Activity {

    // Chave primária gerada automaticamente com UUID
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Coluna 'title' não pode ser nula
    @Column(nullable = false)
    private String title;

    // Coluna 'occurs_at' que armazena data e hora e não pode ser nula
    @Column(name = "occurs_at", nullable = false)
    private LocalDateTime occursAt;

    // Muitos 'Activity' para um 'Trip', coluna 'trip_id' não pode ser nula
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    // Construtor personalizado para criar uma 'Activity' a partir de um título, data/hora e uma viagem
    public Activity(String title, String occursAt, Trip trip){
        // Atribui o título da atividade
        this.title = title;
        // Converte a string occursAt para LocalDateTime usando o formato ISO_DATE_TIME
        this.occursAt = LocalDateTime.parse(occursAt, DateTimeFormatter.ISO_DATE_TIME);
        // Atribui a viagem associada à atividade
        this.trip = trip;
    }
}
