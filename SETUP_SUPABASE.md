# Setup Supabase pour SokoData

## Étapes de Configuration

### 1. Créer un projet Supabase

1. Allez sur [https://supabase.com](https://supabase.com)
2. Cliquez sur "New Project"
3. Remplissez les détails :
   - **Project name**: SokoData
   - **Database Password**: [Générez un mot de passe sécurisé]
   - **Region**: Choisissez la région la plus proche

4. Attendez que la base de données soit créée (~2 min)

### 2. Récupérer les clés

1. Allez dans **Settings** → **API**
2. Copiez :
   - **Project URL** (ex: https://projectid.supabase.co)
   - **Public API Key** (Anon role)

### 3. Configurer SecretConfig.kt

Ouvrez `app/src/main/java/com/example/sokodata/data/SecretConfig.kt` et remplissez :

```kotlin
object SecretConfig {
    const val SUPABASE_URL = "https://YOUR_PROJECT_ID.supabase.co"
    const val SUPABASE_KEY = "YOUR_PUBLIC_ANON_KEY"
}
```

### 4. Créer la Table PostgreSQL

1. Allez dans Supabase → **SQL Editor**
2. Cliquez sur "New query"
3. Copier-collez et exécutez :

```sql
-- Table sellers
CREATE TABLE sellers (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name TEXT NOT NULL,
  tableNumber TEXT NOT NULL,
  category TEXT NOT NULL,
  imageUrl TEXT,
  created_at TIMESTAMP DEFAULT now(),
  updated_at TIMESTAMP DEFAULT now()
);

-- Indexes pour performances
CREATE INDEX idx_sellers_name ON sellers(name);
CREATE INDEX idx_sellers_table ON sellers(tableNumber);
CREATE INDEX idx_sellers_created ON sellers(created_at DESC);

-- Enable RLS (Row Level Security)
ALTER TABLE sellers ENABLE ROW LEVEL SECURITY;

-- Policies (permissif en développement)
CREATE POLICY "Enable read for all users"
  ON sellers FOR SELECT
  USING (true);

CREATE POLICY "Enable insert for all users"
  ON sellers FOR INSERT
  WITH CHECK (true);

CREATE POLICY "Enable update for all users"
  ON sellers FOR UPDATE
  USING (true);

CREATE POLICY "Enable delete for all users"
  ON sellers FOR DELETE
  USING (true);
```

### 5. Créer le Bucket Storage

1. Allez dans **Storage** (à gauche)
2. Cliquez sur "Create a new bucket"
3. Remplissez :
   - **Name**: seller_images
   - **Public bucket**: ✅ (cochez pour accès public)
4. Cliquez "Create bucket"

### 6. Configuration du Bucket

1. Allez dans le bucket `seller_images`
2. Cliquez sur **Policies** (3 points)
3. Cliquez "Add Policy"
4. Sélectionnez **For SELECT** → **via default mode**
5. Acceptez les paramètres par défaut (Allow all)
6. Répétez pour INSERT et UPDATE

### 7. Vérifier la Connexion

Dans l'app, vérifiez :
1. Allez  à la page "Add Seller"
2. L'app devrait pouvoir :
   - Afficher un formulaire vide
   - Créer les vendeurs
   - Afficher la liste

Si erreur "PGRST", vérifiez les clés d'API.

## Environnement de Développement vs Production

### Développement (Actuel)
- RLS désactivé pour chaque table
- Accès public au bucket
- Clé anon exposée dans code (usage interne seulement !)

### Production (À faire)
- Implémenter GoTrue pour authentification utilisateur
- Configurer RLS policies strictes
- Utiliser clé avec permissions limitées
- URL API cachée dans backend

## Dépannage

### Error: "PGRST" ou "Unauthorized"
→ Vérifiez les clés Supabase dans SecretConfig.kt

### Images ne s'affichent pas
→ Vérifiez que le bucket est **public** et les CORS sont activés

### Erreur insertion vendeur
→ Vérifiez la table "sellers" existe et RLS permet les inserts

### Crash lors de suppression
→ Vérifiez seller.id n'est pas null et la policy permet les deletes

## Architecture Supabase

```
supabase-project/
├── Database (PostgreSQL)
│   ├── Table: sellers
│   │   ├── id (UUID key)
│   │   ├── name, tableNumber, category
│   │   ├── imageUrl (T ext)
│   │   └── timestamps
│   └── RLS Policies
│
├── Storage (S3-like)
│   └── Bucket: seller_images
│       └── Objects: {uuid}.jpg
│
└── API (Postgrest + Storage API)
    ├── https://project.supabase.co/rest/v1
    └── Auth via header: Authorization: Bearer ANON_KEY
```

## Exemple d'Appel API (via SDK)

```kotlin
// Récupérer tous les vendeurs
val sellers = client.postgrest["sellers"]
    .select()
    .decodeList<Seller>()

// Upload image
val bucket = client.storage.from("seller_images")
bucket.upload("${UUID.randomUUID()}.jpg", imageBytes)
val url = bucket.publicUrl(fileName)

// Insert vendeur
val seller = Seller(name = "Jean", tableNumber = "15", category = "Fruits")
client.postgrest["sellers"].insert(seller)
```

## Support

- **Documentation**: https://supabase.com/docs
- **SDK Kotlin**: https://github.com/supabase-community/supabase-kt
- **Issues**: Ouvrir issue sur GitHub du projet
