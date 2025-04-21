package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import metier.SingletonConnection;
import metier.Produit;
public class ProduitDaoImpl implements IProduitDao {
@Override
public Produit save(Produit p) {
Connection conn=SingletonConnection.getConnection();
try {
PreparedStatement ps= conn.prepareStatement("INSERT INTO produit (nom,prix) VALUES(?,?)");
ps.setString(1, p.getNomProduit());
ps.setDouble(2, p.getPrix());
ps.executeUpdate();
PreparedStatement ps2= conn.prepareStatement
("SELECT MAX(id) as MAX_ID FROM produit");

ResultSet rs =ps2.executeQuery();
if (rs.next()) {
p.setIdProduit(rs.getLong("MAX_ID"));
}
ps.close();
ps2.close();
} catch (SQLException e) {
e.printStackTrace();
}
return p;

}
@Override
public List<Produit> produitsParMC(String mc) {
List<Produit> prods= new ArrayList<Produit>();
Connection conn=SingletonConnection.getConnection();
try {
PreparedStatement ps= conn.prepareStatement("select idp,nom , prix from produit where nom LIKE ?");
ps.setString(1, "%"+mc+"%");
ResultSet rs = ps.executeQuery();
while (rs.next()) {
Produit p = new Produit();
p.setIdProduit(rs.getLong("idp"));
p.setNomProduit(rs.getString("nom"));
p.setPrix(rs.getDouble("prix"));
prods.add(p);
}
} catch (SQLException e) {
e.printStackTrace();
}
return prods;

}
@Override
public Produit getProduit(Long id) {
	    Connection conn = SingletonConnection.getConnection();
	    Produit produit = null;
	    try {
	        PreparedStatement ps = conn.prepareStatement("SELECT * FROM produit WHERE idp = ?");
	        ps.setLong(1, id); // Set the provided ID
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            produit = new Produit();
	            produit.setIdProduit(rs.getLong("idp"));
	            produit.setNomProduit(rs.getString("nom"));
	            produit.setPrix(rs.getDouble("prix"));
	        }
	        ps.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return produit;
	
}
@Override
public Produit updateProduit(Produit p) {
    Connection conn = SingletonConnection.getConnection();
    try {
        PreparedStatement ps = conn.prepareStatement("UPDATE produit SET nom = ?, prix = ? WHERE idp = ?");
        ps.setString(1, p.getNomProduit());
        ps.setDouble(2, p.getPrix());
        ps.setLong(3, p.getIdProduit());
        int rows = ps.executeUpdate(); // Ne surtout pas utiliser executeQuery ici
        ps.close();

        if (rows > 0) {
            System.out.println("Produit mis à jour avec succès.");
        } else {
            System.out.println("Aucune mise à jour effectuée (ID non trouvé ?).");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return p;
}

@Override
public void deleteProduit(Long id) {
	 Connection conn = SingletonConnection.getConnection();
	   
	    try {
	        PreparedStatement ps = conn.prepareStatement("DELETE FROM produit WHERE idp = ?");
	        ps.setLong(1, id); // Set the provided ID
	        ps.executeUpdate(); 
	        ps.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	
	    
}
@Override
public List<Produit> getAllProduits() {
	 List<Produit> produits = new ArrayList<>();
	    try {
	        Connection conn = SingletonConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement("SELECT idp,nom,prix FROM produit");
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            Produit p = new Produit();
	            p.setIdProduit(rs.getLong("idp"));
	            p.setNomProduit(rs.getString("nom"));
	            p.setPrix(rs.getDouble("PRIX"));
	            produits.add(p);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return produits;
}
}