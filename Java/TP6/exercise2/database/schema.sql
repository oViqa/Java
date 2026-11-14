CREATE TABLE IF NOT EXISTS PRODUIT (
    code INT PRIMARY KEY CHECK (code > 0),
    nom VARCHAR(255) NOT NULL CHECK (CHAR_LENGTH(TRIM(nom)) > 0),
    prix_achat DECIMAL(12, 2) NOT NULL CHECK (prix_achat >= 0),
    prix_vente DECIMAL(12, 2) NOT NULL CHECK (prix_vente >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
