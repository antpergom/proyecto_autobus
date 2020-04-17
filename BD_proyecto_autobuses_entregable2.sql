-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: proyecto_autobuses
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrador`
--

LOCK TABLES `administrador` WRITE;
/*!40000 ALTER TABLE `administrador` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispositivo_autobus`
--

LOCK TABLES `dispositivo_autobus` WRITE;
/*!40000 ALTER TABLE `dispositivo_autobus` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispositivo_parada`
--

LOCK TABLES `dispositivo_parada` WRITE;
/*!40000 ALTER TABLE `dispositivo_parada` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispositivo_parada-dispositivo_autobus`
--

LOCK TABLES `dispositivo_parada-dispositivo_autobus` WRITE;
/*!40000 ALTER TABLE `dispositivo_parada-dispositivo_autobus` DISABLE KEYS */;
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
  CONSTRAINT `relacion_dispositivo_parada_linea` FOREIGN KEY (`idlinea`) REFERENCES `linea` (`idlinea`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispositivo_parada-linea`
--

LOCK TABLES `dispositivo_parada-linea` WRITE;
/*!40000 ALTER TABLE `dispositivo_parada-linea` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `linea`
--

LOCK TABLES `linea` WRITE;
/*!40000 ALTER TABLE `linea` DISABLE KEYS */;
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
  `tipo_sensor` enum('GPS','TEMPERATURA','HUMEDAD','PROXIMIDAD') DEFAULT NULL,
  PRIMARY KEY (`idsensor_gps`),
  KEY `iddispositivo_idx` (`iddispositivo_autobus`),
  CONSTRAINT `sensor_gps_dispositivo_autobus` FOREIGN KEY (`iddispositivo_autobus`) REFERENCES `dispositivo_autobus` (`iddispositivo_autobus`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor_gps`
--

LOCK TABLES `sensor_gps` WRITE;
/*!40000 ALTER TABLE `sensor_gps` DISABLE KEYS */;
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
  `tipo_sensor` enum('GPS','TEMPERATURA','HUMEDAD','PROXIMIDAD') DEFAULT NULL,
  PRIMARY KEY (`idsensor_humedad`),
  KEY `idsensor_humedad_idx` (`iddispositivo_autobus`),
  CONSTRAINT `idsensor_humedad` FOREIGN KEY (`iddispositivo_autobus`) REFERENCES `dispositivo_autobus` (`iddispositivo_autobus`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor_humedad`
--

LOCK TABLES `sensor_humedad` WRITE;
/*!40000 ALTER TABLE `sensor_humedad` DISABLE KEYS */;
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
  `tipo_sensor` enum('GPS','TEMPERATURA','HUMEDAD','PROXIMIDAD') DEFAULT NULL,
  PRIMARY KEY (`idsensor_proximidad`),
  KEY `sensor_proximidad_dispositivo_autobus_idx` (`iddispositivo_autobus`),
  CONSTRAINT `sensor_proximidad_dispositivo_autobus` FOREIGN KEY (`iddispositivo_autobus`) REFERENCES `dispositivo_autobus` (`iddispositivo_autobus`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor_proximidad`
--

LOCK TABLES `sensor_proximidad` WRITE;
/*!40000 ALTER TABLE `sensor_proximidad` DISABLE KEYS */;
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
  `tipo_sensor` enum('GPS','TEMPERATURA','HUMEDAD','PROXIMIDAD') DEFAULT NULL,
  PRIMARY KEY (`idsensor_temperatura`),
  KEY `sensor_temperatura_dispositivo_autobus_idx` (`iddispositivo_autobus`),
  CONSTRAINT `sensor_temperatura_dispositivo_autobus` FOREIGN KEY (`iddispositivo_autobus`) REFERENCES `dispositivo_autobus` (`iddispositivo_autobus`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor_temperatura`
--

LOCK TABLES `sensor_temperatura` WRITE;
/*!40000 ALTER TABLE `sensor_temperatura` DISABLE KEYS */;
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

-- Dump completed on 2020-04-17  3:47:13
