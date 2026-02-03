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


<h2>Contribution :</h2>
<h4> Sofiane Tacherift </h4>
<ul>
  <li>Organisation de la structure du projet et des classes.</li>
  <li>Implémentation de la méthode d’Euclide pour casser la clé d’une image brouillée.</li>
  <li>Optimisation des méthodes Euclid et Pearson.</li>
  <li>Création d’une méthode hybride : s via Euclid, r via Pearson.</li>
  <li>Ajout du critère de Manhattan pour le calcul du score.</li>
  <li>Création de la classe <code>CompareImages</code> pour comparer deux images.</li>
  <li>Création de la classe <code>ImageReader</code> pour récupérer les chemins du dossier <code>.images/</code>.</li>
  <li>Création de la classe <code>Profiler</code> pour mesurer temps et opérations du cassage de clé.</li>
  <li>Ajout d’une interface graphique avec :
    <ul>
      <li>Une page Brouillage (sélection et brouillage d’image).</li>
      <li>Une page Débrouillage (décryptage d’image brouillée).</li>
    </ul>
  </li>
</ul>
</section>
