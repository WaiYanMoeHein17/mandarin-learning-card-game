# Spaced Repetition System (SRS) for Mandarin Learning Card Game

## Overview
The Spaced Repetition System is a learning technique that helps improve long-term retention of vocabulary by spacing reviews at increasing intervals. This implementation follows a 5-level SRS model:

1. **Level 1**: Review after 1 day
2. **Level 2**: Review after 3 days
3. **Level 3**: Review after 7 days
4. **Level 4**: Review after 14 days
5. **Level 5**: Review after 30 days

## How to Use

1. **Studying Cards**:
   - Cards are initially shown with the term side visible
   - Click "Flip Card" or press spacebar to see the definition
   - When the definition is shown, the rating panel appears

2. **Rating Your Answer**:
   - **1 (Completely Forgot)**: You didn't remember the answer at all
   - **2 (Incorrect but Recognized)**: You recognized the term but couldn't recall the definition
   - **3 (Correct with Difficulty)**: You remembered the answer but with significant effort
   - **4 (Correct)**: You correctly remembered the answer with some hesitation
   - **5 (Perfect Recall)**: You immediately and confidently knew the answer

3. **What Happens After Rating**:
   - Ratings 1-2: Card is reset to SRS level 1 (review tomorrow)
   - Ratings 3-5: Card advances one SRS level (longer interval before next review)
   - The next card is automatically shown after rating

## Database Structure

The SRS data is stored in a new `srs_data` table in the database:

```sql
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
```

## Technical Implementation

1. **Cards.java**:
   - Added `srsLevel` and `nextReviewDate` fields
   - Added getter/setter methods for SRS data
   - Defined SRS intervals for each level

2. **FlashCards.java**:
   - Added SRS rating panel with 5 buttons
   - Implemented logic to update SRS levels based on ratings
   - Added database operations for saving/loading SRS data
   - Configured the SRS panel to show only when viewing definitions

## Future Enhancements

1. **Due Card Filter**: Option to show only cards due for review today
2. **SRS Statistics**: Visual representation of learning progress
3. **Customizable Intervals**: Allow users to adjust the spacing intervals
4. **Prioritization**: Sort cards by SRS level or due date
5. **Export/Import**: Allow SRS data to be exported/imported

## Running the Database Update

To add the SRS table to your database:

1. Open MySQL command line or a database management tool
2. Connect to your database
3. Run the SQL script in `srs_table.sql`