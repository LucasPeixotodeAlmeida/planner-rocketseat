package com.lucas.planner.activity;

/**
 * Record ActivityRequestPayload para encapsular os dados de uma solicitação de criação de atividade.
 * Um record é uma classe imutável que simplifica a criação de classes de dados.
 *
 * parametro title Título da atividade.
 * parametro occurs_at Data e hora em que a atividade ocorre, representada como uma string.
 */
public record ActivityRequestPayload(String title, String occurs_at) {
}
