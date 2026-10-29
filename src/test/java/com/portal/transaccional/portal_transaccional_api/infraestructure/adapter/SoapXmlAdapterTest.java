package com.portal.transaccional.portal_transaccional_api.infraestructure.adapter;

import com.portal.transaccional.portal_transaccional_api.domain.model.Orden;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SoapXmlAdapterTest {

    private final SoapXmlAdapter adapter = new SoapXmlAdapter();

    @Test
    void adaptarReturnsEmptyListForNullOrBlankXml() {
        assertEquals(List.of(), adapter.adaptar(null));
        assertEquals(List.of(), adapter.adaptar(" \n\t"));
    }

    @Test
    void adaptarMapsAndTrimsMultipleOrdersFromXml() {
        String xml = """
                <soapenv:Envelope>
                  <ns:Orden>
                    <ns:IdOrden> ORD-1 </ns:IdOrden>
                    <ns:EstadoOrden> APROBADO </ns:EstadoOrden>
                    <ns:FechaCreacion> 2026-10-29 </ns:FechaCreacion>
                  </ns:Orden>
                  <ns:Orden>
                    <ns:IdOrden>ORD-2</ns:IdOrden>
                    <ns:EstadoOrden>EN_PROCESO</ns:EstadoOrden>
                    <ns:FechaCreacion>2026-10-28</ns:FechaCreacion>
                  </ns:Orden>
                </soapenv:Envelope>
                """;

        assertEquals(List.of(
                new Orden("ORD-1", "APROBADO", "2026-10-29"),
                new Orden("ORD-2", "EN_PROCESO", "2026-10-28")
        ), adapter.adaptar(xml));
    }

    @Test
    void adaptarReturnsEmptyListWhenXmlContainsNoOrders() {
        assertEquals(List.of(), adapter.adaptar("<soapenv:Envelope/>"));
    }
}
