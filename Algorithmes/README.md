# Projet SAE S3 : Algorithmique - Création de Groupes

**Auteurs :** Youcef BOUZID & Yassine RHARRABTI  
**Langage :** Java  
**Date :** Janvier 2026

---

## 1. Présentation Générale

Ce projet propose un module algorithmique capable de répartir une promotion d'étudiants en groupes de TD ou TP. L'objectif est de respecter des contraintes strictes d'effectifs (taille maximale des groupes) tout en optimisant des critères pédagogiques ou sociaux.

Pour répondre à des besoins variés, nous avons implémenté **deux modes de création distincts**, chacun géré par un responsable spécifique.

---

## 2. Modes de Création et Responsabilités

### Mode A : Optimisation Académique Pure
**Responsable :** Yassine Rharrabti
* **Objectif :** Minimiser les écarts de niveau entre les groupes pour obtenir des classes homogènes.
* **Contraintes gérées :** Capacité maximale des groupes (jauge fixe).
* **Algorithmes implémentés :**
    1.  **Glouton Distributeur :** Répartition cyclique simple. Rapide mais ne prend pas en compte le niveau.
    2.  **Glouton Compensateur (Recommandé) :** Trie les élèves par moyenne. À chaque étape, l'élève est assigné au groupe ayant actuellement la moyenne la plus faible pour "compenser" l'écart.
    3.  **Force Brute (Individuelle) :** Teste toutes les combinaisons possibles étudiant par étudiant via un algorithme de backtracking.

### Mode B : Gestion des Affinités (Covoiturage)
**Responsable :** Youcef Bouzid
* **Objectif :** Respecter les liens sociaux (amis/covoiturage) tout en équilibrant les effectifs ou le niveau global.
* **Contraintes gérées :** Groupes d'amis insécables + Capacité maximale des groupes.
* **Algorithmes implémentés :**
    1.  **Glouton "Logistique" (Équilibré) :** Priorise le remplissage optimal des groupes en plaçant les plus gros groupes d'amis en premier (*First Fit Decreasing*).
    2.  **Glouton "Snake" (Niveau) :** Combine le respect des amis et l'équilibre des notes. Les groupes d'amis sont triés par niveau et distribués en serpentin (A -> B -> C puis C -> B -> A) pour lisser les moyennes.
    3.  **Force Brute (par Blocs) :** Version optimisée du backtracking qui déplace des groupes entiers d'étudiants pour trouver la solution idéale.

---

## 3. Analyse Technique et Améliorations

### L'Innovation "Blocs Insécables" (Amélioration majeure)
Pour le Mode B, Youcef a développé une méthode de prétraitement des données : `formerBlocs()`.
Au lieu de traiter les étudiants comme des entités isolées, nous transformons le graphe des amitiés en une liste de listes (les "Blocs").
* **Intérêt algorithmique :** Cela réduit drastiquement la complexité de la Force Brute. Au lieu de calculer $k^N$ (où $N$ est le nombre d'étudiants), on calcule $k^B$ (où $B$ est le nombre de blocs d'amis). Comme $B < N$, l'algorithme est beaucoup plus performant.

### Gestion de la Complexité (Sécurité Force Brute)
Les algorithmes de Force Brute garantissent la solution mathématique parfaite (écart minimal). Cependant, leur complexité est exponentielle.
* **Mécanisme de sécurité :** Nous avons implémenté une bascule automatique. Si le nombre d'éléments à placer (étudiants ou blocs) dépasse 10, le programme détecte le risque de temps de calcul infini et lance automatiquement l'algorithme Glouton correspondant à la place.

---

## 4. Résultats des Tests Comparatifs

Nous avons développé une classe `TestComparatif` qui simule 100 répartitions aléatoires pour comparer l'efficacité des algorithmes.
Le **Score** représente la variance des moyennes entre les groupes (plus il est proche de 0, meilleure est la répartition).

| Algorithme | Score Moyen (Écart) | Temps d'exécution |
| :--- | :--- | :--- |
| **Yassine - Glouton Distributeur** | ~3.5 | Instantané (<1ms) |
| **Yassine - Glouton Compensateur** | **~0.25** | Instantané (<1ms) |
| **Youcef - Glouton Covoit (Logistique)**| ~2.8 | Instantané (<2ms) |
| **Youcef - Glouton Covoit (Snake)** | **~0.45** | Instantané (<2ms) |
| **Force Brute (sur <12 items)** | **0.12 (Optimal)** | ~15-200ms |

**Conclusion des tests :**
* Pour une optimisation pure des notes (sans amis), le **Glouton Compensateur** de Yassine est le plus efficace.
* Dès qu'il y a des contraintes de covoiturage, le **Glouton Snake** de Youcef est le meilleur compromis : il respecte 100% des liens d'amitié tout en gardant un score de déséquilibre très faible (proche de l'optimal).

## 5. Instructions pour le Jury

### A. Test des Algorithmes (Console)
Le code source algorithmique pur se trouve dans ce rendu.

### B. Démonstration de l'Intégration (Application Web)
Nos algorithmes sont pleinement intégrés et fonctionnels dans l'application complète. Vous pouvez tester la création de groupes en conditions réelles via notre déploiement web :

* **URL :** [https://projets.iut-orsay.fr/saes3-ybouzid1/index.php](https://projets.iut-orsay.fr/saes3-ybouzid1/index.php)
* **Identifiants Administrateur :**
    * **Login :** `admin.sys`
    * **Mot de passe :** `adminMdp`

*Note : Une fois connecté, vous pourrez accéder au module de gestion des groupes et lancer nos algorithmes de répartition (Distributeur, Compensateur, Snake) sur les données de la base.*