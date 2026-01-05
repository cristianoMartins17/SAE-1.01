import java.util.ArrayList;
import java.util.List;
import java.util.function.*;
import java.awt.image.BufferedImage;

public class Profiler {
    static long globalTime;
    static int nbExecutions;

    private Profiler() {
    }

    @FunctionalInterface
    interface BufferedImage1String {
    int apply(BufferedImage image, String string);
    }

    /**
     * Si clock0 est >0, retourne une chaîne de caractères
     * représentant la différence de temps depuis clock0.
     * @param clock0 instant initial
     * @return expression du temps écoulé depuis clock0
     */
    public static String timestamp(long clock0) {
        String result = null;
        if (clock0 > 0) {
            double elapsed = (System.nanoTime() - clock0) / 1e6;
            String unit = "s";
            if (elapsed < 1.0) {
                elapsed *= 1000.0;
                unit = "ms";
            }
            result = String.format("%.4g%s elapsed", elapsed, unit);
        }
        return result;
    }
    public static String timestamp(long clock0, long clock1) {
        String result = null;
        if (clock0 > 0) {
            double elapsed = (clock1 - clock0) / 1e9;
            String unit = "s";
            if (elapsed < 1.0) {
                elapsed *= 1000.0;
                unit = "ms";
            }
            result = String.format("%.4g%s elapsed", elapsed, unit);
        }
        return result;
    }
    /**
     * retourne l'heure courante en ns.
     * @return
     */
    public static long timestamp() {
        return System.nanoTime();
    }

    public static void init() {
        globalTime=0;
    }

    public static Double getGlobalTimeMs() {
        return ((double)(globalTime))/1000000;
    }
}
