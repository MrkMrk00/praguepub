package cz.vse.praguePub.logika;

import com.mongodb.MongoCommandException;
import com.mongodb.MongoSecurityException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import cz.vse.praguePub.util.AesUtil;
import lombok.Getter;
import lombok.ToString;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.mongodb.client.model.Filters.*;
import static cz.vse.praguePub.util.AesUtil.fillTo16Chars;

/**
 * Třída uživatele databáze.<br>
 * Obsahuje logiku přihlášení do databáze. Public konstruktor přijímá jako argumenty uživ. jméno a heslo.
 * Z databáze je staženo heslo s příslušným uživatelským jménem z konstruktoru a je dešifrováno.<br>
 * Výsledek dešifrování je heslo databázového uživatele, který může databázi upravovat: registrovaný uživatel aplikace.<br>
 * <br>
 * Přihlašovací jména databázových uživatelů jsou uloženy v konstantě DB_USERNAMES.<br>
 * <br>
 * Třída zároveň řeší i speciální případ uživatele - hosta (guest). Heslo příslušného databázového uživatele
 * je uloženo v konstantě GUEST_PASSWORD. Pro přihlášení hosta se používá privátní konstruktor bez argumentů.
 */
@ToString
public class Uzivatel {
    private static final Logger LOGGER = LoggerFactory.getLogger(Uzivatel.class);

    @Getter private final String        username;
    @Getter private final MongoClient   client;
    @Getter private boolean             prihlasen = false;
    @Getter private final boolean       isGuest;

    @Getter private ObjectId            _id = null;
    private MongoDatabase               praguePubDatabaze = null;


    private static Uzivatel guest = null;
    private final static String GUEST_PASSWORD = "w22dY4DAJ7c2Rzf";

    //connection string je adresa clusteru mongoDB. V místě, kde je <> je potřeba doplnit jméno:heslo.
    private final static String CON_STRING = "mongodb+srv://<>@cluster0.g7yxo.mongodb.net";
    private final static Map<String, String> DB_USERNAMES = Map.of(
            "guest",    "guest",
            "admin",    "admin",
            "regular",  "registered_user"
    );

    /**
     * Konstruktor uživatele aplikace, přihlašuje uživatele zároveň do databáze
     * @param prihlasovaciJmeno přihlašovací jméno uživatele
     * @param heslo heslo uživatele
     */
    public Uzivatel(String prihlasovaciJmeno, String heslo) {
        this(prihlasovaciJmeno, heslo, "regular");
    }

    /**
     * Konstruktor uživatele aplikace, přihlašuje uživatele zároveň do databáze
     * @param prihlasovaciJmeno přihlašovací jméno uživatele
     * @param heslo heslo uživatele
     * @param dbUsername jméno databázového uživatele
     */
    public Uzivatel(String prihlasovaciJmeno, String heslo, String dbUsername) {
        this.username = prihlasovaciJmeno;
        this.isGuest = false;

        String dbPassword = this.getPasswordFromDatabase(prihlasovaciJmeno, heslo);
        this.client = this.dbLogin(DB_USERNAMES.get(dbUsername), dbPassword);

        Document userDoc = null;
        if (this.prihlasen) {
            userDoc = this.client
                    .getDatabase("prague_pub")
                    .getCollection("uzivatele")
                    .find(Filters.eq("userName", prihlasovaciJmeno))
                    .first();
        }

        this._id = (userDoc == null ? null : userDoc.getObjectId("_id"));
    }

    /**
     * Vytvoří instanci uživatele - guesta a přihlásí ho do databáze
     */
    private Uzivatel() {
        this.username = "guest";
        this.isGuest = true;
        this.client = this.dbLogin(DB_USERNAMES.get("guest"), GUEST_PASSWORD);
    }

    /**
     * Metoda řeší speciální instanci uživatele - guest <br>
     * Pokud je již guest vytvořen, tak pouze vrátí jeho již vytvořenou instanci, v opačném případě instanci vytvoří.
     * @return instanci uživatele - guest
     */
    public static Uzivatel guest() {
        if (Uzivatel.guest == null) {
            Uzivatel guestLogin = new Uzivatel();

            if (!guestLogin.isPrihlasen()) {
                return null;
            }
            Uzivatel.guest = guestLogin;
        }
        return Uzivatel.guest;
    }

    /**
     * Získá podle přihlašovacího jména a hesla Document z databáze a z něho následně položku password<br>
     * Password v databázi je zašifrované heslo databázového uživatele heslem uživatelským. Tzn. po dešifrování metodou AesUtil.decrypt()
     * je získáno heslo pro přihlášení do databáze.
     * @param username přihlašovací jméno uživatele aplikace
     * @param password heslo uživatele aplikace
     * @return heslo databázového uživatele
     */
    private String getPasswordFromDatabase(String username, String password) {
        String dbPassword = null;
        AesUtil au = null;

        Uzivatel guestInstance = Uzivatel.guest();
        if (guestInstance == null) return null;

        Document userDocument = guestInstance
                .getPraguePubDatabaze()
                .getCollection("uzivatele")
                .find(eq("userName", username))
                .first();

        if (userDocument != null && !userDocument.isEmpty()) {
            dbPassword = userDocument.get("password").toString();
            au = new AesUtil(128, 1000);
        }

        if (au == null) return null;

        return au.decrypt(dbPassword, fillTo16Chars(password));
    }

    /**
     * Inicializace databázového klienta
     * @param username přihlašovací jméno databázového uživatele
     * @param password heslo databázového uživatele
     * @return instanci databázového klienta
     */
    private MongoClient dbLogin(String username, String password) {
        MongoClient mongoClient = MongoClients.create(fillConString(username, password));
        try {
            Document doc = mongoClient
                    .getDatabase("prague_pub")
                    .getCollection("uzivatele")
                    .find(Filters.eq("userName", "admin"))
                    .first();

            this.prihlasen = doc != null && !doc.isEmpty();
        } catch ( MongoCommandException | MongoSecurityException e) {
            mongoClient.close();
            this.prihlasen = false;
        }
        LOGGER.info(this.prihlasen ? "Přihlášení se zdařilo: uživatel " + this.username : "Přihlášení se nezdařilo");
        return mongoClient;
    }

    /**
     * @return databázi <i>prague_pub</i> v db clusteru
     */
    public MongoDatabase getPraguePubDatabaze() {
        if (this.praguePubDatabaze == null) {
            this.praguePubDatabaze = this.getClient().getDatabase("prague_pub");
        }
        return this.praguePubDatabaze;
    }

    /**
     * Metoda doplnuje connection string pro přihlášení do databáze
     * @param username přihlašovací jméno databázového uživatele
     * @param password heslo databázového uživatele
     * @return doplněný String
     */
    private static String fillConString(String username, String password) {
        String[] splitByUsername = CON_STRING.split("<>");
        return splitByUsername[0] + username + ":" + password + splitByUsername[1];
    }
}
