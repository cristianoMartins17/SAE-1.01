/*MARTINS Cristiano (référent)
  TACHERIFT Sofiane
  groupe 20
*/

public class Score {

    // ==================Euclid================================================================

    /**
     * Calcule la distance euclidienne entre deux lignes d'une image en niveaux de
     * gris.
     * 
     * @param imageGris La matrice de l'image en niveaux de gris
     * @param l1        L'index de la première ligne
     * @param l2        L'index de la deuxième ligne
     * @return La distance euclidienne entre les deux lignes
     */
    public static double euclideanDistance(int[] l1, int[] l2) {
        double somme = 0.0;
        for (int i = 0; i < l1.length; i++) {
            int rgb1 = l1[i];
            int rgb2 = l2[i];
            somme += (rgb1 - rgb2) * (rgb1 - rgb2);
        }
        return Math.sqrt(somme);
    }

    /**
     * Calcule le score euclidien total d'une image en sommant les distances
     * entre chaque paire de lignes consécutives.
     * Plus le score est faible, plus l'image est probablement correcte.
     * 
     * @param imageGris La matrice de l'image en niveaux de gris
     * @return Le score euclidien total (plus petit = meilleur)
     */
    public static double scoreEuclidean(int[][] tab2DGL, int[] permutation) {
        double score = 0.0;
        for (int i = 1; i < tab2DGL.length; i = i + 1) {
            score += euclideanDistance(tab2DGL[permutation[i]],
                    tab2DGL[permutation[i - 1]]);
        }
        return (score);
    }

    // =======================================================================================

    // ===========================Pearson==================================================

    /**
     * Calcule le coefficient de corrélation de Pearson entre deux lignes.
     * Le résultat est compris entre -1 et 1 (1 = corrélation parfaite).
     * 
     * @param rowX La première ligne
     * @param rowY La deuxième ligne
     * @param moyX la moyenne de la ligne x
     * @param moyY la moyenne de la ligne y
     * @param varX les ecarts de la ligne x
     * @param varY les ecarts de la ligne y
     * @return Le coefficient de corrélation ρ(x,y)
     */
    public static double pearsonCorrelation(int[] rowX, int[] rowY, double moyX, double moyY, double ecartsX,double ecartsY) {
        double numerator = 0.0;
        for (int i = 0; i < rowX.length; i++) {
            numerator += ((rowX[i] - moyX) * (rowY[i] - moyY));
        }

        double denominator = (Math.sqrt(ecartsX) * Math.sqrt(ecartsY));
        if (denominator == 0.0) {
            return 0.0;
        }
        return numerator / denominator;
    }

    /**
     * Calcule le score de Pearson total d'une image en sommant les corrélations
     * entre chaque paire de lignes consécutives.
     * Plus le score est élevé, plus l'image est probablement correcte. Le score
     * euclidien a été amélioré
     * 
     * @param tag2DGL     La matrice de l'image en niveaux de gris
     * @param permutation la permutation que l'on teste
     * @param moyennes    le tableau qui contient la moyenne de chaque ligne i
     * @param ecarts      le tableau qui contient les ecarts de chaque ligne i
     * @return Le score de Pearson total (plus grand = meilleur)
     */
    public static double scorePearson(int[][] tab2DGL, int[] permutation, double[] moyennes, double[] ecarts) {
        double scoreTotal = 0.0;
        for (int i = 0; i < tab2DGL.length - 1; i++) { /* On précalcule les moyennes et les écarts */
            double moyX = moyennes[permutation[i]];
            double moyY = moyennes[permutation[i + 1]];
            double ecartsX = ecarts[permutation[i]];
            double ecartsY = ecarts[permutation[i + 1]];
            double correlation = pearsonCorrelation(tab2DGL[permutation[i]],
                    tab2DGL[permutation[i + 1]], moyX,
                    moyY, ecartsX, ecartsY);
            // Ajouter cette corrélation au score total
            scoreTotal += correlation;
        }

        return scoreTotal;
    }

    // ======================================================================================================

    // ====================================EuclidOpti=========================================================

    /**
     * Calcule la distance euclidienne entre deux lignes d'une image en niveaux de
     * gris.
     * 
     * @param imageGris La matrice de l'image en niveaux de gris
     * @param l1        L'index de la première ligne
     * @param l2        L'index de la deuxième ligne
     * @return La distance euclidienne entre les deux lignes
     */
    public static long euclideanDistanceOpti(int[] l1, int[] l2) {
        long somme = 0;
        for (int i = 0; i < l1.length; i += 1) {
            int rgb1 = l1[i];
            int rgb2 = l2[i];
            somme += (rgb1 - rgb2) * (rgb1 - rgb2);
        }
        return somme;
    }

    /**
     * Calcule le carré de la distance euclidienne
     * entre chaque paire de lignes consécutives et utilise le type long pour un
     * gain de temps.
     * Plus le score est faible, plus l'image est probablement correcte.
     * 
     * @param tab2GL      La matrice de l'image en niveaux de gris
     * @param permutation Le tableau de permutation
     * @return Le score euclidien total (plus petit = meilleur)
     */
    public static long scoreEuclideanOpti(int[][] tab2DGL, int[] permutation) {
        long score = 0;
        for (int i = 1; i < tab2DGL.length; i = i + 1) {
            score += euclideanDistanceOpti(tab2DGL[permutation[i]],
                    tab2DGL[permutation[i - 1]]);
        }
        return score;
    }

    // ===============================================================================================

    // ====================================Hybrid=====================================================================================

    /**
     * Cette fonction trouve le meilleur s pour un tableau de gris en les
     * testant de 0 à 127
     * 
     * @param tab2DGL le tableau de gris en question
     * 
     * @return le meilleur s possible en se basant sur le score d'Euclide optimisé
     * 
     */
    public static int trouverMeilleurS(int[][] tab2DGL) {
        int hauteur = tab2DGL.length;
        int meilleurS = 0;
        long scoreMin = Long.MAX_VALUE;
        for (int s = 0; s < 128; s++) {
            int[] permCandidatS = Brouillimg.generatePermutation(hauteur, s);
            long score = scoreEuclideanOpti(tab2DGL, permCandidatS);
            if (score < scoreMin) {
                scoreMin = score;
                meilleurS = s;
            }
        }
        return meilleurS;
    }

    /**
     * Cette fonction trouve la meilleur clé une fois qu'on connait le meilleur s
     * en se basant sur le score de Pearson amélioré
     * 
     * @param tab2DGL   le tableau 2D d'entier de gris
     * @param meilleurS le meilleur s connu
     * @return la meilleur clé pour ce s
     */

    public static int trouverMeilleurCle(int[][] tab2DGL, int meilleurS) {
        int meilleurCleCandidate = meilleurS << 7;
        double scoreMax = -Double.MAX_VALUE;
        int hauteur = tab2DGL.length;
        double[] moyennes = new double[tab2DGL.length];
        for (int i = 0; i < tab2DGL.length; i++) {
            moyennes[i] = calculerMoyenne(tab2DGL[i]);
        }
        double[] ecarts = new double[tab2DGL.length];
        for (int i = 0; i < moyennes.length; i++) {
            ecarts[i] = calculerEcarts(tab2DGL[i], moyennes[i]);
        }
        for (int r = 0; r < 256; r++) {
            int cleCandidat = (r << 7) | meilleurS;
            int[] permCandidat = Brouillimg.generatePermutation(hauteur, cleCandidat);
            double score = scorePearson(tab2DGL, permCandidat, moyennes, ecarts);
            if (score > scoreMax) {
                scoreMax = score;
                meilleurCleCandidate = cleCandidat;
            }
        }
        return meilleurCleCandidate;
    }

    // ==================================================================================

    /**
     * calcule la moyenne d'une ligne
     * 
     * @param ligne la ligne dont on veut calculer la moyenne
     * @return la moyenne
     */

    public static double calculerMoyenne(int[] ligne) {
        int somme = 0;
        for (int i = 0; i < ligne.length; i++) {
            somme += ligne[i];
        }
        return ((double) (somme)) / ligne.length;
    }

    /**
     * calcule la somme des carrés des ecarts d'une ligne
     * 
     * @param ligne   la ligne dont on veut les ecarts
     * @param moyenne la moyenne de la ligne
     * @return les ecarts
     */

    public static double calculerEcarts(int[] ligne, double moyenne) {
        double ecarts = 0.0;
        for (int i = 0; i < ligne.length; i++) {
            double difference = (ligne[i] - moyenne);
            ecarts += difference * difference;
        }
        return ecarts;
    }



    public static long manathanDistance(int[] rowX, int[] rowY) {
        long score=0;
        for (int i = 0; i < rowX.length; i++) {
            score+=Math.abs(rowX[i]-rowY[i]);
        }
        return score;
    }

    public static long scoreManathan(int[][] tab2DGL, int[] permutation) {
        long score=0;
        for (int i = 0; i < tab2DGL.length-1; i++) {
            int l1=permutation[i];
            int l2=permutation[i+1];
            score+=manathanDistance(tab2DGL[l1], tab2DGL[l2]);       
        }
        return score;
    }
    

}
