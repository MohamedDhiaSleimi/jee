package dao;
import java.util.List;
import metier.Produit;
public class TestDao {
public static void main(String[] args) {
ProduitDaoImpl pdao= new ProduitDaoImpl();
//List<Produit> prods =pdao.produitsParMC("iphone");
//for (Produit p : prods)
	// System.out.println(p.getNomProduit() + " - " + p.getPrix()); 

List<Produit> produits = pdao.getAllProduits();
for (Produit p : produits) {
	System.out.println(p.getIdProduit() + " - " + p.getNomProduit() + " - " + p.getPrix());
}
}
}
