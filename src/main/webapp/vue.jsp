<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="metier.Produit" %>
<%@ page import="metier.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>MiniProjet - Produits</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .navbar {
            background: linear-gradient(90deg, #007bff, #6610f2);
        }
        .navbar-brand {
            font-weight: bold;
            font-size: 1.4rem;
        }
        .card {
            border-radius: 12px;
        }
        .table th {
            background-color: #343a40;
            color: #fff;
        }
        .btn {
            border-radius: 30px;
        }
        .btn-success, .btn-outline-dark {
            transition: all 0.3s ease-in-out;
        }
        .btn-success:hover {
            background-color: #28a745;
            transform: scale(1.05);
        }
        .form-control:focus {
            box-shadow: 0 0 10px rgba(0, 123, 255, 0.25);
        }
    </style>
</head>
<body>

<!-- NAVBAR -->
<nav class="navbar navbar-expand-lg navbar-dark shadow">
  <div class="container-fluid">
    <a class="navbar-brand" href="#"><i class="bi bi-box-seam me-2"></i>MiniProjet</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
        data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false"
        aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
      <ul class="navbar-nav gap-2">
        <li class="nav-item">
          <form action="index" method="post" class="d-inline">
            <input type="hidden" name="action" value="showAddForm">
            <button class="nav-link btn btn-outline-light" type="submit">
              <i class="bi bi-plus-circle me-1"></i>Ajouter
            </button>
          </form>
        </li>
        <li class="nav-item">
          <form action="index" method="post" class="d-inline">
            <input type="hidden" name="action" value="showList">
            <button class="nav-link btn btn-outline-light" type="submit">
              <i class="bi bi-list-ul me-1"></i>Liste
            </button>
          </form>
        </li>
        <li class="nav-item">
          <form action="index" method="post" class="d-inline">
            <input type="hidden" name="action" value="showUserList">
            <button class="nav-link btn btn-outline-light" type="submit">
              <i class="bi bi-people-fill me-1"></i>Utilisateurs
            </button>
          </form>
        </li>
        <li class="nav-item">
          <form action="index" method="post" class="d-inline">
            <input type="hidden" name="action" value="logout">
            <button class="nav-link btn btn-danger text-white" type="submit">
              <i class="bi bi-box-arrow-right me-1"></i>Déconnexion
            </button>
          </form>
        </li>
      </ul>
    </div>
  </div>
</nav>

<!-- CONTENU -->
<div class="container py-5">

    <%-- Message --%>
    <% String msg = (String) request.getAttribute("message");
       if (msg != null) { %>
        <div class="alert alert-success text-center shadow-sm">
            <%= msg %>
        </div>
    <% } %>

    <%-- Formulaire Ajout --%>
    <% if (request.getAttribute("produits") == null) { %>
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white text-center">
                        <h4 class="mb-0"><i class="bi bi-plus-circle me-2"></i>Ajouter un produit</h4>
                    </div>
                    <div class="card-body">
                        <form action="index" method="post">
                            <input type="hidden" name="action" value="addProduit">
                            <div class="mb-3">
                                <label for="nomp" class="form-label">Nom du produit</label>
                                <input type="text" name="nomp" id="nomp" class="form-control" required autofocus>
                            </div>
                            <div class="mb-3">
                                <label for="prix" class="form-label">Prix (DT)</label>
                                <input type="number" step="0.01" name="prix" id="prix" class="form-control" required>
                            </div>
                            <div class="text-center">
                                <button type="submit" class="btn btn-success px-4">
                                    <i class="bi bi-check-circle me-1"></i>Ajouter
                                </button>
                                <a href="index?action=showList" class="btn btn-secondary">
                                    <i class="bi bi-x-circle me-1"></i>Annuler
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    <% } %>

    <%-- Liste Produits --%>
    <%
        List<Produit> produits = (List<Produit>) request.getAttribute("produits");
        if (produits != null && !produits.isEmpty()) {
    %>
    <div class="card shadow">
        <div class="card-header bg-primary text-white">
            <h4 class="mb-0"><i class="bi bi-list-ul me-2"></i>Liste des Produits</h4>
        </div>
        <div class="card-body">
            <!-- Recherche -->
            <form class="d-flex mb-3" action="index" method="post">
                <input class="form-control me-2" type="search" name="motcle" placeholder="Rechercher un produit..." aria-label="Rechercher" required>
                <input type="hidden" name="action" value="rechercher">
                <button class="btn btn-outline-dark" type="submit">
                    <i class="bi bi-search me-1"></i>Rechercher
                </button>
            </form>

            <div class="table-responsive">
                <table class="table table-hover align-middle shadow-sm">
                    <thead class="table-dark text-center">
                        <tr>
                            <th>ID</th>
                            <th>Nom du produit</th>
                            <th>Prix (DT)</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody class="text-center">
                        <% for (Produit p : produits) { %>
                        <tr>
                            <td><%= p.getIdProduit() %></td>
                            <td><%= p.getNomProduit() %></td>
                            <td><%= p.getPrix() %></td>
                            <td>
                                <form action="index" method="post" class="d-inline">
                                    <input type="hidden" name="action" value="details">
                                    <input type="hidden" name="id" value="<%= p.getIdProduit() %>">
                                    <button class="btn btn-info btn-sm">
                                        <i class="bi bi-info-circle me-1"></i>Détails
                                    </button>
                                </form>

                                <form action="index" method="post" class="d-inline">
                                    <input type="hidden" name="action" value="edit">
                                    <input type="hidden" name="id" value="<%= p.getIdProduit() %>">
                                    <button class="btn btn-warning btn-sm">
                                        <i class="bi bi-pencil-fill me-1"></i>Modifier
                                    </button>
                                </form>

                                <form action="index" method="post" class="d-inline">
                                    <input type="hidden" name="action" value="deleteProduit">
                                    <input type="hidden" name="id" value="<%= p.getIdProduit() %>">
                                    <button class="btn btn-danger btn-sm" onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce produit?')">
                                        <i class="bi bi-trash-fill me-1"></i>Supprimer
                                    </button>
                                </form>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <% } else if (produits != null) { %>
        <div class="alert alert-info text-center">
            Aucun produit trouvé.
        </div>
    <% } %>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>