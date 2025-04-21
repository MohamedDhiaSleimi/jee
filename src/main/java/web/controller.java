package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import metier.Produit;
import metier.User;

import java.io.IOException;
import java.util.List;

import dao.ProduitDaoImpl;
import dao.UserDaoImpl;

/**
 * Servlet implementation class controller
 */
@WebServlet(name="cs",urlPatterns={"/index"})
public class controller extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    ProduitDaoImpl produitDao = new ProduitDaoImpl();
    UserDaoImpl userDao = new UserDaoImpl();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response); // redirige les requêtes GET vers doPost()
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        // Action de déconnexion
        if ("logout".equals(action)) {
            session.invalidate();
            response.sendRedirect("login.html");
            return;
        }

        // Vérifier si l'utilisateur est connecté (sauf pour login et register)
        if (!"login".equals(action) && !"register".equals(action) && !"showRegisterForm".equals(action)) {
            if (session.getAttribute("user") == null) {
                response.sendRedirect("login.html");
                return;
            }
        }

        if ("login".equals(action)) {
            String login = request.getParameter("login");
            String password = request.getParameter("mdp");

            User user = userDao.authenticate(login, password);
            
            if (user != null) {
                session.setAttribute("user", user);
                request.setAttribute("produits", produitDao.getAllProduits());
                request.getRequestDispatcher("vue.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Login ou mot de passe incorrect");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        }
        else if ("showRegisterForm".equals(action)) {
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
        else if ("register".equals(action)) {
            String login = request.getParameter("login");
            String password = request.getParameter("mdp");
            String confirmPassword = request.getParameter("confirm_mdp");
            
            // Vérifier si les mots de passe correspondent
            if (!password.equals(confirmPassword)) {
                request.setAttribute("errorMessage", "Les mots de passe ne correspondent pas");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            
            // Vérifier si l'utilisateur existe déjà
            if (userDao.getUser(login) != null) {
                request.setAttribute("errorMessage", "Cet utilisateur existe déjà");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            
            User newUser = new User(login, password);
            User savedUser = userDao.save(newUser);
            
            if (savedUser != null) {
                request.setAttribute("successMessage", "Inscription réussie. Veuillez vous connecter.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Erreur lors de l'inscription");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        }
        else if ("addProduit".equals(action)) {
            String nomp = request.getParameter("nomp");
            String prixStr = request.getParameter("prix");

            double prix = Double.parseDouble(prixStr);
            Produit produit = new Produit(nomp, prix);
            produitDao.save(produit);
            response.sendRedirect("index?action=showList");
        }
        else if ("showAddForm".equals(action)) {
            request.getRequestDispatcher("vue.jsp").forward(request, response);
        } 
        else if ("showList".equals(action)) {
            request.setAttribute("produits", produitDao.getAllProduits());
            request.getRequestDispatcher("vue.jsp").forward(request, response);
        }
        else if ("rechercher".equals(action)){
            String motCle = request.getParameter("motcle");
            List<Produit> produits = produitDao.produitsParMC(motCle);
            request.setAttribute("produits", produits);
            request.getRequestDispatcher("vue.jsp").forward(request, response);
        }
        else if ("details".equals(action)) {
            String idstr = request.getParameter("id");
            Long id = Long.parseLong(idstr);
            Produit p = produitDao.getProduit(id);
            request.setAttribute("produit", p);
            request.getRequestDispatcher("details.jsp").forward(request, response);
        }
        else if ("deleteProduit".equals(action)) {
            String idStr = request.getParameter("id");
            Long id = Long.parseLong(idStr);

            produitDao.deleteProduit(id);

            request.setAttribute("produits", produitDao.getAllProduits());
            request.setAttribute("message", "Produit supprimé avec succès !");
            request.getRequestDispatcher("vue.jsp").forward(request, response);
        }
        else if ("edit".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            Produit p = produitDao.getProduit(id);

            if (p != null) {
                request.setAttribute("produit", p);
                request.getRequestDispatcher("update.jsp").forward(request, response);
            } else {
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

                    Produit updated = produitDao.updateProduit(p);

                    if (updated != null) {
                        request.setAttribute("message", "Produit mis à jour avec succès !");
                    } else {
                        request.setAttribute("message", "Erreur : produit introuvable.");
                    }

                    request.setAttribute("produits", produitDao.getAllProduits());
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
        // User management actions
        else if ("showUserList".equals(action)) {
            request.setAttribute("users", userDao.getAllUsers());
            request.getRequestDispatcher("userList.jsp").forward(request, response);
        }
        else if ("deleteUser".equals(action)) {
            String login = request.getParameter("login");
            userDao.deleteUser(login);
            
            request.setAttribute("users", userDao.getAllUsers());
            request.setAttribute("message", "Utilisateur supprimé avec succès !");
            request.getRequestDispatcher("userList.jsp").forward(request, response);
        }
        else if ("editUser".equals(action)) {
            String login = request.getParameter("login");
            User user = userDao.getUser(login);
            
            if (user != null) {
                request.setAttribute("user", user);
                request.getRequestDispatcher("updateUser.jsp").forward(request, response);
            } else {
                response.sendRedirect("index?action=showUserList");
            }
        }
        else if ("updateUser".equals(action)) {
            String login = request.getParameter("login");
            String password = request.getParameter("mdp");
            
            User user = new User(login, password);
            User updated = userDao.updateUser(user);
            
            if (updated != null) {
                request.setAttribute("message", "Utilisateur mis à jour avec succès !");
            } else {
                request.setAttribute("message", "Erreur : utilisateur introuvable.");
            }
            
            request.setAttribute("users", userDao.getAllUsers());
            request.getRequestDispatcher("userList.jsp").forward(request, response);
        }
        else {
            // Default action - redirect to product list
            request.setAttribute("produits", produitDao.getAllProduits());
            request.getRequestDispatcher("vue.jsp").forward(request, response);
        }
    }
}