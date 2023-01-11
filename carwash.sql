-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.21 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for carwash
CREATE DATABASE IF NOT EXISTS `carwash` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `carwash`;

-- Dumping structure for table carwash.additional
CREATE TABLE IF NOT EXISTS `additional` (
  `id_additional` int NOT NULL AUTO_INCREMENT,
  `namaAdd` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `harga_additional` double NOT NULL,
  PRIMARY KEY (`id_additional`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table carwash.additional: ~2 rows (approximately)
INSERT INTO `additional` (`id_additional`, `namaAdd`, `harga_additional`) VALUES
	(0, 'No Additional', 0),
	(1, 'Vacuum', 15000);

-- Dumping structure for table carwash.admin
CREATE TABLE IF NOT EXISTS `admin` (
  `id_admin` int NOT NULL AUTO_INCREMENT,
  `nama_admin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id_admin`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table carwash.admin: ~1 rows (approximately)
INSERT INTO `admin` (`id_admin`, `nama_admin`, `username`, `password`) VALUES
	(1, 'dicky', 'admin', 'admin123');

-- Dumping structure for table carwash.member
CREATE TABLE IF NOT EXISTS `member` (
  `id_member` int NOT NULL AUTO_INCREMENT,
  `nama_member` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `alamat` varchar(50) DEFAULT NULL,
  `noHp` varchar(50) DEFAULT NULL,
  `tanggal_pendaftaran` date DEFAULT NULL,
  `masa_aktif` date DEFAULT NULL,
  PRIMARY KEY (`id_member`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table carwash.member: ~3 rows (approximately)
INSERT INTO `member` (`id_member`, `nama_member`, `alamat`, `noHp`, `tanggal_pendaftaran`, `masa_aktif`) VALUES
	(0, 'NonMember', '', '', NULL, NULL),
	(3, 'Andi', 'Cikutra', '08999085719', '2023-01-08', '2028-01-08'),
	(4, 'Budi', 'Jakarta', '08981231021', '2023-01-04', '2023-01-20');

-- Dumping structure for table carwash.pegawai
CREATE TABLE IF NOT EXISTS `pegawai` (
  `id_pegawai` int NOT NULL AUTO_INCREMENT,
  `nama_pegawai` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `alamat` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `noHp` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `gaji` double NOT NULL,
  PRIMARY KEY (`id_pegawai`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table carwash.pegawai: ~2 rows (approximately)
INSERT INTO `pegawai` (`id_pegawai`, `nama_pegawai`, `alamat`, `noHp`, `status`, `gaji`) VALUES
	(1, 'agus', 'cikutra', '0898231312', 'aktif', 2500000),
	(2, 'budi', 'antapani', '089990857192', 'aktif', 3000000);

-- Dumping structure for table carwash.pencucian
CREATE TABLE IF NOT EXISTS `pencucian` (
  `id_pencucian` int NOT NULL AUTO_INCREMENT,
  `jenis` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `harga_pencucian` double NOT NULL,
  PRIMARY KEY (`id_pencucian`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table carwash.pencucian: ~4 rows (approximately)
INSERT INTO `pencucian` (`id_pencucian`, `jenis`, `harga_pencucian`) VALUES
	(1, 'Steam', 30000),
	(2, 'Steam Salju', 40000),
	(3, 'Steam Hidrolik', 40000),
	(4, 'Steam Hidrolik Salju', 60000);

-- Dumping structure for table carwash.transaksi
CREATE TABLE IF NOT EXISTS `transaksi` (
  `id_transaksi` int NOT NULL AUTO_INCREMENT,
  `id_pegawai` int NOT NULL,
  `id_member` int NOT NULL,
  `id_pencucian` int NOT NULL,
  `id_additional` int NOT NULL,
  `tanggal_transaksi` date NOT NULL,
  `jenis_mobil` varchar(50) NOT NULL,
  `plat_nomor` varchar(50) NOT NULL,
  `total_harga` double NOT NULL,
  PRIMARY KEY (`id_transaksi`),
  KEY `FK_transaksi_additional` (`id_additional`),
  KEY `FK_transaksi_member` (`id_member`),
  KEY `FK_transaksi_pencucian` (`id_pencucian`),
  KEY `FK__pegawai` (`id_pegawai`),
  CONSTRAINT `FK__pegawai` FOREIGN KEY (`id_pegawai`) REFERENCES `pegawai` (`id_pegawai`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_transaksi_additional` FOREIGN KEY (`id_additional`) REFERENCES `additional` (`id_additional`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_transaksi_member` FOREIGN KEY (`id_member`) REFERENCES `member` (`id_member`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_transaksi_pencucian` FOREIGN KEY (`id_pencucian`) REFERENCES `pencucian` (`id_pencucian`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table carwash.transaksi: ~1 rows (approximately)
INSERT INTO `transaksi` (`id_transaksi`, `id_pegawai`, `id_member`, `id_pencucian`, `id_additional`, `tanggal_transaksi`, `jenis_mobil`, `plat_nomor`, `total_harga`) VALUES
	(1, 1, 0, 1, 0, '2023-01-08', 'APV', 'B1231D', 30000);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
