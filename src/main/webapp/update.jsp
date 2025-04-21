<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="metier.Produit" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modifier un produit</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow">
        <div class="card-header bg-warning text-white">
            <h4>Modifier le produit</h4>
        </div>
        <div class="card-body">
            <%
                Produit p = (Produit) request.getAttribute("produit");
                if (p != null) {
            %>
            <form action="index" method="post">
                <input type="hidden" name="action" value="updateProduit">
                <input type="hidden" name="id" value="<%= p.getIdProduit() %>">

                <div class="mb-3">
                    <label for="nomp" class="form-label">Nom du produit</label>
                    <input type="text" class="form-control" name="nomp" id="nomp" value="<%= p.getNomProduit() %>" required>
                </div>
                <div class="mb-3">
                    <label for="prix" class="form-label">Prix</label>
                    <input type="text" class="form-control" name="prix" id="prix" value="<%= p.getPrix() %>" required>
                </div>

                <button type="submit" class="btn btn-success">Mettre à jour</button>
                <a href="index?action=showList" class="btn btn-secondary">Annuler</a>
            </form>
            <%
                } else {
            %>
            <div class="alert alert-danger">Produit non trouvé.</div>
            <%
                }
            %>
        </div>
    </div>
</div>
</body>
</html>
