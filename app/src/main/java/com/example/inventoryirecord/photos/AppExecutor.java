package com.example.inventoryirecord.photos;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {

    private final Executor diskIO;
    private final Executor networkIO;
    private final Executor workerThread;


    AppExecutor(Executor diskIO, Executor networkIO, Executor workerThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.workerThread = workerThread;
    }

    public AppExecutor(){
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3),
                new WorkerThread());
    }

    public Executor diskIO(){
        return diskIO;
    }

    public Executor netwrokIO() {
        return networkIO;
    }
    public Executor workerThread(){
        return workerThread;
    }

    private static class WorkerThread implements Executor{
        private android.os.Handler threadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command){threadHandler.post(command);}
    }
}
