package com.pcchin.actualtime;

class TimeThread extends Thread {
    private boolean running;
    private MainActivity mainActivity;

    TimeThread(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        while (this.running) {

        }
    }

    void setRunning(boolean running) {
        this.running = running;
    }
}