package Utility;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Logger class for making a time stamp and reporting whether the login attempt was successful or not
 */
public class Log {
    public Logger logger;
    FileHandler fileHandler;

    public Log(String file_name) throws SecurityException, IOException{
        File file = new File(file_name);
        if(!file.exists()){
            file.createNewFile();
        }
        fileHandler = new FileHandler(file_name, true);
        logger = Logger.getLogger("test");
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
    }
}
