<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <title>Inscription</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
  <style>
    body {
      background: linear-gradient(to right, #6610f2, #0d6efd);
      height: 100vh;
    }
    .card {
      animation: fadeIn 0.7s ease;
    }
    @keyframes fadeIn {
      from { opacity: 0; transform: translateY(30px); }
      to { opacity: 1; transform: translateY(0); }
    }
  </style>
</head>
<body>
  <nav class="navbar navbar-dark bg-dark">
    <div class="container-fluid">
      <a class="navbar-brand fw-bold" href="#">MiniProjet</a>
    </div>
  </nav>

  <div class="container d-flex justify-content-center align-items-center h-100">
    <div class="col-md-5">
      <div class="card shadow-lg">
        <div class="card-header bg-primary text-white text-center">
          <h4><i class="bi bi-person-plus-fill me-2"></i>Inscription</h4>
        </div>
        <div class="card-body bg-light">
          <% if(request.getAttribute("errorMessage") != null) { %>
            <div class="alert alert-danger">
              <%= request.getAttribute("errorMessage") %>
            </div>
          <% } %>
          
          <form action="index" method="post">
            <input type="hidden" name="action" value="register">
            <div class="mb-3">
              <label class="form-label">Nom d'utilisateur</label>
              <div class="input-group">
                <span class="input-group-text"><i class="bi bi-person-fill"></i></span>
                <input type="text" name="login" class="form-control" required>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label">Mot de passe</label>
              <div class="input-group">
                <span class="input-group-text"><i class="bi bi-shield-lock-fill"></i></span>
                <input type="password" name="mdp" class="form-control" required>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label">Confirmer le mot de passe</label>
              <div class="input-group">
                <span class="input-group-text"><i class="bi bi-shield-check"></i></span>
                <input type="password" name="confirm_mdp" class="form-control" required>
              </div>
            </div>
            <div class="d-grid">
              <button class="btn btn-success" type="submit">
                <i class="bi bi-person-plus"></i> S'inscrire
              </button>
            </div>
          </form>
          
          <div class="mt-3 text-center">
            <p>Déjà un compte ? 
              <a href="login.jsp" class="text-decoration-none">Se connecter</a>
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>