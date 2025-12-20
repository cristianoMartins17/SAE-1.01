import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Test {

    public static void main(String[] args) throws IOException {
        BufferedImage test = ImageIO.read(new File("out.png"));
        double[][] analyse = new double[test.getHeight()][test.getWidth()];
        int[][] GL = Brouillimg.rgb2gl(test);
        for (int i = 0; i < analyse.length; i++) {
            for (int j = 0; j < analyse.length; j++) {
                if (i==j) {continue;}
                analyse[i][j]=Debrouillage.euclideanDistance(GL, i, j);
            }
        }
        
    }
    
}
