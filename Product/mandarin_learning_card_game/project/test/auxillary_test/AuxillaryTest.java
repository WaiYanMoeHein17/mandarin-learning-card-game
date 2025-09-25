package auxillary_test;

import static org.junit.Assert.*;

import auxillary_functions.MailSelector;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.Test;

/**
 * Lightweight tests for auxiliary data holders that do not require a database connection.
 */
public class AuxillaryTest {

    private final LocalDate date = LocalDate.now();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Test
    public void testMailSelectorStoresFields() {
        int mailIndex = 42;
        String recipient = "testUser";
        String sender = "sender1";
        String topic = "Test Topic";
        String message = "Test Message";
        String dateSent = date.format(formatter);
        int viewTimes = 3;
        boolean pinned = true;

        MailSelector selector = new MailSelector(mailIndex, recipient, sender, topic, message, dateSent, viewTimes, pinned);

        assertEquals("Mail index should match", mailIndex, selector.getMailIndex());
        assertEquals("Recipient should match", recipient, selector.getRecipient());
        assertEquals("Sender should match", sender, selector.getSender());
        assertEquals("Topic should match", topic, selector.getTopic());
        assertEquals("Message should match", message, selector.getMessage());
        assertEquals("Date should match", dateSent, selector.getDateSent());
        assertEquals("View count should match", viewTimes, selector.getViewTimes());
        assertTrue("Pinned flag should match", selector.isPinned());
    }

    @Test
    public void testMailSelectorViewTimesCanUpdate() {
        MailSelector selector = new MailSelector(1, "recipient", "sender", "Topic", "Message", formatter.format(date), 0, false);

        selector.setViewTimes(5);
        assertEquals("View count should update", 5, selector.getViewTimes());

        selector.setViewTimes(0);
        assertEquals("View count should support reset", 0, selector.getViewTimes());
    }
}
