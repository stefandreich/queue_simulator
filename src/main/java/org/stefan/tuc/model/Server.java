package org.stefan.tuc.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {

    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server() {
        super();
        this.tasks = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger(0);
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public void setTasks(BlockingQueue<Task> tasks) {
        this.tasks = tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    public void addTask(Task newTask) {
        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }

    @Override
    public void run() {
        while (!tasks.isEmpty()) {
            try {
                Thread.sleep(1000);
                {
                    setWaitingPeriod(new AtomicInteger(getWaitingPeriod().intValue()-1));

                    tasks.peek().setServiceTime(tasks.peek().getServiceTime()-1);

                    if (tasks.peek().getServiceTime() == 0) {
                        tasks.remove();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        String s = "";

        if (tasks.isEmpty()) {
            s += "Closed";
        } else {
            for (Task task : tasks) {
                s += task.toString();
            }

            s += "empty";
        }

        return s;
    }
}
