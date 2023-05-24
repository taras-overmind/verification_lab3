import org.example.MyParser;
import org.example.MyScanner;
import org.mockito.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class MyParserTest {
    @InjectMocks
    MyParser myParser;

    @Mock
    MyScanner myScanner;

    @Spy
    MyParser myParserSpy = new MyParser();

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcess() throws IOException {
        String text = "Hello world";
        Path path = Path.of("test.txt");
        when(myScanner.read(path)).thenReturn(text);
        String result = myParser.process(path);
        Assert.assertEquals(result, "Hello world \n");
    }

    @Test(expectedExceptions  = IOException.class)
    public void testProcessWithIOException() throws IOException {
        Path path = Path.of("test.txt");
        when(myScanner.read(path)).thenThrow(IOException.class);
        myParser.processThrows(path);
    }

    @Test
    public void testProcessWithIOExceptionProcess() throws IOException {
        Path path = Path.of("test.txt");
        when(myScanner.read(path)).thenThrow(IOException.class);
        String result = myParser.process(path);
        Assert.assertEquals(result, "Oops");
    }

    @Test
    public void testSpyProcess() throws IOException {
        myParserSpy.myScanner = myScanner;
        Path path = Path.of("test.txt");
        String testString = "test text tent";
        when(myScanner.read(path)).thenReturn(testString);
        myParserSpy.process(path);
        verify(myParserSpy, times(1)).splitWords(testString);
        verify(myParserSpy, times(6)).getDistance(any(), any());
    }

    @Test
    public void testSplitWords() {
        String text = "Hello world";
        String[] expected = {"Hello", "world"};
        String[] result = myParser.splitWords(text);
        Assert.assertEquals(result, expected);
    }
}
