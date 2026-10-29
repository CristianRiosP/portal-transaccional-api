package com.portal.transaccional.portal_transaccional_api.infraestructure.in.rest;

import com.portal.transaccional.portal_transaccional_api.application.ConsultarOrdenesUseCase;
import com.portal.transaccional.portal_transaccional_api.domain.model.Orden;
import com.portal.transaccional.portal_transaccional_api.infraestructure.in.rest.dto.OrdenResponseDto;
import com.portal.transaccional.portal_transaccional_api.infraestructure.security.JwtService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;



import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
public class OrdenesController {

    private final ConsultarOrdenesUseCase consultarOrdenesUseCase;
    private final JwtService jwtService;

    public OrdenesController(ConsultarOrdenesUseCase useCase, JwtService jwtService) {
        this.consultarOrdenesUseCase = useCase;
        this.jwtService = jwtService;
    }

    @GetMapping("/ordenes")
    public ResponseEntity<OrdenResponseDto> consultarOrdenes(
            @RequestParam @NotBlank(message = "El tipo de documento es obligatorio")
            @Pattern(regexp = "^(CC|CE|PAS)$", message = "Tipo de documento no válido") String tipoDocumento,
            @RequestParam @NotBlank(message = "El número de documento es obligatorio")
            @Pattern(regexp = "^[0-9]{5,12}$", message = "Formato de documento inválido") String numeroDocumento,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authHeader.substring(7);

        // Extracción de datos del Token (se validan tipo y numero doc aplicando politica Zero Trust)
        String documentoToken = jwtService.extraerDocumentoDelToken(token);
        String tipoDocumentoToken = jwtService.extraerTipoDocumentoDelToken(token);

        // Mitigación IDOR
        if (!documentoToken.equals(numeroDocumento) || !tipoDocumentoToken.equals(tipoDocumento)) {
            return ResponseEntity.status(403).build();
        }

        // Ejecutar Caso de Uso
        List<Orden> ordenes = consultarOrdenesUseCase.ejecutar(tipoDocumento, numeroDocumento);

        // Mapeo a dto response
        List<OrdenResponseDto.OrdenItemDto> itemDtos = ordenes.stream()
                .map(orden -> new OrdenResponseDto.OrdenItemDto(
                        orden.id(), orden.estado(), orden.fecha()))
                .toList();

        return ResponseEntity.ok(new OrdenResponseDto(itemDtos));
    }

}
