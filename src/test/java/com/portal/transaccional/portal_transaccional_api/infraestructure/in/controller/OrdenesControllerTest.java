package com.portal.transaccional.portal_transaccional_api.infraestructure.in.controller;

import com.portal.transaccional.portal_transaccional_api.application.ConsultarOrdenesUseCase;
import com.portal.transaccional.portal_transaccional_api.domain.model.Orden;
import com.portal.transaccional.portal_transaccional_api.domain.port.LoggerPort;
import com.portal.transaccional.portal_transaccional_api.infraestructure.in.dto.OrdenResponseDto;
import com.portal.transaccional.portal_transaccional_api.infraestructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdenesControllerTest {

    @Mock
    private ConsultarOrdenesUseCase useCase;

    @Mock
    private JwtService jwtService;

    @Mock
    private LoggerPort logger;

    @Test
    void consultarOrdenesReturnsUnauthorizedWhenAuthorizationHeaderIsMissingOrInvalid() {
        OrdenesController controller = new OrdenesController(useCase, jwtService,logger);

        ResponseEntity<OrdenResponseDto> missingHeader =
                controller.consultarOrdenes("CC", "123456", null);
        ResponseEntity<OrdenResponseDto> invalidHeader =
                controller.consultarOrdenes("CC", "123456", "Basic credentials");

        assertEquals(HttpStatus.UNAUTHORIZED, missingHeader.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, invalidHeader.getStatusCode());
        verify(useCase, never()).ejecutar("CC", "123456");
    }

    @Test
    void consultarOrdenesReturnsForbiddenWhenTokenDocumentDoesNotMatchRequest() {
        OrdenesController controller = new OrdenesController(useCase, jwtService,logger);
        when(jwtService.extraerDocumentoDelToken("token")).thenReturn("654321");
        when(jwtService.extraerTipoDocumentoDelToken("token")).thenReturn("CC");

        ResponseEntity<OrdenResponseDto> response =
                controller.consultarOrdenes("CC", "123456", "Bearer token");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(useCase, never()).ejecutar("CC", "123456");
    }

    @Test
    void consultarOrdenesReturnsForbiddenWhenTokenDocumentTypeDoesNotMatchRequest() {
        OrdenesController controller = new OrdenesController(useCase, jwtService,logger);
        when(jwtService.extraerDocumentoDelToken("token")).thenReturn("123456");
        when(jwtService.extraerTipoDocumentoDelToken("token")).thenReturn("CE");

        ResponseEntity<OrdenResponseDto> response =
                controller.consultarOrdenes("CC", "123456", "Bearer token");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(useCase, never()).ejecutar("CC", "123456");
    }

    @Test
    void consultarOrdenesReturnsMappedOrdersWhenTokenMatchesRequest() {
        OrdenesController controller = new OrdenesController(useCase, jwtService, logger);
        List<Orden> ordenes = List.of(
                new Orden("ORD-1", "APROBADO", "2026-10-29"),
                new Orden("ORD-2", "EN_PROCESO", "2026-10-28")
        );
        when(jwtService.extraerDocumentoDelToken("token")).thenReturn("123456");
        when(jwtService.extraerTipoDocumentoDelToken("token")).thenReturn("CC");
        when(useCase.ejecutar("CC", "123456")).thenReturn(ordenes);

        ResponseEntity<OrdenResponseDto> response =
                controller.consultarOrdenes("CC", "123456", "Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(
                new OrdenResponseDto.OrdenItemDto("ORD-1", "APROBADO", "2026-10-29"),
                new OrdenResponseDto.OrdenItemDto("ORD-2", "EN_PROCESO", "2026-10-28")
        ), response.getBody().ordenes());
        verify(useCase).ejecutar("CC", "123456");
    }
}
