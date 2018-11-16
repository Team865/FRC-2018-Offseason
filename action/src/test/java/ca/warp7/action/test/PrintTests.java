package ca.warp7.action.test;

import ca.warp7.action.IAction;
import ca.warp7.action.impl.ActionMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static junit.framework.TestCase.assertEquals;

public class PrintTests {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private void startMode(ActionMode mode) {
        IAction action = mode.getAction();
        double timeout = 0.5;
        IAction runner = ActionMode.createRunner(new DefaultTimer(), 0.02, timeout, action, false);
        runner.onStart();
        double old = System.nanoTime();
        try {
            while (!runner.shouldFinish() && System.nanoTime() - old < timeout * 1000000000) {
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                return queue(new Print("hello"));
            }
        });
        assertEquals("hello", outContent.toString().trim());
    }

    @Test
    public void printQueue2() {
        startMode(new ActionMode() {
            @Override
            public IAction getAction() {
                return queue(
                        new Print("hello "),
                        new Print("world")
                );
            }
        });
        assertEquals("hello world", outContent.toString().trim());
    }

    @Test
    public void printQueue3() {
        startMode(new ActionMode() {
            @Override
            public IAction getAction() {
                return queue(
                        queue(
                                new Print("hello "),
                                new Print("world ")
                        ),
                        new Print("Action")
                );
            }
        });
        assertEquals("hello world Action", outContent.toString().trim());
    }

    @Test
    public void printAsync() {
        startMode(new ActionMode() {
            @Override
            public IAction getAction() {
                return async(
                        new Print("hello "),
                        new Print("world ")
                );
            }
        });
        assertEquals("hello world", outContent.toString().trim());
    }
}
