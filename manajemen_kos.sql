-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 31, 2026 at 02:27 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `manajemen_kos`
--

-- --------------------------------------------------------

--
-- Table structure for table `kamar`
--

CREATE TABLE `kamar` (
  `id_kamar` int(11) NOT NULL,
  `nomor_kamar` varchar(10) NOT NULL,
  `tipe` varchar(20) NOT NULL,
  `harga_per_bulan` decimal(12,2) NOT NULL,
  `status` varchar(15) NOT NULL DEFAULT 'Tersedia',
  `fasilitas` varchar(200) DEFAULT NULL,
  `keterangan` varchar(200) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kamar`
--

INSERT INTO `kamar` (`id_kamar`, `nomor_kamar`, `tipe`, `harga_per_bulan`, `status`, `fasilitas`, `keterangan`, `created_at`) VALUES
(1, 'K-101', 'Standard', 800000.00, 'Tersedia', 'Kipas angin, Lemari, Kasur', 'Lantai 1', '2026-05-17 11:59:06'),
(2, 'K-102', 'Standard', 800000.00, 'Terisi', 'Kipas angin, Lemari, Kasur', 'Lantai 1', '2026-05-17 11:59:06'),
(3, 'K-201', 'Deluxe', 1200000.00, 'Tersedia', 'AC, Lemari, Kasur, Meja belajar', 'Lantai 2', '2026-05-17 11:59:06'),
(4, 'K-202', 'Deluxe', 1200000.00, 'Tersedia', 'AC, Lemari, Kasur, Meja belajar', 'Lantai 2', '2026-05-17 11:59:06'),
(5, 'K-301', 'VIP', 1800000.00, 'Tersedia', 'AC, Kamar mandi dalam, TV, Kulkas mini', 'Lantai 3 - Premium', '2026-05-17 11:59:06');

-- --------------------------------------------------------

--
-- Table structure for table `penyewa`
--

CREATE TABLE `penyewa` (
  `id_penyewa` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `nik` varchar(20) NOT NULL,
  `no_hp` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `alamat_asal` varchar(200) DEFAULT NULL,
  `pekerjaan` varchar(100) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `id_kamar` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `penyewa`
--

INSERT INTO `penyewa` (`id_penyewa`, `nama`, `nik`, `no_hp`, `email`, `alamat_asal`, `pekerjaan`, `created_at`, `id_kamar`) VALUES
(8, 'Syifa', '3278031231234567', '911', 'syifaputra@gmail.com', 'Tasik', 'Mahasiswa', '2026-05-31 12:27:16', 2);

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id_transaksi` int(11) NOT NULL,
  `id_kamar` int(11) NOT NULL,
  `id_penyewa` int(11) NOT NULL,
  `tanggal_masuk` date NOT NULL,
  `tanggal_keluar` date NOT NULL,
  `durasi_bulan` int(11) NOT NULL,
  `total_bayar` decimal(12,2) NOT NULL,
  `status_pembayaran` varchar(15) NOT NULL DEFAULT 'Belum Lunas',
  `keterangan` varchar(200) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`id_transaksi`, `id_kamar`, `id_penyewa`, `tanggal_masuk`, `tanggal_keluar`, `durasi_bulan`, `total_bayar`, `status_pembayaran`, `keterangan`, `created_at`) VALUES
(4, 2, 8, '2026-05-31', '2026-07-31', 2, 1600000.00, 'Belum Lunas', 'Transaksi otomatis saat pendaftaran', '2026-05-31 12:27:16');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `kamar`
--
ALTER TABLE `kamar`
  ADD PRIMARY KEY (`id_kamar`),
  ADD UNIQUE KEY `nomor_kamar` (`nomor_kamar`);

--
-- Indexes for table `penyewa`
--
ALTER TABLE `penyewa`
  ADD PRIMARY KEY (`id_penyewa`),
  ADD UNIQUE KEY `nik` (`nik`),
  ADD KEY `penyewa_ibfk_1` (`id_kamar`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `id_kamar` (`id_kamar`),
  ADD KEY `id_penyewa` (`id_penyewa`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `kamar`
--
ALTER TABLE `kamar`
  MODIFY `id_kamar` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `penyewa`
--
ALTER TABLE `penyewa`
  MODIFY `id_penyewa` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id_transaksi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `penyewa`
--
ALTER TABLE `penyewa`
  ADD CONSTRAINT `penyewa_ibfk_1` FOREIGN KEY (`id_kamar`) REFERENCES `kamar` (`id_kamar`) ON DELETE SET NULL;

--
-- Constraints for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD CONSTRAINT `transaksi_ibfk_1` FOREIGN KEY (`id_kamar`) REFERENCES `kamar` (`id_kamar`) ON DELETE CASCADE,
  ADD CONSTRAINT `transaksi_ibfk_2` FOREIGN KEY (`id_penyewa`) REFERENCES `penyewa` (`id_penyewa`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
