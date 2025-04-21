<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="metier.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modifier un utilisateur</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow">
        <div class="card-header bg-warning text-white">
            <h4><i class="bi bi-person-gear me-2"></i>Modifier l'utilisateur</h4>
        </div>
        <div class="card-body">
            <%
                User user = (User) request.getAttribute("user");
                if (user != null) {
            %>
            <form action="index" method="post">
                <input type="hidden" name="action" value="updateUser">
                <input type="hidden" name="login" value="<%= user.getLogin() %>">

                <div class="mb-3">
                    <label for="login" class="form-label">Nom d'utilisateur</label>
                    <input type="text" class="form-control" id="login" value="<%= user.getLogin() %>" disabled>
                    <div class="form-text text-muted">Le nom d'utilisateur ne peut pas être modifié.</div>
                </div>
                <div class="mb-3">
                    <label for="mdp" class="form-label">Nouveau mot de passe</label>
                    <input type="password" class="form-control" name="mdp" id="mdp" required>
                </div>

                <button type="submit" class="btn btn-success">
                    <i class="bi bi-check-circle me-1"></i>Mettre à jour
                </button>
                <a href="index?action=showUserList" class="btn btn-secondary">
                    <i class="bi bi-x-circle me-1"></i>Annuler
                </a>
            </form>
            <%
                } else {
            %>
            <div class="alert alert-danger">Utilisateur non trouvé.</div>
            <%
                }
            %>
        </div>
    </div>
</div>
</body>
</html>
