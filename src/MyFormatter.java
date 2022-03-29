import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class MyFormatter extends Formatter {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss");
    LocalDateTime localDateTime = LocalDateTime.now();

    @Override
    public String format(LogRecord record) {
        return localDateTime.format(dateTimeFormatter) + " " + record.getMessage() + "\n";
    }
}