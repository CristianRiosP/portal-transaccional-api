package com.portal.transaccional.portal_transaccional_api.application;

import com.portal.transaccional.portal_transaccional_api.domain.model.Orden;
import com.portal.transaccional.portal_transaccional_api.domain.port.CorePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultarOrdenesUseCase {

    private final CorePort corePort;

    public ConsultarOrdenesUseCase(CorePort corePort) {
        this.corePort = corePort;
    }

    public List<Orden> ejecutar(String tipoDocumento, String documento){
        return corePort.obtenerOrdenesPorDocumento(tipoDocumento, documento);
    }
}
