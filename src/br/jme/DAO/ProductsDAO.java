package br.jme.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.jme.Helper.ConnectionHelper;
import br.jme.Model.Products;

/**
 * @author Juan Marques
 */
public class ProductsDAO {

    public List<Products> findAll() {
        List<Products> list = new ArrayList<Products>();
        Connection c = null;
    	String sql = "SELECT * FROM products ORDER BY name";
        try {
            c = ConnectionHelper.getConnection();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);
            System.out.println(sql);
            while (rs.next()) {
                list.add(processRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return list;
    }

    
    public List<Products> findByName(String name) {
        List<Products> list = new ArrayList<Products>();
        Connection c = null;
    	String sql = "SELECT * FROM products as e " +
			"WHERE UPPER(name) LIKE ? " +	
			"ORDER BY name";
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, "%" + name.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(processRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return list;
    }
    
    public Products findById(int id) {
    	String sql = "SELECT * FROM products WHERE id = ?";
        Products products = null;
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                products = processRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return products;
    }

    public Products save(Products products)
	{
		return products.getId() > 0 ? update(products) : create(products);
	}    
    
    public Products create(Products products) {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = ConnectionHelper.getConnection();
            ps = c.prepareStatement("INSERT INTO products (name, description, price, category, picture) VALUES (?, ?, ?, ?, ?)",
                new String[] { "ID" });
            ps.setString(1, products.getName());
            ps.setString(2, products.getDescription());
            ps.setLong(3, (long) products.getPrice());
            ps.setString(4, products.getCategory());
            ps.setString(5, products.getPicture());  
            
            
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
          // Atualiza o id do objeto retornado. Isto e importante pois este valor deve ser devolvido ao cliente.
            int id = rs.getInt(1);
            products.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return products;
    }

    public Products update(Products products) {
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement("UPDATE products SET name=?, description=?, price=?, category=?, picture=? WHERE id=?");
            ps.setString(1, products.getName());
            ps.setString(2, products.getDescription());
            ps.setLong(3, (long) products.getPrice());
            ps.setString(4, products.getCategory());
            ps.setString(5, products.getPicture());  
            ps.setInt(6, products.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return products;
    }

    public boolean remove(int id) {
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement("DELETE FROM products WHERE id=?");
            ps.setInt(1, id);
            int count = ps.executeUpdate();
            return count == 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
    }

    protected Products processRow(ResultSet rs) throws SQLException {
        Products products = new Products();
        products.setId(rs.getInt("id"));
        products.setName(rs.getString("name"));
        products.setDescription(rs.getString("description"));
        products.setPrice(rs.getLong("price"));
        products.setCategory(rs.getString("category"));
        products.setPicture(rs.getString("picture"));
        return products;
    }
    
}
