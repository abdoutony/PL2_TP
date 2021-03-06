/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp_jdbc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

/**
 * Programme de démonstration de JDBC. Permet de se connecter à une BD Oracle et
 * de tester différentes commandes SQL sur des tables représentant les
 * consommations de café de différents programmeurs.
 *
 * Les différentes opérations possibles sont : <ol> <li>Plus gros consommateurs
 * de café sur une semaine</li> <li>Nombre total de tasse pour un programmeur
 * donné</li> <li>La liste triées des consommations sur une semaine</li>
 * <li>Saisir les consommations de tous les programmeurs pour une semaine
 * donnée</li> <li>Exécuter une requête quelconque saisie au clavier et afficher
 * ses résultats</li> </ol>
 *
 *
 * @author Philippe.Genoud@imag.fr
 */
public class GestionCafes {

    /**
     * le scanner pour la saisie des données au clavier
     */
    private static Scanner sc = new Scanner(System.in);

    /**
     * la connexion jdbc utilisée pour effectuer les différentes requêtes
     */
    //private static Connection conn = null;
    /**
     * Affiche le ou les programmeurs ayant consommé le nombre maximum de café
     * en une semaine et leur consommation pour cette semaine.
     *
     * @param conn
     * @throws java.sql.SQLException
     */
    public static void plusGrosConsommateurs(Connection conn) throws SQLException {

        int i = 1;
        System.out.println("Les plus gros consommateurs de cafés sont :");
        String myQuery = "SELECT PROGRAMMEUR, PRENOM, NOM, NB_TASSES, NO_SEMAINE"
                + " FROM CONSOS_CAFE c JOIN PROGRAMMEURS p ON p.ID=c.PROGRAMMEUR"
                + " WHERE c.NB_TASSES = (SELECT MAX(NB_TASSES) FROM CONSOS_CAFE)";
        //Réalisation d'un try-with-ressources pour les objets Statement et ResultSet
        //possible car ces classes implémentent l'interface AutoClosable
        //l'exception SQLexception générée est trhows (sera gérée dans le main)
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(myQuery)) {

            while (rs.next()) {

                int id = rs.getInt("PROGRAMMEUR");
                String prenom = rs.getString("PRENOM");
                String nom = rs.getString("NOM");
                int nbTasses = rs.getInt("NB_TASSES");
                int noSemaine = rs.getInt("NO_SEMAINE");
                System.out.println("ROW" + i + " = " + id + ", " + prenom + ", " + nom + ", " + nbTasses
                        + ", " + noSemaine);
                i++;
            }
        }
    }

    /**
     * Affiche pour une semaine donnée la liste des programmeurs triée dans
     * l'ordre décroissant selon leur nombre de consommations et le nombre total
     * de tasses consommées cette semaine
     *
     * @param conn
     * @throws java.sql.SQLException
     */
    public static void consommationsPourUneSemaine(Connection conn) throws SQLException {

        int i = 1;
        System.out.print("Numéro de la semaine : ");
        int numeroDeSemaine = sc.nextInt();
        String requete = "SELECT PROGRAMMEUR,  PRENOM, NOM, NB_TASSES FROM \n"
                + "       CONSOS_CAFE c JOIN PROGRAMMEURS p ON c.PROGRAMMEUR=p.ID \n"
                + "       WHERE c.NO_SEMAINE = " + numeroDeSemaine + "\n"
                + "       ORDER BY NB_TASSES DESC";
        //Réalisation d'un try-with-ressources pour les objets Statement et ResultSet
        //possible car ces classes implémentent l'interface AutoClosable
        //l'exception SQLexception générée est trhows (sera gérée dans le main)
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(requete)) {

            while (rs.next()) {
                int id = rs.getInt("PROGRAMMEUR");
                String prenom = rs.getString("PRENOM");
                String nom = rs.getString("NOM");
                int nbTasses = rs.getInt("NB_TASSES");
                System.out.println("ROW" + i + " = " + id + ", " + prenom + ", " + nom + ", " + nbTasses);
                i++;
            }
        }
    }

    /**
     * pour un programmeur donné affiche le nombre total de tasses de café
     * consommées.
     *
     * @param conn
     * @throws java.sql.SQLException
     */
    public static void nbreTotalDeTasses(Connection conn) throws SQLException {

        int i = 1;
        System.out.print("Identifiant du programmeur : ");
        int idProgrammeur = sc.nextInt();
        String requete = "SELECT PRENOM, NOM, SUM(NB_TASSES) as TOT_TASSES FROM CONSOS_CAFE c "
                + "join PROGRAMMEURS p on p.ID = c.PROGRAMMEUR "
                + "WHERE PROGRAMMEUR=" + idProgrammeur + "Group by PRENOM, NOM";

        //Réalisation d'un try-with-ressources pour les objets Statement et ResultSet
        //possible car ces classes implémentent l'interface AutoClosable
        //l'exception SQLexception générée est trhows (sera gérée dans le main)
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(requete)) {
            while (rs.next()) {
                String prenom = rs.getString("PRENOM");
                String nom = rs.getString("NOM");
                int nbTasses = rs.getInt("TOT_TASSES");
                System.out.println("ROW" + i + " = " + prenom + ", " + nom + ", " + nbTasses);
                i++;
            }
        }
    }

    /**
     * Saisit un numéro de semaine et ensuite pour chaque programmeur permet de
     * rentrer le nombre de tasses qu'il a consommées durant cette semaine.
     *
     * @param conn
     * @throws java.sql.SQLException
     */
    public static void sasirConsommations(Connection conn) throws SQLException {

        System.out.print("Numéro de de la semaine : ");
        int noSemaine = sc.nextInt();
        System.out.println("ID du programmeur : ");
        int idProgrammeur = sc.nextInt();
        System.out.println("Nombre de tasses consommées : ");
        int nbTasses = sc.nextInt();
        //Réalisation d'un try-with-ressources pour l'objet PreparedStatement
        //possible car cette classe implémente l'interface AutoClosable
        //l'exception SQLexception générée est trhows (sera gérée dans le main)
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO CONSOS_CAFE (NO_SEMAINE, PROGRAMMEUR, NB_TASSES)\n"
                + "       VALUES(" + noSemaine + "," + idProgrammeur + "," + nbTasses + ")")) {
            ps.executeUpdate();
        }

    }

    /**
     * Exécute une requête libre définie par une chaîne donnée au clavier et
     * affiche les méta données concernant le résultat de cette requête
     * quelconque.<br/> <ul> <li>Si la command renvoie un ResultSet (Query)
     * cette méthode indique : <ul> <li>le nombre de colonnes, et pour chaque
     * colonne le nom et le type de la colonne.</li> <li>le contenu du resultSet
     * est affiché ligne par ligne sur la sortie standard.</li> </ul> </li>
     * <li>Si la commande ne renvoie pas un ResultSet (Update) cette méthode
     * indique le nombre de lignes de la table qui ont été modifiées. <li> </ul>
     *
     * @param conn
     * @throws java.sql.SQLException
     */
    public static void requeteLibreEtMetaDonnees(Connection conn) throws SQLException {

        boolean typeRequete;
        System.out.print("Rentrez votre requête : ");
        String cmd = sc.next() + sc.nextLine();
        //Réalisation d'un try-with-ressources pour les objets Statement et ResultSet
        //possible car ces classes implémentent l'interface AutoClosable
        //l'exception SQLexception générée est trhows (sera gérée dans le main)
        try (Statement stmt = conn.createStatement()) {
            typeRequete = stmt.execute(cmd);
            try (ResultSet rs = stmt.getResultSet()) {
                if (typeRequete) {

                    ResultSetMetaData rsmd = rs.getMetaData();
                    int nbColumn = rsmd.getColumnCount();
                    System.out.println("Le réslutat contient " + nbColumn + " colonne(s)");
                    String[] tabNameColumn = new String[nbColumn];
                    String[] tabTypeColumn = new String[nbColumn];
                    for (int i = 0; i <= nbColumn - 1; i++) {
                        tabNameColumn[i] = rsmd.getColumnName(i + 1);
                        tabTypeColumn[i] = rsmd.getColumnTypeName(i + 1);
                    }
                    for (int i = 0; i <= nbColumn - 1; i++) {

                        System.out.println("------------------------------------------------");
                        System.out.println("Colonne : " + (i + 1));
                        System.out.println("Nom : " + tabNameColumn[i] + ", Type : " + tabTypeColumn[i]);
                        System.out.println("------------------------------------------------");
                    }

                    System.out.println("Résultats de la requête : ");
                    System.out.println("");

                    while (rs.next()) {

                        String[] attribut = new String[nbColumn];
                        for (int j = 0; j <= nbColumn - 1; j++) {
                            attribut[j] = rs.getString(tabNameColumn[j]);
                        }

                        for (int j = 0; j <= nbColumn - 1; j++) {
                            if (j + 1 == nbColumn) {
                                System.out.println(" " + attribut[j]);
                            } else {
                                System.out.print(" " + attribut[j]);
                            }
                        }

                    }

                } else {
                    int nbUpdatedRows = stmt.getUpdateCount();
                    System.out.println("Nombre de lignes modifiées : " + nbUpdatedRows);
                }

            }

        }
    }

    public static void insertionDonneesCSV(Connection conn) throws FileNotFoundException, IOException, SQLException {
        System.out.println("Indiquez le nom de la table dans laquelle vous désirez insérer des données (programmeurs ou consos) : ");
        String fileName;
        String table = sc.next();
        if (table.equalsIgnoreCase("programmeurs")) {
            fileName = "schema/progs.data";
        } else {
            fileName = "schema/consos.data";
        }
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        if (fileName.contains("progs")) {
            PreparedStatement pstmt = conn.prepareStatement("INSERT into PROGRAMMEURS Values (?,?,?,?)");
            String ligne = reader.readLine();
            while (ligne != null) {
                String[] currentLine = ligne.split(",");
                pstmt.setInt(1, Integer.parseInt(currentLine[0]));
                pstmt.setString(2, currentLine[1]);
                pstmt.setString(3, currentLine[2]);
                pstmt.setInt(4, Integer.parseInt(currentLine[3]));
                pstmt.addBatch();
                ligne = reader.readLine();
            }
            int[] countInsertion = pstmt.executeBatch();
            System.out.println("Les données ont été insérées dans la table : PROGRAMMEURS");
        } else {
            PreparedStatement pstmt = conn.prepareStatement("INSERT into CONSOS_CAFE Values (?,?,?)");
            String ligne = reader.readLine();
            while (ligne != null) {
                String[] currentLine = ligne.split(",");
                for (int i = 0; i < currentLine.length; i++) {
                    pstmt.setInt(i + 1, Integer.parseInt(currentLine[i]));
                }
                pstmt.addBatch();
                ligne = reader.readLine();
            }
            int[] countInsertion = pstmt.executeBatch();
            System.out.println("Les données ont été insérées dans la table : CONSOS_CAFE");
        }

    }

    public static void sauvegardeDonneesCSV(Connection conn) throws IOException, SQLException {
        System.out.println("Quelle table souhaitez-vous sauvergarder au format CSV (programmeurs ou consos) : ");
        String tableName = sc.next();
        System.out.println("Quel sera le nom du fichier de sauvegarde : ");
        String saveName = sc.next();
        saveName = "sauvegardeCSV/" + saveName;
        if (tableName.equalsIgnoreCase("programmeurs")) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveName))) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PROGRAMMEURS");
                while (rs.next()) {
                    int attr1 = rs.getInt("ID");
                    String attr2 = rs.getString("NOM");
                    String attr3 = rs.getString("PRENOM");
                    int attr4 = rs.getInt("BUREAU");
                    writer.write(attr1 + "," + attr2 + "," + attr3 + "," + attr4);
                    writer.newLine();
                }
            }
            System.out.println("la table PROGRAMMEURS a été sauvergardée sous le nom " + saveName);
        } else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveName))) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM CONSOS_CAFE");
                while (rs.next()) {
                    int attr1 = rs.getInt("NO_SEMAINE");
                    int attr2 = rs.getInt("PROGRAMMEUR");
                    int attr3 = rs.getInt("NB_TASSES");
                    writer.write(attr1 + "," + attr2 + "," + attr3);
                    writer.newLine();
                }
            }
            System.out.println("la table CONSOS_CAFE a été sauvergardée sous le nom " + saveName);
        }
    }

    /**
     * affiche le menu présentant les différentes opérations possibles
     */
    public static void affMenu() {
        System.out.println("\n\n------------------------------------------");
        System.out.println("1 : Plus gros consommateurs sur une semaine");
        System.out.println("2 : Nombre total de tasses consommées par un programmeur");
        System.out.println("3 : Consommations pour une semaine donnée");
        System.out.println("4 : Sasie des consommations pour une semaine");
        System.out.println("5 : Requête Libre ");
        System.out.println("6 : Insertion de données depuis un fichier (CSV)");
        System.out.println("7 : Sauvegarde de données dans un fichier (CSV)");
        System.out.println("0 : Quitter l'application");
    }

    public static String getLogin() throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        String propertiesFileName = "../../../JDBC.properties";
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(propertiesFileName));
        prop.load(input);
        return prop.getProperty("login");
    }
    
    public static String getPassword() throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        String propertiesFileName = "../../../JDBC.properties";
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(propertiesFileName));
        prop.load(input);
        return prop.getProperty("password");
    }

    public static void main(String[] args) {

        // TODO
        // chargement du driver JDBC
        try {
            Class.forName("oracle.jdbc.OracleDriver").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            System.exit(0); //Si on peut pas charger le driver on quitte le système.
        }
        // et ouverture d'une connexion
        try {
            String username = getLogin();
            String password = getPassword();
            try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:im2ag", username, password)) {
                System.out.println("Connexion réussie !");
                boolean encore = true;
                do {
                    affMenu();
                    try {
                        System.out.print("votre choix : ");
                        int rep = sc.nextInt();

                        System.out.println("\n\n");

                        switch (rep) {
                            case 0:
                                System.out.print("voulez-vous vraimment quitter le programme O/N ?");
                                encore = sc.next().toUpperCase().charAt(0) != 'O';
                                break;
                            case 1:
                                plusGrosConsommateurs(conn);
                                break;
                            case 2:
                                nbreTotalDeTasses(conn);
                                break;
                            case 3:
                                consommationsPourUneSemaine(conn);
                                break;
                            case 4:
                                sasirConsommations(conn);
                                break;
                            case 5:
                                requeteLibreEtMetaDonnees(conn);
                                break;
                            case 6:
                                insertionDonneesCSV(conn);
                                break;
                            case 7:
                                sauvegardeDonneesCSV(conn);
                                break;
                            default:
                                System.out.println("valeur erronée !");
                        }  // end switch

                    } catch (InputMismatchException ex) {
                        System.out.println("saisie incorrecte\nRecommencez !!");
                        sc.nextLine();   // pour "vider" le scanner
                    }
                } while (encore);
            } catch (SQLException | IOException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                System.out.println(Arrays.toString(e.getStackTrace()));
            }

            // TODO
            // Fermer la connexion à la BD
        }catch (IOException e){
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
        }
    }
}
