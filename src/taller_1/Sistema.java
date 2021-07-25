/*
 * Este package contiene lo hecho para la entrega del taller 1
 */
package taller_1;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Andres David Hoyos Velasquez
 * <p>
 * TODO: expand symbols table
 * DONE: read source file
 * DONE: distinguish words
 * DONE: write output
 * TODO: Document new methods
 * log(String), log(Throwable), categorizar(List<List<String>>, String...)
 */
public final class Sistema {

    /**
     * <p>Direccion donde se ubica el proyecto.</p>
     */
    public static final String PROJECT_DIRECTORY = System.getProperty("user.dir") + File.separatorChar;

    /**
     * <p>Direccion de la carpeta data, dentro del proyecto..</p>
     */
    private static final String DATA_DIRECTORY = PROJECT_DIRECTORY + "data" + File.separatorChar;

    /**
     * <p>Direccion de la carpeta de output, dentro del proyecto.</p>
     */
    private static final String OUTPUT_DIRECTORY = PROJECT_DIRECTORY + "output" + File.separatorChar;

    /**
     * <p>Direccion de la carpeta de input, dentro del proyecto.</p>
     */
    private static final String INPUT_DIRECTORY = PROJECT_DIRECTORY + "input" + File.separatorChar;

    /**
     * <p>Direccion del archivo en el cual se va a hacer log</p>
     */
    private static final String LOG_FILE = OUTPUT_DIRECTORY + "log.txt";

    /**
     * <p>Nombre del archivo: texto-Mapa.</p>
     */
    private static final String SYMBOLS_TXT_FILE = "symbols.txt";

    /**
     * <p>Nombre del archivo: Objeto-Mapa.</p>
     */
    private static final String SYMBOL_OBJECT_FILE = "symbols.symb";

    /**
     * <p>{@code Map<String, Map<String, String>>} de los simbolos, con {@code Map<String, String>} de las caracteristicas del simbolo.</p>
     */
    private static final Map<String, Map<String, String>> SYMBOLS;

    // bloque estatico para cargar los symbolos
    static {
        SYMBOLS = loadSymbols();
    }

    /**
     * <p>Non Instantiable Class.</p>
     */
    private Sistema() {
        //no instance
    }

    /**
     * <p>Punto de entrada del programa.</p>
     *
     * @param args No hay implementacion actual para usar algun argumento de entrada
     */
    public static void main(String[] args) {
//        System.out.println(System.getProperty("user.dir") + File.separatorChar + "input" + File.separatorChar + "prueba.txt");
//        displaySymbols();
//        Sistema.log("World");
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String formatedDate = now.format(formatter);
//        System.out.println(formatedDate);
//        now = LocalDateTime.now();
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MM y");
//
//        System.out.println(now.format(format));

        categorizar(loadSource("prueba.txt"));

    }

    /*-------------------- SYMBOLS SECTION START --------------------*/

    /**
     * <p>Recorre el {@code Map<String, Map<String, String>> } de simbolos de forma que primero muestra el
     * header y luego las key y value de las caracteristicas. Recorre primero el header en conjunto con
     * sus caracteristicas, sin e mbargo no hay garantia de que orden sea igual al del archivo o cada vez
     * que se ejecute el programa.</p>
     */
    private static void displaySymbols() {
        // for-each con los elementos del mapa de los simbolos (String, Map)
        for (Map.Entry<String, Map<String, String>> entry : SYMBOLS.entrySet()) {
            // Se imprime la llave(String), que es el simbolo
            System.out.println(entry.getKey());
            // for-each con los elementos del Map
            for (Map.Entry<String, String> stringEntry : entry.getValue().entrySet()) {
                // se imprime la key y el value, caracteristicas del simbolo
                System.out.println(stringEntry.getKey() + " : " + stringEntry.getValue());
            }
        }
    }

    /**
     * <p>Lee el archivo de simbolos en la version de objeto y de texto. Si uno de los 2 no esta, usa el otro.</p>
     * <p>En caso de que esten los 2, usa la version de texto, ya que se considera la mas actualizada.</p>
     * <p><b>El mapa es inmodificable.</b></p>
     * @return {@code Map<String, Map<String, String>>}, String de los simbolos, {@code Map<String, String} con las caracteristicas.
     * @throws RuntimeException si los 2 archivos no estan o no se pudieron leer.
     */
    private static Map<String, Map<String, String>> loadSymbols() throws RuntimeException {
        // load the Symbols form object file
        Map<String, Map<String, String>> f = loadSymbolsFile();
        // load the symbols form text file
        Map<String, Map<String, String>> o = loadSymbolsObject();

        // check if any instance of the symbols file is found
        /* Expressions have been simplified using boolean algebra*/
        // both the text-based and the object-based maps were not found
        if (o == null && f == null) {
            // throw exception
            throw new RuntimeException("SYMBOLS NOT FOUND");

            // object-based was not found
        } else if (o == null) {
            // o == null && f != null
            // creates copy of the text-based as an object file on storage
            writeFileObject(f);
            // return text-based
            return f;

            // text-based was not found
        } else if (f == null) {
            // f == null && o != null
            // return object-based
            return o;

        } else {
            // f != null && o != null
            // object-based and text-based found
            // check if they are different, if different return text-based(assuming it's and updated version)
            return f.equals(o) ? o : f;
        }


    }

    /**
     * <p>Mapear el titulo con un mapa de las caracteristicas de manera similar a lo que
     * interprete que funciona un mapeado de informacion .jason .</p>
     *
     * <p><b>El mapa es inmodificable.</b></p>
     *
     * @return un mapeado de tipo {@code Map<String, Map<String, String>>}.
     */
    private static Map<String, Map<String, String>> loadSymbolsFile() {
        File file = new File(DATA_DIRECTORY + SYMBOLS_TXT_FILE);
        // leer el archivo
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            // string para almacenar las lineas leidas, inicializada en la primera linea
            String line = in.readLine();
            // strings para almacenar el header, la key y el value
            String header, key, value;
            // Mapa que va a contener todos los header y los mapas de sus caracteristicas
            Map<String, Map<String, String>> head = new HashMap<>();
            // Mapa que va a contener las caracteristicas de cada header
            Map<String, String> body;
            // ciclo para navegar las lineas del archivo
            // cada iteracion lee la siguiente linea
            while (line != null) {
                // ciclo para saltarse lineas en blanco entre los 'cuerpos'
                while (line != null && line.isBlank()) {
                    // leer la siguiente linea
                    line = in.readLine();
                }
                // revisar que no se este en el fin del archivo
                if (line != null) {
                    // almacenar el header(keyword)
                    header = line;
                    // leer la siguiente linea
                    line = in.readLine();
                    // inicializar el mapa de caracteristicas
                    body = new HashMap<>();
                    // ciclo para mapear las caracteristicas en keys y values
                    // revisar que no este en el fin del archivo y que la linea no este en blanco
                    while (line != null && !line.isBlank()) {
                        // !(line == null || line.isBlank())
                        // quitar los espacios " " de la linea
                        line = line.replaceAll(" ", "");
                        // revisar que la linea tiene una ":" para poder separarla
                        if (line.contains(":")) {
                            // almacenar la parte antes de ":" como la key
                            key = line.split(":")[0];
                            // almacenar la parte que esta despues del ":" como el value
                            value = line.split(":")[1];
                            // mapear la key con el value
                            body.put(key, value);
                        }
                        // leer la siguiente linea
                        line = in.readLine();
                    }
                    // mapear el header com key y el mapa de caracteristicas como el value
                    head.put(header, Collections.unmodifiableMap(body));
                }
            }
            // retornar el mapa completo, no modificable
            return Collections.unmodifiableMap(head);

        } catch (IOException e) {
            // se imprime el error a la consola estandar de errores
            e.printStackTrace(System.err);
            // se imprime el error al log
            log(e);
        }
        // retornar null
        return null;
    }

    /**
     * <p>Carga el archivo con el mapeado de los simbolos.</p>
     * <p><b>El mapa es inmodificable.</b></p>
     *
     * @return {@code Map<String, Map<String, String>>} de los simbolos con sus caracteristicas.
     */
    private static Map<String, Map<String, String>> loadSymbolsObject() {
        // se crea el archivo para leer
        File file = new File(DATA_DIRECTORY + SYMBOL_OBJECT_FILE);
        // try-catch para posibles errores al guardar el objeto, o al hacer casting
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            // se retorna el archivo leido, despues de hacerle casting a Map<String, Map<String, String>>, no modificable
            return Collections.unmodifiableMap((HashMap<String, Map<String, String>>) in.readObject());

        } catch (IOException | ClassNotFoundException e) {
            // se imprime el error a la consola estandar de errores
            e.printStackTrace(System.err);
            // se imprime el error al log
            log(e);
        }
        return null;
    }

    /**
     * <p>Guarda el {@code Map<String, Map<String, String>>} como objeto.</p>
     *
     * @param map - el mapa de simbolos.
     */
    private static void writeFileObject(Map<String, Map<String, String>> map) {
        // crea el archivo para guardar el objeto
        File file = new File(DATA_DIRECTORY + SYMBOL_OBJECT_FILE);
        // try-catch para posibles errores al guardar el objeto
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            // guarda el objeto
            out.writeObject(map);
        } catch (IOException e) {
            // se imprime el error a la consola estandar de errores
            e.printStackTrace(System.err);
            // se imprime el error al log
            log(e);
        }
    }

    /**
     * <p>Guarda el {@code Map<String, Map<String, String>>} como objeto. Es equivalente a
     * llamar {@link #writeFileObject(Map)} con el mapa de simbolos.</p>
     */
    private static void writeFileObject() {
        writeFileObject(SYMBOLS);
    }


    /*-------------------- SYMBOLS SECTION END --------------------*/


    /*----------------- SOURCE FILE SECTION START -----------------*/

    /**
     * <p>Carga el codigo fuente del archivo "fileName" ubicado en la carpeta ".\input\" dentro del proyecto</p>
     * <p>Guarda las lineas en {@code List<List<String>>}, mientras que las palabras de la linea las separa y las
     * guarda en {@code List<String>}.</p>
     *
     * <p><b>La lista es inmodificable.</b></p>
     *
     * <p>
     * TODO: DECIDIR UNA EXTENSION PARA EL TIPO DE ARCHIVO DEL SOURCE CODE
     *
     * @param fileName nombre del archivo que se va a leer, dentro de la carpeta .\input\ .
     * @return {@code List<List<String>>} con las palabras sepa.
     */
    public static List<List<String>> loadSource(String fileName) {
        Objects.requireNonNull(fileName);

        File file = new File(INPUT_DIRECTORY + fileName);
        // leer el archivo
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            // lista para guardar la lista de palabras
            List<List<String>> lineList = new LinkedList<>();
            // lista para guardar las palabras de la linea
            List<String> wordList;
            // String para guardar la linea
            String line;
            // ciclo para leer las lineas
            while ((line = in.readLine()) != null) {
                // inicializar la lista de palabras
                wordList = new LinkedList<>();
                // for-each para guardar separar y guardar las palabras
                for (String s : line.split(" ")) {
                    // revisar que la palabra no este en blanco
                    if (!s.isBlank()) {
                        // guardar la palabra en la lista de palabras
                        wordList.add(s);
                    }
                }
                // guardar la lista de palabras(la linea)
                lineList.add(Collections.unmodifiableList(wordList));
            }
            // retornar la lista de lineas
            return Collections.unmodifiableList(lineList);
        } catch (IOException e) {
            // se imprime el error a la consola estandar de errores
            e.printStackTrace(System.err);
            // se imprime el error al log
            log(e);

        }
        return null;
    }

    public static void categorizar(List<List<String>> list, String... args) {
        Objects.requireNonNull(list);

        try (PrintWriter out = new PrintWriter(new FileWriter(OUTPUT_DIRECTORY + "output.txt"))) {

            out.printf("%10s%10s%10s%18s", "symbol", "line", "column", "type");
            for (String arg : args) {
                out.printf("%20s", arg);
            }
            out.print("\n");

            int line = 0, pos;

            for (List<String> lines : list) {

                line++;
                pos = 1;

                for (String word : lines) {
                    out.printf("%10s%10s%10s", word, line, pos);
                    if (SYMBOLS.containsKey(word)) {
                        // belongs to symbols
                        out.printf("%18s", SYMBOLS.get(word).get("type"));
                        for (String arg : args) {
                            out.printf("%20s", SYMBOLS.get(word).get(arg));
                        }
                    } else {
                        // for now, they are identifiers
                        out.printf("%18s", "identificador");
                    }
                    out.print("\n");
                    pos += word.length() + 1;
                }

            }

        } catch (IOException e) {
            // se imprime el error a la consola estandar de errores
            e.printStackTrace(System.err);
            // se imprime el error al log
            log(e);
        }
    }

    /*------------------ SOURCE FILE SECTION END ------------------*/

    // TODO: DOCUMENTATION FOR LOGGER

    public static void log(String message) {

        System.out.println();


        /* TODO: document effect of invoked parameters
         *
         * autoFlush = true; Every print flushes the stream, writing it to the file
         * append = true; Appends to the file
         */
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true), true)) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            out.printf("%-21s%s\n", now.format(formatter), message);

        } catch (IOException e) {
            // se imprime el error a la consola estandar de errores
            e.printStackTrace(System.err);
            // no se imprime en el log para evitar problemas de recusivas,
            // ya que si se genera un error lo más probable sea que se continue repitiendo
        }
    }

    public static void log(Throwable throwable) {
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true), true)) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            out.printf("%-21s%s\n", now.format(formatter), throwable.getMessage());

            for (StackTraceElement stack : throwable.getStackTrace()) {
                out.printf("%-21s%s\n", "", stack.toString());
            }

        } catch (IOException e) {
            // se imprime el error a la consola estandar de errores
            e.printStackTrace(System.err);
            // no se imprime en el log para evitar problemas de recusivas,
            // ya que si se genera un error lo más probable sea que se continue repitiendo
        }
    }
}
