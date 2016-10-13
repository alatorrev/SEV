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
import com.sev.entity.Cita;
import com.sev.entity.Usuario;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * Universidad Politécnica Salesiana
 * @author Axel Latorre, Jorge Castañeda
 * Tutor: Ing. Vanessa Jurado
 * 
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
                producto.setFechavigenciai(rs.getDate(4));
                producto.setFechavigenciaf(rs.getDate(5));
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
    
    public List<Producto> supportedProductbyDate(Cita c) throws SQLException {
        Conexion con = new Conexion();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Producto> listadoProductos = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select idprod, descripcion, precio, fechavigenciai, fechavigenciaf,"
                + "case estado when '1' then 'activo' else 'inactivo' end as estado "
                + "from producto where FECHAVIGENCIAI<=? and FECHAVIGENCIAF>=? and estado=1";
        pst = con.getConnection().prepareStatement(query);
        try {
            String ini=sdf.format(c.getFechaInicio());
            String fin=sdf.format(c.getFechaFin());
            pst.setString(1, ini);
            pst.setString(2, fin);
            rs = pst.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdprod(rs.getInt(1));
                producto.setDescripcion(rs.getString(2));
                producto.setPrecio(rs.getDouble(3));
                producto.setFechavigenciai(rs.getDate(4));
                producto.setFechavigenciaf(rs.getDate(5));
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
    
    public void createProducto(Producto producto, Usuario u) throws SQLException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into producto values(?,?,?,?,1)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, producto.getDescripcion().toUpperCase());
            pst.setDouble(2, producto.getPrecio());
            pst.setString(3, format.format(producto.getFechavigenciai()));
            pst.setString(4, format.format(producto.getFechavigenciaf()));
            pst.executeUpdate();
            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("producto", "insert", u);
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO PRODUCTO: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
    }

    public void editProducto(Producto producto, Usuario u) throws SQLException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update producto "
                + " set descripcion=?, precio=?, fechavigenciai=?, fechavigenciaf=? "
                + " where idprod=? ";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, producto.getDescripcion().toUpperCase());
            pst.setDouble(2, producto.getPrecio());
            pst.setString(3, format.format(producto.getFechavigenciai()));
            pst.setString(4, format.format(producto.getFechavigenciaf()));
            pst.setInt(5, producto.getIdprod());
            pst.executeUpdate();
            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("producto", "Edit", u);
        } catch (Exception e) {
            System.out.println("DAO PRODUCTO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
    
    public void deleteProducto(Producto producto, Usuario u) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update producto set estado = 0 where idprod=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, producto.getIdprod());
            pst.executeUpdate();
            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("producto", "delete", u);
        } catch (Exception e) {
            System.out.println("DAO PRODUCTO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
    
    public List<Producto> completeProductoMethod(String pattern) throws SQLException {
        Conexion con = new Conexion();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Producto> listadoProductos = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select idprod, descripcion, precio, fechavigenciai, fechavigenciaf,"
                + "case estado when '1' then 'activo' else 'inactivo' end as estado "
                + "from producto where estado=1 "
                + "and upper(descripcion) like upper(?) and FECHAVIGENCIAI<=? and FECHAVIGENCIAF>=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1,pattern.trim().concat("%"));
            String ini=sdf.format(new Date());
            String fin=sdf.format(new Date());
            pst.setString(2, ini);
            pst.setString(3, fin);
            rs = pst.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdprod(rs.getInt(1));
                producto.setDescripcion(rs.getString(2));
                producto.setPrecio(rs.getDouble(3));
                producto.setFechavigenciai(rs.getDate(4));
                producto.setFechavigenciaf(rs.getDate(5));
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
}
