import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Debrouillage {
  
    /**
    * Calcule la distance euclidienne entre deux lignes d'une image en niveaux de gris.
    * @param imageGris La matrice de l'image en niveaux de gris
    * @param l1 L'index de la première ligne
    * @param l2 L'index de la deuxième ligne
    * @return La distance euclidienne entre les deux lignes
    */
    public static double euclideanDistance(int[][] imageGris , int l1, int l2) {
        double somme=0.0;
        for (int i = 0; i < imageGris[0].length; i++) {
            int rgb1=imageGris[l1][i];
            int rgb2=imageGris[l2][i];
            somme+=(rgb1-rgb2)*(rgb1-rgb2);
        }
        return Math.sqrt(somme);
    }

    /**
     * Calcule le score euclidien total d'une image en sommant les distances
     * entre chaque paire de lignes consécutives.
     * Plus le score est faible, plus l'image est probablement correcte.
     * @param imageGris La matrice de l'image en niveaux de gris
     * @return Le score euclidien total (plus petit = meilleur)
     */
    public static double scoreEuclidean(int[][] imageGris) {
        double score=0.0;
        for (int i = 1; i < imageGris.length; i=i+1) {
            score+=euclideanDistance(imageGris, i, i-1);
        }
        return (score);
    }

    /**
     * Casse la clé de chiffrement en testant toutes les clés possibles (1 à 32767)
     * en utilisant le score euclidien comme critère de qualité.
     * @param image L'image chiffrée à débrouiller
     * @return La clé qui donne le meilleur score euclidien (distance minimale)
     */
    public static int breakKeyEuclid(BufferedImage image) {
        int hauteur=image.getHeight();
        double distanceMin =Double.MAX_VALUE;
        int[][] tab2DGL=Brouillimg.rgb2gl(image);
        int meilleurCandidat=0;
        for (int i = 1; i < 32768; i++) {
            int[] permCandidat=Brouillimg.generatePermutation(hauteur, i);
            int[][] rgb2glImage=UnScrambleLignesTab2D(tab2DGL, permCandidat);
            double score=scoreEuclidean(rgb2glImage);
            if (score<distanceMin) {
                distanceMin=score;
                meilleurCandidat=i;
            }
        }
        return meilleurCandidat;
    }

    /**
     * Casse la clé de chiffrement en testant toutes les clés possibles (1 à 32767)
     * en utilisant le score de Pearson comme critère de qualité.
     * @param image L'image chiffrée à débrouiller
     * @return La clé qui donne le meilleur score de Pearson (corrélation maximale)
     */
    public static int breakKeyPearson(BufferedImage image) {
        // Récupérer la hauteur de l'image (nombre de lignes)
        int[][] tab2DGL=Brouillimg.rgb2gl(image);
        int hauteur = image.getHeight();
        
        // Initialiser le meilleur score à la valeur minimale possible
        // Pour Pearson, on cherche le MAXIMUM (corrélation la plus élevée)
        double scoreMax = Double.MIN_VALUE;
        
        // Variable pour stocker la meilleure clé trouvée
        int meilleurCandidat = 0;
        
        // Tester toutes les clés possibles de 1 à 32767
        for (int i = 1; i < 32768; i++) {
            // Générer la permutation correspondant à cette clé
            int[] permCandidat = Brouillimg.generatePermutation(hauteur, i);
            
            // Débrouiller l'image avec cette image candidate
            // Convertir l'image RGB en niveaux de gris pour le calcul du score
            int[][] rgb2glImage = unScrambleLignesTab2D(tab2DGL, permCandidat);
            
            // Calculer le score de Pearson pour cette image candidate
            double score = scorePearson(rgb2glImage);
            // Si ce score est plus grand que le meilleur score actuel
            if (score > scoreMax) {
                // Mettre à jour le meilleur score
                scoreMax = score;
                // Sauvegarder cette clé comme meilleur candidat
                meilleurCandidat = i;
            }
        }
        
        // Retourner la clé qui a donné le meilleur score
        return meilleurCandidat;
    }


    /**
     * Calcule le coefficient de corrélation de Pearson entre deux lignes.
     * Le résultat est compris entre -1 et 1 (1 = corrélation parfaite).
     * @param rowX La première ligne
     * @param rowY La deuxième ligne
     * @return Le coefficient de corrélation ρ(x,y)
     */
    public static double pearsonCorrelation(int[] rowX, int[] rowY) {
        int longueur = rowX.length;
        double numerator = 0.0;
        double moyenneX = 0.0;
        double moyenneY = 0.0;
        for (int i = 0; i < longueur; i++) {
            moyenneX += rowX[i];
            moyenneY += rowY[i];
        }
        moyenneX /= longueur;
        moyenneY /= longueur;

        for (int i=0 ; i<longueur; i++) {
            numerator += (rowX[i] - moyenneX) * (rowY[i] - moyenneY);
        }

        double sumX = 0.0;
        double sumY = 0.0;

        for (int i = 0; i < longueur; i++) {
            sumX += (rowX[i] - moyenneX) * (rowX[i] - moyenneX);
            sumY += (rowY[i] - moyenneY) * (rowY[i] - moyenneY);
        }
        double denominator = Math.sqrt(sumX) * Math.sqrt(sumY);

        return numerator / denominator;
    }

    /**
     * Calcule le score de Pearson total d'une image en sommant les corrélations
     * entre chaque paire de lignes consécutives.
     * Plus le score est élevé, plus l'image est probablement correcte.
     * @param image La matrice de l'image en niveaux de gris
     * @return Le score de Pearson total (plus grand = meilleur)
     */
    public static double scorePearson(int[][] image) {
        double scoreTotal = 0.0;
        
        // Parcourir toutes les paires de lignes consécutives
        for (int i = 0; i < image.length - 1; i++) {
            // Calculer la corrélation entre la ligne i et la ligne i+1
            double correlation = pearsonCorrelation(image[i], image[i + 1]);
            
            // Ajouter cette corrélation au score total
            scoreTotal += correlation;
        }
        
        return scoreTotal;
    }



  
    /**
    * Calcule la distance euclidienne entre deux lignes d'une image en niveaux de gris.
    * @param imageGris La matrice de l'image en niveaux de gris
    * @param l1 L'index de la première ligne
    * @param l2 L'index de la deuxième ligne
    * @return La distance euclidienne entre les deux lignes
    */
    public static long euclideanDistanceOpti(int[][] imageGris , int l1, int l2) {
        long somme=0;
        for (int i = 0; i < imageGris[0].length/3; i++) {
            int rgb1=imageGris[l1][i];
            int rgb2=imageGris[l2][i];
            somme+=(rgb1-rgb2)*(rgb1-rgb2);
        }
        return somme;
    }

    /**
     * Calcule le score euclidien total d'une image en sommant les distances
     * entre chaque paire de lignes consécutives.
     * Plus le score est faible, plus l'image est probablement correcte.
     * @param imageGris La matrice de l'image en niveaux de gris
     * @return Le score euclidien total (plus petit = meilleur)
     */
    public static long scoreEuclideanOpti(int[][] imageGris) {
        long score=0;
        for (int i = 1; i < imageGris.length; i=i+1) {
            score+=euclideanDistance(imageGris, i, i-1);
        }
        return score;
    }

    /**
     * Casse la clé de chiffrement en testant toutes les clés possibles (1 à 32767)
     * en utilisant le score euclidien comme critère de qualité.
     * @param image L'image chiffrée à débrouiller
     * @return La clé qui donne le meilleur score euclidien (distance minimale)
     */
    public static int breakKeyEuclidOpti(BufferedImage image) {
        int hauteur=image.getHeight();
        long distanceMin =Long.MAX_VALUE;
        int[][] tab2DGL=Brouillimg.rgb2gl(image);
        int meilleurCandidat=0;
        for (int i = 0; i < 32768; i++) {
            int[] permCandidat=Brouillimg.generatePermutation(hauteur, i);
            int[][] rgb2glImage=unScrambleLignesTab2D(tab2DGL, permCandidat);
            long score=scoreEuclideanOpti(rgb2glImage);
            if (score<distanceMin) {
                distanceMin=score;
                meilleurCandidat=i;
            }
        }
        return meilleurCandidat;
    }
    /**
     * Casse la clé de chiffrement selon la méthode spécifiée.
     * @param image L'image chiffrée à débrouiller
     * @param methode La méthode à utiliser : "Euclid", "Pearson" ou "optimisation"
     * @return La clé trouvée, ou -1 si la méthode est invalide
     */
    /**
     * Casse la clé de chiffrement en utilisant une méthode optimisée en deux étapes :
     * 1) Teste les 128 premiers bits (bits de poids faible)
     * 2) Affine avec les 256 combinaisons des bits de poids fort
     * Cette méthode est plus rapide que de tester toutes les 32768 clés.
     * @param image L'image chiffrée à débrouiller
     * @return La clé optimale trouvée
     */


    public static int breakKeyIntelligent(BufferedImage image) {
        double distanceMin=Double.MAX_VALUE;
        int[][] tab2DGL=Brouillimg.rgb2gl(image);
        int hauteur=image.getHeight();
        int meilleurS=0;
        for (int s = 0; s < 128; s++) {
            if (! Brouillimg.validKey(s, hauteur)) {continue;}
            int[] permCandidatS=Brouillimg.generatePermutation(hauteur, s);
            int[][] rgb2glImage=unScrambleLignesTab2D(tab2DGL, permCandidatS);
            double score = scoreEuclideanOpti(rgb2glImage);
            if (score<distanceMin) {
                distanceMin=score;
                meilleurS=s;
            }
        }
        int meilleurCleCandidate=meilleurS<<7;
        for (int r = 0; r < 256; r++) {
            int cleCandidat=(r << 7) | meilleurS;
            if (! Brouillimg.validKey(cleCandidat, hauteur)) {System.out.println("o");}
            int[] permCandidat=Brouillimg.generatePermutation(hauteur, 
                cleCandidat);
            int[][] rgb2glImage=unScrambleLignesTab2D(tab2DGL, permCandidat);
            double score = scoreEuclideanOpti(rgb2glImage);
            if (score<distanceMin) {
                distanceMin=score;
                meilleurCleCandidate=cleCandidat;
            }
        }
        return meilleurCleCandidate;
    }

    public static int breakKey(BufferedImage image, String methode) {
        switch (methode) {
            case "Euclid":
                return breakKeyEuclid(image);
            case "Pearson":
                return breakKeyPearson(image);
            case "EuclidOpti":
                return breakKeyEuclidOpti(image);
            case "Intelligent":
                return breakKeyIntelligent(image);
            default:
                return -1;
        }
    }


    public static void main(String[] args) throws IOException{
        if (args.length<2) {
            System.out.println("Debrouillage <chemin_image_brouillée> <méthode utilisée> [creer_image_debrouillée]");
            return;
        }
        BufferedImage inputimg = ImageIO.read(new File(args[0]));
        String methode = args[1];
        int cle=breakKey(inputimg, methode);
        if (args.length==3) {
            String outChemin=args[2];
            int[] perm=Brouillimg.generatePermutation(inputimg.getHeight(),cle);
            BufferedImage outImage=Brouillimg.unscrambleLines(inputimg, perm);
            System.out.print("image écrite : "+outChemin);
            ImageIO.write(outImage, "png",new File(outChemin));
        }
        else {
            System.out.println("La clé pour débrouiller l'image vaut : "+cle);
        }
    }

    public static int[][] unScrambleLignesTab2D(int[][] tab, int[] permutation) {
        int[][] resultat = new int[tab.length][tab[0].length];
        for (int i = 0; i < permutation.length; i++) {
            int srcY=permutation[i];
            for (int j = 0; j < tab[0].length; j++) {
                resultat[i][j]=tab[srcY][j];
            }
        }
        return resultat;
    }

}
