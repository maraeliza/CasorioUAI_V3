CREATE DATABASE  IF NOT EXISTS `bancocasoriouai` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bancocasoriouai`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: bancocasoriouai
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `tb_cartorio`
--

DROP TABLE IF EXISTS `tb_cartorio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_cartorio` (
  `id` bigint NOT NULL,
  `nome` varchar(100) NOT NULL,
  `telefone` varchar(20) NOT NULL,
  `endereco` varchar(100) NOT NULL,
  `eventoVinculado` tinyint(1) NOT NULL,
  `dataCriacao` date NOT NULL,
  `dataModificacao` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_cartorio`
--

LOCK TABLES `tb_cartorio` WRITE;
/*!40000 ALTER TABLE `tb_cartorio` DISABLE KEYS */;
INSERT INTO `tb_cartorio` VALUES (1,'Cartório Central','(34) 1234-5678','Avenida Brasil, 100',0,'2024-11-18',NULL),(2,'novo nome','novo telefone','novo endereco',0,'2024-11-11','2024-11-18'),(3,'Cartório e Registro São José','(34) 5678-1234','Praça da República, 300',0,'2024-11-17',NULL);
/*!40000 ALTER TABLE `tb_cartorio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_cerimonial`
--

DROP TABLE IF EXISTS `tb_cerimonial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_cerimonial` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `idUsuario` bigint NOT NULL,
  `idPessoa` bigint NOT NULL,
  `nome` varchar(255) NOT NULL,
  `dataCriacao` date NOT NULL,
  `dataModificacao` date DEFAULT NULL,
  `eventoVinculado` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tb_cerimonial_tb_usuario` (`idUsuario`),
  KEY `fk_tb_cerimonial_tb_pessoa` (`idPessoa`),
  CONSTRAINT `fk_tb_cerimonial_tb_pessoa` FOREIGN KEY (`idPessoa`) REFERENCES `tb_pessoa` (`id`),
  CONSTRAINT `fk_tb_cerimonial_tb_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `tb_usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_cerimonial`
--

LOCK TABLES `tb_cerimonial` WRITE;
/*!40000 ALTER TABLE `tb_cerimonial` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_cerimonial` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_convidado_familia`
--

DROP TABLE IF EXISTS `tb_convidado_familia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_convidado_familia` (
  `id` bigint NOT NULL,
  `nome` varchar(100) NOT NULL,
  `acesso` varchar(50) NOT NULL,
  `dataCriacao` date NOT NULL,
  `dataModificacao` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_convidado_familia`
--

LOCK TABLES `tb_convidado_familia` WRITE;
/*!40000 ALTER TABLE `tb_convidado_familia` DISABLE KEYS */;
INSERT INTO `tb_convidado_familia` VALUES (1,'Lopes','JoséMaria15122024mxhx','2024-11-18',NULL),(2,'Silva','JoséMaria15122024ewqa','2024-11-18',NULL),(3,'Sales','JoséMaria15122024mlhe','2024-11-18',NULL),(4,'Genesio','JoséMaria15122024whub','2024-11-18',NULL),(5,'Limeira','TOP','2024-11-16','2024-11-18'),(6,'Alves','JoséMaria15122024gzju','2024-11-17','2024-11-18');
/*!40000 ALTER TABLE `tb_convidado_familia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_convidado_individual`
--

DROP TABLE IF EXISTS `tb_convidado_individual`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_convidado_individual` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `idPessoa` bigint NOT NULL,
  `idFamilia` bigint NOT NULL,
  `idUser` bigint NOT NULL,
  `parentesco` varchar(100) DEFAULT NULL,
  `confirmacao` tinyint(1) NOT NULL DEFAULT '0',
  `dataCriacao` date NOT NULL,
  `dataModificacao` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idPessoa` (`idPessoa`),
  KEY `idFamilia` (`idFamilia`),
  KEY `idUser` (`idUser`),
  CONSTRAINT `tb_convidado_individual_ibfk_1` FOREIGN KEY (`idPessoa`) REFERENCES `tb_pessoa` (`id`),
  CONSTRAINT `tb_convidado_individual_ibfk_2` FOREIGN KEY (`idFamilia`) REFERENCES `tb_convidado_familia` (`id`),
  CONSTRAINT `tb_convidado_individual_ibfk_3` FOREIGN KEY (`idUser`) REFERENCES `tb_usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_convidado_individual`
--

LOCK TABLES `tb_convidado_individual` WRITE;
/*!40000 ALTER TABLE `tb_convidado_individual` DISABLE KEYS */;
INSERT INTO `tb_convidado_individual` VALUES (1,5,1,5,'Prima',0,'2024-12-11','2024-12-11');
/*!40000 ALTER TABLE `tb_convidado_individual` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_despesa`
--

DROP TABLE IF EXISTS `tb_despesa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_despesa` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `idFornecedor` bigint DEFAULT NULL,
  `valorTotal` double NOT NULL,
  `dataPrimeiroVencimento` date DEFAULT NULL,
  `dataUltimoVencimento` date DEFAULT NULL,
  `dataAgendamento` date DEFAULT NULL,
  `dataQuitacao` date DEFAULT NULL,
  `pago` tinyint(1) DEFAULT '0',
  `agendado` tinyint(1) DEFAULT '0',
  `parcelado` tinyint(1) DEFAULT '0',
  `nParcelas` int DEFAULT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `dataCriacao` date NOT NULL,
  `dataModificacao` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_despesa`
--

LOCK TABLES `tb_despesa` WRITE;
/*!40000 ALTER TABLE `tb_despesa` DISABLE KEYS */;
INSERT INTO `tb_despesa` VALUES (1,1,2500,'2025-01-14','2025-05-14',NULL,'2024-12-11',1,0,1,5,'Janta, bolo, salgados e doces','COMIDA','2024-12-11','2024-12-11'),(2,2,200,NULL,NULL,NULL,'2024-12-11',1,0,0,1,'Buquê de rosas brancas com fita dourada','BUQUÊ','2024-12-11','2024-12-11'),(3,2,300,'2025-02-16','2025-03-16',NULL,'2024-12-11',1,0,1,2,'Flores, arranjos e enfeites','DECORAÇÃO','2024-12-11','2024-12-11'),(4,3,6000,'2026-03-03','2026-12-03','2025-01-06',NULL,0,1,1,10,'Cenário, iluminação, equipamento e mão de obra','ALBUM DE FOTOGRAFIA','2024-12-11','2024-12-11'),(5,5,60,NULL,NULL,NULL,'2024-12-11',1,0,0,1,'Impressão de convites e envio por correio','CONVITES','2024-12-11','2024-12-11');
/*!40000 ALTER TABLE `tb_despesa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_despesa_parcela`
--

DROP TABLE IF EXISTS `tb_despesa_parcela`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_despesa_parcela` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_parcela` int NOT NULL,
  `id_despesa` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_despesa_parcela`
--

LOCK TABLES `tb_despesa_parcela` WRITE;
/*!40000 ALTER TABLE `tb_despesa_parcela` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_despesa_parcela` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_despesa_parcelas`
--

DROP TABLE IF EXISTS `tb_despesa_parcelas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_despesa_parcelas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idDespesa` bigint DEFAULT NULL,
  `idParcela` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_despesa_parcelas`
--

LOCK TABLES `tb_despesa_parcelas` WRITE;
/*!40000 ALTER TABLE `tb_despesa_parcelas` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_despesa_parcelas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_evento`
--

DROP TABLE IF EXISTS `tb_evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_evento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `idIgreja` bigint DEFAULT NULL,
  `idCerimonial` bigint DEFAULT NULL,
  `idCartorio` bigint DEFAULT NULL,
  `idNoiva` bigint DEFAULT NULL,
  `idNoivo` bigint DEFAULT NULL,
  `data` date NOT NULL,
  `dataCriacao` date NOT NULL,
  `dataModificacao` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idIgreja` (`idIgreja`),
  KEY `idCerimonial` (`idCerimonial`),
  KEY `idCartorio` (`idCartorio`),
  KEY `idNoiva` (`idNoiva`),
  KEY `idNoivo` (`idNoivo`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_evento`
--

LOCK TABLES `tb_evento` WRITE;
/*!40000 ALTER TABLE `tb_evento` DISABLE KEYS */;
INSERT INTO `tb_evento` VALUES (1,'❤ Casorio na Igreja ⛪❤',1,1,NULL,4,3,'2024-12-15','2024-11-20',NULL),(2,'❤ Casorio no Civil ❤',NULL,1,1,4,3,'2024-12-10','2024-11-20',NULL),(3,'Apresentação do Casório UAI❤',NULL,NULL,NULL,4,3,'2024-11-20','2024-11-20',NULL);
/*!40000 ALTER TABLE `tb_evento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_fornecedor`
--

DROP TABLE IF EXISTS `tb_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_fornecedor` (
  `id` int NOT NULL,
  `nome` varchar(255) NOT NULL,
  `cnpj` varchar(18) NOT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `valorAPagar` decimal(10,2) DEFAULT '0.00',
  `valorTotal` decimal(10,2) DEFAULT '0.00',
  `valorPago` decimal(10,2) DEFAULT '0.00',
  `parcelas` int DEFAULT '0',
  `estado` int DEFAULT '0',
  `quitado` tinyint(1) DEFAULT '0',
  `dataCriacao` date NOT NULL,
  `dataModificacao` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cnpj` (`cnpj`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_fornecedor`
--

LOCK TABLES `tb_fornecedor` WRITE;
/*!40000 ALTER TABLE `tb_fornecedor` DISABLE KEYS */;
INSERT INTO `tb_fornecedor` VALUES (1,'Buffet Delicioso','12.345.678/0001-99','(34) 1234-5678',0.00,2500.00,2500.00,0,0,1,'2024-12-02',NULL),(2,'Flores e Cores Decoração','98.765.432/0001-11','(34) 9876-5432',0.00,500.00,500.00,0,0,1,'2024-12-02',NULL),(3,'Momentos Eternos Fotografia','11.223.344/0001-22','(34) 1122-3344',0.00,0.00,0.00,0,0,0,'2024-12-02',NULL),(4,'Som & Luz Banda','22.334.556/0001-33','(34) 2233-4455',0.00,0.00,0.00,0,0,0,'2024-12-02',NULL),(5,'Convites Perfeitos','33.445.667/0001-44','(34) 3344-5566',0.00,60.00,60.00,0,0,1,'2024-12-02',NULL);
/*!40000 ALTER TABLE `tb_fornecedor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_igreja`
--

DROP TABLE IF EXISTS `tb_igreja`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_igreja` (
  `id` bigint NOT NULL,
  `nome` varchar(100) NOT NULL,
  `endereco` varchar(100) NOT NULL,
  `eventoVinculado` tinyint(1) NOT NULL,
  `dataCriacao` date NOT NULL,
  `dataModificacao` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_igreja`
--

LOCK TABLES `tb_igreja` WRITE;
/*!40000 ALTER TABLE `tb_igreja` DISABLE KEYS */;
INSERT INTO `tb_igreja` VALUES (1,'nome trocado','Rua das Flores, 123',0,'2024-11-15','2024-11-18'),(2,'Capela São José','Avenida Central, 456',0,'2024-11-17',NULL),(3,'Igreja Nossa Senhora das Graças','Praça das Palmeiras, 789',0,'2024-11-17',NULL);
/*!40000 ALTER TABLE `tb_igreja` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_pagamento`
--

DROP TABLE IF EXISTS `tb_pagamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_pagamento` (
  `id` int NOT NULL AUTO_INCREMENT,
  `data` date NOT NULL,
  `idPessoa` int DEFAULT NULL,
  `idUser` int DEFAULT NULL,
  `idDespesa` int DEFAULT NULL,
  `idParcela` int DEFAULT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `idFornecedor` int DEFAULT NULL,
  `valor` decimal(15,2) DEFAULT NULL,
  `nParcela` int DEFAULT NULL,
  `dataCriacao` date DEFAULT NULL,
  `dataModificacao` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_pagamento`
--

LOCK TABLES `tb_pagamento` WRITE;
/*!40000 ALTER TABLE `tb_pagamento` DISABLE KEYS */;
INSERT INTO `tb_pagamento` VALUES (1,'2024-12-11',4,3,2,1,'Buquê de rosas brancas com fita dourada',NULL,2,200.00,1,'2024-12-11',NULL),(2,'2024-12-11',4,3,3,0,'Flores, arranjos e enfeites',NULL,2,150.00,0,'2024-12-11',NULL),(3,'2024-12-11',4,3,3,0,'Flores, arranjos e enfeites',NULL,2,150.00,0,'2024-12-11',NULL),(4,'2024-12-11',3,2,0,0,'Surpresa para amada',NULL,2,77.00,0,'2024-12-11',NULL),(5,'2024-12-11',3,2,5,1,'Impressão de convites e envio por correio',NULL,5,60.00,1,'2024-12-11',NULL),(6,'2024-12-11',3,2,1,0,'Janta, bolo, salgados e doces',NULL,1,500.00,0,'2024-12-11',NULL),(7,'2024-12-11',3,2,1,0,'Janta, bolo, salgados e doces',NULL,1,500.00,0,'2024-12-11',NULL),(8,'2024-12-11',3,2,1,0,'Janta, bolo, salgados e doces',NULL,1,500.00,0,'2024-12-11',NULL),(9,'2024-12-11',3,2,1,0,'Janta, bolo, salgados e doces',NULL,1,500.00,0,'2024-12-11',NULL),(10,'2024-12-11',3,2,1,0,'Janta, bolo, salgados e doces',NULL,1,500.00,0,'2024-12-11',NULL);
/*!40000 ALTER TABLE `tb_pagamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_parcela`
--

DROP TABLE IF EXISTS `tb_parcela`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_parcela` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idDespesa` bigint DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `valor` double NOT NULL,
  `dataVencimento` date DEFAULT NULL,
  `dataPagamento` date DEFAULT NULL,
  `dataAgendamento` date DEFAULT NULL,
  `pago` tinyint(1) DEFAULT '0',
  `agendado` tinyint(1) DEFAULT '0',
  `status` varchar(50) DEFAULT NULL,
  `n` int NOT NULL,
  `nTotal` int NOT NULL,
  `dataCriacao` date NOT NULL,
  `dataModificacao` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_parcela`
--

LOCK TABLES `tb_parcela` WRITE;
/*!40000 ALTER TABLE `tb_parcela` DISABLE KEYS */;
INSERT INTO `tb_parcela` VALUES (1,1,'COMIDA 1/5',500,'2025-01-14',NULL,NULL,0,0,'PENDENTE',1,5,'2024-12-11',NULL),(2,1,'COMIDA 2/5',500,'2025-02-14',NULL,NULL,0,0,'PENDENTE',2,5,'2024-12-11',NULL),(3,1,'COMIDA 3/5',500,'2025-03-14',NULL,NULL,0,0,'PENDENTE',3,5,'2024-12-11',NULL),(4,1,'COMIDA 4/5',500,'2025-04-14',NULL,NULL,0,0,'PENDENTE',4,5,'2024-12-11',NULL),(5,1,'COMIDA 5/5',500,'2025-05-14',NULL,NULL,0,0,'PENDENTE',5,5,'2024-12-11',NULL),(16,4,'ALBUM DE FOTOGRAFIA 9/10',600,'2026-11-03',NULL,'2025-01-06',0,1,'PENDENTE',9,10,'2024-12-11','2024-12-11'),(17,4,'ALBUM DE FOTOGRAFIA 10/10',600,'2026-12-03',NULL,'2025-01-06',0,1,'PENDENTE',10,10,'2024-12-11','2024-12-11'),(25,4,'ALBUM DE FOTOGRAFIA 1/10',600,'2026-03-03',NULL,NULL,0,0,'PENDENTE',1,10,'2024-12-11',NULL),(26,4,'ALBUM DE FOTOGRAFIA 2/10',600,'2026-04-03',NULL,NULL,0,0,'PENDENTE',2,10,'2024-12-11',NULL),(27,4,'ALBUM DE FOTOGRAFIA 3/10',600,'2026-05-03',NULL,NULL,0,0,'PENDENTE',3,10,'2024-12-11',NULL),(28,4,'ALBUM DE FOTOGRAFIA 4/10',600,'2026-06-03',NULL,NULL,0,0,'PENDENTE',4,10,'2024-12-11',NULL),(29,4,'ALBUM DE FOTOGRAFIA 5/10',600,'2026-07-03',NULL,NULL,0,0,'PENDENTE',5,10,'2024-12-11',NULL),(30,4,'ALBUM DE FOTOGRAFIA 6/10',600,'2026-08-03',NULL,NULL,0,0,'PENDENTE',6,10,'2024-12-11',NULL),(31,4,'ALBUM DE FOTOGRAFIA 7/10',600,'2026-09-03',NULL,NULL,0,0,'PENDENTE',7,10,'2024-12-11',NULL),(32,4,'ALBUM DE FOTOGRAFIA 8/10',600,'2026-10-03',NULL,NULL,0,0,'PENDENTE',8,10,'2024-12-11',NULL),(33,4,'ALBUM DE FOTOGRAFIA 9/10',600,'2026-11-03',NULL,NULL,0,0,'PENDENTE',9,10,'2024-12-11',NULL),(34,4,'ALBUM DE FOTOGRAFIA 10/10',600,'2026-12-03',NULL,NULL,0,0,'PENDENTE',10,10,'2024-12-11',NULL);
/*!40000 ALTER TABLE `tb_parcela` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_pessoa`
--

DROP TABLE IF EXISTS `tb_pessoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_pessoa` (
  `id` bigint NOT NULL,
  `nome` varchar(100) NOT NULL,
  `telefone` varchar(20) NOT NULL,
  `tipo` varchar(50) NOT NULL,
  `nascimento` date NOT NULL,
  `dataCriacao` date NOT NULL,
  `dataModificacao` date DEFAULT NULL,
  `idade` int NOT NULL,
  `userVinculado` tinyint(1) NOT NULL,
  `cerimonialVinculado` tinyint(1) NOT NULL,
  `convidadoVinculado` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_pessoa`
--

LOCK TABLES `tb_pessoa` WRITE;
/*!40000 ALTER TABLE `tb_pessoa` DISABLE KEYS */;
INSERT INTO `tb_pessoa` VALUES (1,'Pagamento agendado','00000000','sys','2000-01-01','2024-11-19',NULL,24,0,0,0),(2,'ADMINISTRADOR','7777 5555','ADMIN','2001-01-01','2024-11-19',NULL,23,0,0,0),(3,'José','3432 2556','NOIVO','2001-01-01','2024-11-19',NULL,23,0,0,0),(4,'Maria','3431 1335','NOIVA','2001-01-01','2024-11-19',NULL,23,0,0,0),(5,'Ana','3431 1335','convidado','2001-01-01','2024-11-19',NULL,23,1,0,1),(6,'Ricardo','3431 1335','cerimonial','1989-01-31','2024-11-19',NULL,35,0,0,0),(7,'Fábio','3431 1335','cerimonial','1989-05-15','2024-11-19',NULL,35,0,0,0),(8,'Marisvalda','3431 1335','convidado','1989-05-15','2024-11-19',NULL,35,0,0,0),(9,'Andreia','34 6548 4567','convidado','2009-01-03','2024-12-11',NULL,15,0,0,0),(10,'Andre','130912830','outros','2007-07-19','2024-11-18','2024-12-07',17,0,0,0);
/*!40000 ALTER TABLE `tb_pessoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_presente`
--

DROP TABLE IF EXISTS `tb_presente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_presente` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `tipo` varchar(255) NOT NULL,
  `link` varchar(500) DEFAULT NULL,
  `idPessoa` bigint DEFAULT NULL,
  `dataCompra` date DEFAULT NULL,
  `dataCriacao` date NOT NULL,
  `dataModificacao` date DEFAULT NULL,
  `escolhido` tinyint(1) NOT NULL DEFAULT '0',
  `comprado` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idPessoa` (`idPessoa`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_presente`
--

LOCK TABLES `tb_presente` WRITE;
/*!40000 ALTER TABLE `tb_presente` DISABLE KEYS */;
INSERT INTO `tb_presente` VALUES (1,'Fogão','Eletrodomésticos','https://www.casasbahia.com.br/fogao/b',2,NULL,'2024-11-16','2024-12-07',1,0),(2,'Cama','Móveis','https://www.casasbahia.com.br/cama/b',NULL,NULL,'2024-11-20',NULL,0,0),(3,'Sofá','Móveis','https://www.casasbahia.com.br/sofa/b',2,NULL,'2024-11-18','2024-12-07',1,1),(4,'Conjunto de panelas','Outros','shopee',0,NULL,'2024-12-04','2024-12-07',0,0),(5,'Máquina de lava','eletrodomestico','casasbahia',2,NULL,'2024-12-05','2024-12-07',1,0);
/*!40000 ALTER TABLE `tb_presente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_recado`
--

DROP TABLE IF EXISTS `tb_recado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_recado` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `idPessoa` bigint DEFAULT NULL,
  `comentario` text NOT NULL,
  `dataCriacao` date NOT NULL,
  `dataModificacao` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tb_recado_tb_pessoa` (`idPessoa`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_recado`
--

LOCK TABLES `tb_recado` WRITE;
/*!40000 ALTER TABLE `tb_recado` DISABLE KEYS */;
INSERT INTO `tb_recado` VALUES (1,2,'aaa','2024-12-11',NULL);
/*!40000 ALTER TABLE `tb_recado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_usuario`
--

DROP TABLE IF EXISTS `tb_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_usuario` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `idPessoa` bigint NOT NULL,
  `tipo` int NOT NULL,
  `login` varchar(255) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `dataCriacao` date NOT NULL,
  `dataModificacao` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tb_usuario_tb_pessoa` (`idPessoa`),
  CONSTRAINT `fk_tb_usuario_tb_pessoa` FOREIGN KEY (`idPessoa`) REFERENCES `tb_pessoa` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_usuario`
--

LOCK TABLES `tb_usuario` WRITE;
/*!40000 ALTER TABLE `tb_usuario` DISABLE KEYS */;
INSERT INTO `tb_usuario` VALUES (1,2,1,'admin','1234','2024-11-19',NULL),(2,3,1,'loginNoivo','senha','2024-11-19',NULL),(3,4,1,'loginNoiva','senha','2024-11-19',NULL),(4,6,1,'RICARDO','senhaCasorioUai','2024-11-19',NULL),(5,5,2,'ANA','JoséMaria15122024onhv','2024-11-19',NULL),(7,8,2,'MARISVALDA','JoséMaria15122024gzju','2024-12-11',NULL),(8,8,2,'MARISVALDA','JoséMaria15122024ewqa','2024-12-11',NULL);
/*!40000 ALTER TABLE `tb_usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-11  9:19:05
