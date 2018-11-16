package ca.warp7.action.test;

import ca.warp7.action.IAction;
import ca.warp7.action.impl.ActionMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static junit.framework.TestCase.assertEquals;

public class SimpleTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @SuppressWarnings("StatementWithEmptyBody")
    private void startMode(ActionMode mode) {
        IAction action = mode.getAction();
        IAction runner = ActionMode.createRunner(new DefaultTimer(), 0.02, 5, action, false);
        runner.onStart();
        while (!runner.shouldFinish()) ;
    }

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void printOnly() {
        // Test for single actions
        startMode(new ActionMode() {
            @Override
            public IAction getAction() {
                return new Print("hello");
            }
        });
        assertEquals("hello", outContent.toString().trim());
    }

    @Test
    public void printQueue() {

        startMode(new ActionMode() {
            @Override
            public IAction getAction() {

                System.out.println("hello");
                return queue(new Print("hello"));
            }
        });
        assertEquals("hello", outContent.toString().trim());
    }

    public static void main(String[] args) {
        new SimpleTest().printQueue();
    }
}
