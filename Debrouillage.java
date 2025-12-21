import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Debrouillage {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Debrouillage <image_brouillée> <méthode utilisée> [creer_image_debrouillée]");
            System.exit(1);
        }
        BufferedImage inputimg = ImageIO.read(new File(args[0]));
        String methode = args[1];
        long tempsDepart = System.currentTimeMillis();
        int cle = breakKey(inputimg, methode);
        if (cle == -1) {
            System.out.println("methode non valide");
            System.exit(2);
        }
        long tempsFin = System.currentTimeMillis();
        if (args.length == 3) {
            String outChemin = args[2];
            int[] perm = Brouillimg.generatePermutation(inputimg.getHeight(), cle);
            BufferedImage outImage = Brouillimg.unscrambleLines(inputimg, perm);
            System.out.println("image écrite : " + outChemin);
            ImageIO.write(outImage, "png", new File(outChemin));
        }
        System.out.println("La clé pour débrouiller l'image vaut : " + cle);
        System.out.println("temps de débrouillage : " + ((double) (tempsFin - tempsDepart)) / 1000 + " s");
    }

    /**
     * Casse la clé de chiffrement en testant toutes les clés possibles (1 à 32767)
     * en utilisant le score euclidien comme critère de qualité.
     * 
     * @param image L'image chiffrée à débrouiller
     * @return La clé qui donne le meilleur score euclidien (distance minimale)
     */
    // plûtot lent
    public static int breakKeyEuclid(BufferedImage image) {
        int hauteur = image.getHeight();
        double distanceMin = Double.MAX_VALUE;
        int[][] tab2DGL = Brouillimg.rgb2gl(image);
        int meilleurCandidat = 0;
        for (int i = 0; i < 32768; i++) {
            int[] permCandidat = Brouillimg.generatePermutation(hauteur, i);
            double score = Score.scoreEuclidean(tab2DGL, permCandidat);
            if (score < distanceMin) {
                distanceMin = score;
                meilleurCandidat = i;
            }
        }
        return meilleurCandidat;
    }

    /**
     * Casse la clé de chiffrement en testant toutes les clés possibles (1 à 32767)
     * en utilisant le score de Pearson comme critère de qualité.
     * 
     * @param image L'image chiffrée à débrouiller
     * @return La clé qui donne le meilleur score de Pearson (corrélation maximale)
     */
    // très lent
    public static int breakKeyPearson(BufferedImage image) {
        int[][] tab2DGL = Brouillimg.rgb2gl(image);
        int hauteur = image.getHeight();
        // Pour Pearson, on cherche le MAXIMUM (corrélation la plus élevée)
        double scoreMax = -Double.MAX_VALUE;
        int meilleurCandidat = 0;
        for (int i = 0; i < 32768; i++) {
            int[] permCandidat = Brouillimg.generatePermutation(hauteur, i);
            double score = Score.scorePearson(tab2DGL, permCandidat);
            if (score > scoreMax) {
                scoreMax = score;
                meilleurCandidat = i;
            }
        }
        return meilleurCandidat;
    }

    /**
     * Casse la clé de chiffrement en testant toutes les clés possibles (1 à 32767)
     * en utilisant le score euclidien optimisé comme de qualité.
     * Le score euclidien a été optimisé pour prendre le moins de temps possible
     * 
     * @param image L'image chiffrée à débrouiller
     * @return La clé qui donne le meilleur score euclidien (distance minimale)
     */
    // environ 2.5 fois plus rapide que le breakKeyEuclid normal
    public static int breakKeyEuclidOpti(BufferedImage image) {
        int hauteur = image.getHeight();
        long distanceMin = Long.MAX_VALUE;
        int[][] tab2DGL = Brouillimg.rgb2gl(image);
        int meilleurCandidat = 0;
        for (int i = 0; i < 32768; i++) {
            int[] permCandidat = Brouillimg.generatePermutation(hauteur, i);
            long score = Score.scoreEuclideanOpti(tab2DGL, permCandidat);
            if (score < distanceMin) {
                distanceMin = score;
                meilleurCandidat = i;
            }
        }
        return meilleurCandidat;
    }

    /**
     * Debrouille une image par brute-force en utilisant
     * le score euclidien comme critère de qualité en utilisant
     * une optimisation du score d'euclide
     * 
     * @param image l'image a débrouiller
     * @return la clé qui a été utilisée pour permuter les lignes de l'image
     */
    // environ 2x plus rapide que le breakKeyPearson
    public static int breakKeyPearsonOpti(BufferedImage image) {
        int[][] tab2DGL = Brouillimg.rgb2gl(image);
        double[] moyennes = new double[tab2DGL.length];
        double[] ecarts = new double[tab2DGL.length];
        for (int i = 0; i < moyennes.length; i++) {
            moyennes[i] = Score.calculerMoyenne(tab2DGL[i]);
        }
        for (int i = 0; i < moyennes.length; i++) {
            ecarts[i] = Score.calculerEcarts(tab2DGL[i], moyennes[i]);
        }
        int hauteur = image.getHeight();
        // Initialiser le meilleur score à la valeur minimale possible
        // Pour Pearson, on cherche le MAXIMUM (corrélation la plus élevée)
        double scoreMax = -Double.MAX_VALUE;
        // Variable pour stocker la meilleure clé trouvée
        int meilleurCandidat = 0;
        for (int i = 0; i < 32768; i++) {
            int[] permCandidat = Brouillimg.generatePermutation(hauteur, i);
            double score = Score.scorePearsonOpti(tab2DGL, permCandidat, moyennes, ecarts);
            // Si ce score est plus grand que le meilleur score actuel, on le met à jour
            if (score > scoreMax) {
                scoreMax = score;
                meilleurCandidat = i;
            }
        }
        return meilleurCandidat;
    }

    /**
     * Cette fonction casse la clé d'une manière optimisée
     * 
     * @param image l'image à débrouiller
     * @return la clé qui a été utilisée pour brouiler l'image
     */
    // il explose les autres break key
    public static int breakKeyHybrid(BufferedImage image) {
        int[][] tab2DGL = Brouillimg.rgb2gl(image); // on trouve le meilleur s puis on l'utilise pour trouver la clé
        int meilleurS = Score.trouverMeilleurS(tab2DGL);
        return Score.trouverMeilleurCle(tab2DGL, meilleurS);
    }

    /**
     * Cette fonction renvoie la clé en prenant en charge plusieurs méthodes
     * 
     * @param image   dont on veut casser la clé
     * @param methode la méthode a utiliser
     * @return la clé
     */
    public static int breakKey(BufferedImage image, String methode) {
        switch (methode) {
            case "Euclid":
                return breakKeyEuclid(image);
            case "Pearson":
                return breakKeyPearson(image);
            case "EuclidOpti":
                return breakKeyEuclidOpti(image);
            case "PearsonOpti":
                return breakKeyPearsonOpti(image);
            case "Hybrid":
                return breakKeyHybrid(image);
            case "Auto":
                /*
                 * on fait ca car des fois, pour une image petite, la méthode hybride
                 * renvoie la clé pour débrouiller l'image mais en invérsée. On utilise donc
                 * pearsonOpti
                 * car l'image est petite et qu'il est fiable
                 */
                if (image.getHeight() < 512) {
                    return breakKeyPearsonOpti(image);
                } else {
                    return breakKeyHybrid(image);
                }
            default:
                return -1;
        }
    }

}
