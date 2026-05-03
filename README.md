# NSIAGO'ASSUR — Backend

API REST pour la plateforme de simulation et souscription d'assurance automobile de l'association Action'Elles en partenariat avec NSIAGO'ASSUR.

---

## Stack technique

| Outil | Version | Rôle |
|---|---|---|
| Java | 21 | Langage |
| Spring Boot | 4.x | Framework backend |
| Spring Security | — | Authentification JWT |
| Spring Data JPA | — | Accès base de données |
| MySQL | 8 | Base de données |
| Maven | 3.9 | Gestion des dépendances |
| Docker | — | Conteneurisation |

---

## Prérequis

### Sans Docker
- Java 21
- Maven 3.9+
- MySQL 8

### Avec Docker
- Docker
- Docker Compose

---

## Installation et lancement

### Option 1 — Sans Docker (développement local)

**1. Cloner le projet**
```bash
git clone https://github.com/brahima-fofana/nsiaga-assur-backend.git
cd nsiaga-assur-backend
```

**2. Créer la base de données MySQL**
```sql
CREATE DATABASE IF NOT EXISTS nsiago_assur_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

**3. Configurer `application.yml`**

Le fichier se trouve dans `src/main/resources/application.yml`. Mettez à jour les informations de connexion :

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/nsiago_assur_db
    username: votre_utilisateur
    password: votre_mot_de_passe
  mail:
    host: localhost
    port: 9025  # smtp4dev pour le dev

server:
  port: 8000
```

**4. Lancer l'application**
```bash
mvn spring-boot:run
```

L'API est accessible sur : `http://localhost:8000/api`

---

### Option 2 — Avec Docker

**1. Cloner le projet**
```bash
git clone https://github.com/brahima-fofana/nsiaga-assur-backend.git
cd nsiaga-assur-backend
```

**2. Créer le fichier `.env`** à la racine du projet en vous basant sur `.env.example` :
```bash
cp .env.example .env
```

Remplissez les variables dans `.env` :
```env
# Base de données
DB_URL=jdbc:mysql://db:3306/nsiago_assur_db
DB_USERNAME=votre_utilisateur
DB_PASSWORD=votre_mot_de_passe
MYSQL_ROOT_PASSWORD=votre_mot_de_passe_root
MYSQL_DATABASE=nsiago_assur_db

# Mail
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=votre_email@gmail.com
MAIL_PASSWORD=votre_app_password
```

**3. Builder l'image Docker**
```bash
docker build -t nsiago-assur-backend:latest .
```

**4. Lancer avec Docker Compose**
```bash
docker compose up -d
```

L'API est accessible sur : `http://localhost:8000/api`

**5. Vérifier les logs**
```bash
docker compose logs -f app
```

## Endpoints principaux

### Authentification
```
POST /api/inscription        — Créer un compte
POST /api/activation         — Activer le compte (code 6 chiffres)
POST /api/connexion          — Se connecter → retourne un token JWT
POST /api/deconnexion        — Se déconnecter
```

### Simulations (devis)
```
POST /api/simulations        — Créer une simulation
GET  /api/simulations        — Lister toutes les simulations
GET  /api/simulations/{id}   — Récupérer une simulation
```

---

## Exemple de requête — Simulation

```bash
curl -X POST http://localhost:8000/api/simulations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <votre_token>" \
  -d '{
    "product": "PAPILLON",
    "vehicleCategory": "CAT_201",
    "fiscalPower": 7,
    "vehicleNewValue": 8000000,
    "vehicleVenalValue": 5000000,
    "vehicleAge": 3
  }'
```

**Réponse attendue :**
```json
{
  "quoteReference": "QTXYZ123456789",
  "price": 266078.0,
  "endDate": "2026-05-16T14:54:19",
  "product": "PAPILLON",
  "vehicleCategory": "CAT_201",
  "fiscalPower": 7,
  "vehicleNewValue": 8000000.0,
  "vehicleVenalValue": 5000000.0,
  "vehicleAge": 3
}
```

---

## Produits disponibles

| Produit | Garanties | Catégories éligibles |
|---|---|---|
| PAPILLON | RC + Dommages + Vol | CAT_201 |
| DOUBY | RC + Dommages + Tierce Collision | CAT_202 |
| DOUYOU | RC + Dommages + Collision + Incendie | CAT_201, CAT_202 |
| TOUTOURISQUOU | Toutes les garanties | CAT_201 |

---

## Catégories de véhicules

| Code | Description |
|---|---|
| CAT_201 | Véhicule personnel |
| CAT_202 | Moto / Tricycle |
| CAT_203 | Transport en commun |
| CAT_204 | Taxi |

---

## Structure du projet

```
src/main/java/ci/nsiago/assur/
├── controllers/         — Contrôleurs REST
├── service/             — Logique métier
├── repository/          — Accès base de données
├── entity/              — Entités JPA
├── dto/                 — Objets de transfert
└── configuration/       — Configuration Spring Security, CORS
└── NsiagoApplication    — Point d'entré de l'application Spring Boot
```

---

## Déploiement en production

L'application est disponible en ligne à l'adresse :
**https://nsiaga-assur-backend.devbrahima.com**

Pour déployer votre propre instance :

```bash
# Builder et pousser l'image
docker build -t ghcr.io/votre-username/nsiago-assur-backend:latest .
docker push ghcr.io/votre-username/nsiago-assur-backend:latest

# Sur le serveur
docker pull ghcr.io/votre-username/nsiago-assur-backend:latest
docker compose up -d
```

---

## Auteur

**Brahima Fofana**
GitHub : [github.com/brahima-fofana](https://github.com/brahima-fofana)

---

## Licence

Tous droits réservés © 2026 NSIAGO'ASSUR