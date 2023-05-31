package org.stefan.tuc.logic;

import org.stefan.tuc.model.Server;
import org.stefan.tuc.model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class SimulationManager implements Runnable {

    private Scheduler scheduler;
    public static ArrayList<Task> tasks;
    public static InputHandler input;

    public SimulationManager(String firstPath, String secondPath) {
        try {
            input = new InputHandler(firstPath, secondPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        scheduler = new Scheduler(input.getQueues(), input.getTasks());

        generateNRandomTasks();
    }

    private static void generateNRandomTasks() {
        Random random = new Random();
        tasks = new ArrayList<>();

        for (int i = 0; i < input.getTasks(); i++) {
            tasks.add(new Task(i + 1,
                    random.nextInt(input.getArrivalTimeMax()) + input.getArrivalTimeMin(),
                    random.nextInt(input.getServiceTimeMin()) + input.getServiceTimeMax()));
        }
    }

    private void displayGeneratedTasks() throws IOException {
        input.getFileWriter().write("Waiting clients ready to enter in queue:\n");

        for (Task task : tasks) {
            input.getFileWriter().write(" " + task.toString());
        }
    }

    private int getNrOfTasks(ArrayList<Server> servers) {
        int nrOfTasks = 0;
        for (Server server : servers) {
            nrOfTasks += server.getTasks().size();
        }

        return nrOfTasks;
    }

    private void onClose(int sum, int sum2, int peakHour) {
        double averageTime = (double) sum / input.getTasks();
        double averageTime2 = (double) sum2/ input.getTasks();
        try {
            input.getFileWriter().write("\n\nAverage Service Time: " + averageTime + "\n");
            input.getFileWriter().write("\n\nAverage Waiting Time: " + averageTime2 + "\n");
            input.getFileWriter().write("\n\nPeak Hour: " + peakHour + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int time = 0;
        int sum = 0;
        int sum2 = 0;

        for (Task task : tasks) {
            sum += task.getServiceTime();
            sum2 += task.getArrivalTime();
        }

        int peakHour = 0;
        int maxNrOfTasks = 0;

        while (time < input.getSimulationInterval()) {
            try {
                input.getFileWriter().write("\nTime = " + time + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Iterator<Task> iterator = tasks.iterator();

            while (iterator.hasNext()) {
                Task newTask = iterator.next();

                if (newTask.getArrivalTime() == time) {
                    scheduler.dispatchTask(newTask);
                    iterator.remove();
                }
            }

            int nrOfTasks = getNrOfTasks(scheduler.getServers());

            if (nrOfTasks > maxNrOfTasks) {
                peakHour = time;
                maxNrOfTasks = nrOfTasks;
            }

            System.out.println("\nTime is now: " + time + "\n");
            time++;


            try {
                displayGeneratedTasks();
            } catch (IOException e) {
                e.printStackTrace();
            }

            scheduler.printQueues(input.getFileWriter());

            int nr = 0;

            if (tasks.isEmpty()) {
                for (Server server : scheduler.getServers()) {
                    if (server.getTasks().isEmpty()) {
                        nr++;
                    }
                }

                if (nr == input.getQueues()) {
                    onClose(sum, sum2, peakHour);
                    break;
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        onClose(sum, sum2, peakHour);

        try {
            input.getFileWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
