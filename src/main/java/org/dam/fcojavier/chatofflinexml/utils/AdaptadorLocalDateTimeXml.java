package org.dam.fcojavier.chatofflinexml.utils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Adaptador JAXB para convertir LocalDate a String y viceversa durante la serialización XML.
 */
public class AdaptadorLocalDateTimeXml extends XmlAdapter<String, LocalDateTime> {
    private static final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public LocalDateTime unmarshal(String v) throws Exception {
        if (v == null || v.trim().isEmpty()) {
            return null;
        } else {
            return LocalDateTime.parse(v, formato);
        }
    }

    @Override
    public String marshal(LocalDateTime v) throws Exception {
        if (v == null) {
            return "";
        } else {
            return v.format(formato);
        }
    }
}
