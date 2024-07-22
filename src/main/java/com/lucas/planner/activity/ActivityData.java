package com.lucas.planner.activity;


import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Record ActivityData para encapsular os dados de uma atividade.
 * Um record é uma classe imutável que simplifica a criação de classes de dados.
 *
 * parametro id Identificador único da atividade.
 * parametro title Título da atividade.
 * parametro occurs_at Data e hora em que a atividade ocorre.
 */
public record ActivityData(UUID id, String title, LocalDateTime occurs_at) {
}