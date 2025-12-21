import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
// import java.security.PublicKey;
// import java.util.concurrent.atomic.AtomicInteger;
// import java.util.concurrent.atomic.AtomicLong;
// import java.util.stream.IntStream;

public class Brouillimg {

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("Usage: java Brouillimg <image_entrée> <clé> [image_sortie] <processus>");
            System.exit(1);
        }
        String inPath = args[0];
        String outPath = (args.length > 3) ? args[2] : "out.png";
        // Masque 0x7FFF pour garantir que la clé ne dépasse pas les 15 bits
        int key = Integer.parseInt(args[1]) & 0x7FFF ;
        int indiceProcess= (args.length==4)? 3 : 2;
        String process=args[indiceProcess];

        BufferedImage inputImage = ImageIO.read(new File(inPath));
        if (inputImage == null) {
            throw new IOException("Format d’image non reconnu: " + inPath);
        }

        final int height = inputImage.getHeight();
        final int width = inputImage.getWidth();
        System.out.println("Dimensions de l'image : " + width + "x" + height);
        // Pré‑calcul des lignes en niveaux de gris pour accélérer le calcul
        // du critère
        // int[][] inputImageGL = rgb2gl(inputImage);

        int[] perm = generatePermutation(height, key);
        switch (process) {
            case "scramble":
                BufferedImage scrambledImage = scrambleLines(inputImage, perm);
                ImageIO.write(scrambledImage, "png", 
                new File(outPath));
                System.out.println("Image écrite: " + outPath);
                break;
            case "unscramble":
                BufferedImage unscrambledImage = unscrambleLines(inputImage,
                     perm);
                ImageIO.write(unscrambledImage, "png",
                 new File(outPath));
                System.out.println("Image écrite: " + outPath);
                break;
            default:
                System.out.println("processus non reconnu : "+process);
                System.exit(2);
                break;
        }
        if (! validKey(key, height)) {
            System.out.println("cle non valide , la permutation n'est pas reversible");
            int keyPlusProche=trouverCleValide(key, height);
            if (key==-1) {
                System.out.println("il n'existe pas de clé valide pour cette hauteur");
            }
            else {
                System.out.println(keyPlusProche+ " est la clé valide la plus proche de votre clé");
            }
        
        }
    }
    /**
     * Convertit une image RGB en niveaux de gris (GL).
     * @param inputRGB image d'entrée en RGB
     * @return tableau 2D des niveaux de gris (0-255)
     */
    public static int[][] rgb2gl(BufferedImage inputRGB) {
        final int height = inputRGB.getHeight();
        final int width = inputRGB.getWidth();
        int[][] outGL = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = inputRGB.getRGB(x, y);
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;
                // luminance simple (évite float)
                int gray = (r * 299 + g * 587 + b * 114) / 1000;
                outGL[y][x] = gray;
            }
        }
        return outGL;
    }
    /**
     * Génère une permutation des entiers 0..size-1 en fonction d'une clé.
     * @param size taille de la permutation
     * @param key clé de génération (15 bits)
     * @return tableau de taille 'size' contenant une permutation des 
     * entiers 0..size-1
     */
    public static int[] generatePermutation(int size, int key){
        int[] scrambleTable = new int[size];
        for (int i = 0; i < size; i++) 
            scrambleTable[i] = scrambledId(i, size, key);
        return scrambleTable;
    }

    /**
     * Mélange les lignes d'une image selon une permutation donnée.
     * @param inputImg image d'entrée
     * @param perm permutation des lignes (taille = hauteur de l'image)
     * @return image de sortie avec les lignes mélangées
     */
    public static BufferedImage scrambleLines(BufferedImage inputImg, int[] perm){
        int width = inputImg.getWidth();
        int height = inputImg.getHeight();
        if (perm.length != height) throw new 
        IllegalArgumentException("Taille d'image <> taille permutation");

        BufferedImage out = new BufferedImage(width, height,
             BufferedImage.TYPE_INT_ARGB);

        //on parcours chaque ligne de l'image de sortie
        for (int y = 0; y < height; y++) { 
            // position de la ligne y dans l'image brouillée
            int srcY = perm[y];
            //on parcours chaque pixel de la ligne
            for (int x = 0; x < width; x++) { 
                //on récupère la couleur du pixel dans l'image d'entrée
                int rgb = inputImg.getRGB(x, y); 
                //on place la couleur dans l'image de sortie
                out.setRGB(x, srcY, rgb); 
            }
        }
        
        return out;
    }

    /**
     * Renvoie la position de la ligne id dans l'image brouillée.
     * @param id  indice de la ligne dans l'image claire (0..size-1)
     * @param size nombre total de lignes dans l'image
     * @param key clé de brouillage (15 bits)
     * @return indice de la ligne dans l'image brouillée (0..size-1)
     */
    public static int scrambledId(int id, int size, int key) {
        // pour obtenir les 8 premiers bits on décale chaque bit vers
        // la droite de 7
        int r=(key>>7 & 0xFF); 
        // pour les 7 derniers on fait juste un et logique avec 
        // 000000001111111 ca gardera que les 7 à droite
        int s=(key & 0x7F); 
        return ((r+(2*s+1)*id)%size);
    }

    /**
     * Affiche un tableau d'entiers dans la console au format [ e1, e2, e3, ... ].
     * Chaque élément du tableau est affiché sur une nouvelle ligne, séparé par des virgules.
     * @param tab Le tableau d'entiers à afficher. Peut être vide.
     * @example
     * int[] nombres = {1, 2, 3, 4, 5};
     * afficherTab(nombres);
     * Affiche :[ 1, 2, 3, 4, 5]
     */
    public static void afficherTab(int[] tab ) {
        System.out.print("[ ");
        for (int i = 0; i < tab.length; i++) {
            System.out.print( tab[i]+" ");
            if (i<tab.length-1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    /**
     * Débrouille une image en réorganisant ses lignes selon une permutation donnée.
     * Chaque ligne de l'image de sortie correspond à une ligne spécifique 
     * de l'image d'entrée définie par la permutation.
     * @param inputImg L'image d'entrée brouillée à débrouiller
     * @param perm Le tableau de permutation indiquant pour chaque ligne y de sortie,
     * quelle ligne de l'image d'entrée doit être utilisée.
     * La taille de ce tableau doit correspondre à la hauteur de l'image.
     * @return Une nouvelle BufferedImage avec les lignes réorganisées selon la permutation
     * @throws IllegalArgumentException Si la taille de la permutation ne correspond pas
     * à la hauteur de l'image
     * @example
     * Si perm = [2, 0, 1], alors :
     *  - La ligne 0 de sortie vient de la ligne 2 de l'entrée
     *  - La ligne 1 de sortie vient de la ligne 0 de l'entrée
     *  - La ligne 2 de sortie vient de la ligne 1 de l'entrée
     */
    public static BufferedImage unscrambleLines(BufferedImage inputImg, int[] perm){
        int width = inputImg.getWidth();
        int height = inputImg.getHeight();
        if (perm.length != height) throw new 
        IllegalArgumentException("Taille d'image <> taille permutation");

        BufferedImage out = new BufferedImage(width, height, 
            BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) { 
            int srcY = perm[y];  
            for (int x = 0; x < width; x++) { 
                int rgb = inputImg.getRGB(x, srcY); 
                out.setRGB(x, y, rgb);
            }
        } 
        return out;
    }

    /**
     * Calcule le Plus Grand Commun Diviseur (PGCD) de deux entiers
     * en utilisant l'algorithme d'Euclide récursif.
     * Le PGCD est le plus grand entier qui divise à la fois i1 et i2.
     * 
     * @param i1 Le premier entier (peut être plus petit ou plus grand que i2)
     * @param i2 Le second entier (peut être plus petit ou plus grand que i1)
     * @return Le PGCD de i1 et i2. Retourne i1 si l'un des deux nombres est 0.
     * 
     * @example
     * pgcd(48, 18) retourne 6
     * pgcd(100, 50) retourne 50
     * pgcd(17, 19) retourne 1 (nombres premiers entre eux)
     * pgcd(0, 5) retourne 5
     */
    public static int pgcd(int i1, int i2) {
        if (i2==0) {
            return i1;
        }
        return pgcd(i2, i1%i2);
    }

    public static boolean validKey(int key, int height) {
        return (pgcd(2*(key & 0x7F)+1,height)==1);
    }

    public static int trouverCleValide(int key, int height) {
        int keyPlus=key+1;
        int keyMoins=key-1;
        while (!validKey(keyMoins, height) && !validKey(keyPlus, height)) {
            keyPlus++;
            keyMoins--;
        }
        if (validKey(keyPlus, height) && keyPlus<32768) {
            return keyPlus;
        }
        else if (validKey(keyMoins, height) && keyMoins>=0) {
            return keyMoins;
        }
        return -1;
    }

}

