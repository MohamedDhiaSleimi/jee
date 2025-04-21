package dao;

import java.sql.*;
import java.util.*;

import metier.SingletonConnection;
import metier.Produit;

public class ProduitDaoImpl implements IProduitDao {

	@Override
	public Produit save(Produit p) {
		Connection conn = SingletonConnection.getConnection();
		try {
			String insertQuery = "INSERT INTO PRODUIT(NomP, PRIX) VALUES(?, ?)";
			PreparedStatement ps = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, p.getNomProduit());
			ps.setDouble(2, p.getPrix());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				p.setIdProduit(rs.getLong(1));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p;
	}

	@Override
	public List<Produit> produitsParMC(String mc) {
		List<Produit> prods = new ArrayList<>();
		Connection conn = SingletonConnection.getConnection();
		try {
			String query = "SELECT * FROM PRODUIT WHERE NomP LIKE ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, "%" + mc + "%");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Produit p = new Produit();
				p.setIdProduit(rs.getLong("IdP"));
				p.setNomProduit(rs.getString("NomP"));
				p.setPrix(rs.getDouble("PRIX"));
				prods.add(p);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prods;
	}

	@Override
	public Produit getProduit(Long id) {
		Connection conn = SingletonConnection.getConnection();
		Produit p = null;
		try {
			String query = "SELECT * FROM PRODUIT WHERE IdP = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				p = new Produit();
				p.setIdProduit(rs.getLong("IdP"));
				p.setNomProduit(rs.getString("NomP"));
				p.setPrix(rs.getDouble("PRIX"));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p;
	}

	@Override
	public Produit updateProduit(Produit p) {
		Connection conn = SingletonConnection.getConnection();
		try {
			String query = "UPDATE PRODUIT SET NomP = ?, PRIX = ? WHERE IdP = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, p.getNomProduit());
			ps.setDouble(2, p.getPrix());
			ps.setLong(3, p.getIdProduit());

			int rowsUpdated = ps.executeUpdate();
			ps.close();

			return rowsUpdated > 0 ? p : null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void deleteProduit(Long id) {
		Connection conn = SingletonConnection.getConnection();
		try {
			String query = "DELETE FROM PRODUIT WHERE IdP = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setLong(1, id);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Produit> getAllProduits() {
		List<Produit> produits = new ArrayList<>();
		try (Connection conn = SingletonConnection.getConnection();
		     PreparedStatement ps = conn.prepareStatement("SELECT IdP, NomP, PRIX FROM PRODUIT");
		     ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Produit p = new Produit();
				p.setIdProduit(rs.getLong("IdP"));
				p.setNomProduit(rs.getString("NomP"));
				p.setPrix(rs.getDouble("PRIX"));
				produits.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace(); // à remplacer par un logger
		}
		return produits;
	}

}
