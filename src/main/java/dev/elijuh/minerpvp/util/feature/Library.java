package dev.elijuh.minerpvp.util.feature;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Logger;

/**
 * @author elijuh
 */
@RequiredArgsConstructor
public enum Library {
    BSON(
        "org.bson.Document",
        "bson",
        "4.11.1",
        "https://repo1.maven.org/maven2/org/mongodb/bson/4.11.1/bson-{v}.jar"
    ),
    MONGO_SYNC(
        "com.mongodb.client.MongoClient",
        "mongodb-driver-sync",
        "4.11.1",
        "https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-sync/{v}/mongodb-driver-sync-{v}.jar"
    ),
    MONGO_DRIVER_CORE(
        "com.mongodb.ConnectionString",
        "mongodb-driver-core",
        "4.11.1",
        "https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-core/4.11.1/mongodb-driver-core-4.11.1.jar"
    );

    private final String className;
    private final String artifactId;
    private final String version;
    private final String repository;

    private static final Logger logger = Logger.getLogger("Libraries");
    private static final File FOLDER;
    private static final Method ADD_URL;
    private static final URLClassLoader CLASS_LOADER;

    static {
        FOLDER = new File(JavaPlugin.getProvidingPlugin(Library.class).getDataFolder(), "libraries");
        if (!FOLDER.exists()) {
            if (FOLDER.mkdirs()) {
                logger.info("Created folder: " + FOLDER.getAbsolutePath());
            }
        }
        Method a;
        try {
            a = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            a.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            a = null;
        }
        ADD_URL = a;
        CLASS_LOADER = (URLClassLoader) Library.class.getClassLoader();
    }

    public boolean isLoaded() {
        if (className == null) return false;

        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public void load() {
        if (isLoaded()) return;
        File file = new File(FOLDER, artifactId + "-" + version + ".jar");
        if (!file.exists()) {
            download(file);
        }
        try {
            ADD_URL.invoke(CLASS_LOADER, file.toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Loaded library: " + artifactId + "-" + version + ".jar");
    }

    public void download(File file) {
        try {
            String urlString = repository.replace("{v}", version);
            final URL url = new URL(urlString);
            final URLConnection connection = url.openConnection();

            connection.setRequestProperty("User-Agent", "me");

            logger.info("Downloading library: " + urlString);
            try (InputStream input = connection.getInputStream();
                 ReadableByteChannel channel = Channels.newChannel(input);
                 FileOutputStream outputStream = new FileOutputStream(file)) {
                outputStream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}