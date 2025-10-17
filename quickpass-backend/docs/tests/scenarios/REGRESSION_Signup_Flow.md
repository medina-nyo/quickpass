# Tests de Régression (RÉUSSITE) - Parcours d'Inscription Boulanger

## 🎯 Objectif
Valider l'intégralité du parcours d'inscription Boulanger (8 étapes) en mode nominal (succès) via l'API, s'assurant que l'état de la session (`signup_session`) progresse correctement après chaque étape.

**Endpoint de base :** `{{baseUrl}}/signup/{{sessionId}}/...`
**Outil :** Postman (Utilisation de la collection "QuickPass - Parcours d’inscription Boulanger")

---

### 1. Étape 1 : Démarrage de Session (Start Session)

| Réf. | Requête Postman | Méthode | Endpoint | Payload d'Exemple |
| :---: | :--- | :---: | :--- | :--- |
| **A1** | `1️⃣ Start Session` | `POST` | `/api/v1/signup/start` | `{"email": "boulangerie.test@quickpass.com"}` |

#### Résultat Attendu
* **Statut HTTP :** `200 OK`.
* **Réponse JSON :** `{"success": true, "data": {"sessionId": 1}}`. (Le `sessionId` est capturé dans l'environnement Postman).
* **Action BDD :** Création d'une ligne dans `signup_session`. `current_step` = `EMAIL`. `completed` = `FALSE`.

---

### 2. Étape 2 : Vérification du Code E-mail

| Réf. | Requête Postman | Méthode | Endpoint | Payload d'Exemple |
| :---: | :--- | :---: | :--- | :--- |
| **A2** | `2️⃣ Email Code` | `POST` | `/api/v1/signup/{{sessionId}}/email-code` | `{"emailCode": "123456"}` |

#### Résultat Attendu
* **Statut HTTP :** `200 OK`.
* **Action BDD :** Mise à jour de `signup_session`. **`current_step`** = **`PROFIL_BOULANGER`**.

---

### 3. Étape 3 : Informations de Profil (Boulanger)

| Réf. | Requête Postman | Méthode | Endpoint | Payload d'Exemple |
| :---: | :--- | :---: | :--- | :--- |
| **A3** | `3️⃣ Profil Boulanger` | `PUT` | `/api/v1/signup/{{sessionId}}/profil` | Contient les informations de profil (Prénom, Nom, SIRET, Téléphones, etc.). |

#### Résultat Attendu
* **Statut HTTP :** `200 OK`.
* **Action BDD :** Mise à jour des champs de profil dans `signup_session`. **`current_step`** = **`SMS_CODE`**.

---

### 4. Étape 4 : Vérification du Code SMS

| Réf. | Requête Postman | Méthode | Endpoint | Payload d'Exemple |
| :---: | :--- | :---: | :--- | :--- |
| **A4** | `4️⃣ Code SMS` | `POST` | `/api/v1/signup/{{sessionId}}/sms-code` | `{"smsCode": "654321"}` |

#### Résultat Attendu
* **Statut HTTP :** `200 OK`.
* **Action BDD :** Mise à jour de `signup_session`. **`current_step`** = **`ADRESSE_VENTE`**.

---

### 5. Étape 5 : Adresse et Mode de Vente

| Réf. | Requête Postman | Méthode | Endpoint | Payload d'Exemple |
| :---: | :--- | :---: | :--- | :--- |
| **A5** | `5️⃣ Adresse & Mode Vente` | `PUT` | `/api/v1/signup/{{sessionId}}/adresse-vente` | Contient Adresse, Ville, Code Postal, et `modeVente`. |

#### Résultat Attendu
* **Statut HTTP :** `200 OK`.
* **Action BDD :** Mise à jour des champs d'adresse et de vente. **`current_step`** = **`STRIPE_CONNECT`**.

---

### 6. Étape 6 : Connexion Stripe

| Réf. | Requête Postman | Méthode | Endpoint | Payload d'Exemple |
| :---: | :--- | :---: | :--- | :--- |
| **A6** | `6️⃣ Stripe` | `PUT` | `/api/v1/signup/{{sessionId}}/stripe` | `{"stripeAccountId": "acct_test_12345"}` |

#### Résultat Attendu
* **Statut HTTP :** `200 OK`.
* **Action BDD :** `stripe_account_id` est enregistré. **`current_step`** = **`MOT_DE_PASSE`**.

---

### 7. Étape 7 : Définition du Mot de Passe

| Réf. | Requête Postman | Méthode | Endpoint | Payload d'Exemple |
| :---: | :--- | :---: | :--- | :--- |
| **A7** | `7️⃣ Mot de passe` | `PUT` | `/api/v1/signup/{{sessionId}}/password` | `{"motDePasse": "MonSuperMotDePasse123!"}` |

#### Résultat Attendu
* **Statut HTTP :** `200 OK`.
* **Action BDD :** Le hachage du mot de passe est enregistré. **`current_step`** = **`CONSENTEMENTS_LEGAUX`**.

---

### 8. Étape 8 : Consentements Légaux (Finalisation)

| Réf. | Requête Postman | Méthode | Endpoint | Payload d'Exemple |
| :---: | :--- | :---: | :--- | :--- |
| **A8** | `8️⃣ Consentements` | `PUT` | `/api/v1/signup/{{sessionId}}/consentements` | `{"accepteCgu": true, "accepteRgpd": true, "accepteStripe": true}` |

#### Résultat Attendu (Final)
* **Statut HTTP :** `200 OK`.
* **Action BDD :** Le champ **`completed`** de `signup_session` passe à **`TRUE`**. Le champ `current_step` passe à **`FINI`** (ou à l'étape finale définie).
* **Action Applicative :** Le compte `Boulanger` est créé dans les tables métier (`compte`, `boulanger`, `preference`).

---


