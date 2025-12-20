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

    public static int breakKey(BufferedImage image, String methode) {
        switch (methode) {
            case "Euclid":
                return breakKeyEuclid(image);
            // case "Pearson":
            //     break;
            case "optimisation":
                return breakKeyOpti(image);
            default:
                return -1;
        }






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
