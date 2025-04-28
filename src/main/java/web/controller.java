package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import metier.Produit;
import metier.User;
import dao.ProduitDaoImpl;
import dao.UserDaoImpl;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ControllerServlet", urlPatterns = { "/index" })
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response); // Redirige GET vers POST
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		HttpSession session = request.getSession();

		if (action == null)
			action = "showList"; // Action par défaut

		// Gestion de la déconnexion
		if ("logout".equals(action)) {
			session.invalidate();
			response.sendRedirect("login.html");
			return;
		}

		// Vérification de connexion sauf pour login et register
		if (!isPublicAction(action) && session.getAttribute("user") == null) {
			response.sendRedirect("login.html");
			return;
		}

		try {
			switch (action) {
			case "login":
				processLogin(request, response);
				break;
			case "showRegisterForm":
				forward(request, response, "register.jsp");
				break;
			case "register":
				processRegister(request, response);
				break;
			case "addProduit":
				processAddProduit(request, response);
				break;
			case "showAddForm":
				forward(request, response, "vue.jsp");
				break;
			case "showList":
				showProduitList(request, response);
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
				showUserList(request, response);
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
				showProduitList(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Erreur : " + e.getMessage());
			forward(request, response, "error.jsp");
		}
	}

	private boolean isPublicAction(String action) {
		return "login".equals(action) || "register".equals(action) || "showRegisterForm".equals(action);
	}

	private void forward(HttpServletRequest request, HttpServletResponse response, String page)
			throws ServletException, IOException {
		request.getRequestDispatcher(page).forward(request, response);
	}

	// Gestion de l'authentification
	private void processLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String login = request.getParameter("login");
		String password = request.getParameter("mdp");

		if (isNullOrEmpty(login) || isNullOrEmpty(password)) {
			request.setAttribute("errorMessage", "Veuillez remplir tous les champs");
			forward(request, response, "login.jsp");
			return;
		}

		User user = userDao.authenticate(login, password);

		if (user != null) {
			request.getSession().setAttribute("user", user);
			showProduitList(request, response);
		} else {
			request.setAttribute("errorMessage", "Login ou mot de passe incorrect");
			forward(request, response, "login.jsp");
		}
	}

	// Gestion de l'inscription
	private void processRegister(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String login = request.getParameter("login");
		String password = request.getParameter("mdp");
		String confirmPassword = request.getParameter("confirm_mdp");

		if (isNullOrEmpty(login) || isNullOrEmpty(password) || isNullOrEmpty(confirmPassword)) {
			request.setAttribute("errorMessage", "Veuillez remplir tous les champs");
			forward(request, response, "register.jsp");
			return;
		}

		if (!password.equals(confirmPassword)) {
			request.setAttribute("errorMessage", "Les mots de passe ne correspondent pas");
			forward(request, response, "register.jsp");
			return;
		}

		if (userDao.getUser(login) != null) {
			request.setAttribute("errorMessage", "Cet utilisateur existe déjà");
			forward(request, response, "register.jsp");
			return;
		}

		User newUser = new User(login, password, "user");
		if (userDao.save(newUser) != null) {
			request.setAttribute("successMessage", "Inscription réussie. Veuillez vous connecter.");
			forward(request, response, "login.jsp");
		} else {
			request.setAttribute("errorMessage", "Erreur lors de l'inscription");
			forward(request, response, "register.jsp");
		}
	}

	private void showProduitList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("produits", produitDao.getAllProduits());
		forward(request, response, "vue.jsp");
	}

	private void processAddProduit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String nomp = request.getParameter("nomp");
		String prixStr = request.getParameter("prix");

		try {
			double prix = Double.parseDouble(prixStr);
			produitDao.save(new Produit(nomp, prix));
			response.sendRedirect("index?action=showList");
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Format de prix invalide");
			forward(request, response, "vue.jsp");
		}
	}

	private void processSearchProduit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String motCle = request.getParameter("motcle");

		if (!isNullOrEmpty(motCle)) {
			List<Produit> produits = produitDao.produitsParMC(motCle);
			request.setAttribute("produits", produits);
			forward(request, response, "vue.jsp");
		} else {
			response.sendRedirect("index?action=showList");
		}
	}

	private void processDetailsProduit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Long id = Long.parseLong(request.getParameter("id"));
			Produit produit = produitDao.getProduit(id);
			request.setAttribute("produit", produit);
			forward(request, response, "details.jsp");
		} catch (NumberFormatException e) {
			response.sendRedirect("index?action=showList");
		}
	}

	private void processDeleteProduit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User currentUser = (User) request.getSession().getAttribute("user");

		if (!isAdmin(currentUser)) {
			request.setAttribute("errorMessage", "Seul un administrateur peut supprimer un produit.");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}

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

	private void processEditProduit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User currentUser = (User) request.getSession().getAttribute("user");

		if (!isAdmin(currentUser)) {
			request.setAttribute("errorMessage", "Seul un administrateur peut modifier un produit.");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}

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

	private void processUpdateProduit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User currentUser = (User) request.getSession().getAttribute("user");

		if (!isAdmin(currentUser)) {
			request.setAttribute("errorMessage", "Seul un administrateur peut mettre à jour un produit.");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}

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

	private void showUserList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("users", userDao.getAllUsers());
		forward(request, response, "userList.jsp");
	}

	private void processDeleteUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String login = request.getParameter("login");
		User currentUser = (User) request.getSession().getAttribute("user");

		if (login != null && !login.isEmpty()) {
			// Authorization check
			if (!isAdmin(currentUser) && !currentUser.getLogin().equals(login)) {
				request.setAttribute("errorMessage", "Vous n'êtes pas autorisé à supprimer cet utilisateur.");
				request.getRequestDispatcher("error.jsp").forward(request, response);
				return;
			}

			userDao.deleteUser(login);
			request.setAttribute("users", userDao.getAllUsers());
			request.setAttribute("message", "Utilisateur supprimé avec succès !");
			request.getRequestDispatcher("userList.jsp").forward(request, response);
		} else {
			response.sendRedirect("index?action=showUserList");
		}
	}

	private boolean isAdmin(User user) {
		return "admin".equalsIgnoreCase(user.getLogin());
	}

	private void processEditUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String login = request.getParameter("login");
		User currentUser = (User) request.getSession().getAttribute("user");

		if (login != null && !login.isEmpty()) {
			// Authorization check
			if (!isAdmin(currentUser) && !currentUser.getLogin().equals(login)) {
				request.setAttribute("errorMessage", "Vous n'êtes pas autorisé à modifier cet utilisateur.");
				request.getRequestDispatcher("error.jsp").forward(request, response);
				return;
			}

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

	private void processUpdateUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String login = request.getParameter("login");
		String password = request.getParameter("mdp");
		User currentUser = (User) request.getSession().getAttribute("user");

		if (login != null && password != null && !login.isEmpty() && !password.isEmpty()) {
			// Authorization check
			if (!isAdmin(currentUser) && !currentUser.getLogin().equals(login)) {
				request.setAttribute("errorMessage", "Vous n'êtes pas autorisé à mettre à jour cet utilisateur.");
				request.getRequestDispatcher("error.jsp").forward(request, response);
				return;
			}

			User user = new User(login, password, "user");
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

	private boolean isNullOrEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}
}
