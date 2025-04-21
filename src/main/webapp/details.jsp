<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="metier.Produit" %>
<%
    Produit p = (Produit) request.getAttribute("produit");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Détails du Produit</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background: #f8f9fa;
        }
        .card {
            animation: fadeIn 0.5s ease-in;
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <h2 class="text-primary mb-4"><i class="bi bi-info-circle-fill me-2"></i>Détails du Produit</h2>
    <% if (p != null) { %>
        <div class="card shadow">
            <div class="card-body">
                <h5 class="card-title"><i class="bi bi-box-seam me-2"></i><%= p.getNomProduit() %></h5>
                <p class="card-text"><strong>ID :</strong> <%= p.getIdProduit() %></p>
                <p class="card-text"><strong>Prix :</strong> <%= p.getPrix() %> DT</p>
                <a href="index?action=showList" class="btn btn-outline-secondary mt-3">
                    <i class="bi bi-arrow-left-circle"></i> Retour à la liste
                </a>
            </div>
        </div>
    <% } else { %>
        <div class="alert alert-danger mt-3">Produit introuvable !</div>
    <% } %>
</div>
</body>
</html>
