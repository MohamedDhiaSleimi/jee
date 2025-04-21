package web;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import metier.Produit;
import metier.User;

import java.io.IOException;
import java.util.List;

import dao.ProduitDaoImpl;

/**
 * Servlet implementation class controller
 */
@WebServlet(name="cs",urlPatterns={"/index"})
public class controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	ProduitDaoImpl dao = new ProduitDaoImpl(); 
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doPost(request, response); // redirige les requêtes GET vers doPost()
	    
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String action = request.getParameter("action");

	    if ("login".equals(action)) {
	        String mail = request.getParameter("login");
	        String mdp = request.getParameter("mdp");

	        request.setAttribute("resultat", mail);
	        request.setAttribute("motdepasse", mdp); 
	        System.out.println("Email saisi : " + mail);
	        System.out.println("Mot de passe saisi : " + mdp);

	        User u1 = new User(mail, mdp);
	        if (u1.verif()) {
	        	request.setAttribute("produits", dao.getAllProduits());
	            request.getRequestDispatcher("vue.jsp").forward(request, response);
	        } else {
	            request.getRequestDispatcher("login.html").forward(request, response);
	        }
	    }

	    else if ("addProduit".equals(action)) {
	        String nomp = request.getParameter("nomp");
	        String prixStr = request.getParameter("prix");

	        double prix = Double.parseDouble(prixStr);
	        Produit produit = new Produit(nomp, prix);
	        dao.save(produit);
	        response.sendRedirect("index?action=showList");
	        /*List<Produit> produits = dao.getAllProduits();
	        request.setAttribute("produits", produits); 
	        request.setAttribute("message", "Produit ajouté avec succès !");
	        // Tu peux remettre les infos de l'utilisateur si besoin
	        request.setAttribute("resultat", "user");
	        request.setAttribute("motdepasse", "123");
	        request.getRequestDispatcher("vue.jsp").forward(request, response);*/
	    }
	    //-------------------------------
	    else if ("showAddForm".equals(action)) {
	        // Affiche le formulaire d'ajout sans ajouter
	        request.setAttribute("resultat", "user");
	        request.setAttribute("motdepasse", "123");
	        request.getRequestDispatcher("vue.jsp").forward(request, response);
	    } else if ("showList".equals(action)) {
	        request.setAttribute("produits", dao.getAllProduits());
	        request.setAttribute("resultat", "user");
	        request.setAttribute("motdepasse", "123");
	        request.getRequestDispatcher("vue.jsp").forward(request, response);
	    }
	    else if ("rechercher".equals(action)){
	    String motCle = request.getParameter("motcle");
	    List<Produit> produits = dao.produitsParMC(motCle);
	    request.setAttribute("produits", produits);
	    request.setAttribute("resultat", "user");
	    request.setAttribute("motdepasse", "123");
	    request.getRequestDispatcher("vue.jsp").forward(request, response);
	    }
	    else if ("details".equals(request.getParameter("action"))) {
	    	String idstr = request.getParameter("id");
	    	Long id = Long.parseLong(idstr);
	    	Produit p= dao.getProduit(id);
	    	request.setAttribute("produit", p);
	    	request.getRequestDispatcher("details.jsp").forward(request, response);
	    	
	    }
	    else if ("deleteProduit".equals(action)) {
	        String idStr = request.getParameter("id");
	        Long id = Long.parseLong(idStr);

	        // Appel de la méthode pour supprimer le produit
	        dao.deleteProduit(id);

	        // Redirection vers la liste des produits après suppression
	        request.setAttribute("produits", dao.getAllProduits());
	        request.setAttribute("message", "Produit supprimé avec succès !");
	        request.setAttribute("resultat", "user");
	        request.setAttribute("motdepasse", "123");
	        request.getRequestDispatcher("vue.jsp").forward(request, response);
	    }
	    else if ("edit".equals(action)) {
	        // Récupérer l’ID du produit à modifier
	        Long id = Long.parseLong(request.getParameter("id"));
	        Produit p = dao.getProduit(id); // récupérer depuis la base

	        if (p != null) {
	            request.setAttribute("produit", p);
	            request.getRequestDispatcher("update.jsp").forward(request, response);
	        } else {
	            // produit introuvable, retour à la liste
	            response.sendRedirect("index?action=showList");
	        }
	    }
	    else if ("updateProduit".equals(action)) {
	        String idStr = request.getParameter("id");
	        String nomp = request.getParameter("nomp");
	        String prixStr = request.getParameter("prix");

	        if (idStr != null && nomp != null && prixStr != null) {
	            try {
	                Long id = Long.parseLong(idStr);
	                double prix = Double.parseDouble(prixStr);

	                Produit p = new Produit();
	                p.setIdProduit(id);
	                p.setNomProduit(nomp);
	                p.setPrix(prix);

	                Produit updated = dao.updateProduit(p);

	                if (updated != null) {
	                    request.setAttribute("message", "Produit mis à jour avec succès !");
	                } else {
	                    request.setAttribute("message", "Erreur : produit introuvable.");
	                }

	                request.setAttribute("produits", dao.getAllProduits());
	                request.setAttribute("resultat", "user");
	                request.setAttribute("motdepasse", "123");
	                request.getRequestDispatcher("vue.jsp").forward(request, response);

	            } catch (NumberFormatException e) {
	                request.setAttribute("message", "Erreur : format invalide.");
	                request.getRequestDispatcher("vue.jsp").forward(request, response);
	            }
	        } else {
	            request.setAttribute("message", "Champs manquants.");
	            request.getRequestDispatcher("vue.jsp").forward(request, response);
	        }
	    }



	    	
	}


}
