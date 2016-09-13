/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

import com.sev.entity.Producto;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.sev.conexion.Conexion;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author usuario1
 */
public class ProductoDAO implements Serializable {

    public List<Producto> findAll() throws SQLException {
        Conexion con = new Conexion();
        List<Producto> listadoProductos = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        //String query = "select * from producto";
        String query = "select idprod, descripcion, precio, fechavigenciai, fechavigenciaf, case estado when '1' then 'activo' else 'inactivo' end as estado from producto where estado = 1";
        pst = con.getConnection().prepareStatement(query);
        try {
            rs = pst.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdprod(rs.getInt(1));
                producto.setDescripcion(rs.getString(2));
                producto.setPrecio(rs.getDouble(3));
                producto.setFechavigenciai(rs.getString(4));
                producto.setFechavigenciaf(rs.getString(5));
                producto.setEstado(rs.getString(6));
                listadoProductos.add(producto);
            }
        } catch (Exception e) {
            System.out.println("DAO PRODUCTOS: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return listadoProductos;
    }

    public void createProducto(Producto producto) throws SQLException {
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into producto values(?,?,?,?,1)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, producto.getDescripcion());
            pst.setDouble(2, producto.getPrecio());
            pst.setString(3, producto.getFechavigenciai());
            pst.setString(4, producto.getFechavigenciaf());
            pst.executeUpdate();
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO PRODUCTO: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
    }

    public void editProducto(Producto producto) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update producto "
                + " set descripcion=?, precio=?, fechavigenciai=?, fechavigenciaf=? "
                + " where idprod=? ";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, producto.getDescripcion());
            pst.setDouble(2, producto.getPrecio());
            pst.setString(3, producto.getFechavigenciai());
            pst.setString(4, producto.getFechavigenciaf());
            pst.setInt(5, producto.getIdprod());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("DAO PRODUCTO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
    
    public void deleteProducto(Producto producto) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update producto set estado = 0 where idprod=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, producto.getIdprod());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("DAO PRODUCTO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
}
