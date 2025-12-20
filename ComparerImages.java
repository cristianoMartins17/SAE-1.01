import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
public class ComparerImages {

    public static void main(String[] args) throws IOException {
        if (args.length!=2) {System.out.println("il faut mettre 2 images en arguments");}
        String image1Path = args[0];
        String image2Path = args[1];
        BufferedImage image1=ImageIO.read(new File(image1Path));
        BufferedImage image2=ImageIO.read(new File(image2Path));
        if (image1 == null || image2==null) {
            throw new IOException("Format d’image non reconnu: ");
        }
        comparerDeuxImages(image1, image2);
    }

        public static int comparerDeuxImages(BufferedImage image1, BufferedImage image2) {
            if (image1.getHeight()!=image2.getHeight() || image2.getWidth() != image1.getWidth()) {
                System.out.println("les images ne sont pas de même taille");
                return -1; // Si les deux images sont pas de même taille, ca sert à rien de comparer, on retourne -1 parce que on saura que c'est une erreur
            }
            int compteur=0;
            for (int y = 0; y < image1.getHeight(); y++) {
                for (int x= 0; x < image2.getWidth(); x++) {
                    int rgb1=image1.getRGB(x, y);
                    int rgb2=image2.getRGB(x, y);
                    if (rgb1!=rgb2) {
                        compteur++;
                    }
                }
            }

            return compteur;
        }
    
}
