import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Countdown {

    static int seconds = 0;
    static long timestamp = 0;

    public static void main(String[] args) {
    	
        if (args.length == 1) {
            seconds = Integer.parseInt(args[0]);
        } else if (args.length == 2) {
        	LocalTime now = LocalTime.now();
        	try {
        		LocalTime then = LocalTime.of(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        		seconds = (int) now.until(then, ChronoUnit.SECONDS);
        		if(seconds < 0) {
        			seconds += 24 * 60 * 60;
        		}
        	}catch(DateTimeException e) {
        		System.out.println("Invalid arguments. Hour must be between 0-23, minute must be between 0-59.");
        		System.exit(1);
        	}
        } else {
            seconds = 60;
        }

        System.out.printf("Countdown from %d seconds%n", seconds);

        timestamp = System.currentTimeMillis();
        updateCountdown();
    }

    private static void updateCountdown() {

        long currentTime = (System.currentTimeMillis() - timestamp) / 1000;
        String msg = "";
        boolean stop = false;
        if (currentTime > seconds) {
            msg = "--:--";
            stop = true;
        } else {
            long minutes = (seconds - currentTime) / 60;
            long seconds = (Countdown.seconds - currentTime) % 60;

            msg = (minutes < 10 ? "0"+minutes : ""+minutes) +":"+(seconds < 10 ? "0"+seconds : ""+seconds);
        }

        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("countdown.txt"), StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
            writer.write(msg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (stop) {
            return;
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        updateCountdown();
    }
}