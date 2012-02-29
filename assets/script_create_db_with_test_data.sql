-- MySQL dump 10.13  Distrib 5.1.54, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: bd_tfg
-- ------------------------------------------------------
-- Server version	5.1.54-1ubuntu4

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `bd_tfg`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `bd_tfg` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `bd_tfg`;

--
-- Table structure for table `BusCompanies`
--

DROP TABLE IF EXISTS `BusCompanies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BusCompanies` (
  `CIF` varchar(9) NOT NULL,
  `name` varchar(45) NOT NULL,
  `auth_code` varchar(12) NOT NULL,
  `email` varchar(45) NOT NULL,
  PRIMARY KEY (`CIF`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BusCompanies`
--

LOCK TABLES `BusCompanies` WRITE;
/*!40000 ALTER TABLE `BusCompanies` DISABLE KEYS */;
INSERT INTO `BusCompanies` VALUES ('A11223344','Compañía de ejemplo','112233445566','compañia@compañia.com');
/*!40000 ALTER TABLE `BusCompanies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BusDrivers`
--

DROP TABLE IF EXISTS `BusDrivers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BusDrivers` (
  `DNI` varchar(9) NOT NULL,
  `company_CIF` varchar(9) NOT NULL,
  `name` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  PRIMARY KEY (`DNI`),
  KEY `fk_BusDrivers_1` (`company_CIF`),
  CONSTRAINT `fk_BusDrivers_1` FOREIGN KEY (`company_CIF`) REFERENCES `BusCompanies` (`CIF`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BusDrivers`
--

LOCK TABLES `BusDrivers` WRITE;
/*!40000 ALTER TABLE `BusDrivers` DISABLE KEYS */;
INSERT INTO `BusDrivers` VALUES ('11223344B','A11223344','Conductor Ejemplo','86f7e437faa5a7fce15d1ddcb9eaeaea377667b8','conductor@conductor.com');
/*!40000 ALTER TABLE `BusDrivers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Cities`
--

DROP TABLE IF EXISTS `Cities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Cities` (
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Cities`
--

LOCK TABLES `Cities` WRITE;
/*!40000 ALTER TABLE `Cities` DISABLE KEYS */;
INSERT INTO `Cities` VALUES ('A Coruña'),('Lugo'),('Ourense'),('Santiago'),('Vigo');
/*!40000 ALTER TABLE `Cities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Lines`
--

DROP TABLE IF EXISTS `Lines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Lines` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `city_name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Lines_1` (`city_name`)
) ENGINE=MyISAM AUTO_INCREMENT=134 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Lines`
--

LOCK TABLES `Lines` WRITE;
/*!40000 ALTER TABLE `Lines` DISABLE KEYS */;
INSERT INTO `Lines` VALUES (1,'L1','Santiago'),(2,'L2','Santiago'),(3,'LC11','Santiago'),(4,'L4','Santiago'),(5,'L5','Santiago'),(6,'L6','Santiago'),(7,'L7','Santiago'),(8,'L8','Santiago'),(9,'L12','Santiago'),(10,'L13','Santiago'),(11,'L15','Santiago'),(12,'LC2','Santiago'),(13,'LC4','Santiago'),(14,'LC5','Santiago'),(15,'LC6','Santiago'),(16,'LC9','Santiago'),(18,'LP1','Santiago'),(19,'LP2','Santiago'),(20,'LP3','Santiago'),(21,'LP4','Santiago'),(22,'LP5','Santiago'),(23,'LP6','Santiago'),(24,'LP7','Santiago'),(25,'LP8','Santiago'),(26,'L1','A Coruña'),(27,'L1A','A Coruña'),(28,'L2','A Coruña'),(29,'L2A','A Coruña'),(30,'L3','A Coruña'),(31,'L3A','A Coruña'),(32,'L4','A Coruña'),(33,'L5','A Coruña'),(34,'L6','A Coruña'),(35,'L6A','A Coruña'),(36,'L7','A Coruña'),(37,'L11','A Coruña'),(38,'L12','A Coruña'),(39,'L12A','A Coruña'),(40,'L14','A Coruña'),(41,'L17','A Coruña'),(42,'LB','A Coruña'),(43,'L20','A Coruña'),(44,'L21','A Coruña'),(45,'L22','A Coruña'),(46,'L23','A Coruña'),(47,'L23A','A Coruña'),(48,'L24','A Coruña'),(49,'LE','A Coruña'),(50,'LCCV','Vigo'),(51,'LC1','Vigo'),(52,'LC2','Vigo'),(53,'LC3','Vigo'),(54,'LC4A','Vigo'),(55,'LC4B','Vigo'),(56,'LC4C','Vigo'),(57,'LC5A','Vigo'),(58,'LC5B','Vigo'),(59,'LC6','Vigo'),(60,'LC7','Vigo'),(61,'L8','Vigo'),(62,'LC9A','Vigo'),(63,'LC9B','Vigo'),(64,'L10','Vigo'),(65,'L11','Vigo'),(66,'L12A','Vigo'),(67,'L12B','Vigo'),(68,'L13','Vigo'),(69,'L14','Vigo'),(70,'LC15A','Vigo'),(71,'LC15B','Vigo'),(72,'LC15C','Vigo'),(73,'LC16','Vigo'),(74,'L17','Vigo'),(75,'L18','Vigo'),(76,'L20','Vigo'),(77,'L21','Vigo'),(78,'LC22','Vigo'),(79,'L23','Vigo'),(80,'L25','Vigo'),(81,'L28','Vigo'),(82,'L29','Vigo'),(83,'L31','Vigo'),(84,'LN1','Vigo'),(85,'LN2','Vigo'),(86,'LU1','Vigo'),(87,'LU2','Vigo'),(88,'L1','Lugo'),(89,'L1A','Lugo'),(90,'L2','Lugo'),(91,'L3','Lugo'),(92,'L3A','Lugo'),(93,'L4A','Lugo'),(94,'L5A','Lugo'),(95,'L6','Lugo'),(96,'L7','Lugo'),(97,'L7A','Lugo'),(98,'L8','Lugo'),(99,'L9','Lugo'),(100,'L9A','Lugo'),(101,'L10','Lugo'),(102,'L11','Lugo'),(103,'L12','Lugo'),(104,'L14','Lugo'),(105,'L14A','Lugo'),(106,'L1','Ourense'),(107,'L2','Ourense'),(108,'L3','Ourense'),(109,'L4','Ourense'),(110,'L5','Ourense'),(111,'L6A','Ourense'),(112,'L6B','Ourense'),(113,'L7','Ourense'),(114,'L9','Ourense'),(115,'L10','Ourense'),(116,'L11A','Ourense'),(117,'L11B','Ourense'),(118,'L12','Ourense'),(119,'L13A','Ourense'),(120,'L13B','Ourense'),(121,'L14','Ourense'),(122,'L16','Ourense'),(123,'L15','Ourense'),(124,'L17','Ourense'),(125,'L18','Ourense'),(126,'L22','Ourense'),(127,'L33','Ourense'),(128,'L19','Ourense'),(129,'L21','Ourense'),(130,'L24','Ourense'),(131,'L23','Ourense'),(132,'LL1','Ourense'),(133,'LL2','Ourense');
/*!40000 ALTER TABLE `Lines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TaxiDrivers`
--

DROP TABLE IF EXISTS `TaxiDrivers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TaxiDrivers` (
  `DNI` varchar(9) NOT NULL,
  `name` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `licence_number` varchar(15) DEFAULT NULL,
  `car_brand` varchar(20) DEFAULT NULL,
  `car_model` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`DNI`),
  KEY `fk_TaxiDrivers_1` (`licence_number`),
  CONSTRAINT `fk_TaxiDrivers_1` FOREIGN KEY (`licence_number`) REFERENCES `TaxiLicences` (`licence_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TaxiDrivers`
--

LOCK TABLES `TaxiDrivers` WRITE;
/*!40000 ALTER TABLE `TaxiDrivers` DISABLE KEYS */;
INSERT INTO `TaxiDrivers` VALUES ('33445566A','Taxista Ejemplo','86f7e437faa5a7fce15d1ddcb9eaeaea377667b8','taxista@taxista.com','444555111','Marca','Modelo');
/*!40000 ALTER TABLE `TaxiDrivers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TaxiLicences`
--

DROP TABLE IF EXISTS `TaxiLicences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TaxiLicences` (
  `licence_number` varchar(15) NOT NULL,
  `assigned_DNI` varchar(9) NOT NULL,
  PRIMARY KEY (`licence_number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TaxiLicences`
--

LOCK TABLES `TaxiLicences` WRITE;
/*!40000 ALTER TABLE `TaxiLicences` DISABLE KEYS */;
INSERT INTO `TaxiLicences` VALUES ('444555111','32471278B');
/*!40000 ALTER TABLE `TaxiLicences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Users` (
  `nickname` varchar(10) NOT NULL,
  `name` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  PRIMARY KEY (`nickname`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-02-05 20:33:22
