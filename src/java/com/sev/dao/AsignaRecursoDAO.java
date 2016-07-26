/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

import com.sev.entity.AsignaRecurso;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import sqlserver.Conexion;

/**
 *
 * @author Axel Latorre, Jorge Castañeda
 */
public class AsignaRecursoDAO implements Serializable {

    public List<AsignaRecurso> recursosAsignadosbyRol() {
        List<AsignaRecurso> lista = new ArrayList<>();
        Conexion con = new Conexion();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select * from rol";
        try {

        } catch (Exception e) {
        }
        return lista;
    }

}
