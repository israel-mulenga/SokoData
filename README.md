#  SokoData - Registre Numérique des Vendeurs

**Application mobile Android native pour gérer les vendeurs d'un marché local**

##  Aperçu du Projet

SokoData est une application Android native conçue pour moderniser la gestion et le recensement des vendeurs dalam de grands espaces commerciaux tels que les marchés municipaux.

Cette solution remplace les registres physiques par un système numérique robuste permettant aux administrateurs de marché de :
- **Enregistrer rapidement** chaque vendeur avec ses informations
- **Photographier les étalages** pour identification visuelle
- **Rechercher instantanément** par nom ou numéro de table
- **Synchroniser les données** en temps réel avec le cloud

##  Fonctionnalités Principales

- **CRUD Complet** - Créer, lire, modifier, supprimer des vendeurs
- **Identification Visuelle** - Capture et affichage des photos des étalages
- **Recherche Dynamique** - Filtrage réactif en temps réel par nom/numéro
- **Synchronisation Cloud** - Supabase pour les données et images
- **Material Design 3** - Interface moderne et professionnelle
- **Responsive** - Optimisée pour utilisation sur le terrain

## Architecture MVVM + State Hoisting

### Structure du Projet

```
app/
├── data/
│   ├── SellerRepository.kt           # Interface du repository
│   ├── SellerRepositoryImpl.kt        # Implémentation avec Supabase
│   ├── SupabaseClient.kt             # Configuration client Supabase
│   └── SecretConfig.kt               # Clés API (NE PAS COMMITER)
├── model/
│   └── Seller.kt                     # Modèle de donnée avec @Serializable
├── viewmodel/
│   └── SellerViewModel.kt            # État réactif + logique métier
├── ui/
│   ├── screens/
│   │   ├── SellerListScreen.kt       # Écran principal (liste/grille)
│   │   ├── SellerSearchBar.kt        # Barre de recherche Material
│   │   ├── SellerCard.kt             # Carte vendeur réutilisable
│   │   ├── AddSellerScreen.kt        # Écran d'ajout
│   │   ├── EditSellerScreen.kt       # Écran d'édition
│   │   └── DeleteConfirmationDialog.kt
│   ├── theme/
│   │   ├── Color.kt                  # Palette Material Design 3
│   │   ├── Type.kt                   # Typographie Poppins
│   │   └── Theme.kt                  # Configuration du thème
│   └── navigation/
│       ├── NavigationRoute.kt         # Routes de navigation
│       └── SokoDataNavGraph.kt        # NavGraph composable
└── MainActivity.kt                   # Point d'entrée de l'app
```

### Flux de Données Réactif

```
SellerViewModel (StateFlow)
    ├── _sellers: List[Seller]        → Tous les vendeurs
    ├── _filteredSellers: List        → Vendeurs filtrés par recherche
    ├── _searchQuery: String          → Terme de recherche
    ├── _isLoading: Boolean           → État de chargement
    ├── _errorMessage: String?        → Messages d'erreur
    └── _selectedSeller: Seller?      → Vendeur en édition

SellerListScreen (collectAsState)
    ├── Affiche les vendeurs filtrés
    ├── Appelle viewModel.updateSearchQuery()
    └── Navigate vers Add/Edit

AddSellerScreen
    └── Appelle viewModel.addSeller()

EditSellerScreen
    ├── Appelle viewModel.updateSeller()
    └── Appelle viewModel.deleteSeller()
```

## Technologie & Dépendances

| Technologie | Version | Utilisation |
|-------------|---------|------------|
| **Jetpack Compose** | 2024.09.00 | UI 100% déclarative |
| **Material Design 3** | Intégré | Thème, composants MD3 |
| **Kotlin** | 2.2.10 | Langage principal |
| **Coroutines** | 1.8.0 | Asynchronisme |
| **Supabase** | 2.5.0 | Backend BaaS |
| **Coil** | 2.6.0 | Image loading |
| **Navigation Compose** | 2.7.7 | Routage entre écrans |

## Getting Started

### Prérequis

1. **Android Studio Hedgehog+** (~9.1) ou récent
2. **JDK 11+**
3. **Gradlenomically-compatible** (gradle 9.3.1 fourni)
4. **Compte Supabase** (gratuit sur https://supabase.com)

### Installation

1. **Cloner le projet**
   ```bash
   git clone <repo-url>
   cd SokoData
   ```

2. **Configurer Supabase**
   - Créer un nouveau projet sur https://supabase.com
   - Récupérer l'URL du projet et la clé public (anon)
   - Ouvrir `app/src/main/java/com/example/sokodata/data/SecretConfig.kt`
   - Remplacer les valeurs :
   ```kotlin
   const val SUPABASE_URL = "https://YOUR_PROJECT.supabase.co"
   const val SUPABASE_KEY = "YOUR_ANON_KEY"
   ```

3. **Créer la table PostgreSQL**
   - Aller dans Supabase → SQL Editor
   - Exécuter :
   ```sql
   CREATE TABLE sellers (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     name TEXT NOT NULL,
     tableNumber TEXT NOT NULL,
     category TEXT NOT NULL,
     imageUrl TEXT,
     created_at TIMESTAMP DEFAULT now()
   );
   
   CREATE BUCKET seller_images;
   ALTER TABLE seller_images OWNER TO postgres;
   ```

4. **Build & Run**
   ```bash
   ./gradlew build
   # Puis lancer depuis Android Studio ou :
   ./gradlew installDebug
   ```

## Modèle de Données

### Seller (Kotlin Data Class)

```kotlin
@Serializable
data class Seller(
    val id: String? = null,
    val name: String,
    val tableNumber: String,
    val category: String,
    val imageUrl: String? = null
)
```

### PostgreSQL Table

| Colonne | Type | Notes |
|---------|------|-------|
| id | UUID | Primary key, auto-generated |
| name | TEXT | Nom du vendeur |
| tableNumber | TEXT | Numéro de table/pavillon |
| category | TEXT | Catégorie d'articles |
| imageUrl | TEXT | URL publique Supabase Storage |
| created_at | TIMESTAMP | Timestamp création |

## Material Design 3 Theme

### Palette Couleur (Marché/Vendeurs)

**Light Theme:**
- Primary: Teal (#006C4E) - Brand principal
- Secondary: Dark Green (#386A4B) - Accent complémentaire
- Tertiary: Orange (#C9620D) - Accent actions

**Dark Theme:**
- Colors adaptées pour OLED

### Typographie

- Police: **Poppins** (System Sans-Serif en fallback)
- Scales complètes : Display, Headline, Title, Body, Label
- Poids: Regular, Medium, SemiBold, Bold

## Permissions

L'application requiert les permissions suivantes dans `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

Les permissions sont gérées au runtime avec `PermissionX`.

## Intégration Supabase

### Authentification

Pour la phase 1, l'app utilise la clé publique (anon) de Supabase.
Pour la production, implémenter GoTrue (authentification).

### Repository Pattern

Le `SellerRepository` interface défini les opérations CRUD :

```kotlin
suspend fun getAllSellers(): List<Seller>
suspend fun addSeller(seller: Seller, imageBytes: ByteArray?): Boolean
suspend fun updateSeller(seller: Seller): Boolean
suspend fun deleteSeller(sellerId: String): Boolean
```

L'implémentation `SellerRepositoryImpl` :
- Upload les images dans le bucket `seller_images`
- Obtient les URLs publiques pour stockage en DB
- Gère les exceptions et logging

## Navigation

Navigation entre écrans via **Compose Navigation**:

| Route | Écran | Description |
|-------|-------|-------------|
| `seller_list` | SellerListScreen | Liste + SearchBar |
| `add_seller` | AddSellerScreen | Formulaire d'ajout |
| `edit_seller/{sellerId}` | EditSellerScreen | Édition + Suppression |

## Débogage & Édition

### Logging

Les erreurs Supabase sont loggées via `e.printStackTrace()`.
Pour débuguer, utiliser les outils de logging Android :

```bash
./gradlew logcat
```

### État du ViewModel

Accéder à l'état via preview composes ou debugger :

```kotlin
@Preview
@Composable
fun SellerListPreview() {
    val viewModel = SellerViewModel()
    SellerListScreen(viewModel = viewModel)
}
```

## Cas d'Utilisation Principaux

### Administrateur au marché

1. **Ajouter un vendeur** 
   - Taper nom, table, catégorie
   - Photographier l'étalage
   - Cliquer "Créer"
   - Sync auto vers Supabase

2. **Rechercher un vendeur**
   - Ouvrir l'app (SearchBar visible)
   - Taper "Jean" ou "Table 15"
   - Résultats filtres réactifs
   - Cliquer pour éditer

3. **Modifier les infos**
   - Ouvrir la carte vendeur
   - Modifier nom/catégorie
   - Remplacer la photo si besoin
   - Cliquer "Enregistrer"

4. **Vendeur part du marché**
   - Ouvrir la fiche vendeur
   - Cliquer "Supprimer"
   - Confirmer → Suppression instant



## Build & Deployment

### Debug APK

```bash
./gradlew assembleDebug
# APK à : app/build/outputs/apk/debug/app-debug.apk
```

### Release APK (Production)

```bash
./gradlew assembleRelease
# Signer avec keystore et aligner
```

SokoData - Projet académique 2025

