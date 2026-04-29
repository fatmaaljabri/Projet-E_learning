-- =============================================
-- data.sql — Données de test ELearn FST
-- Mots de passe hashés BCrypt pour "password123"
-- =============================================

-- Désactiver les contraintes FK temporairement
SET FOREIGN_KEY_CHECKS = 0;

-- Vider les tables
TRUNCATE TABLE reponses_apprenants;
TRUNCATE TABLE progression_lecons;
TRUNCATE TABLE inscriptions;
TRUNCATE TABLE questions;
TRUNCATE TABLE quiz;
TRUNCATE TABLE lecons;
TRUNCATE TABLE modules;
TRUNCATE TABLE cours;
TRUNCATE TABLE utilisateurs;

SET FOREIGN_KEY_CHECKS = 1;

-- ===== UTILISATEURS =====
-- Mot de passe pour tous : password123
-- BCrypt hash de "password123"
INSERT INTO utilisateurs (email, mot_de_passe, nom, prenom, role, actif) VALUES
('admin@fst.tn',      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh7y', 'Admin',    'Super',   'ADMIN',      true),
('formateur1@fst.tn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh7y', 'Belhadj',  'Nader',   'FORMATEUR',  true),
('formateur2@fst.tn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh7y', 'Ben Ali',  'Sonia',   'FORMATEUR',  true),
('apprenant1@fst.tn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh7y', 'Trabelsi', 'Ahmed',   'APPRENANT',  true),
('apprenant2@fst.tn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh7y', 'Mbarek',   'Fatma',   'APPRENANT',  true),
('apprenant3@fst.tn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh7y', 'Rekik',    'Mohamed', 'APPRENANT',  true);

-- ===== COURS =====
INSERT INTO cours (titre, description, categorie, niveau, image_url, actif, date_creation, formateur_id) VALUES
('Spring Boot — Développement Web Java',
 'Apprenez à créer des applications web robustes avec Spring Boot 3, Spring MVC, JPA et Thymeleaf. Ce cours couvre toute la stack moderne du développement Java backend.',
 'Programmation', 'INTERMEDIAIRE', NULL, true, NOW(), 2),

('Bases de données MySQL avancées',
 'Maîtrisez MySQL 8 : conception de schémas, optimisation des requêtes, indexation, procédures stockées et administration. Idéal pour les développeurs souhaitant approfondir leur maîtrise des SGBD relationnels.',
 'Base de données', 'AVANCE', NULL, true, NOW(), 2),

('Introduction à la programmation Java',
 'Découvrez les fondamentaux de Java : types, conditions, boucles, fonctions, et programmation orientée objet. Cours parfait pour les débutants qui souhaitent apprendre à programmer.',
 'Programmation', 'DEBUTANT', NULL, true, NOW(), 3);

-- ===== MODULES — Cours 1 : Spring Boot =====
INSERT INTO modules (titre, description, ordre, cours_id) VALUES
('Introduction à Spring Boot',       'Présentation du framework, installation et premier projet', 1, 1),
('Spring MVC et Thymeleaf',          'Contrôleurs, vues Thymeleaf, layouts et formulaires',     2, 1);

-- ===== MODULES — Cours 2 : MySQL =====
INSERT INTO modules (titre, description, ordre, cours_id) VALUES
('Modélisation et conception',       'Schémas entité-relation, normalisation, clés',            1, 2),
('Optimisation et performance',      'Index, EXPLAIN, requêtes optimisées, cache',               2, 2);

-- ===== MODULES — Cours 3 : Java débutant =====
INSERT INTO modules (titre, description, ordre, cours_id) VALUES
('Variables et types de données',    'Types primitifs, déclaration, conversions',                1, 3),
('Structures de contrôle',           'Conditions, boucles, switch',                              2, 3);

-- ===== LEÇONS — Module 1 (Spring Boot Intro) =====
INSERT INTO lecons (titre, contenu, ordre, duree_min, module_id) VALUES
('Qu''est-ce que Spring Boot ?',
 'Spring Boot est un framework Java qui simplifie la création d''applications Spring. Il élimine la configuration XML et propose des starters prêts à l''emploi. Avec Spring Boot, on peut créer une application web complète en quelques minutes.',
 1, 10, 1),
('Installation et configuration',
 'Pour commencer avec Spring Boot, vous avez besoin de Java 17+, Maven ou Gradle, et un IDE comme IntelliJ IDEA. Créez votre projet sur start.spring.io en sélectionnant les dépendances : Spring Web, Thymeleaf, JPA, MySQL Driver.',
 2, 15, 1),
('Votre premier projet Spring Boot',
 'Créons ensemble notre première application. Nous allons créer un controller simple qui retourne une page HTML. Annotations clés : @SpringBootApplication, @Controller, @GetMapping.',
 3, 20, 1);

-- ===== LEÇONS — Module 2 (Spring MVC) =====
INSERT INTO lecons (titre, contenu, ordre, duree_min, module_id) VALUES
('Les contrôleurs Spring MVC',
 'Un contrôleur est une classe annotée @Controller. Elle reçoit les requêtes HTTP, traite les données et renvoie une vue. Utilisez @GetMapping, @PostMapping pour mapper les URLs.',
 1, 15, 2),
('Thymeleaf : syntaxe de base',
 'Thymeleaf est un moteur de templates Java. Syntaxe essentielle : th:text pour afficher du texte, th:each pour les listes, th:if pour les conditions, th:href pour les liens.',
 2, 20, 2),
('Formulaires et validation',
 'Pour créer un formulaire Spring : th:action pour l''URL cible, th:object pour lier un objet, th:field pour lier un champ. Utilisez @Valid et BindingResult pour valider côté serveur.',
 3, 25, 2);

-- ===== LEÇONS — Module 3 (MySQL Modélisation) =====
INSERT INTO lecons (titre, contenu, ordre, duree_min, module_id) VALUES
('Modèle Entité-Relation',
 'Le modèle E-R permet de concevoir la structure d''une base de données. Les entités représentent les objets du monde réel, les attributs leurs propriétés, et les relations les liens entre entités.',
 1, 20, 3),
('Normalisation des données',
 'La normalisation évite la redondance et les anomalies. 1NF : attributs atomiques. 2NF : dépendance complète à la clé. 3NF : pas de dépendance transitive.',
 2, 25, 3),
('Clés primaires et étrangères',
 'La clé primaire identifie de façon unique chaque enregistrement. La clé étrangère crée un lien entre deux tables. Ces contraintes d''intégrité référentielle garantissent la cohérence des données.',
 3, 15, 3);

-- ===== LEÇONS — Module 4 (MySQL Performance) =====
INSERT INTO lecons (titre, contenu, ordre, duree_min, module_id) VALUES
('Les index MySQL',
 'Un index accélère les recherches mais ralentit les insertions. Types : PRIMARY, UNIQUE, INDEX, FULLTEXT. Créez des index sur les colonnes fréquemment utilisées dans les clauses WHERE et JOIN.',
 1, 20, 4),
('La commande EXPLAIN',
 'EXPLAIN analyse une requête SELECT et montre comment MySQL l''exécute. Lisez le type (ALL = scan complet, ref = utilise un index), les lignes examinées et les index possibles.',
 2, 15, 4),
('Optimisation des requêtes',
 'Évitez SELECT * et préférez lister les colonnes nécessaires. Utilisez des JOIN plutôt que des sous-requêtes. Limitez les résultats avec LIMIT. Évitez les fonctions sur les colonnes indexées dans WHERE.',
 3, 25, 4);

-- ===== LEÇONS — Module 5 (Java Variables) =====
INSERT INTO lecons (titre, contenu, ordre, duree_min, module_id) VALUES
('Types primitifs Java',
 'Java possède 8 types primitifs : int (entier), double (décimal), boolean (vrai/faux), char (caractère), long, float, byte, short. Exemple : int age = 25; double prix = 19.99; boolean actif = true;',
 1, 15, 5),
('Déclaration de variables',
 'Une variable est déclarée avec son type suivi de son nom : type nomVariable = valeur; Depuis Java 10, vous pouvez utiliser var pour l''inférence de type : var nom = "Ahmed"; Java infère que c''est un String.',
 2, 15, 5),
('Conversions de types',
 'La conversion implicite (widening) se fait automatiquement : int → long → double. La conversion explicite (casting) nécessite une syntaxe : int x = (int) 3.7; // x = 3. Attention aux pertes de données!',
 3, 20, 5);

-- ===== LEÇONS — Module 6 (Java Structures) =====
INSERT INTO lecons (titre, contenu, ordre, duree_min, module_id) VALUES
('Les conditions if/else',
 'La structure conditionnelle if/else permet d''exécuter du code selon une condition : if (age >= 18) { System.out.println("Majeur"); } else { System.out.println("Mineur"); } Vous pouvez chaîner avec else if.',
 1, 15, 6),
('Les boucles for et while',
 'La boucle for : for (int i = 0; i < 10; i++) { ... } La boucle while : while (condition) { ... } La boucle for-each pour les collections : for (String item : liste) { ... }',
 2, 20, 6),
('L''instruction switch',
 'Le switch teste une variable contre plusieurs valeurs : switch (jour) { case 1: System.out.println("Lundi"); break; ... default: System.out.println("Autre"); } Depuis Java 14 : switch expressions avec ->.',
 3, 15, 6);

-- ===== QUIZ — un par module =====
INSERT INTO quiz (titre, module_id) VALUES
('Quiz : Introduction Spring Boot',    1),
('Quiz : Spring MVC et Thymeleaf',     2),
('Quiz : Modélisation MySQL',          3),
('Quiz : Performance MySQL',           4),
('Quiz : Variables Java',              5),
('Quiz : Structures de contrôle Java', 6);

-- ===== QUESTIONS — Quiz 1 (Spring Boot) =====
INSERT INTO questions (enonce, choix_a, choix_b, choix_c, choix_d, bonne_reponse, quiz_id) VALUES
('Quelle annotation marque le point d''entrée d''une application Spring Boot ?',
 '@SpringApplication', '@SpringBootApplication', '@EnableAutoConfiguration', '@Controller', 'B', 1),
('Quel fichier contient la configuration d''une application Spring Boot ?',
 'web.xml', 'beans.xml', 'application.properties', 'config.java', 'C', 1),
('Quelle annotation mappe une méthode à une requête GET ?',
 '@PostMapping', '@RequestMapping(POST)', '@GetMapping', '@HttpGet', 'C', 1),
('Quel outil permet de créer rapidement un projet Spring Boot ?',
 'Spring CLI', 'start.spring.io', 'Maven Archetype', 'Eclipse Wizard', 'B', 1),
('Quelle version Java minimum est requise pour Spring Boot 3 ?',
 'Java 8', 'Java 11', 'Java 17', 'Java 21', 'C', 1);

-- ===== QUESTIONS — Quiz 2 (Spring MVC/Thymeleaf) =====
INSERT INTO questions (enonce, choix_a, choix_b, choix_c, choix_d, bonne_reponse, quiz_id) VALUES
('Quelle annotation transforme une classe en contrôleur web Spring ?',
 '@Service', '@Component', '@Controller', '@Repository', 'C', 2),
('Quel attribut Thymeleaf permet d''afficher un texte dynamique ?',
 'th:value', 'th:text', 'th:content', 'th:show', 'B', 2),
('Comment itérer sur une liste avec Thymeleaf ?',
 'th:loop', 'th:for', 'th:each', 'th:iterate', 'C', 2),
('Quel attribut Thymeleaf lie un formulaire à un objet Java ?',
 'th:model', 'th:bind', 'th:object', 'th:form', 'C', 2),
('Quelle annotation Spring lit les données d''un formulaire POST ?',
 '@RequestParam', '@PathVariable', '@ModelAttribute', '@FormParam', 'C', 2);

-- ===== QUESTIONS — Quiz 3 (MySQL Modélisation) =====
INSERT INTO questions (enonce, choix_a, choix_b, choix_c, choix_d, bonne_reponse, quiz_id) VALUES
('Quelle est la définition d''une clé primaire ?',
 'Un attribut qui peut être NULL', 'Un identifiant unique de chaque enregistrement', 'Un lien vers une autre table', 'Un index facultatif', 'B', 3),
('La 3NF élimine quelle type de dépendance ?',
 'Dépendance partielle', 'Dépendance totale', 'Dépendance transitive', 'Dépendance circulaire', 'C', 3),
('Une clé étrangère crée :',
 'Un index unique', 'Une contrainte d''intégrité référentielle', 'Une nouvelle table', 'Un trigger automatique', 'B', 3),
('En 1NF, les attributs doivent être :',
 'Triés alphabétiquement', 'Atomiques (valeurs indivisibles)', 'Tous du même type', 'Non nuls', 'B', 3),
('Quel modèle est utilisé pour la conception conceptuelle d''une BDD ?',
 'MVC', 'UML de séquence', 'Entité-Relation (ER)', 'Diagramme de classes', 'C', 3);

-- ===== QUESTIONS — Quiz 4 (MySQL Performance) =====
INSERT INTO questions (enonce, choix_a, choix_b, choix_c, choix_d, bonne_reponse, quiz_id) VALUES
('Quel type d''index garantit l''unicité des valeurs ?',
 'INDEX', 'FULLTEXT', 'UNIQUE', 'PRIMARY KEY uniquement', 'C', 4),
('EXPLAIN dans MySQL sert à :',
 'Exécuter une requête', 'Analyser le plan d''exécution d''une requête', 'Créer un index', 'Commenter du code SQL', 'B', 4),
('Quel type de scan EXPLAIN est le moins performant ?',
 'ref', 'eq_ref', 'ALL', 'index', 'C', 4),
('Pourquoi éviter SELECT * en production ?',
 'C''est une faute de syntaxe', 'Ramène toutes les colonnes inutilement, surcharge le réseau', 'MySQL ne l''accepte pas', 'Provoque des erreurs de type', 'B', 4),
('Que fait la clause LIMIT dans une requête ?',
 'Limite la taille des données', 'Restreint le nombre de lignes retournées', 'Filtre les doublons', 'Trie les résultats', 'B', 4);

-- ===== QUESTIONS — Quiz 5 (Java Variables) =====
INSERT INTO questions (enonce, choix_a, choix_b, choix_c, choix_d, bonne_reponse, quiz_id) VALUES
('Combien de types primitifs Java existe-t-il ?',
 '4', '6', '8', '10', 'C', 5),
('Quel type primitif stocke une valeur décimale en double précision ?',
 'float', 'decimal', 'double', 'number', 'C', 5),
('Depuis quelle version Java peut-on utiliser "var" ?',
 'Java 8', 'Java 10', 'Java 14', 'Java 17', 'B', 5),
('Que vaut (int) 3.9 en Java ?',
 '4', '3', '3.9', 'Erreur de compilation', 'B', 5),
('Quelle déclaration est correcte en Java ?',
 'int 1age = 25;', 'int age-user = 25;', 'int ageUser = 25;', 'int age user = 25;', 'C', 5);

-- ===== QUESTIONS — Quiz 6 (Java Structures) =====
INSERT INTO questions (enonce, choix_a, choix_b, choix_c, choix_d, bonne_reponse, quiz_id) VALUES
('Quelle structure répète un bloc tant qu''une condition est vraie ?',
 'if', 'switch', 'while', 'for-each uniquement', 'C', 6),
('Dans une boucle for : for(int i=0; i<5; i++), combien d''itérations ?',
 '4', '5', '6', 'Dépend du compilateur', 'B', 6),
('Le mot-clé "break" dans un switch sert à :',
 'Sortir de la boucle for', 'Terminer le case et éviter le fall-through', 'Lever une exception', 'Continuer à l''itération suivante', 'B', 6),
('La boucle for-each est utilisée pour :',
 'Itérer avec un compteur', 'Parcourir des tableaux et collections', 'Créer des boucles infinies', 'Remplacer le while', 'B', 6),
('Quel est l''opérateur de comparaison d''égalité en Java ?',
 '=', '===', '==', 'equals uniquement', 'C', 6);

-- ===== INSCRIPTIONS =====
INSERT INTO inscriptions (apprenant_id, cours_id, date_inscription, statut) VALUES
(4, 1, CURDATE(), 'EN_COURS'),
(4, 3, CURDATE(), 'EN_COURS'),
(5, 1, CURDATE(), 'EN_COURS'),
(5, 2, CURDATE(), 'EN_COURS'),
(6, 3, CURDATE(), 'EN_COURS');
