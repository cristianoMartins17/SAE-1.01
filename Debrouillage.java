import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Debrouillage {


    public static void main(String[] args) throws IOException{
        if (args.length<2) {
            System.out.println("Debrouillage <image_brouillée> <méthode utilisée> [creer_image_debrouillée]");
            System.exit(1);;
        }
        BufferedImage inputimg = ImageIO.read(new File(args[0]));
        String methode = args[1];
        long tempsDepart=System.currentTimeMillis();
        int cle=breakKey(inputimg, methode);
        if (cle==-1) {
            System.out.println("methode non valide");
            System.exit(2);
        }
        long tempsFin=System.currentTimeMillis();
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
        System.out.println("temps de débrouillage : "+((double)(tempsFin-tempsDepart))/1000 +" s");
    }


    /**
    * Calcule la distance euclidienne entre deux lignes d'une image en niveaux de gris.
    * @param imageGris La matrice de l'image en niveaux de gris
    * @param l1 L'index de la première ligne
    * @param l2 L'index de la deuxième ligne
    * @return La distance euclidienne entre les deux lignes
    */
    public static double euclideanDistance( int[] l1, int[] l2) {
        double somme=0.0;
        for (int i = 0; i < l1.length; i++) {
            int rgb1=l1[i];
            int rgb2=l2[i];
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
    public static double scoreEuclidean(int[][] tab2DGL, int[] permutation) {
        double score=0.0;
        for (int i = 1; i < tab2DGL.length; i=i+1) {
            score+=euclideanDistance(tab2DGL[permutation[i]], tab2DGL[permutation[i-1]]);
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
        for (int i = 0; i < 32768; i++) {
            int[] permCandidat=Brouillimg.generatePermutation(hauteur, i);
            double score=scoreEuclidean(tab2DGL, permCandidat);
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
        double scoreMax = -Double.MAX_VALUE;
        
        // Variable pour stocker la meilleure clé trouvée
        int meilleurCandidat = 0;
        
        // Tester toutes les clés possibles de 1 à 32767
        for (int i = 0; i < 32768; i++) {
            // Générer la permutation correspondant à cette clé
            int[] permCandidat = Brouillimg.generatePermutation(hauteur, i);
            // Débrouiller l'image avec cette image candidate
            // Convertir l'image RGB en niveaux de gris pour le calcul du score
            
            // Calculer le score de Pearson pour cette image candidate
            double score = scorePearson(tab2DGL, permCandidat);
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
        double sumX = 0.0;
        double sumY = 0.0;
        for (int i=0 ; i<longueur; i++) {
            numerator += (rowX[i] - moyenneX) * (rowY[i] - moyenneY);
            sumX += (rowX[i] - moyenneX) * (rowX[i] - moyenneX);
            sumY += (rowY[i] - moyenneY) * (rowY[i] - moyenneY);
        }

        double denominator = Math.sqrt(sumX) * Math.sqrt(sumY);
        if (denominator==0.0) {return 0.0;}
        return numerator / denominator;
    }

    /**
     * Calcule le score de Pearson total d'une image en sommant les corrélations
     * entre chaque paire de lignes consécutives.
     * Plus le score est élevé, plus l'image est probablement correcte.
     * @param image La matrice de l'image en niveaux de gris
     * @return Le score de Pearson total (plus grand = meilleur)
     */
    public static double scorePearson(int[][] tab2DGL, int[] permutation) {
        double scoreTotal = 0.0;
        // Parcourir toutes les paires de lignes consécutives
        for (int i = 0; i < tab2DGL.length - 1; i++) {
            // Calculer la corrélation entre la ligne i et la ligne i+1
            double correlation = pearsonCorrelation(tab2DGL[permutation[i]], tab2DGL[permutation[i + 1]]);
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
    public static long euclideanDistanceOpti(int[][] imageGris , int[] l1, int[] l2) {
        long somme=0;
        for (int i = 0; i < imageGris[0].length; i+=2) {
            int rgb1=l1[i];
            int rgb2=l2[i];
            somme+=(rgb1-rgb2)*(rgb1-rgb2);
        }
        return somme;
    }

    /**
     * Calcule le carré de la distance euclidienne
     * entre chaque paire de lignes consécutives et utilise le type long pour un gain de temps.
     * Plus le score est faible, plus l'image est probablement correcte.
     * @param tab2GL La matrice de l'image en niveaux de gris
     * @param permutation Le tableau de permutation
     * @return Le score euclidien total (plus petit = meilleur)
     */
    public static long scoreEuclideanOpti(int[][] tab2DGL, int[] permutation) {
        long score=0;
        for (int i = 1; i < tab2DGL.length; i=i+1) {
            score+=euclideanDistanceOpti(tab2DGL,  tab2DGL[permutation[i]],tab2DGL[permutation[i-1]]);
        }
        return score;
    }

    /**
     * Casse la clé de chiffrement en testant toutes les clés possibles (1 à 32767)
     * en utilisant le score euclidien optimisé comme de qualité.
     * Le score euclidien a été optimisé pour prendre le moins de temps possible
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
            long score=scoreEuclideanOpti(tab2DGL, permCandidat);
            if (score<distanceMin) {
                distanceMin=score;
                meilleurCandidat=i;
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

    public static int breakKeyPearsonOpti(BufferedImage image) {
        // Récupérer la hauteur de l'image (nombre de lignes)
        int[][] tab2DGL=Brouillimg.rgb2gl(image);
        double[] moyennes=new double[tab2DGL.length];
        double[] variances=new double[tab2DGL.length];
        for (int i = 0; i < moyennes.length; i++) {
            moyennes[i]=calculerMoyenne(tab2DGL[i]);
        }
        for (int i = 0; i < moyennes.length; i++) {
            variances[i]=calculerVariance(tab2DGL[i], moyennes[i]);
        }
        int hauteur = image.getHeight();
        // Initialiser le meilleur score à la valeur minimale possible
        // Pour Pearson, on cherche le MAXIMUM (corrélation la plus élevée)
        double scoreMax = -Double.MAX_VALUE;
        // Variable pour stocker la meilleure clé trouvée
        int meilleurCandidat = 0;
        // Tester toutes les clés possibles de 1 à 32767
        for (int i = 0; i < 32768; i++) {
            // Générer la permutation correspondant à cette clé
            int[] permCandidat = Brouillimg.generatePermutation(hauteur, i);
            // Débrouiller l'image avec cette image candidate
            // Convertir l'image RGB en niveaux de gris pour le calcul du score
            
            // Calculer le score de Pearson pour cette image candidate
            double score = scorePearsonOpti(tab2DGL, permCandidat, moyennes, variances);
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
     * @param moyX la moyenne de la ligne x
     * @param moyY la moyenne de la ligne y
     * @param varX la variance de la ligne x
     * @param varY la variance de la ligne y
     * @return Le coefficient de corrélation ρ(x,y)
     */
    public static double pearsonCorrelationOpti(int[] rowX, int[] rowY, double moyX,double moyY, double varX, double varY) {
        double numerator=0.0;
        for (int i = 0; i < rowX.length; i++) {
            numerator+=((rowX[i] - moyX)*(rowY[i]-moyY));  
        }

        double denominator=(Math.sqrt(varX)*Math.sqrt(varY));
        if (denominator==0.0) {return 0.0;}
        return numerator/denominator;
    }

    /**
     * Calcule le score de Pearson total d'une image en sommant les corrélations
     * entre chaque paire de lignes consécutives.
     * Plus le score est élevé, plus l'image est probablement correcte. Le score
     * euclidien a été amélioré
     * @param tag2DGL La matrice de l'image en niveaux de gris
     * @param permutation la permutation que l'on teste
     * @param moyennes le tableau qui contient la moyenne de chaque ligne i
     * @param variances le tableau qui contient la variances de chaque ligne i
     * @return Le score de Pearson total (plus grand = meilleur)
     */
    public static double scorePearsonOpti(int[][] tab2DGL, int[] permutation, double[] moyennes, double[] variances) {
        double scoreTotal = 0.0;
        for (int i = 0; i < tab2DGL.length - 1; i++) { /* On précalcule les moyennes et variances */
            double moyX=moyennes[permutation[i]];
            double moyY=moyennes[permutation[i+1]];
            double varX=variances[permutation[i]];
            double varY=variances[permutation[i+1]];
            double correlation = pearsonCorrelationOpti(tab2DGL[permutation[i]],tab2DGL[permutation[i+1]], moyX, moyY, varX, varY );
            // Ajouter cette corrélation au score total
            scoreTotal += correlation;
        }
        
        return scoreTotal;
    }
  


    /**
    * Cette fonction trouve le meilleur s pour un tableau de gris en les
    * testant de 0 à 127
    
    * @param tab2DGL le tableau de gris en question

    * @return le meilleur s possible en se basant sur le score d'Euclide optimisé
    
    */
    public static int trouverMeilleurS(int[][] tab2DGL) {
        int hauteur=tab2DGL.length;
        int meilleurS=0;
        double scoreMin=Double.MAX_VALUE;
        for (int s = 0; s < 128; s++) {
            int[] permCandidatS=Brouillimg.generatePermutation(hauteur, s);
            double score = scoreEuclideanOpti(tab2DGL, permCandidatS);
            if (score<scoreMin) {
                scoreMin=score;
                meilleurS=s;
            }
        }
        return meilleurS;
    }

    /** 
     * Cette fonction trouve la meilleur clé une fois qu'on connait le meilleur s
     * en se basant sur le score de Pearson amélioré
     * @param tab2DGL le tableau 2D d'entier de gris
     * @param meilleurS le meilleur s connu
     * @return la meilleur clé pour ce s
     */

    public static int trouverMeilleurCle(int[][] tab2DGL, int meilleurS) {
        int meilleurCleCandidate=meilleurS<<7;
        double scoreMax=-Double.MAX_VALUE;
        int hauteur=tab2DGL.length;
        double[] moyennes= new double[tab2DGL.length];
        for (int i = 0; i < tab2DGL.length; i++) {
            moyennes[i]=calculerMoyenne(tab2DGL[i]);
        }
        double[] variances= new double[tab2DGL.length];
        for (int i = 0; i < moyennes.length; i++) {
            variances[i]=calculerVariance(tab2DGL[i], moyennes[i]);
        }
        for (int r = 0; r < 256; r++) {
            int cleCandidat=(r << 7) | meilleurS;
            int[] permCandidat=Brouillimg.generatePermutation(hauteur, cleCandidat);
            double score = scorePearsonOpti(tab2DGL, permCandidat,moyennes,variances);
            if (score>scoreMax) {
                scoreMax=score;
                meilleurCleCandidate=cleCandidat;
            }
        }
        return meilleurCleCandidate;
    }
    /**
     * Cette fonction casse la clé d'une manière optimisée
     * @param image l'image à débrouiller
     * @return la clé qui a été utilisée pour brouiler l'image
     */
    public static int breakKeyOpti(BufferedImage image) {
        int[][] tab2DGL=Brouillimg.rgb2gl(image);
        int meilleurS=trouverMeilleurS(tab2DGL);
        return trouverMeilleurCle(tab2DGL, meilleurS);
    }

    /**
     * calcule la moyenne d'une ligne
     * @param ligne la ligne dont on veut calculer la moyenne
     * @return la moyenne
     */

    public static double calculerMoyenne(int[] ligne) {
        int somme=0;
        for (int i = 0; i < ligne.length; i++) {
            somme+=ligne[i];
        }
        return ((double)(somme))/ligne.length;
    }

    /**
     * calcule la variance d'une ligne
     * @param ligne la ligne dont on veut la variance
     * @param moyenne la moyenne de la ligne
     * @return la variance
     */

    public static double calculerVariance(int[] ligne, double moyenne) {
        double variance=0.0;
        for (int i = 0; i < ligne.length; i++) {
            double difference=(ligne[i]-moyenne);
            variance+=difference*difference;
        }
        return variance;
    }

    /**
     * Cette fonction renvoie la clé en prenant en charge plusieurs méthodes
     * @param image dont on veut casser la clé
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
            case "Optimisation":
                return breakKeyOpti(image);
            default:
                return -1;
        }
    }





}
