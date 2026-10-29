package com.portal.transaccional.portal_transaccional_api.infraestructure.adapter;
import com.portal.transaccional.portal_transaccional_api.domain.model.Orden;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SoapXmlAdapter {

    /**
     * Patrón Adapter: Transforma el formato incompatible (XML SOAP)
     * al formato esperado por el dominio (List<Orden>).
     */
    public List<Orden> adaptar(String xmlResponse) {
        List<Orden> ordenes = new ArrayList<>();

        if (xmlResponse == null || xmlResponse.isBlank()) {
            return ordenes;
        }

        try {
            Pattern pattern = Pattern.compile(
                    "<ns:Orden>\\s*<ns:IdOrden>(.*?)</ns:IdOrden>\\s*<ns:EstadoOrden>(.*?)</ns:EstadoOrden>\\s*<ns:FechaCreacion>(.*?)</ns:FechaCreacion>\\s*</ns:Orden>",
                    Pattern.DOTALL
            );

            Matcher matcher = pattern.matcher(xmlResponse);

            while (matcher.find()) {
                ordenes.add(new Orden(
                        matcher.group(1).trim(),
                        matcher.group(2).trim(),
                        matcher.group(3).trim()
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error traduciendo XML legacy: " + e.getMessage(), e);
        }

        return ordenes;
    }
}