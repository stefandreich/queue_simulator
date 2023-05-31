package org.stefan.tuc.logic;

import org.stefan.tuc.model.Server;
import org.stefan.tuc.model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Scheduler {

    private static ArrayList<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        servers = new ArrayList<>();
        for (int i = 0; i < maxNoServers; i++) {
            servers.add(new Server());
        }
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
    }

    public int getMaxNoServers() {
        return maxNoServers;
    }

    public void setMaxNoServers(int maxNoServers) {
        this.maxNoServers = maxNoServers;
    }

    public int getMaxTasksPerServer() {
        return maxTasksPerServer;
    }

    public void setMaxTasksPerServer(int maxTasksPerServer) {
        this.maxTasksPerServer = maxTasksPerServer;
    }

    public void printQueues(FileWriter f) {
        for (Server server : servers) {
            try {
                f.write("\nQueue " + (servers.indexOf(server) + 1) + ": " + server.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Server minimumQueue() {
        int min = servers.get(0).getWaitingPeriod().intValue();

        Server server = servers.get(0);

        for (Server server1 : servers) {
            if (server1.getWaitingPeriod().intValue() < min) {
                min = server1.getWaitingPeriod().intValue();
                server = server1;
            }
        }

        return server;
    }

    public void dispatchTask(Task task) {
        Server minimumQueue = minimumQueue();
        int flag = 0;

        if (minimumQueue.getTasks().size() == 0) {
            flag = 1;
        }
        minimumQueue.addTask(task);

        if (flag == 1) {
            Thread thread = new Thread(minimumQueue);
            thread.start();
        }
    }

    public ArrayList<Server> getServers() {
        return servers;
    }
}
