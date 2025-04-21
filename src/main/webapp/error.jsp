<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Erreur</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            padding: 50px;
        }
        .error-container {
            max-width: 600px;
            margin: 0 auto;
        }
        .error-icon {
            font-size: 5rem;
            color: #dc3545;
        }
    </style>
</head>
<body>
    <div class="container error-container text-center">
        <div class="mb-4">
            <i class="bi bi-exclamation-triangle-fill error-icon"></i>
        </div>
        <div class="card shadow">
            <div class="card-header bg-danger text-white">
                <h3>Erreur</h3>
            </div>
            <div class="card-body">
                <p class="lead">Une erreur s'est produite pendant le traitement de votre demande.</p>
                
                <% if(request.getAttribute("errorMessage") != null) { %>
                    <div class="alert alert-danger">
                        <%= request.getAttribute("errorMessage") %>
                    </div>
                <% } %>
                
                <div class="mt-4">
                    <a href="index?action=showList" class="btn btn-primary">
                        <i class="bi bi-house-fill me-2"></i>Retour à l'accueil
                    </a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>