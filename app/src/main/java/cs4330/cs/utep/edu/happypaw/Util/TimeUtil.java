package cs4330.cs.utep.edu.happypaw.Util;

public class TimeUtil {
    public static String formatElapsedTime(long elapsedTime) {
        int seconds = (int) (elapsedTime / 1000) % 60;
        int minutes = (int) ((elapsedTime / (1000*60)) % 60);
        int hours   = (int) ((elapsedTime / (1000*60*60)) % 24);
        return String.format("%dh:%02dm:%02ds", hours, minutes, seconds);
    }
}
