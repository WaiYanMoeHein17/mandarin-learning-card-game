-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 07, 2022 at 08:37 AM
-- Server version: 10.4.21-MariaDB
-- PHP Version: 7.3.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `database`
--

-- --------------------------------------------------------

--
-- Table structure for table `folders`
--

CREATE TABLE `folders` (
  `FolderIndex` int(11) NOT NULL,
  `FolderName` text NOT NULL,
  `User` text NOT NULL,
  `SetNumbers` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `folders`
--

INSERT INTO `folders` (`FolderIndex`, `FolderName`, `User`, `SetNumbers`) VALUES
(49, 'Miscellaneous Sets', 'testUser', ',43,44,');

-- --------------------------------------------------------

--
-- Table structure for table `mail`
--

CREATE TABLE `mail` (
  `MailIndex` int(11) NOT NULL,
  `Recipient` text NOT NULL,
  `Sender` text NOT NULL,
  `Topic` text NOT NULL,
  `Message` text NOT NULL,
  `DateSent` text NOT NULL,
  `ViewTimes` int(11) NOT NULL,
  `Pinned` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `mail`
--

INSERT INTO `mail` (`MailIndex`, `Recipient`, `Sender`, `Topic`, `Message`, `DateSent`, `ViewTimes`, `Pinned`) VALUES
(40, 'OtherAdmin', 'Enter Username here', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nEnter Username here has created setNumber  [0] \n\n Should the set be verified?', '03-01-2022', 1, 0),
(41, 'OtherAdmin', 'Enter Username here', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nEnter Username here has created setNumber  [0] \n\n Should the set be verified?', '03-01-2022', 1, 0),
(42, 'BiologyAdmin', 'Enter Username here', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nEnter Username here has created setNumber  [0] \n\n Should the set be verified?', '03-01-2022', 1, 0),
(44, 'OtherAdmin', 'Batman', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nBatman has created setNumber  [0] \n\n Should the set be verified?', '03-01-2022', 1, 0),
(45, 'OtherAdmin', 'Jimmy', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nJimmy has created setNumber  [0] \n\n Should the set be verified?', '03-01-2022', 1, 0),
(46, 'OtherAdmin', 'BasketBall73', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nBasketBall73 has created setNumber  [0] \n\n Should the set be verified?', '03-01-2022', 1, 0),
(47, 'OtherAdmin', 'Green Lantern', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nGreen Lantern has created setNumber  [0] \n\n Should the set be verified?', '03-01-2022', 1, 0),
(48, 'BiologyAdmin', 'Green Lantern', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nGreen Lantern has created setNumber  [0] \n\n Should the set be verified?', '03-01-2022', 1, 0),
(49, 'OtherAdmin', 'Wonder Woman', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nWonder Woman has created setNumber  [0] \n\n Should the set be verified?', '03-01-2022', 1, 0),
(50, 'PhysicsAdmin', 'Aquaman', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nAquaman has created setNumber  [0] \n\n Should the set be verified?', '03-01-2022', 1, 0),
(51, 'SpanishAdmin', 'Aquaman', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nAquaman has created setNumber  [0] \n\n Should the set be verified?', '03-01-2022', 1, 0),
(52, 'CSAdmin', 'IBStudent123', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nIBStudent123 has created setNumber  [0] \n\n Should the set be verified?', '03-01-2022', 1, 0),
(53, 'CSAdmin', 'IBStudent123', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nIBStudent123 has created setNumber  [35] \n\n Should the set be verified?', '03-01-2022', 1, 0),
(54, 'DTAdmin', 'IBStudent123', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nIBStudent123 has created setNumber  [0] \n\n Should the set be verified?', '03-01-2022', 1, 0),
(59, 'DTAdmin', 'Enter Username here', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nEnter Username here has created setNumber  [0] \n\n Should the set be verified?', '03-01-2022', 1, 0),
(60, 'CSAdmin', 'Enter Username here', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nEnter Username here has created setNumber  [0] \n\n Should the set be verified?', '03-01-2022', 1, 0),
(61, 'EnglishAdmin', 'Enter Username here', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nEnter Username here has created setNumber  [0] \n\n Should the set be verified?', '07-01-2022', 1, 0),
(66, 'OtherAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [0] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(67, 'OtherAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [0] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(68, 'OtherAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [0] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(69, 'OtherAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [0] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(70, 'OtherAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [0] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(71, 'OtherAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [0] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(72, 'OtherAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [0] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(73, 'OtherAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [0] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(74, 'OtherAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [0] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(75, 'OtherAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [0] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(76, 'OtherAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [0] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(77, 'OtherAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [0] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(83, '', 'Enter Username here', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nEnter Username here has created setNumber  [0] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(84, 'ChineseAdmin', 'Enter Username here', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\nEnter Username here has created setNumber  [39] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(85, 'Ben123', 'testUser', 'Help/Error Found', 'test message 123\r\n\r\nI was wonder why XX is true for XX question in set no XX\r\n\r\nalso I spotted and error for set no YY\r\n\r\n092rj2o3rj jedo f', '08-01-2022', 2, 0),
(93, 'OtherAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [44] \n\n Should the set be verified?', '28-03-2022', 1, 0),
(94, 'OtherAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [44] \n\n Should the set be verified?', '07-01-2022', 1, 0),
(95, 'GermanAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [44] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(96, 'GermanAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [44] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(97, 'GermanAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [44] \n\n Should the set be verified?', '08-01-2022', 1, 0),
(98, 'DTAdmin', 'testUser', 'Set Verification Required: ActionRequired', 'ActionRequired: Verify Set\ntestUser has created setNumber  [44] \n\n Should the set be verified?', '08-01-2022', 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `setdata`
--

CREATE TABLE `setdata` (
  `Set number` int(11) NOT NULL,
  `Set Creator` text NOT NULL,
  `Verified` tinyint(1) NOT NULL,
  `accessType` text NOT NULL,
  `Password` text NOT NULL,
  `TableTitles` text NOT NULL,
  `SetData` longtext NOT NULL,
  `Set Name` text NOT NULL,
  `SetNotes` text NOT NULL,
  `SetTopic` text NOT NULL,
  `DateCreated` text NOT NULL,
  `DateUpdated` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `setdata`
--

INSERT INTO `setdata` (`Set number`, `Set Creator`, `Verified`, `accessType`, `Password`, `TableTitles`, `SetData`, `Set Name`, `SetNotes`, `SetTopic`, `DateCreated`, `DateUpdated`) VALUES
(2, 'IBStudent123', 1, 'Public', 'null', 'Name,fruit or veg,Cost, Rating,Rater,', '5apple,banana,carrot,pineapple,potato,/f,f,v,f,v,/2,3,4,5,6,/dislike,like,alirght,dislike,like,/Mr Alex,Ben,Chloe,Danny,Eva,/', 'Food Rating', 'Ratings of food and veggies by Class 9', 'Food and Nutrition', '15-10-2021', '22-13-2021'),
(5, 'IceAge3', 1, 'Protected', 'dinosaur', 'Name,Name Meaning,Diet,Period,', '4Spinosarus,Stegosarus,Maiasarus,Triceratops,Characharadontosauus,Troodon,Velociraptor,/Spined lizard,roofed lizard,Good mother lizard,three horned face,Shark Tooth Lizard,Wounding Tooth ,Fast Thief,/Carnivore,Herbivore,Herbivore,Herbivore,Carnivore,Omnivore,Carnivore,/Createous,Jurassic,Jurassic,Createous,Createous,Triassic,Jurassic,/', 'Dinosaurus Revision', 'Dinosaurus needed to be know for History test', 'History', '14-10-2021', '24-11-2021'),
(6, 'Batman', 0, 'Private', 'null', 'Villan,Name,', '2Joker,Riddler,Two Face,Ra-a-ghul,Solomon Grundy,,/ ,,,,,,/', 'Villans Of Batman', 'Real names of my villan', 'Other', '22-10-2021', '22-10-2021'),
(8, 'Green Lantern', 0, 'Private', 'null', 'term 1,term 2,', '2Jurassic Park,Jurassic Park: The Lost World,Jurassic Park 3,Jurassic World,Jurassic World: fallen Kingdom,/ ,,,,,/', 'Jurassic Park Movies', 'Name of the jurassic park movie', 'Other', '12-11-2021', '14-11-2021'),
(10, 'Jimmy', 0, 'Public', 'null', 'Country,Capital,', '2UK,France,Spain,Portugal,Switzerland,Estonia,/London,Paris,Madrid,Lisbon,Bern,Tallinn,/', 'European Capitals', 'Capitals of European countries', 'Geography', '15-11-2021', '15-11-2021'),
(12, 'Wonder Woman', 0, 'Protected', '123', 'term 1,term 2,', '2London,Manchester,Leeds,New Castle,Sunderland,Swansea,,/ ,,,,,,,/', 'UK Cities', 'Most Populous Cities in the UK', 'Other', '2-12-2021', '5-12-2021'),
(13, 'BasketBall73', 0, 'Private', 'null', 'term 1,term 2,', '2Suppy,Demand Definition,PED meaning,PED equation,Law of Suppy definition,5 factors that influence suppy,,/ ,,,,,,,/', 'Econ Terminology Topic 3', 'My Mistakes in the last Tes', 'Other', '5-12-2021', '5-12-2021'),
(14, 'Mathlover123', 1, 'Public', 'null', 'Identity Name,Term,Definition,Notes,', '4The Pythagorean Theorem ,Pythagorean Trig Identity,Cos Double Angle,Sin Double Angle,/a^2=,1=,cos(2a),sin(2y),/b^2+c^2,sin^2x+cos^2x,cos^2a-sin^2a,2sinycosy,/Only for right angle triangle,Derived from unit circle,,,/', 'Mathematic Identities', 'Basic identities for math paper ', 'Math', '5-12-2021', '5-12-2021'),
(28, 'Jimmy', 0, 'Private', 'null', 'term 1,term 2,', '21,2,3,4,5,6,7,8,9,/2,3,5,7,11,13,17,19,29,/', 'Prime Numbers to 100', 'Prime Numbers Qui', 'All', '03-01-2022', '03-01-2022'),
(31, 'Green Lantern', 0, 'Public', 'null', 'term 1,term 2,', '2Mitrocondria,Flagellem,Cell Wall, Nucleus,/ ,,,,/', 'Cell Parts', 'Topic 12 revisio', 'Biology', '03-01-2022', '03-01-2022'),
(33, 'Aquaman', 0, 'Private', 'null', 'Equation,value,', '2E=,R=,A=,T = (SHM of pendulumn),a = ,v^2 = ,,/ ,,,,,,,/', 'Physics equations', 'null', 'Physics', '03-01-2022', '03-01-2022'),
(34, 'Aquaman', 0, 'Protected', 'Uno Dos Tres', 'Spanish,English,', '2uno,dos,tres,quatro,quinco ,/ ,,,,,/', 'Counting in Spanish', 'null', 'Spanish', '03-01-2022', '03-01-2022'),
(35, 'IBStudent123', 0, 'Private', 'null', 'term 1,Defin,', '2Direct,Parallel,Phased,Pilot,/ ,,,,/', 'Types of System Changeover', 'Types of System Changeover', 'CS', '03-01-2022', '03-01-2022'),
(36, 'IBStudent123', 0, 'Password', 'null', 'Material,Type,', '2Iron,Acyrlic,LDPE,HDPE,ABS,Styrene,,/ ,,,,,,,/', 'DT materials', 'null', 'DT', '03-01-2022', '03-01-2022'),
(37, 'Ben123', 1, 'Public', 'null\n', 'Question,Answer,Topic,Past Paper Date,', '41+2,3^2,i^2,1/2+2/2,9*9,100/10,/3,9,-1,3/2,81,10 ,/1.1,2.0,7.2,3.0,1.4,1,/ 1998, 2001, 2021, 2010, 2011, 2003,/', 'Math Test Practise', 'Math Test Practise', 'Math', '03-01-2022', '03-01-2022'),
(38, 'Ben123', 0, 'Public', 'null', 'math qusion,asnwer,', '22+2=,123-341,1221-21312,123/123,24!,6534*12002,100/0,-1^2,/7,3,3,3,3,3,3,,/', 'MathTest', 'A simple set with math equations', 'Math', '03-01-2022', '03-01-2022'),
(39, 'Enter Username here', 0, 'Public', 'null', 'Chinese Character,pinyin,English Translation,Type of Word,', '4跑步,小狗,数不胜数 ,天气,家庭,吃,打网球,/Pǎobù,Xiǎo gǒu,Shǔbùshèngshǔ,Tiānqì,Jiātíng,Chī,Dǎ wǎngqiú,/To run,Puppy,Countless,Weather,Family,To eat,To play tennis,/Verb,Noun,Idiom,Noun,Noun,Verb,Verb,/', 'Chinese Vocab', 'Chinese Vocab', 'Chinese', '03-01-2022', '07-01-2022'),
(43, 'testUser', 1, 'Public', 'null', 'Capital Letters,Lowercase Letters,Numerical Numbers,', '3A,B,C,D,E,F,G,/a,b,c,d,e,f,g,/1,2,3,4,5,6, ,/', 'Test Set', 'Test Set', 'Math', '07-01-2022', '07-01-2022'),
(44, 'testUser', 0, 'Public+++++', '', 'TestData,TestData Second Column,', '21,2,3,4,5,6,7,8,9,/ A,B,C,D,E,F,G,H,I,/', 'Test Set 2', 'Test Set 2', 'DT', '08-01-2022', '08-01-2022'),
(45, 'testUser', 0, 'Public', 'null', 'term 1,term 2,', '2 ,/ ,/', 'Test Set 3', 'This is a temporary set for testing purposes', 'Other', '09-01-2022', '11-01-2022'),
(46, 'testUser', 0, 'Public', 'null', 'term 1,term 2,', '2 ,/ ,/', 'Test Set 4', 'This is a temporary set for testing purposes', 'Other', '10-01-2022', '08-02-2022'),
(47, 'testUser', 0, 'Public', 'null', 'term 1,term 2,', '2 ,/ ,/', 'Test Set 5', 'This is a temporary set for testing purposes', 'Other', '11-01-2022', '11-01-2022'),
(48, 'testUser', 0, 'Public', 'null', 'term 1,term 2,', '2 ,/ ,/', 'Test Set 6', 'This is a temporary set for testing purposes', 'Other', '12-01-2022', '12-01-2023'),
(49, 'testUser', 0, 'Public', 'null', 'term 1,term 2,', '2 ,/ ,/', 'Test Set 7', 'This is a temporary set for testing purposes', 'Other', '13-01-2022', '12-03-2022'),
(50, 'testUser', 0, 'Public', 'null', 'term 1,term 2,', '2 ,/ ,/', 'Test Set 8', 'This is a temporary set for testing purposes', 'Other', '14-01-2022', '02-06-2022'),
(51, 'testUser', 0, 'Public', 'null', 'term 1,term 2,', '2 ,/ ,/', 'Test Set 9', 'This is a temporary set for testing purposes', 'Other', '15-01-2022', '08-03-2022'),
(52, 'testUser', 0, 'Public', 'null', 'term 1,term 2,', '2 ,/ ,/', 'Test Set 10', 'This is a temporary set for testing purposes', 'Other', '16-01-2022', '11-02-2022'),
(53, 'testUser', 0, 'Public', 'null', 'term 1,term 2,', '2 ,/ ,/', 'Test Set 11', 'nullThis is a temporary set for testing purposes', 'Other', '17-01-2022', '17-01-2022'),
(54, 'testUser', 0, 'Public', 'null', 'term 1,term 2,', '2 ,/ ,/', 'Test Set 12', 'This is a temporary set for testing purposes', 'Other', '18-01-2022', '12-09-2022'),
(55, 'testUser', 0, 'Public', 'null', 'term 1,term 2,', '2 ,/ ,/', 'Test Set 13', 'This is a temporary set for testing purposes', 'Other', '19-01-2022', '12-12-2022'),
(56, 'testUser', 0, 'Public', 'null', 'term 1,term 2,', '2 ,/ ,/', 'Test Set 14\r\n\r\n', 'This is a temporary set for testing purposes', 'Other', '20-01-2022', '14-01-2024'),
(57, 'Enter Username here', 0, 'null', 'null', 'term 1,term 2,', '2 ,/ ,/', '', 'null', 'All', '08-01-2022', '08-01-2022'),
(58, 'Enter Username here', 0, 'null', 'null', 'term 1,term 2,', '2 ,/ ,/', '', 'null', 'Chinese', '08-01-2022', '08-01-2022');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `Username` text NOT NULL,
  `Forename` text NOT NULL,
  `Surname` text NOT NULL,
  `Password` text NOT NULL,
  `ADMIN` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`Username`, `Forename`, `Surname`, `Password`, `ADMIN`) VALUES
('Admin', 'ADMIN', 'ADMIN', '123', 1),
('Aquaman', 'Arthur', 'Curry', '1941', 0),
('BasketBall73', 'Darren', 'Daniels', 'shaq999', 0),
('Batman', 'Bruce', 'Wayne', '1939', 0),
('Ben123', 'Ben', '123', 'oldPassword', 0),
('ChineseAdmin', 'ADMIN', 'ADMIN', 'CA', 1),
('Cyborg', 'Victor', 'Stone', '1980', 0),
('Enter Username here', 'User', 'fast Access', 'enterpassword12', 0),
('Food and NutritionAdmin', 'ADMIN', 'ADMIN', 'fandna', 1),
('Green Lantern', 'Hal', 'Jordan', '1940', 0),
('IBStudent123', 'Catherine', 'Cloe', 'cloe123', 0),
('IceAge3', 'Ella', 'Eva', 'scrat123', 0),
('Jimmy', 'Jim', 'my', '123', 0),
('MathAdmin', 'ADMIN', 'ADMIN', 'ma', 1),
('Mathlover123', 'Mr', 'Alex', 'maths', 0),
('Superman', 'Clark', 'Kent', '1938', 0),
('testUser', 'alpha', 'beta', '123', 0),
('The Flash', 'Barry', 'Allen', '1940', 0),
('Wonder Woman', 'Diana', 'Prince', '1941 ', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `folders`
--
ALTER TABLE `folders`
  ADD PRIMARY KEY (`FolderIndex`);

--
-- Indexes for table `mail`
--
ALTER TABLE `mail`
  ADD PRIMARY KEY (`MailIndex`);

--
-- Indexes for table `setdata`
--
ALTER TABLE `setdata`
  ADD PRIMARY KEY (`Set number`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`Username`(15));

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `folders`
--
ALTER TABLE `folders`
  MODIFY `FolderIndex` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT for table `mail`
--
ALTER TABLE `mail`
  MODIFY `MailIndex` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=100;

--
-- AUTO_INCREMENT for table `setdata`
--
ALTER TABLE `setdata`
  MODIFY `Set number` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
