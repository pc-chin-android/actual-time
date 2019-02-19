package com.pcchin.actualtime;

class LocationThread extends Thread {
    private boolean running;
    private MainActivity mainActivity;

    LocationThread(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        while (running) {

        }
    }

    void setRunning(boolean running) {
        this.running = running;
    }
}