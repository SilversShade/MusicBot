package sigamebot.bot.userinteraction.filehandlers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.io.IOException;

public class NewUserPackHandlerTest {

    @Test
    public void documentHasWrongExtensionReturnsTrueIfExtensionIsWrong() {
        var document = new Document();
        document.setFileName("name.txt");
        Assertions.assertTrue(NewUserPackHandler.documentHasWrongExtension(document, "json"));
    }

    @Test
    public void getNumberOfFilesInDirectoryThrowsExceptionIfDirectoryDoesNotExist() {
        Assertions.assertThrows(IOException.class,
                () -> NewUserPackHandler.getNumberOfFilesInDirectory("src/main/resouccer/"));
    }
}
