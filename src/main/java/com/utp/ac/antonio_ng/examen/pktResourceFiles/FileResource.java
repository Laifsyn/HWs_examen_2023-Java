package com.utp.ac.antonio_ng.examen.pktResourceFiles;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class FileResource {
    public static void main(String[] args) throws URISyntaxException, IOException {
        // Test
        File file = new FileResource().getFileFromResource("lista_de_clientes/source_data.json");
        System.out.println("Resultado de lectura:\n" + Files.readString(file.toPath(), StandardCharsets.UTF_8));
    }

    public FileResource() {
    }

    public String getFileContent(String file_name) throws URISyntaxException, IOException {
        return Files.readString(getFileFromResource(file_name).toPath());
    }

    public FileResource(String file_name) throws URISyntaxException {
        if (file_name == null) return;
        System.out.println("getResourceAsStream : " + file_name);
        InputStream is = getFileFromResourceAsStream(file_name);
        printInputStream(is);

        System.out.println("\ngetResource : " + file_name);
        File file = getFileFromResource(file_name);
        printFile(file);

    }

    // get a file from the resources folder
    // works everywhere, IDEA, unit test and JAR file.
    public InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    /*
        The resource URL is not working in the JAR
        If we try to access a file that is inside a JAR,
        It throws NoSuchFileException (linux), InvalidPathException (Windows)

        Resource URL Sample: file:java-io.jar!/json/file1.json
     */
    public File getFileFromResource(String fileName) throws URISyntaxException {

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());

            return new File(resource.toURI());
        }

    }

    // print input stream
    private static void printInputStream(InputStream input_stream) {

        try (InputStreamReader streamReader = new InputStreamReader(input_stream, StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // print a file
    private static void printFile(File file) {
        List<String> lines;
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}