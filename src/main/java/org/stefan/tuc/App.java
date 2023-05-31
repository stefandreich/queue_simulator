package org.stefan.tuc;

import org.stefan.tuc.logic.SimulationManager;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {

        Thread t;

        SimulationManager simulationManager = new SimulationManager(args[0], args[1]);

        t = new Thread(simulationManager);
        t.start();
    }
}
