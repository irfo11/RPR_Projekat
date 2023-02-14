CREATE DATABASE  IF NOT EXISTS `freedb_RPR_Project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `freedb_RPR_Project`;
-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: sql.freedb.tech    Database: freedb_RPR_Project
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `micronutrients`
--

DROP TABLE IF EXISTS `micronutrients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `micronutrients` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `role` mediumtext NOT NULL,
  `isVitamin` tinyint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `micronutrients`
--

LOCK TABLES `micronutrients` WRITE;
/*!40000 ALTER TABLE `micronutrients` DISABLE KEYS */;
INSERT INTO `micronutrients` VALUES (11,'A','Keeps eyes healthy, growth, cell division, reproduction, immunity',1),(12,'D','Helps the body absorb and retain calcium and phosphorus, controls infection, reduces inflammation',1),(13,'C','Protects cells against free radicals, immunity',1),(14,'B1','Changes carbohydrates into energy',1),(15,'B3','Helps body use fats and proteins, keeps skin, hair and nervous system healthy',1),(16,'B6','Creates red blood cells, forms genetic material',1),(17,'B12','Forms red blood cells, and genetic material',1),(18,'Calcium','Blood clotting, helping muscles to contract, regulating normal heart rythms',0),(19,'Magnesium','Regulates muscle and nerve function, blood sugar levels, blood pressure, bone and DNA',0),(20,'Iron','Red blood cells need iron and it\'s needed for some hormones',0),(21,'Potassium','Help mantain normal levels of fluid inside cells',0);
/*!40000 ALTER TABLE `micronutrients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `presence`
--

DROP TABLE IF EXISTS `presence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `presence` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `micronutrient` int unsigned NOT NULL,
  `source` int unsigned NOT NULL,
  `amount` double unsigned NOT NULL COMMENT 'per 100 grams',
  PRIMARY KEY (`id`),
  UNIQUE KEY `micronutrient` (`micronutrient`,`source`),
  KEY `source_id_idx` (`source`),
  KEY `fk_micronutrient_id_idx` (`micronutrient`),
  CONSTRAINT `fk_micronutrient_id` FOREIGN KEY (`micronutrient`) REFERENCES `micronutrients` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_source_id` FOREIGN KEY (`source`) REFERENCES `sources` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `presence`
--

LOCK TABLES `presence` WRITE;
/*!40000 ALTER TABLE `presence` DISABLE KEYS */;
INSERT INTO `presence` VALUES (17,13,29,4.6),(18,18,29,6),(19,21,30,380),(20,18,31,6),(21,21,31,422),(22,13,31,10.3),(23,17,37,0.00245),(24,20,37,3.5),(25,16,37,0.25),(26,19,37,25),(27,21,37,300),(28,18,32,29),(29,20,32,0.62),(30,21,32,162),(31,11,33,0.003),(32,11,34,0.003),(33,15,36,1.4),(34,16,36,0.3),(35,13,36,9.6),(36,19,36,28),(37,20,36,1.1),(38,13,35,26.6),(39,15,35,0.6),(40,14,35,0.07);
/*!40000 ALTER TABLE `presence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sources`
--

DROP TABLE IF EXISTS `sources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sources` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sources`
--

LOCK TABLES `sources` WRITE;
/*!40000 ALTER TABLE `sources` DISABLE KEYS */;
INSERT INTO `sources` VALUES (29,'Apple'),(30,'Avocado'),(31,'Banana'),(37,'Beef'),(32,'Blackberry'),(33,'Cherry'),(34,'Grape'),(36,'Potato'),(35,'Tomato');
/*!40000 ALTER TABLE `sources` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-14 12:22:37
