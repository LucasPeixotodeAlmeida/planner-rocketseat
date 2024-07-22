package com.lucas.planner.activity;

import java.util.UUID;

/**
 * Record ActivityResponse para encapsular a resposta ao registrar uma atividade.
 * Um record é uma classe imutável que simplifica a criação de classes de dados.
 *
 * parametro activityId O identificador único da atividade recém-criada.
 */
public record ActivityResponse(UUID activityId) {
}
