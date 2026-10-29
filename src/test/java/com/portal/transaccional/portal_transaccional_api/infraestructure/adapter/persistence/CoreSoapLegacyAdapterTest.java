package com.portal.transaccional.portal_transaccional_api.infraestructure.adapter.persistence;

import com.portal.transaccional.portal_transaccional_api.SOAPLegacy.LegacyXmlDataLake;
import com.portal.transaccional.portal_transaccional_api.domain.model.Orden;
import com.portal.transaccional.portal_transaccional_api.infraestructure.adapter.SoapXmlAdapter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CoreSoapLegacyAdapterTest {

    private final LegacyXmlDataLake dataLake = mock(LegacyXmlDataLake.class);
    private final SoapXmlAdapter soapXmlAdapter = mock(SoapXmlAdapter.class);
    private final CoreSoapLegacyAdapter adapter = new CoreSoapLegacyAdapter(dataLake, soapXmlAdapter);

    @Test
    void obtenerOrdenesPorDocumentoLoadsAndAdaptsLegacyXml() {
        String xml = "<legacy/>";
        List<Orden> ordenes = List.of(new Orden("ORD-1", "APROBADO", "2026-10-29"));
        when(dataLake.obtenerXmlCrudoPorDocumento("CC", "123456")).thenReturn(xml);
        when(soapXmlAdapter.adaptar(xml)).thenReturn(ordenes);

        List<Orden> resultado = adapter.obtenerOrdenesPorDocumento("CC", "123456");

        assertSame(ordenes, resultado);
        verify(dataLake).obtenerXmlCrudoPorDocumento("CC", "123456");
        verify(soapXmlAdapter).adaptar(xml);
    }
}
