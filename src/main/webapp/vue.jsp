<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="metier.Produit" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>MiniProjet - Produits</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
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

<!-- 🔹 NAVBAR -->
<nav class="navbar navbar-expand-lg navbar-dark shadow">
  <div class="container-fluid">
    <a class="navbar-brand" href="#">🛍️ MiniProjet</a>
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
            <button class="nav-link btn btn-outline-light" type="submit">➕ Ajouter</button>
          </form>
        </li>
        <li class="nav-item">
          <form action="index" method="post" class="d-inline">
            <input type="hidden" name="action" value="showList">
            <button class="nav-link btn btn-outline-light" type="submit">📋 Liste</button>
          </form>
        </li>
        <li class="nav-item">
          <a class="nav-link btn btn-danger text-white" href="login.html">🚪 Déconnexion</a>
        </li>
      </ul>
    </div>
  </div>
</nav>

<!-- 🔹 CONTENU -->
<%
    String res = (String) request.getAttribute("resultat");
    String mdp = (String) request.getAttribute("motdepasse");
    if ("user".equals(res) && "123".equals(mdp)) {
%>

<div class="container py-5">

    <%-- 🔔 Message --%>
    <% String msg = (String) request.getAttribute("message");
       if (msg != null) { %>
        <div class="alert alert-success text-center shadow-sm">
            <%= msg %>
        </div>
    <% } %>

    <%-- 🔹 Formulaire Ajout --%>
    <% if (request.getAttribute("produits") == null) { %>
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white text-center">
                        <h4 class="mb-0">📝 Ajouter un produit</h4>
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
                                <button type="submit" class="btn btn-success px-4">✅ Ajouter</button>
                                <a href="index?action=showList" class="btn btn-secondary">✅Annuler</a>
                            </div>
                            
                        </form>
                    </div>
                </div>
            </div>
        </div>
    <% } %>

    <%-- 🔹 Liste Produits --%>
    <%
        List<Produit> produits = (List<Produit>) request.getAttribute("produits");
        if (produits != null && !produits.isEmpty()) {
    %>

    <!-- 🔍 Recherche -->
    <form class="d-flex mt-4 mb-3" action="index" method="post">
        <input class="form-control me-2" type="search" name="motcle" placeholder="🔍 Rechercher un produit..." aria-label="Rechercher" required>
        <input type="hidden" name="action" value="rechercher">
        <button class="btn btn-outline-dark" type="submit">Rechercher</button>
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
                            <button class="btn btn-info btn-sm">Détails</button>
                        </form>

                        <form action="index" method="post" class="d-inline">
                            <input type="hidden" name="action" value="deleteProduit">
                            <input type="hidden" name="id" value="<%= p.getIdProduit() %>">
                            <button class="btn btn-danger btn-sm">Supprimer</button>
                        </form>

                        <form action="index" method="post" class="d-inline">
                            <input type="hidden" name="action" value="edit">
                            <input type="hidden" name="id" value="<%= p.getIdProduit() %>">
                            <button class="btn btn-warning btn-sm">Modifier</button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
    <% } %>

</div>

<% } else { %>
<div class="container mt-5">
    <div class="alert alert-danger text-center shadow-sm">
        ❌ Accès refusé. Veuillez vous connecter.
    </div>
    <div class="text-center">
        <a href="login.html" class="btn btn-secondary">Retour à la connexion</a>
    </div>
</div>
<% } %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
