-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: proyectotussam
-- ------------------------------------------------------
-- Server version	8.0.19

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
-- Table structure for table `administrador`
--

DROP TABLE IF EXISTS `administrador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administrador` (
  `idadministrador` int NOT NULL AUTO_INCREMENT,
  `dni` varchar(9) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `apellidos` varchar(45) NOT NULL,
  `fecha_creacion` bigint DEFAULT NULL,
  PRIMARY KEY (`idadministrador`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrador`
--

LOCK TABLES `administrador` WRITE;
/*!40000 ALTER TABLE `administrador` DISABLE KEYS */;
INSERT INTO `administrador` VALUES (11,'12456781X','Gilberto','Torres Torres',12141254),(12,'2456789I','Alberto','Gonzáles ',24254252),(13,'56781245K','Manuel','Valdevieso',67687968),(14,'67891245O','Angel','Gutierrez',24254252),(15,'78901245','Antonio','Giménez',NULL);
/*!40000 ALTER TABLE `administrador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dispositivo_autobus`
--

DROP TABLE IF EXISTS `dispositivo_autobus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dispositivo_autobus` (
  `iddispositivo_autobus` int NOT NULL AUTO_INCREMENT,
  `identificador_autobus` varchar(45) NOT NULL,
  `fecha_creacion` bigint NOT NULL,
  `capacidad` int NOT NULL,
  `ocupacion` int NOT NULL,
  `idadministrador` int DEFAULT NULL,
  `idlinea` int DEFAULT NULL,
  PRIMARY KEY (`iddispositivo_autobus`),
  KEY `dispositivo_autobus_administrador_idx` (`idadministrador`),
  KEY `dispositivo_autobus_linea_idx` (`idlinea`),
  CONSTRAINT `dispositivo_autobus_administrador` FOREIGN KEY (`idadministrador`) REFERENCES `administrador` (`idadministrador`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `dispositivo_autobus_linea` FOREIGN KEY (`idlinea`) REFERENCES `linea` (`idlinea`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispositivo_autobus`
--

LOCK TABLES `dispositivo_autobus` WRITE;
/*!40000 ALTER TABLE `dispositivo_autobus` DISABLE KEYS */;
INSERT INTO `dispositivo_autobus` VALUES (1,'6621 IQS',42145425,80,20,11,1),(2,'4058 TIN',52454526,80,60,12,1),(3,'2344 OJP',52452452,80,10,12,2),(4,'0783 AAX',65465646,40,5,14,6),(5,'3899 MPD',66547657,40,10,11,7),(6,'0079 OPJ',67675688,80,21,15,9),(7,'4117 QET',68989080,80,78,13,8);
/*!40000 ALTER TABLE `dispositivo_autobus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dispositivo_parada`
--

DROP TABLE IF EXISTS `dispositivo_parada`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dispositivo_parada` (
  `iddispositivo_parada` int NOT NULL AUTO_INCREMENT,
  `fecha_creacion` bigint NOT NULL,
  `numero_parada` int NOT NULL,
  `longitud` float NOT NULL,
  `latitud` float NOT NULL,
  PRIMARY KEY (`iddispositivo_parada`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispositivo_parada`
--

LOCK TABLES `dispositivo_parada` WRITE;
/*!40000 ALTER TABLE `dispositivo_parada` DISABLE KEYS */;
INSERT INTO `dispositivo_parada` VALUES (1,54542526,158,-5.94687,37.4006),(2,54542527,299,-5.94687,37.4006),(3,54542528,452,-5.95687,37.4006),(4,54542528,460,-5.94687,37.4006),(5,54542510,471,-5.94687,37.4006),(6,54542511,728,-5.94687,37.4006),(7,54542512,1,-5.94687,37.4006);
/*!40000 ALTER TABLE `dispositivo_parada` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dispositivo_parada-dispositivo_autobus`
--

DROP TABLE IF EXISTS `dispositivo_parada-dispositivo_autobus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dispositivo_parada-dispositivo_autobus` (
  `iddispositivo_parada-dispositivo_autobus` int NOT NULL AUTO_INCREMENT,
  `fecha_creacion` bigint NOT NULL,
  `iddispositivo_parada` int DEFAULT NULL,
  `iddispositivo_autobus` int DEFAULT NULL,
  PRIMARY KEY (`iddispositivo_parada-dispositivo_autobus`),
  KEY `relacion_dispositivo_parada_dispositivo_autobus_idx` (`iddispositivo_autobus`),
  KEY `relacion_dispositivo__idx` (`iddispositivo_parada`),
  CONSTRAINT `relacion_dispositivo_` FOREIGN KEY (`iddispositivo_parada`) REFERENCES `dispositivo_parada` (`iddispositivo_parada`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `relacion_dispositivo_parada_dispositivo_autobus` FOREIGN KEY (`iddispositivo_autobus`) REFERENCES `dispositivo_autobus` (`iddispositivo_autobus`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispositivo_parada-dispositivo_autobus`
--

LOCK TABLES `dispositivo_parada-dispositivo_autobus` WRITE;
/*!40000 ALTER TABLE `dispositivo_parada-dispositivo_autobus` DISABLE KEYS */;
INSERT INTO `dispositivo_parada-dispositivo_autobus` VALUES (1,545426422,1,1),(2,545426425,2,1),(3,545426428,3,3),(4,545426429,4,4),(5,545426441,5,5),(6,545426442,6,6),(7,545426444,7,7),(8,545426445,1,2),(9,545426446,4,2),(10,545426447,NULL,NULL);
/*!40000 ALTER TABLE `dispositivo_parada-dispositivo_autobus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dispositivo_parada-linea`
--

DROP TABLE IF EXISTS `dispositivo_parada-linea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dispositivo_parada-linea` (
  `iddispositivo_parada-linea_autobus` int NOT NULL AUTO_INCREMENT,
  `fecha_creacion` bigint NOT NULL,
  `iddispositivo_parada` int DEFAULT NULL,
  `idlinea` int DEFAULT NULL,
  PRIMARY KEY (`iddispositivo_parada-linea_autobus`),
  KEY `relacion_dispositivo_parada_linea_idx` (`idlinea`),
  KEY `relacion_dispositivo_parada_linea_idx1` (`iddispositivo_parada`),
  CONSTRAINT `relacion_dispositivo_parada_linea` FOREIGN KEY (`idlinea`) REFERENCES `linea` (`idlinea`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `relacion_dispositivo_parada_linea_parada` FOREIGN KEY (`iddispositivo_parada`) REFERENCES `dispositivo_parada` (`iddispositivo_parada`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispositivo_parada-linea`
--

LOCK TABLES `dispositivo_parada-linea` WRITE;
/*!40000 ALTER TABLE `dispositivo_parada-linea` DISABLE KEYS */;
INSERT INTO `dispositivo_parada-linea` VALUES (1,254545256,1,1),(2,254545257,1,2),(3,254545257,2,4),(4,254545258,4,1),(5,254545259,3,3),(6,254545260,7,3),(7,254545259,6,1),(8,254545259,5,5),(9,254545259,5,6),(10,254545259,6,7);
/*!40000 ALTER TABLE `dispositivo_parada-linea` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `linea`
--

DROP TABLE IF EXISTS `linea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `linea` (
  `idlinea` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idlinea`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `linea`
--

LOCK TABLES `linea` WRITE;
/*!40000 ALTER TABLE `linea` DISABLE KEYS */;
INSERT INTO `linea` VALUES (1,'C1'),(2,'C2'),(3,'01'),(4,'02'),(5,'03'),(6,'10'),(7,'22'),(8,'LE'),(9,'LN');
/*!40000 ALTER TABLE `linea` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensor_gps`
--

DROP TABLE IF EXISTS `sensor_gps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sensor_gps` (
  `idsensor_gps` int NOT NULL AUTO_INCREMENT,
  `nombre_modelo` varchar(45) NOT NULL,
  `valor_latitud` float NOT NULL,
  `valor_longitud` float NOT NULL,
  `precision` float NOT NULL,
  `iddispositivo_autobus` int DEFAULT NULL,
  PRIMARY KEY (`idsensor_gps`),
  KEY `iddispositivo_idx` (`iddispositivo_autobus`),
  CONSTRAINT `sensor_gps_dispositivo_autobus` FOREIGN KEY (`iddispositivo_autobus`) REFERENCES `dispositivo_autobus` (`iddispositivo_autobus`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor_gps`
--

LOCK TABLES `sensor_gps` WRITE;
/*!40000 ALTER TABLE `sensor_gps` DISABLE KEYS */;
INSERT INTO `sensor_gps` VALUES (1,'GPS NEO-6',-5.97317,37.3828,0.7945,1),(2,'GPS NEO-6',-5.98783,37.3927,0.7945,2),(3,'GPS NEO-6',-5.98523,37.4013,0.7945,3),(4,'GPS NEO-6',-5.9777,37.3919,0.7945,4),(5,'GPS NEO-6',-5.96375,37.3987,0.7945,5),(6,'GPS NEO-6',-5.94687,37.4006,0.7945,6),(7,'GPS NEO-6',-5.97687,37.4046,0.7945,7);
/*!40000 ALTER TABLE `sensor_gps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensor_humedad`
--

DROP TABLE IF EXISTS `sensor_humedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sensor_humedad` (
  `idsensor_humedad` int NOT NULL AUTO_INCREMENT,
  `nombre_modelo` varchar(45) NOT NULL,
  `valor` float NOT NULL,
  `precision` float NOT NULL,
  `iddispositivo_autobus` int DEFAULT NULL,
  PRIMARY KEY (`idsensor_humedad`),
  KEY `idsensor_humedad_idx` (`iddispositivo_autobus`),
  CONSTRAINT `idsensor_humedad` FOREIGN KEY (`iddispositivo_autobus`) REFERENCES `dispositivo_autobus` (`iddispositivo_autobus`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor_humedad`
--

LOCK TABLES `sensor_humedad` WRITE;
/*!40000 ALTER TABLE `sensor_humedad` DISABLE KEYS */;
INSERT INTO `sensor_humedad` VALUES (1,'DHT11',19,2,1),(2,'DHT11',21,2,2),(3,'DHT11',20,2,3),(4,'DHT11',22,2,4),(5,'DHT11',19,2,5),(6,'DHT11',20,2,6),(7,'DHT11',18,2,7);
/*!40000 ALTER TABLE `sensor_humedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensor_proximidad`
--

DROP TABLE IF EXISTS `sensor_proximidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sensor_proximidad` (
  `idsensor_proximidad` int NOT NULL AUTO_INCREMENT,
  `nombre_modelo` varchar(45) NOT NULL,
  `valor` float NOT NULL,
  `precision` float NOT NULL,
  `iddispositivo_autobus` int DEFAULT NULL,
  PRIMARY KEY (`idsensor_proximidad`),
  KEY `sensor_proximidad_dispositivo_autobus_idx` (`iddispositivo_autobus`),
  CONSTRAINT `sensor_proximidad_dispositivo_autobus` FOREIGN KEY (`iddispositivo_autobus`) REFERENCES `dispositivo_autobus` (`iddispositivo_autobus`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor_proximidad`
--

LOCK TABLES `sensor_proximidad` WRITE;
/*!40000 ALTER TABLE `sensor_proximidad` DISABLE KEYS */;
INSERT INTO `sensor_proximidad` VALUES (1,'hc-sr04',0.45,0.12,1),(2,'hc-sr04',0.89,0.12,2),(3,'hc-sr04',1.04,0.12,3),(4,'hc-sr04',0.89,0.12,4),(5,'hc-sr04',0.2,0.12,5),(6,'hc-sr04',0.19,0.12,6),(7,'hc-sr04',1.21,0.12,7);
/*!40000 ALTER TABLE `sensor_proximidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensor_temperatura`
--

DROP TABLE IF EXISTS `sensor_temperatura`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sensor_temperatura` (
  `idsensor_temperatura` int NOT NULL AUTO_INCREMENT,
  `nombre_modelo` varchar(45) NOT NULL,
  `valor` float NOT NULL,
  `precision` float NOT NULL,
  `iddispositivo_autobus` int DEFAULT NULL,
  PRIMARY KEY (`idsensor_temperatura`),
  KEY `sensor_temperatura_dispositivo_autobus_idx` (`iddispositivo_autobus`),
  CONSTRAINT `sensor_temperatura_dispositivo_autobus` FOREIGN KEY (`iddispositivo_autobus`) REFERENCES `dispositivo_autobus` (`iddispositivo_autobus`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor_temperatura`
--

LOCK TABLES `sensor_temperatura` WRITE;
/*!40000 ALTER TABLE `sensor_temperatura` DISABLE KEYS */;
INSERT INTO `sensor_temperatura` VALUES (1,' LM35',25,0.5,1),(2,' LM35',21,0.5,2),(3,' LM35',29,0.5,3),(4,' LM35',25,0.5,4),(5,' LM35',29,0.5,5),(6,' LM35',29,0.5,6),(7,' LM35',24,0.5,7);
/*!40000 ALTER TABLE `sensor_temperatura` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-27 21:40:04
