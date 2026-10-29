package com.portal.transaccional.portal_transaccional_api.application;

import com.portal.transaccional.portal_transaccional_api.domain.model.Orden;
import com.portal.transaccional.portal_transaccional_api.domain.port.CorePort;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConsultarOrdenesUseCaseTest {

    private final CorePort corePort = mock(CorePort.class);
    private final ConsultarOrdenesUseCase useCase = new ConsultarOrdenesUseCase(corePort);

    @Test
    void ejecutarDelegatesToCorePortAndReturnsItsOrders() {
        List<Orden> ordenes = List.of(new Orden("ORD-1", "APROBADO", "2026-10-29"));
        when(corePort.obtenerOrdenesPorDocumento("CC", "123456")).thenReturn(ordenes);

        List<Orden> resultado = useCase.ejecutar("CC", "123456");

        assertSame(ordenes, resultado);
        verify(corePort).obtenerOrdenesPorDocumento("CC", "123456");
    }
}
