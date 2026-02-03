<h1> SAE 1.01 :</h1>
<p> Le travail sera décomposé en trois grandes parties :</p>
<li>Concevoir un programme de chiffrement/déchiffrement d’images selon le principe décrit en présentation.</li>
<li>Concevoir un programme de « casse » de la clé, c’est à dire de recherche par force brute de la clé ayant permis le chiffrement d’une image donnée.</li>

## Utilisation :
- Compiler : javac *.java
- Brouiller/débrouiller : java Brouillimg <image_entrée> <clé> [image_sortie] <processus>
- Cassage de clé : java Debrouillage <image_brouillée> <méthode utilisée> [creer_image_debrouillée]

processus = Scramble / Unscramble
Méthodes : Euclid, Pearson, Hybrid, Manathan


Contribution :

<section>
  <h2>Sofiane au projet</h2>

  <h3>Architecture et organisation</h3>
  <ul>
    <li>Organisation de la structure globale du projet et des différentes classes.</li>
  </ul>

  <h3>Algorithmes de cassage de clé</h3>
  <ul>
    <li>Implémentation de la méthode d’Euclide pour casser la clé d’une image brouillée.</li>
    <li>Améliorations et optimisation des méthodes Euclid et Pearson.</li>
    <li>Ajout d’une méthode hybride permettant de trouver le paramètre <code>s</code> avec Euclid puis le paramètre <code>r</code> avec Pearson.</li>
  </ul>

  <h3>Critères et comparaison d’images</h3>
  <ul>
    <li>Ajout du critère de Manhattan pour calculer le score d’une image.</li>
    <li>Ajout de la classe <code>CompareImages</code> permettant de comparer deux images entre elles.</li>
  </ul>

  <h3>Gestion des images</h3>
  <ul>
    <li>Ajout de la classe <code>ImageReader</code> permettant d’obtenir un tableau des chemins d’images dans le dossier <code>.images/</code>.</li>
  </ul>

  <h3>Profiling et statistiques</h3>
  <ul>
    <li>Ajout de la classe <code>Profiler</code> permettant d’obtenir des statistiques sur le cassage de clé, telles que le temps de cassage et le nombre d’opérations effectuées.</li>
  </ul>

  <h3>Interface graphique</h3>
  <ul>
    <li>Ajout d’une interface graphique permettant d’utiliser le projet via différentes pages :</li>
    <ul>
      <li><strong>Page Brouillage</strong> : permet de brouiller une image en sélectionnant un fichier dans le dossier <code>images</code>.</li>
      <li><strong>Page Débrouillage</strong> : permet de débrouiller une image brouillée présente dans le dossier <code>images</code>.</li>
    </ul>
  </ul>
</section>
