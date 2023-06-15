
import com.mongodb.client.*;
import org.bson.Document;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mongodb.client.model.Filters.eq;

public class DatabaseManager {
    private MongoDatabase database;
    private final Scanner scanner;
    private String id;
    private String nev;
    private String tel;
    private String ceg;

    public DatabaseManager() throws InterruptedException {
        scanner = new Scanner(System.in);
        connect();
        start();
    }

    public void connect() {
        MongoClient mongo = MongoClients.create();
        database = mongo.getDatabase("nyilvantarto");
        System.out.println("Connected to the database successfully");
    }

    public void start() throws InterruptedException {
        while(true) {
            System.out.println("""
                    Kerem adja meg, hogy milyen muveletet szeretne vegrehajtani:
                    1) feltoltes
                    2) modositas
                    3) torles
                    4) lekerdezes
                    kilepeshez nyomjon meg barmilyen mas billenytut meg""");
            String n = scanner.next();
            switch (n) {
                case "1":
                    insert();
                    break;
                case "2":
                    modify();
                    break;
                case "3":
                    delete();
                    break;
                case "4":
                    query();
                    break;

                default:
                    return;
            }
        }
    }

    private void query() {
        System.out.println("""
                Kerem adja meg, hogy melyik kollekcioban kivan torolni adatot:
                1)fejleszto
                2)reszleg
                3)fonok
                4)megbizo
                5)megbizas
                6)az osszes kollekcioba""");
        String collection = scanner.next();
        switch (collection) {
            case "1":
                collection = "feladatkiosztas";
                break;
            case "2":
                collection = "feladattagolas";
                break;
            case "3":
                collection = "fonokok";
                break;
            case "4":
                collection = "megbizatas";
                break;
            case "5":
                collection = "megbizas";
                break;
            case "6":

            default:
                System.out.println("error");
        }
        List<String> keys = getKey(collection);
        System.out.println("Kerem adja meg a modositani kivant mezot:\n");
        int i = 1;
        for(String s : keys) {
            System.out.println(i + ") " + s);
            i ++;
        }
        i = scanner.nextInt();
        System.out.println("collection: " + collection + "\n" + findAllInCollection(collection));
        System.out.println("Adja meg a modositani kivant mezo jelenlegi erteket:\n");
        String value = scanner.next();
        findInCollectionByParameter(collection, keys.get(i-1), value);
    }

    private void delete() {
        System.out.println("""
                Kerem adja meg, hogy melyik kollekcioban kivan torolni adatot:
                1)fejleszto
                2)reszleg
                3)fonok
                4)megbizo
                5)megbizas
                6)az osszes kollekcioba""");
        String collection = scanner.next();
        switch (collection) {
            case "1":
                collection = "feladatkiosztas";
                break;
            case "2":
                collection = "feladattagolas";
                break;
            case "3":
                collection = "fonokok";
                break;
            case "4":
                collection = "megbizatas";
                break;
            case "5":
                collection = "megbizas";
                break;
            default:
                System.out.println("error");
        }
        List<String> keys = getKey(collection);
        System.out.println("Kerem adja meg a modositani kivant mezot:\n");
        int i = 1;
        for(String s : keys) {
            System.out.println(i + ") " + s);
            i ++;
        }
        i = scanner.nextInt();
        System.out.println("collection: " + collection + "\n" + findAllInCollection(collection));
        System.out.println("Adja meg a modositani kivant mezo jelenlegi erteket:\n");
        String value = scanner.next();
        deleteInCollectionByParameter(collection, keys.get(i-1), value);

    }

    public void insert() {
        System.out.println("""
                Kerem adja meg, hogy melyik kollekcioba kivan feltolteni adatot:
                1)fejleszto
                2)reszleg
                3)fonok
                4)megbizo
                5)megbizas
                6)az osszes kollekcioba""");
        int n = scanner.nextInt();
        switch (n) {
            case 1 -> fejleszto();
            case 2 -> reszleg();
            case 3 -> fonok();
            case 4 -> megbizo();
            case 5 -> megbizas();
            case 6 -> {
                fejleszto();
                reszleg();
                fonok();
                megbizo();
                megbizas();
            }
            default -> System.out.println("rossz input");
        }
    }

    public void modify() {
        System.out.println("""
                Kerem adja meg, hogy melyik kollekcioban kivan modositani:
                1)fejleszto
                2)reszleg
                3)fonok
                4)megbizo
                5)megbizas
                """);

        String collection = scanner.next();
        switch (collection) {
            case "1":
                collection = "feladatkiosztas";
                break;
            case "2":
                collection = "feladattagolas";
                break;
            case "3":
                collection = "fonokok";
                break;
            case "4":
                collection = "megbizatas";
                break;
            case "5":
                collection = "megbizas";
                break;
            default:
                System.out.println("error");
        }
        List<String> keys = getKey(collection);
        System.out.println("Kerem adja meg a modositani kivant mezot:\n");
        int i = 1;
        for(String s : keys) {
            System.out.println(i + ") " + s);
            i ++;
        }
        i = scanner.nextInt();
        System.out.println("collection: " + collection + "\n" + findAllInCollection(collection));
        System.out.println("Adja meg a modositani kivant mezo jelenlegi erteket:\n");
        String value = scanner.next();
        updateInCollectionByParameter(collection, keys.get(i-1), value);
    }


    public void fejleszto() {
        System.out.println("Adja meg az id-t:");
        id = scanner.next();
        System.out.println("Adja meg a nevet: ");
        nev = scanner.next();
        System.out.println("Adja meg az emailcimet: ");
        String email = scanner.next();
        System.out.println("Adja meg a nyelveket vesszovel elvalasztva:");
        String nyelvek = scanner.next();
        System.out.println("Adja meg a telefonszamot:");
        tel = scanner.next();
        System.out.println("Adja meg az utcat:");
        String utca = scanner.next();
        System.out.println("Adja meg a hazszamot:");
        int hsz = scanner.nextInt();
        insertFejleszto(id, nev, email, nyelvek, tel, utca, hsz);
    }

    public void reszleg() {
        System.out.println("Adja meg az id-t: ");
        id = scanner.next();
        System.out.println("Adja meg a megnevezest:");
        String megnevezes = scanner.next();
        insertReszleg(id, megnevezes);
    }

    public void fonok() {
        System.out.println("Adja meg az id-t: ");
        id = scanner.next();
        System.out.println("Adja meg a nevet: ");
        String nev = scanner.next();
        System.out.println("Adja meg a ceg nevet: ");
        String ceg = scanner.next();
        System.out.println("Adja meg a telefonszamot:");
        tel = scanner.next();
        insertFonok(id, nev, ceg, tel);
    }

    public void megbizo() {
        System.out.println("Adja meg az id-t: ");
        id = scanner.next();
        System.out.println("Adja meg a nevet: ");
        nev = scanner.next();
        System.out.println("Adja meg a ceg nevet: ");
        ceg = scanner.next();
        insertMegbizo(id, nev, ceg);
    }

    public void megbizas() {
        System.out.println("Adja meg az id-t:");
        id = scanner.next();
        System.out.println("Adja meg a hataridot:");
        String hatarido = scanner.next();
        System.out.println("Adja meg a statuszt:");
        String statusz = scanner.next();
        System.out.println("Adja meg a leirast:");
        String leiras = scanner.next();
        insertMegbizas(id, hatarido, statusz, leiras);
    }

    public void insertFejleszto(String id, String nev, String email, String nyelvek, String tel, String utca, int hsz) {
//        if (database.getCollection("feladatkiosztas")) {
//
//        }
        Document fejlesztoDoc = new Document("_id", id).append("nev", nev).append("email", email).append("nyelvek", nyelvek)
                .append("telefonszam", tel).append("lakcim", List.of(new Document("utca", utca).append("hazszam", hsz)));
        MongoCollection<Document> feladatkiosztas = database.getCollection("feladatkiosztas");
        feladatkiosztas.insertOne(fejlesztoDoc);
        System.out.println("insert success");
    }

    public void insertReszleg(String id, String megnevezes) {
        Document reszlegDoc = new Document("_id", id).append("megnevezes", megnevezes);
        MongoCollection<Document> feladattagolas = database.getCollection("feladattagolas");
        feladattagolas.insertOne(reszlegDoc);
        System.out.println("insert success");
    }

    public void insertFonok(String id, String nev, String ceg, String tel) {
        Document fonokDoc = new Document("_id", id).append("nev", nev).append("ceg", ceg).append("telefonszam", tel);
        MongoCollection<Document> fonokok = database.getCollection("fonokok");
        fonokok.insertOne(fonokDoc);
        System.out.println("insert success");
    }

    public void insertMegbizo(String id, String nev, String ceg) {
        Document megbizoDoc =
                new Document("_id", id).append("nev", nev).append("ceg", ceg);
        MongoCollection<Document> megbizatas = database.getCollection("megbizatas");
        megbizatas.insertOne(megbizoDoc);
        System.out.println("insert success");
    }

    public void insertMegbizas(String id, String hatarido, String statusz, String leiras) {
        Document megbizasDoc =
                new Document("_id", id).append("hatarido", hatarido).append("statusz", statusz)
                        .append("leiras", leiras);
        MongoCollection<Document> megbizas = database.getCollection("megbizas");
        megbizas.insertOne(megbizasDoc);
        System.out.println("insert success");
    }

    public Document findInCollectionByParameter(String collection, String parameter, String value) {

        MongoCollection<Document> collection1 = database.getCollection(collection);
        Document doc = null;
        try {
            doc = collection1.find(eq(parameter, value)).first();
            assert doc != null;
            System.out.println(doc.toJson());
        } catch (NullPointerException e) {
            System.out.println("Exception: " + e.getMessage());
        }


        return doc;
    }

    public List<Document> findAllInCollection(String collection) {
        List<Document> result = new ArrayList<>();
        MongoCollection<Document> collection1 = database.getCollection(collection);
        try (MongoCursor<Document> cursor = collection1.find().iterator()) {
            while (cursor.hasNext()) {
                result.add(cursor.next());
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        System.out.println("result:\n" +result);
        return result;
    }

    public void updateInCollectionByParameter(String collection, String parameter, String value){

        parameter = parameter.replace(" ", "");
        System.out.println(collection + ", " + parameter + ", " + value);
        MongoCollection<Document> collection1 = database.getCollection(collection);
        System.out.println("adja meg az uj erteket: ");
        String value1 = scanner.next();
        System.out.println(value1);
        collection1.updateOne(eq(parameter, value), new Document("$set", new Document(parameter, value1)));
        System.out.println("Sikeres muvelet!");
    }

    public void deleteInCollectionByParameter(String collection, String parameter, String value) {
        parameter = parameter.replace(" ", "");
        System.out.println(collection + ", " + parameter + ", " + value);
        MongoCollection<Document> collection1 = database.getCollection(collection);
        collection1.deleteOne(eq(parameter, value));
    }

    public List<String> getKey(String collection) {
        if(collection == null) {
            System.out.println("null string");
            return null;
        }
        MongoCollection<Document> collection1 = database.getCollection(collection);
        Document myDoc = collection1.find().first();
        assert myDoc != null;
        List<String> result =
                Arrays.stream(myDoc.toJson().replace("{", "").replace("}", "").replace("\"", "").split(":")).toList();

        List<String> buf = new ArrayList<>();
        List<String> bufR = new ArrayList<>();
        boolean isTrue = false;
        for(String s : result) {
            if(s.contains(",")) {
                String[] a = s.split(",");
                buf.add(a[0]);
                buf.add(a[1]);
            } else if(s.contains("[")) {
                bufR.add(s.replace("[", ""));
                isTrue = true;
            } else if(isTrue) {
                bufR.add(s);
            } else {
                buf.add(s);
            }
        }
        bufR = getEvery2ndElement(bufR);


        result = buf;
        result = getEvery2ndElement(result);
        result.addAll(bufR);
        System.out.println(result);

        return result;
    }


    public List<String> getEvery2ndElement(List<String> list) {
        List<String> result = list;
        int skip = 2;
        int size = result.size();
        int limit = size / skip + Math.min(size % skip, 1);
        result = Stream.iterate(0, i -> i + skip)
                .limit(limit)
                .map(result::get)
                .collect(Collectors.toList());
        return result;
    }
}
