package org.stefan.tuc.logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class InputHandler {

    private int tasks;
    private int queues;
    private int simulationInterval;
    private int arrivalTimeMin;
    private int arrivalTimeMax;
    private int serviceTimeMin;
    private int serviceTimeMax;

    public InputHandler(String firstPath, String secondPath) throws IOException {
        File file = new File(firstPath);
        fileWriter = new FileWriter(secondPath);

        Scanner scanner = new Scanner(file);

        int[] v = new int[7];
        int k = 0;

        while (scanner.hasNext()) {
            String s = scanner.next();
            if (s.contains(" ")) {
                s = s.replace(" ", "");
            }
            if (s.contains(",")) {
                String[] arr = s.split(",");
                v[k++] = Integer.parseInt(arr[0]);
                v[k++] = Integer.parseInt(arr[1]);
            } else {
                v[k++] = Integer.parseInt(s);
            }
        }

        this.tasks = v[0];
        this.queues = v[1];
        this.simulationInterval = v[2];
        this.arrivalTimeMin = v[3];
        this.arrivalTimeMax = v[4];
        this.serviceTimeMin = v[5];
        this.serviceTimeMax = v[6];
    }

    private FileWriter fileWriter;

    public int getTasks() {
        return tasks;
    }

    public void setTasks(int tasks) {
        this.tasks = tasks;
    }

    public int getQueues() {
        return queues;
    }

    public void setQueues(int queues) {
        this.queues = queues;
    }

    public int getSimulationInterval() {
        return simulationInterval;
    }

    public void setSimulationInterval(int simulationInterval) {
        this.simulationInterval = simulationInterval;
    }

    public int getArrivalTimeMin() {
        return arrivalTimeMin;
    }

    public void setArrivalTimeMin(int arrivalTimeMin) {
        this.arrivalTimeMin = arrivalTimeMax;
    }

    public int getArrivalTimeMax() {
        return arrivalTimeMax;
    }

    public void setArrivalTimeMax(int arrivalTimeMax) {
        this.arrivalTimeMax = arrivalTimeMax;
    }

    public int getServiceTimeMin() {
        return serviceTimeMin;
    }

    public void setServiceTimeMin(int serviceTimeMin) {
        this.serviceTimeMin = serviceTimeMin;
    }

    public int getServiceTimeMax() {
        return serviceTimeMax;
    }

    public void setServiceTimeMax(int serviceTimeMax) {
        this.serviceTimeMax = serviceTimeMax;
    }

    public FileWriter getFileWriter() {
        return fileWriter;
    }

    public void setFileWriter(FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }
}
