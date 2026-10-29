package com.portal.transaccional.portal_transaccional_api.domain.port;

import com.portal.transaccional.portal_transaccional_api.domain.model.Orden;

import java.util.List;

public interface CorePort {
    List<Orden> obtenerOrdenesPorDocumento(String tipoDocumento, String documento );
}
