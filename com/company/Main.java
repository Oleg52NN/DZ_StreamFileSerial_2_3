package com.company;


import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static java.lang.System.out;

public class Main {

    public static void main(String[] args) {
        final int maxPerson = 3;
        List<String> dir = new ArrayList<>();

        List<GameProgress> gamer = new ArrayList<>();
        gamer.add(new GameProgress(10, 10, 100, 50));
        gamer.add(new GameProgress(5, 3, 100, 70));
        gamer.add(new GameProgress(7, 7, 100, 30));
        for (int i = 0; i < maxPerson; i++) {
            dir.add("d:/games/savegames/game" + (i + 1) + ".dat");
        }
        for (int i = 0; i < maxPerson; i++) {
            saveGame(dir.get(i), gamer.get(i));
        }
        zipFiles("d:/games/savegames/game.zip", dir);
        out.println("Архив создан. Файлы .dat удалены");
        out.println("Задание № 3. Разархивировать? (y/n)");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String s = scanner.nextLine();
            if (s.equals("y")) {
                openZip("d:/games/savegames/game.zip", "d:/games/savegames/game");
                break;
            }
            if (s.equals("n")) {
                break;
            }
        }
        out.println(openProgress(dir.get(1)));

    }

    static void zipFiles(String wayZip, List dir) {
        makeFile(wayZip);

        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(wayZip))) {
            for (int i = 0; i < dir.size(); i++) {
                try (FileInputStream fileInputStream = new FileInputStream((String) dir.get(i))) {
                    ZipEntry entry = new ZipEntry((String) dir.get(i));
                    out.putNextEntry(entry);
                    byte[] buffer = new byte[fileInputStream.available()];
                    fileInputStream.read(buffer);
                    out.write(buffer);
                    out.closeEntry();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        for (int i = 0; i < dir.size(); i++) {
            File file = new File((String) dir.get(i));
            try {
                if (file.delete()) {
                    out.println("Файл " + dir.get(i) + " удалён");
                } else {
                    out.println("Ошибка при удалении файла " + dir.get(i));
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

    }

    static void saveGame(String s, GameProgress gameProgress) {
        makeFile(s);
        try (FileOutputStream saveGame = new FileOutputStream(s);
             ObjectOutputStream gamer = new ObjectOutputStream(saveGame)) {
            gamer.writeObject(gameProgress);
        } catch (Exception ex) {
            out.println(ex.getMessage());
        }
    }

    static void makeFile(String s) {
        File file = new File(s);
        try {
            if (file.createNewFile()) {
                out.println("Файл " + s + " был создан\n");
            } else {
                if (file.exists()) {
                    out.println("Файл " + s + " уже есть в этой папке\n");
                } else {
                    out.println("Файл " + s + " не может быть создан. Проверьте правильно ли указан путь, а также права доступа к папке\n");
                }
            }
        } catch (IOException ex) {
            out.println(ex.getMessage());
        }
    }

    static void openZip(String zip, String outDate) {
        try (ZipInputStream zipIS = new ZipInputStream(new FileInputStream(zip))) {
            ZipEntry entry = new ZipEntry(zip);
            while ((entry = zipIS.getNextEntry()) != null) {
                String name = entry.getName();
                FileOutputStream out = new FileOutputStream(name);
                for (int c = zipIS.read(); c != -1; c = zipIS.read()) {
                    out.write(c);
                }
                out.flush();
                zipIS.closeEntry();
                out.close();
                System.out.println("Файл " + entry.getName() + " разархивирован");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    static GameProgress openProgress(String way) {
        try (FileInputStream fin = new FileInputStream(way)) {
            ObjectInputStream gamer = new ObjectInputStream(fin);
            return (GameProgress) gamer.readObject();
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        return null;
    }
}
