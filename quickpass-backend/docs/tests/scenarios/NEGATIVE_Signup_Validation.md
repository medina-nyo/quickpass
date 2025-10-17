
```markdown
# Tests de Régression (ÉCHEC/NÉGATIF) - Parcours d'Inscription Boulanger

## 🎯 Objectif
Valider la gestion des erreurs et des contraintes du parcours d'inscription Boulanger par l'API.

**Endpoint de base :** `{{baseUrl}}/signup/start`
**Outil :** Postman / cURL (pour les tests manuels de validation)

---

### 1. Étape 1 : Démarrage de Session (Validation d'entrée et Conflits)

| N° | Scénario | Méthode | Payload d'Exemple | Résultat de Test |
| :---: | :--- | :---: | :--- | :--- |
| **⚠️ 1** | **Email déjà utilisé** (Conflit métier) | `POST` | `{"email": "test@quickpass.com"}` | **Statut HTTP :** `409 Conflict`. **JSON :** `code: "SIGNUP_EMAIL_EXISTS"`, `detail: "Une session d'inscription active existe déjà pour cet e-mail."` |
| **⚠️ 2** | **Email invalide** (Violation de contrainte) | `POST` | `{"email": "contact@"}` | **Statut HTTP :** `400 Bad Request`. **JSON :** `code: "VALIDATION_ERREUR"`, `detail: "email: email.invalide"` |
| **⚠️ 3** | **Erreur serveur simulée** (Test Controller Advice) | `POST` | `{"email": "force500@error.com"}` | **Statut HTTP :** `500 Internal Server Error`. **JSON :** `code: "ERREUR_INTERNE"`, `title: "Erreur interne du serveur"` |

---

