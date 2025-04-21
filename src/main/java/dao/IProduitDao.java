package dao;

import java.util.List;
import metier.Produit;

public interface IProduitDao {
	public Produit save(Produit p);

	public List<Produit> getAllProduits();

	public List<Produit> produitsParMC(String mc);

	public Produit getProduit(Long id);

	public Produit updateProduit(Produit p);

	public void deleteProduit(Long id);

}
