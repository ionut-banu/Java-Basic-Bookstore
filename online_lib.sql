-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jan 16, 2016 at 03:34 PM
-- Server version: 10.1.9-MariaDB
-- PHP Version: 5.6.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `online_lib`
--

-- --------------------------------------------------------

--
-- Table structure for table `autori`
--

CREATE TABLE `autori` (
  `id` int(11) NOT NULL,
  `nume` varchar(45) DEFAULT NULL,
  `prenume` varchar(45) DEFAULT NULL,
  `CNP` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `autori`
--

INSERT INTO `autori` (`id`, `nume`, `prenume`, `CNP`) VALUES
(1, 'Pana', 'Laura', '2112265123123'),
(2, 'Cernica', 'Viorel', '1112165123123'),
(3, 'Nita', 'Adrian', '1111065124123'),
(4, 'Stefanescu', 'Marin', '1110064682412'),
(5, 'Banciu', 'Angela', '2110072688912'),
(6, 'Oancea', 'Mircea', '1200072642512'),
(7, 'nume', 'prenume', '1223');

-- --------------------------------------------------------

--
-- Table structure for table `carte_has_autor`
--

CREATE TABLE `carte_has_autor` (
  `id` int(11) NOT NULL,
  `autor` int(11) NOT NULL,
  `carte` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `carte_has_autor`
--

INSERT INTO `carte_has_autor` (`id`, `autor`, `carte`) VALUES
(1, 1, 1),
(2, 4, 1),
(3, 5, 2),
(4, 3, 2),
(5, 2, 3),
(6, 3, 3),
(7, 1, 4),
(8, 5, 4);

-- --------------------------------------------------------

--
-- Table structure for table `carti`
--

CREATE TABLE `carti` (
  `id` int(11) NOT NULL,
  `isbn` varchar(45) DEFAULT NULL,
  `titlu` varchar(45) DEFAULT NULL,
  `stoc` int(11) DEFAULT NULL,
  `pret` double DEFAULT NULL,
  `editura` int(11) DEFAULT NULL,
  `furnizor` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `carti`
--

INSERT INTO `carti` (`id`, `isbn`, `titlu`, `stoc`, `pret`, `editura`, `furnizor`) VALUES
(1, '1231231234569', 'Roboti cognitivi', 24, 19, 1, 2),
(2, '1234567893456', 'Enciclopedie Matematica', 20, 12, 5, 1),
(3, '1234567898765', 'Materiale magnetice', 5, 14, 3, 3),
(4, '122', 'aaa', 4, 25.3, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `clienti`
--

CREATE TABLE `clienti` (
  `id` int(11) NOT NULL,
  `nume` varchar(45) DEFAULT NULL,
  `prenume` varchar(45) DEFAULT NULL,
  `CNP` varchar(45) DEFAULT NULL,
  `oras` varchar(45) DEFAULT NULL,
  `strada` varchar(45) DEFAULT NULL,
  `nr` int(11) DEFAULT NULL,
  `tel` varchar(15) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `clienti`
--

INSERT INTO `clienti` (`id`, `nume`, `prenume`, `CNP`, `oras`, `strada`, `nr`, `tel`, `email`) VALUES
(1, 'Popescu', 'ion', '1020684123654', 'Bucuresti', 'Aurel Vlaicu', 30, '0721234567', 'popescu.i@email.com');

-- --------------------------------------------------------

--
-- Table structure for table `comanda_has_carte`
--

CREATE TABLE `comanda_has_carte` (
  `id` int(11) NOT NULL,
  `comanda` int(11) NOT NULL,
  `carte` int(11) NOT NULL,
  `cantitate` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `comanda_has_carte`
--

INSERT INTO `comanda_has_carte` (`id`, `comanda`, `carte`, `cantitate`) VALUES
(1, 1, 1, 3),
(2, 1, 3, 5),
(3, 2, 2, 20),
(4, 2, 1, 25),
(5, 5, 1, 5),
(6, 5, 3, 1);

-- --------------------------------------------------------

--
-- Table structure for table `comenzi`
--

CREATE TABLE `comenzi` (
  `id` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  `total` double DEFAULT NULL,
  `client` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `comenzi`
--

INSERT INTO `comenzi` (`id`, `date`, `status`, `total`, `client`) VALUES
(1, '2016-01-09 11:09:56', 'EFECTUATA', 127, 1),
(2, '2016-01-09 23:06:02', 'IN DESFASURARE', 715, 1),
(3, '2016-01-11 10:07:49', 'IN DESFASURARE', 0, 1),
(4, '2016-01-14 18:24:27', 'IN DESFASURARE', 0, 1),
(5, '2016-01-14 20:21:33', 'EFECTUATA', 109, 1);

-- --------------------------------------------------------

--
-- Table structure for table `edituri`
--

CREATE TABLE `edituri` (
  `id` int(11) NOT NULL,
  `nume` varchar(45) DEFAULT NULL,
  `oras` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `edituri`
--

INSERT INTO `edituri` (`id`, `nume`, `oras`) VALUES
(1, 'POLITEHNICA PRESS', 'BUCURESTI'),
(2, 'PRINTECH', 'BUCURESTI'),
(3, 'SIGMA', 'BUCURESTI'),
(4, 'ACADEMIEI', 'BUCURESTI'),
(5, 'Agir', 'PLOIESTI');

-- --------------------------------------------------------

--
-- Table structure for table `furnizori`
--

CREATE TABLE `furnizori` (
  `id` int(11) NOT NULL,
  `nume` varchar(45) DEFAULT NULL,
  `tel` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `furnizori`
--

INSERT INTO `furnizori` (`id`, `nume`, `tel`) VALUES
(1, 'Depozitul de Carte', '0721111111'),
(2, 'Printre Carti', '0722222222'),
(3, 'ExtraCarti', '0723333333');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `autori`
--
ALTER TABLE `autori`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `carte_has_autor`
--
ALTER TABLE `carte_has_autor`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_carte_has_autor_autori1_idx` (`autor`),
  ADD KEY `fk_carte_has_autor_carti1_idx` (`carte`);

--
-- Indexes for table `carti`
--
ALTER TABLE `carti`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_carti_edituri1_idx` (`editura`),
  ADD KEY `fk_carti_furnizor1_idx` (`furnizor`);

--
-- Indexes for table `clienti`
--
ALTER TABLE `clienti`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `comanda_has_carte`
--
ALTER TABLE `comanda_has_carte`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_comanda_has_carti_comenzi_idx` (`comanda`),
  ADD KEY `fk_comanda_has_carti_carti1_idx` (`carte`);

--
-- Indexes for table `comenzi`
--
ALTER TABLE `comenzi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_comenzi_clienti1_idx` (`client`);

--
-- Indexes for table `edituri`
--
ALTER TABLE `edituri`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `furnizori`
--
ALTER TABLE `furnizori`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `autori`
--
ALTER TABLE `autori`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `carte_has_autor`
--
ALTER TABLE `carte_has_autor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `carti`
--
ALTER TABLE `carti`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `clienti`
--
ALTER TABLE `clienti`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `comanda_has_carte`
--
ALTER TABLE `comanda_has_carte`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `comenzi`
--
ALTER TABLE `comenzi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `edituri`
--
ALTER TABLE `edituri`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `furnizori`
--
ALTER TABLE `furnizori`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `carte_has_autor`
--
ALTER TABLE `carte_has_autor`
  ADD CONSTRAINT `fk_carte_has_autor_autori1` FOREIGN KEY (`autor`) REFERENCES `autori` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_carte_has_autor_carti1` FOREIGN KEY (`carte`) REFERENCES `carti` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `carti`
--
ALTER TABLE `carti`
  ADD CONSTRAINT `fk_carti_edituri1` FOREIGN KEY (`editura`) REFERENCES `edituri` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_carti_furnizor1` FOREIGN KEY (`furnizor`) REFERENCES `furnizori` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `comanda_has_carte`
--
ALTER TABLE `comanda_has_carte`
  ADD CONSTRAINT `fk_comanda_has_carti_carti1` FOREIGN KEY (`carte`) REFERENCES `carti` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_comanda_has_carti_comenzi` FOREIGN KEY (`comanda`) REFERENCES `comenzi` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `comenzi`
--
ALTER TABLE `comenzi`
  ADD CONSTRAINT `fk_comenzi_clienti1` FOREIGN KEY (`client`) REFERENCES `clienti` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
