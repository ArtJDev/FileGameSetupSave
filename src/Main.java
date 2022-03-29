import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            Handler fileHandler = new FileHandler("logfile.txt");
            fileHandler.setFormatter(new MyFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        logger.setUseParentHandlers(false);
        createDir("Games");
        createDir("Games/src");
        createDir("Games/res");
        createDir("Games/savegames");
        createDir("Games/temp");
        createDir("Games/src/main");
        createDir("Games/src/test");
        createFile("Games/src/main/Main.java");
        createFile("Games/src/main/Utils.java");
        createDir("Games/res/drawables");
        createDir("Games/res/vectors");
        createDir("Games/res/icons");
        createFile("Games/temp/temp.txt");

        GameProgress gp1 = new GameProgress(100, 50, 11, 13.45);
        GameProgress gp2 = new GameProgress(99, 10, 14, 53.17);
        GameProgress gp3 = new GameProgress(84, 75, 8, 9.58);

        saveGame("Games/savegames/save1.dat", gp1);
        saveGame("Games/savegames/save2.dat", gp2);
        saveGame("Games/savegames/save3.dat", gp3);

        zipFile("Games/savegames/saved.zip", "Games/savegames");

        createLogFile("logfile.txt", "Games/temp/temp.txt");
    }

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void createDir(String path) {
        File dir = new File(path);
        if (dir.mkdir()) {
            logger.info("Каталог " + dir.getPath() + " создан");
        } else logger.info("Каталог " + dir.getPath() + " НЕ создан");
    }

    public static void createFile(String path) {
        File file = new File(path);
        try {
            if (file.createNewFile()) {
                logger.info("Файл " + file.getPath() + " создан");
            }
        } catch (IOException exc) {
            logger.info("Файл " + file.getPath() + " НЕ создан");
        }
    }

    public static void saveGame(String path, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            logger.info("Игра сохранена в файле " + path);
        } catch (Exception ex) {
            logger.info("Игра НЕ сохранилась в файле " + path);
        }
    }

    public static void zipFile(String zipFilePath, String fileInPath) throws IOException {
        File fileSource = new File(fileInPath);
        File[] files = fileSource.listFiles();
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFilePath));

        for (File file : files) {
            FileInputStream fis = new FileInputStream(file.getPath());
            ZipEntry entry = new ZipEntry(file.getName());
            zout.putNextEntry(entry);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            zout.write(buffer);
            zout.closeEntry();
            fis.close();
            if (file.delete()) {
                logger.info("Файл " + file.getName() + " добавлен в архив " + zipFilePath);
                logger.info("Файл " + file.getName() + " удален");
            } else {
                logger.info("Файл " + file.getName() + " НЕ добавлен в архив " + zipFilePath);
                logger.info("Файл " + file.getName() + " НЕ удален");
            }
        }
        zout.close();
    }

    public static void createLogFile(String inputSource, String outputFile) {
        try (FileReader reader = new FileReader(inputSource);
             FileWriter writer = new FileWriter(outputFile)) {
            int i;
            while ((i = reader.read()) != -1) {
                writer.write(i);
            }
            logger.info("Лог программы записан в файл " + outputFile);
        } catch (IOException ex) {
            logger.info("Не удалось записать лог программы в файл " + outputFile);
        }
    }
}