package com.dawid.library.io.file;

import com.dawid.library.exception.DataExportException;
import com.dawid.library.exception.DataImportException;
import com.dawid.library.exception.InvalidDataEsception;
import com.dawid.library.model.*;

import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

public class CsvFileManager implements FileManager {
    private static final String FILE_NAME = "Library.csv";
    private static final String USERS_FILE_NAME = "Library_user.csv";

    @Override
    public Library importData() {
        Library library = new Library();
        importPublication(library);
        importUsers(library);
        return library;
    }

    private void importUsers(Library library) {
        try (Scanner filereader = new Scanner(new File(USERS_FILE_NAME))) {
            while (filereader.hasNextLine()) {
                String line = filereader.nextLine();
                LibraryUser libUser = createUserFromString(line);
                library.addUser(libUser);
            }
        } catch (FileNotFoundException e) {
            throw new DataImportException("Brak pliku" + USERS_FILE_NAME);
        }
    }

    private LibraryUser createUserFromString(String csvText) {
        String[] split = csvText.split(";");
        String firstName = split[0];
        String lastName = split[1];
        String pesel = split[2];
        return new LibraryUser(firstName, lastName, pesel);
    }

    private void importPublication(Library library) {
        try (Scanner filereader = new Scanner(new File(FILE_NAME))) {
            while (filereader.hasNextLine()) {
                String line = filereader.nextLine();
                Publication publication = createObjectFromString(line);
                library.addPublication(publication);
            }
        } catch (FileNotFoundException e) {
            throw new DataImportException("Brak pliku" + FILE_NAME);
        }
    }

    private Publication createObjectFromString(String line) {
        String[] split = line.split(";");
        String type = split[0];
        if (Book.TYPE.equals(type)) {
            return createBook(split);
        } else if (Magazine.TYPE.equals(type)) {
            return createMagazine(split);
        }
        throw new InvalidDataEsception("Nieznany typ publikacji " + type);
    }

    private Publication createMagazine(String[] data) {
        String title = data[1];
        String publisher = data[2];
        int year = Integer.valueOf(data[3]);
        int month = Integer.valueOf(data[4]);
        int day = Integer.valueOf(data[5]);
        String language = data[6];
        return new Magazine(title, publisher, language, year, month, day);
    }

    private Publication createBook(String[] data) {
        String title = data[1];
        String publisher = data[2];
        int year = Integer.valueOf(data[3]);
        String author = data[4];
        int pasges = Integer.valueOf(data[5]);
        String isbn = data[6];
        return new Book(title, author, year, pasges, publisher, isbn);
    }


    @Override
    public void exportData(Library library) {
        exportPublications(library);
        exportUsers(library);

    }

    private void exportUsers(Library library) {
        Collection<LibraryUser> users = library.getUsers().values();
        try (var fileWriter = new FileWriter(USERS_FILE_NAME);
             var bufferedWriter = new BufferedWriter(fileWriter)) {
            for (LibraryUser libUser : users) {
                bufferedWriter.write(libUser.toCsv());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new DataExportException("Błąd zapisu danych do pliku " + USERS_FILE_NAME);
        }
    }

    private void exportPublications(Library library) {
        Collection<Publication> publications = library.getPublications().values();
        try (var fileWriter = new FileWriter(FILE_NAME);
             var bufferedWriter = new BufferedWriter(fileWriter)) {
            for (Publication publication : publications) {
                bufferedWriter.write(publication.toCsv());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new DataExportException("Błąd zapisu danych do pliku " + FILE_NAME);
        }
    }
}

