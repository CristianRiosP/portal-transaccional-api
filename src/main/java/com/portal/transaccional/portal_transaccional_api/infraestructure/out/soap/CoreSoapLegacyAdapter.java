package com.portal.transaccional.portal_transaccional_api.infraestructure.out.soap;
import com.portal.transaccional.portal_transaccional_api.SOAPLegacy.LegacyXmlDataLake;
import com.portal.transaccional.portal_transaccional_api.domain.model.Orden;
import com.portal.transaccional.portal_transaccional_api.domain.port.CorePort;
import com.portal.transaccional.portal_transaccional_api.infraestructure.adapter.SoapXmlAdapter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CoreSoapLegacyAdapter implements CorePort {

    private final LegacyXmlDataLake legacyXmlDataLake;
    private final SoapXmlAdapter soapXmlAdapter;

    public CoreSoapLegacyAdapter(LegacyXmlDataLake legacyXmlDataLake, SoapXmlAdapter soapXmlAdapter) {
        this.legacyXmlDataLake = legacyXmlDataLake;
        this.soapXmlAdapter = soapXmlAdapter;
    }

    @Override
    public List<Orden> obtenerOrdenesPorDocumento(String tipoDocumento, String numeroDocumento) {
        // 1. Obtener los datos crudos del "Data Lake" / origen legacy SOAP legacy
        String xmlCrudo = legacyXmlDataLake.obtenerXmlCrudoPorDocumento(tipoDocumento, numeroDocumento);

        // 2. Usar el Adaptador para transformar el XML del soap legacy en objetos de dominio limpios
        return soapXmlAdapter.adaptar(xmlCrudo);
    }
}