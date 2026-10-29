package com.portal.transaccional.portal_transaccional_api.infraestructure.in.rest.dto;

import com.portal.transaccional.portal_transaccional_api.domain.model.Orden;

import java.util.List;

public record OrdenResponseDto(List<OrdenItemDto> ordenes) {
    public record OrdenItemDto(
            String id,
            String estado,
            String fecha
    ) {}}
