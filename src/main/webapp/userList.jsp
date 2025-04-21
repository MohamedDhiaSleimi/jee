<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="metier.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des Utilisateurs</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .navbar {
            background: linear-gradient(90deg, #007bff, #6610f2);
        }
        .card {
            border-radius: 12px;
        }
        .table th {
            background-color: #343a40;
            color: #fff;
        }
    </style>
</head>
<body>

<!-- NAVBAR -->
<nav class="navbar navbar-expand-lg navbar-dark shadow">
  <div class="container-fluid">
    <a class="navbar-brand" href="#"><i class="bi bi-people-fill me-2"></i>Gestion des Utilisateurs</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
        data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false"
        aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
      <ul class="navbar-nav gap-2">
        <li class="nav-item">
          <form action="index" method="post" class="d-inline">
            <input type="hidden" name="action" value="showList">
            <button class="nav-link btn btn-outline-light" type="submit">
              <i class="bi bi-box-seam me-1"></i>Produits
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

    <%-- Liste Utilisateurs --%>
    <%
        List<User> users = (List<User>) request.getAttribute("users");
        if (users != null && !users.isEmpty()) {
    %>
    <div class="card shadow">
        <div class="card-header bg-primary text-white">
            <h4 class="mb-0"><i class="bi bi-people-fill me-2"></i>Liste des Utilisateurs</h4>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover align-middle shadow-sm">
                    <thead class="table-dark text-center">
                        <tr>
                            <th>Nom d'utilisateur</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody class="text-center">
                        <% for (User user : users) { %>
                        <tr>
                            <td><%= user.getLogin() %></td>
                            <td>
                                <form action="index" method="post" class="d-inline">
                                    <input type="hidden" name="action" value="editUser">
                                    <input type="hidden" name="login" value="<%= user.getLogin() %>">
                                    <button class="btn btn-warning btn-sm">
                                        <i class="bi bi-pencil-fill me-1"></i>Modifier
                                    </button>
                                </form>

                                <form action="index" method="post" class="d-inline">
                                    <input type="hidden" name="action" value="deleteUser">
                                    <input type="hidden" name="login" value="<%= user.getLogin() %>">
                                    <button class="btn btn-danger btn-sm" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur?')">
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
    <% } else if (users != null) { %>
        <div class="alert alert-info text-center">
            Aucun utilisateur trouvé.
        </div>
    <% } %>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>