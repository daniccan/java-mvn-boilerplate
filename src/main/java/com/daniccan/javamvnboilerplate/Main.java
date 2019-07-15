package com.daniccan.javamvnboilerplate;

import com.daniccan.javamvnboilerplate.threads.Thread1;
import com.daniccan.javamvnboilerplate.threads.Thread2;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author daniccan
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class);

    private static final String APP_CONFIG_FILE = "conf" + File.separatorChar + "app.xml";
    private static final String LOG_CONFIG_FILE = "conf" + File.separatorChar + "log4j.properties";

    public static final Properties APP_CONFIG = new Properties();

    private ExecutorService exeService;

    private Thread1 thread1;
    private Thread2 thread2;

    public static void main(String args[]) throws IOException, ExecutionException, TimeoutException {

        Main main = new Main();

        main.initLogger();

        main.loadProperties(APP_CONFIG, APP_CONFIG_FILE);

        main.submitThreads();
    }

    private void submitThreads() throws ExecutionException, TimeoutException {

        if (thread1 != null) {
            thread1.shutdown();
            thread1 = null;
        }

        if (thread2 != null) {
            thread2.shutdown();
            thread2 = null;
        }

        exeService = Executors.newFixedThreadPool(10);

        thread1 = new Thread1();
        exeService.submit(thread1);

        thread2 = new Thread2();
        exeService.submit(thread2);

        Runtime runTime = Runtime.getRuntime();

        runTime.addShutdownHook(
                new Thread() {
            @Override
            public void run() {

                if (thread1 != null) {
                    thread1.shutdown();
                    thread1 = null;
                }

                if (thread2 != null) {
                    thread2.shutdown();
                    thread2 = null;
                }

                if (exeService != null) {
                    exeService.shutdown();
                    try {
                        exeService.awaitTermination(5000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        LOGGER.error(e, e);
                    }
                    exeService = null;
                }
            }
        });
    }

    private void initLogger() {
        PropertyConfigurator.configure(LOG_CONFIG_FILE);
    }

    private void loadProperties(Properties configProperties, String configFile) throws FileNotFoundException, IOException {
        try (FileInputStream configInputStream = new FileInputStream(configFile)) {
            configProperties.loadFromXML(configInputStream);
        }
    }
}
