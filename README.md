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
