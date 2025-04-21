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

@WebServlet(name="cs",urlPatterns={"/index"})
public class controller extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    private ProduitDaoImpl produitDao;
    private UserDaoImpl userDao;
    
    @Override
    public void init() throws ServletException {
        produitDao = new ProduitDaoImpl();
        userDao = new UserDaoImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response); // redirige les requêtes GET vers doPost()
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        
        // Default to showList if no action specified
        if (action == null) {
            action = "showList";
        }

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

        try {
            switch(action) {
                case "login":
                    processLogin(request, response);
                    break;
                case "showRegisterForm":
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                    break;
                case "register":
                    processRegister(request, response);
                    break;
                case "addProduit":
                    processAddProduit(request, response);
                    break;
                case "showAddForm":
                    request.getRequestDispatcher("vue.jsp").forward(request, response);
                    break;
                case "showList":
                    request.setAttribute("produits", produitDao.getAllProduits());
                    request.getRequestDispatcher("vue.jsp").forward(request, response);
                    break;
                case "rechercher":
                    processSearchProduit(request, response);
                    break;
                case "details":
                    processDetailsProduit(request, response);
                    break;
                case "deleteProduit":
                    processDeleteProduit(request, response);
                    break;
                case "edit":
                    processEditProduit(request, response);
                    break;
                case "updateProduit":
                    processUpdateProduit(request, response);
                    break;
                case "showUserList":
                    request.setAttribute("users", userDao.getAllUsers());
                    request.getRequestDispatcher("userList.jsp").forward(request, response);
                    break;
                case "deleteUser":
                    processDeleteUser(request, response);
                    break;
                case "editUser":
                    processEditUser(request, response);
                    break;
                case "updateUser":
                    processUpdateUser(request, response);
                    break;
                default:
                    request.setAttribute("produits", produitDao.getAllProduits());
                    request.getRequestDispatcher("vue.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Une erreur s'est produite : " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    private void processLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("mdp");
        
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            request.setAttribute("errorMessage", "Veuillez remplir tous les champs");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        User user = userDao.authenticate(login, password);
        
        if (user != null) {
            request.getSession().setAttribute("user", user);
            request.setAttribute("produits", produitDao.getAllProduits());
            request.getRequestDispatcher("vue.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Login ou mot de passe incorrect");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    
    private void processRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("mdp");
        String confirmPassword = request.getParameter("confirm_mdp");
        
        if (login == null || password == null || confirmPassword == null || 
            login.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("errorMessage", "Veuillez remplir tous les champs");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
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
    
    private void processAddProduit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nomp = request.getParameter("nomp");
        String prixStr = request.getParameter("prix");
        
        try {
            double prix = Double.parseDouble(prixStr);
            Produit produit = new Produit(nomp, prix);
            produitDao.save(produit);
            response.sendRedirect("index?action=showList");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Format de prix invalide");
            request.getRequestDispatcher("vue.jsp").forward(request, response);
        }
    }
    
    private void processSearchProduit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String motCle = request.getParameter("motcle");
        if (motCle != null && !motCle.isEmpty()) {
            List<Produit> produits = produitDao.produitsParMC(motCle);
            request.setAttribute("produits", produits);
            request.getRequestDispatcher("vue.jsp").forward(request, response);
        } else {
            response.sendRedirect("index?action=showList");
        }
    }
    
    private void processDetailsProduit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idstr = request.getParameter("id");
        try {
            Long id = Long.parseLong(idstr);
            Produit p = produitDao.getProduit(id);
            request.setAttribute("produit", p);
            request.getRequestDispatcher("details.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("index?action=showList");
        }
    }
    
    private void processDeleteProduit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        try {
            Long id = Long.parseLong(idStr);
            produitDao.deleteProduit(id);
            request.setAttribute("produits", produitDao.getAllProduits());
            request.setAttribute("message", "Produit supprimé avec succès !");
            request.getRequestDispatcher("vue.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("index?action=showList");
        }
    }
    
    private void processEditProduit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        try {
            Long id = Long.parseLong(idStr);
            Produit p = produitDao.getProduit(id);
            if (p != null) {
                request.setAttribute("produit", p);
                request.getRequestDispatcher("update.jsp").forward(request, response);
            } else {
                response.sendRedirect("index?action=showList");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("index?action=showList");
        }
    }
    
    private void processUpdateProduit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    
    private void processDeleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        if (login != null && !login.isEmpty()) {
            userDao.deleteUser(login);
            request.setAttribute("users", userDao.getAllUsers());
            request.setAttribute("message", "Utilisateur supprimé avec succès !");
            request.getRequestDispatcher("userList.jsp").forward(request, response);
        } else {
            response.sendRedirect("index?action=showUserList");
        }
    }
    
    private void processEditUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        if (login != null && !login.isEmpty()) {
            User user = userDao.getUser(login);
            if (user != null) {
                request.setAttribute("user", user);
                request.getRequestDispatcher("updateUser.jsp").forward(request, response);
            } else {
                response.sendRedirect("index?action=showUserList");
            }
        } else {
            response.sendRedirect("index?action=showUserList");
        }
    }
    
    private void processUpdateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("mdp");
        
        if (login != null && password != null && !login.isEmpty() && !password.isEmpty()) {
            User user = new User(login, password);
            User updated = userDao.updateUser(user);
            
            if (updated != null) {
                request.setAttribute("message", "Utilisateur mis à jour avec succès !");
            } else {
                request.setAttribute("message", "Erreur : utilisateur introuvable.");
            }
            
            request.setAttribute("users", userDao.getAllUsers());
            request.getRequestDispatcher("userList.jsp").forward(request, response);
        } else {
            request.setAttribute("message", "Champs manquants.");
            request.getRequestDispatcher("userList.jsp").forward(request, response);
        }
    }
}