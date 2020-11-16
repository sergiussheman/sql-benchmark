package com.itechart.benchmark.service;

import com.itechart.benchmark.dto.DatabaseSqlExecutionResult;
import com.itechart.benchmark.dto.DatabaseSqlExecutionResult.Result;
import com.itechart.benchmark.dto.DatabaseVendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DatabaseBenchmarkServiceITest {
    @Autowired
    private DatabaseBenchmarkService databaseBenchmarkService;


    @BeforeEach
    void createSchema() {
        String dropExistingSchemaSQL = "DROP TABLE IF EXISTS employee;";
        databaseBenchmarkService.executeQuery(dropExistingSchemaSQL);

        String createSchemaSQL = "CREATE TABLE IF NOT EXISTS employee (\n" +
                "  id int,\n" +
                "  first_name varchar(255) default NULL,\n" +
                "  department varchar(255),\n" +
                "  salary int default NULL\n" +
                ");";
        databaseBenchmarkService.executeQuery(createSchemaSQL);

        String populateDataSQL = "INSERT INTO employee (id,first_name,department,salary) VALUES (1,'Lewis','Luctus Lobortis Class Ltd',1365),(2,'Linus','Sit Limited',1716),(3,'Vladimir','Ultrices Posuere Industries',1549),(4,'Galvin','Cras Interdum Inc.',1606),(5,'Bruno','Nullam Lobortis Limited',1379),(6,'Nathan','Non Corp.',1829),(7,'Ferris','Egestas Foundation',1753),(8,'Wylie','Rutrum Justo Praesent Corporation',1915),(9,'Randall','Ut Nisi A Limited',1298),(10,'Price','Auctor Ullamcorper Inc.',1368),(11,'Kasper','Odio Corporation',1687),(12,'Davis','Libero Morbi Accumsan Foundation',1562),(13,'Hakeem','Eleifend Corporation',1640),(14,'Demetrius','Ante Nunc Mauris Corporation',1223),(15,'Leo','Semper Corp.',1914),(16,'Lewis','Semper Et Lacinia Incorporated',1380),(17,'James','Adipiscing Lobortis Associates',1445),(18,'Dale','Placerat Foundation',1346),(19,'Rigel','Pretium Et Rutrum LLP',1158),(20,'Barrett','Orci LLP',1016),(21,'Emmanuel','Tincidunt Associates',1315),(22,'Walker','Iaculis Incorporated',1221),(23,'Nolan','Lorem Institute',1613),(24,'Uriah','Libero Proin Sed Corporation',1104),(25,'Garth','Sed PC',1239),(26,'Brendan','Et Corp.',1902),(27,'Erasmus','Proin Mi Aliquam Limited',1087),(28,'Kennan','Semper PC',1604),(29,'Troy','Duis Gravida Praesent Foundation',1075),(30,'Giacomo','Sed Dictum Consulting',1567),(31,'Lance','Lorem Fringilla Ornare Associates',1967),(32,'Hakeem','Cursus Inc.',1275),(33,'Damian','Cubilia Curae; Phasellus Incorporated',1411),(34,'Garrett','Nunc Mauris Elit Inc.',1195),(35,'Uriel','Erat Eget Industries',1156),(36,'Jackson','Dui Cum LLP',1108),(37,'Anthony','Lectus Consulting',1182),(38,'Lee','Lacus Vestibulum Company',1563),(39,'Hamish','In Inc.',1920),(40,'Octavius','Dui Fusce PC',1838),(41,'Gray','Mollis Vitae Consulting',1463),(42,'Keefe','Amet Orci LLP',1673),(43,'Abbot','Nulla At Sem LLP',1511),(44,'Myles','Dictum Eu LLP',1508),(45,'Orson','Nascetur Ridiculus Associates',1755),(46,'Evan','Tellus Non Magna LLC',1768),(47,'Chandler','Pede Nunc Corp.',1764),(48,'Wade','Aliquam Associates',1684),(49,'Theodore','Lobortis Mauris Suspendisse PC',1688),(50,'Richard','Dictum Ltd',1554),(51,'Wing','Lacus Mauris Non Industries',1782),(52,'Graham','Orci Associates',1330),(53,'Philip','Tortor Ltd',1706),(54,'Stone','Nam Tempor Corp.',1683),(55,'Emerson','Laoreet Foundation',1540),(56,'Rashad','Id Risus Associates',1057),(57,'Kevin','Vestibulum Ltd',1896),(58,'Berk','Erat Nonummy Foundation',1197),(59,'Rashad','Egestas Nunc Sed Foundation',1177),(60,'Arsenio','A PC',1848),(61,'Amery','Risus Institute',1343),(62,'Nolan','Nisi Sem Inc.',1231),(63,'Brendan','Iaculis Nec Eleifend Inc.',1178),(64,'Alden','Rutrum Magna Associates',1194),(65,'Raja','Aptent Taciti Sociosqu LLC',1374),(66,'Edward','Nibh Phasellus Institute',1037),(67,'Nero','Integer Urna Vivamus Consulting',1504),(68,'Macaulay','Fringilla Consulting',1899),(69,'Trevor','Fames Incorporated',1818),(70,'Vincent','Imperdiet Non Ltd',1071),(71,'Blake','Cursus LLP',1527),(72,'Chester','Sem Ltd',1265),(73,'Price','Donec At LLP',1816),(74,'Wayne','Ac Inc.',1857),(75,'Elvis','Vitae Risus Duis Ltd',1683),(76,'Grant','Duis Mi Associates',1269),(77,'Raphael','Placerat Cras Inc.',1328),(78,'Wang','Ante Company',1813),(79,'Arden','Phasellus Dolor Elit Institute',1358),(80,'Armand','Sem Egestas Blandit Consulting',1010),(81,'Zeph','Turpis Nec Mauris Company',1877),(82,'Judah','Orci Incorporated',1478),(83,'Abbot','Gravida Industries',1788),(84,'Mohammad','Nisi Corporation',1703),(85,'Lionel','Suspendisse LLC',1139),(86,'Jonah','Ut Sagittis Lobortis Institute',1845),(87,'Lars','Elementum Purus Accumsan Limited',1207),(88,'Eagan','Donec Limited',1004),(89,'Wing','Scelerisque Dui Suspendisse Consulting',1096),(90,'Castor','Augue Ut Lacus Incorporated',1125),(91,'Clark','Amet Ultricies Associates',1586),(92,'Duncan','Libero Company',1642),(93,'Jacob','Consectetuer Adipiscing Consulting',1259),(94,'Jordan','Non Ante Bibendum Inc.',1865),(95,'Clarke','Vulputate Lacus Cras Associates',1830),(96,'Raymond','Vulputate Dui Ltd',1067),(97,'Simon','Orci LLC',1160),(98,'Cullen','Fusce Mi Lorem Institute',1810),(99,'Aladdin','Dui Company',1159),(100,'Colorado','Neque Ltd',1855);";
        databaseBenchmarkService.executeQuery(populateDataSQL);
    }

    @Test
    void executeSqlQuery() {
        String selectQuery = "SELECT * FROM employee where salary > 1500;";

        List<DatabaseSqlExecutionResult> databaseExecutionResults = databaseBenchmarkService.executeQuery(selectQuery);
        assertEquals(3, databaseExecutionResults.size());

        // check that all databases successfully executed the query
        for (DatabaseSqlExecutionResult executionResult : databaseExecutionResults) {
            assertEquals(Result.SUCCESS, executionResult.getResult());
            assertNotNull(executionResult.getExecutionTime());
            assertNull(executionResult.getErrorMessage());
        }

        // check the relative performance of databases
        databaseExecutionResults.sort((Comparator.comparing(DatabaseSqlExecutionResult::getExecutionTime)));
        assertEquals(DatabaseVendor.POSTGRESQL, databaseExecutionResults.get(0).getDatabaseVendor());
        assertEquals(DatabaseVendor.MYSQL, databaseExecutionResults.get(1).getDatabaseVendor());
        assertEquals(DatabaseVendor.MARIADB, databaseExecutionResults.get(2).getDatabaseVendor());

    }
}
