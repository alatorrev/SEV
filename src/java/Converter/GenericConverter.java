/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * 
 * Universidad Politécnica Salesiana
 * @author Axel Latorre, Jorge Castañeda
 * Tutor: Ing. Vanessa Jurado
 * 
 */
@FacesConverter(value = "generalConverter")//para poderlo llamar con este nombre
public class GenericConverter implements Converter {

    private static Map<Object, String> entities = new HashMap<Object, String>();

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        synchronized (entities) {
            if (!entities.containsKey(value)) {
                String uuid = UUID.randomUUID().toString();
                entities.put(value, uuid);
                return uuid;
            } else {
                return entities.get(value);
            }
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String uuid) {
        for (Entry<Object, String> entry : entities.entrySet()) {
            if (entry.getValue().equals(uuid)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
