import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Debrouillage {
    public static double euclideanDistance(int[][] imageGris , int l1, int l2) {
        double somme=0.0;
        for (int i = 0; i < imageGris[0].length; i++) {
            int rgb1=imageGris[l1][i];
            int rgb2=imageGris[l2][i];
            somme+=(rgb1-rgb2)*(rgb1-rgb2);
        }
        return Math.sqrt(somme);
    }

    public static double scoreEuclidean(int[][] imageGris) {
        double score=0.0;
        for (int i = 1; i < imageGris.length; ++i) {
            score+=euclideanDistance(imageGris, i, i-1);
        }
        return (score);
    }

    public static int breakKeyEuclid(BufferedImage image) {
        int hauteur=image.getHeight();
        double distanceMin =Double.MAX_VALUE;
        int meilleurCandidat=0;
        for (int i = 1; i < 32768; i++) {
            int[] permCandidat=Brouillimg.generatePermutation(hauteur, i);
            BufferedImage imageCandidate=Brouillimg.unscrambleLines(image, permCandidat);
            int[][] rgb2glImage=Brouillimg.rgb2gl(imageCandidate);
            double score=scoreEuclidean(rgb2glImage);
            if (score<distanceMin) {
                distanceMin=score;
                meilleurCandidat=i;
            }
        }
        return meilleurCandidat;
    }

    public static int breakKeyOpti(BufferedImage image) {
        double distanceMin=Double.MAX_VALUE;
        int hauteur=image.getHeight();
        int meilleurS=0;
        for (int s = 0; s < 128; s++) {
            int[] permCandidatS=Brouillimg.generatePermutation(hauteur, s);
            BufferedImage imageCandidate=Brouillimg.unscrambleLines(image, permCandidatS);
            int[][] rgb2glImage=Brouillimg.rgb2gl(imageCandidate);
            double score = scoreEuclidean(rgb2glImage);
            if (score<distanceMin) {
                distanceMin=score;
                meilleurS=s;
            }
        }
        int meilleurCleCandidate=meilleurS<<7;
        for (int r = 0; r <= 256; r++) {
            int cleCandidat=(r << 7) | meilleurS;
            int[] permCandidat=Brouillimg.generatePermutation(hauteur, cleCandidat);
            BufferedImage imageCandidate=Brouillimg.unscrambleLines(image, permCandidat);
            int[][] rgb2glImage=Brouillimg.rgb2gl(imageCandidate);
            double score = scoreEuclidean(rgb2glImage);
            if (score<distanceMin) {
                distanceMin=score;
                meilleurCleCandidate=cleCandidat;
            }
        }
        return meilleurCleCandidate;
    }

    public static int breakKeyPearson(BufferedImage image) {
        // Récupérer la hauteur de l'image (nombre de lignes)
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
            BufferedImage imageCandidate = Brouillimg.unscrambleLines(image, permCandidat);
            
            // Convertir l'image RGB en niveaux de gris pour le calcul du score
            int[][] rgb2glImage = Brouillimg.rgb2gl(imageCandidate);
            
            // Calculer le score de Pearson pour cette image candidate
            double score = scorePearson(rgb2glImage);
            
            // Si ce score est MEILLEUR (plus grand) que le meilleur score actuel
            // Note : pour Pearson, on utilise ">" car on cherche le maximum
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

    public static int breakKey(BufferedImage image, String methode) {
        switch (methode) {
            case "Euclid":
                return breakKeyEuclid(image);
            case "Pearson":
                return breakKeyPearson(image);
            case "optimisation":
                return breakKeyOpti(image);
            default:
                return -1;
        }
    }

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

    public static void main(String[] args) throws IOException{
        if (args.length<3) {System.out.println("Debrouillage <chemin_image_brouillée> <méthode utilisée> <booleen créer_image> [chemin_sortie]");
            return;
        }
        BufferedImage inputimg = ImageIO.read(new File(args[0]));
        String methode = args[1];
        boolean creerImage=(args[2].equals("true"));
        int cle=breakKey(inputimg, methode);
        if (creerImage) {
            String outChemin=(args.length==4) ? args[3] : "out.png";
            int[] perm=Brouillimg.generatePermutation(inputimg.getHeight(),cle);
            BufferedImage outImage=Brouillimg.unscrambleLines(inputimg, perm);
            System.out.print("image écrite : ");
            ImageIO.write(outImage, "png",new File(outChemin));
        }
        else {
            System.out.println(cle);
        }
    }

}
