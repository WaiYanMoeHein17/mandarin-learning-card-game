-- Add the SRS (Spaced Repetition System) data table to the mandarin database
-- This table tracks user's learning progress for each flashcard

CREATE TABLE IF NOT EXISTS `srs_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `card_id` int(11) NOT NULL,
  `srs_level` int(11) NOT NULL DEFAULT 0,
  `last_rating` int(11) NOT NULL DEFAULT 0,
  `next_review_date` date DEFAULT NULL,
  `last_reviewed_date` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_card_id` (`username`, `card_id`),
  INDEX `next_review_idx` (`username`, `next_review_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert a few sample rows
-- INSERT INTO `srs_data` (`username`, `card_id`, `srs_level`, `last_rating`, `next_review_date`) 
-- VALUES ('demo', 1, 2, 4, DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY));