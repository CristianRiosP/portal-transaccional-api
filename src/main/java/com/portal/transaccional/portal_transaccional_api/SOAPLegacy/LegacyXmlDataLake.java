package com.portal.transaccional.portal_transaccional_api.SOAPLegacy;
import org.springframework.stereotype.Component;

@Component
public class LegacyXmlDataLake {

    /**
     * Simula la capa de almacenamiento (Data Lake / Mainframe storage)
     * que retorna exclusivamente el XML crudo sin procesar.
     */
    public String obtenerXmlCrudoPorDocumento(String tipoDocumento, String numeroDocumento) {
        if ("123456".equals(numeroDocumento)) {
            return """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
                   <soapenv:Body>
                      <ns:ConsultarOrdenesResponse xmlns:ns="http://sura.com.co/legacy/ordenes">
                         <ns:Cliente>
                            <ns:TipoDoc>%s</ns:TipoDoc>
                            <ns:NumDoc>%s</ns:NumDoc>
                         </ns:Cliente>
                         <ns:ListaOrdenes>
                            <ns:Orden>
                               <ns:IdOrden>ORD-SOAP-9001</ns:IdOrden>
                               <ns:EstadoOrden>APROBADO_LEGACY</ns:EstadoOrden>
                               <ns:FechaCreacion>2026-10-29</ns:FechaCreacion>
                            </ns:Orden>
                            <ns:Orden>
                               <ns:IdOrden>ORD-SOAP-9002</ns:IdOrden>
                               <ns:EstadoOrden>EN_PROCESO</ns:EstadoOrden>
                               <ns:FechaCreacion>2026-10-28</ns:FechaCreacion>
                            </ns:Orden>
                         </ns:ListaOrdenes>
                      </ns:ConsultarOrdenesResponse>
                   </soapenv:Body>
                </soapenv:Envelope>
                """.formatted(tipoDocumento, numeroDocumento);
        }

        return "<soapenv:Envelope><soapenv:Body><ns:ListaOrdenes/></soapenv:Body></soapenv:Envelope>";
    }
}