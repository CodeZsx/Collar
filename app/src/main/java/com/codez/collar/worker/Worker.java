package com.codez.collar.worker;

import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by codez on 2018/2/2.
 * Description: Worker-Handler 流水命令任务框架
 */

public class Worker {
    private String name = null;
    //控制现场执行/退出的标志变量
    private volatile boolean isRunning = true;
    //锁和条件变量
    private Lock mLock = new ReentrantLock();
    private Condition mCondition = mLock.newCondition();
    //任务列表
    private Queue<IBaseHandler> mQueue = new LinkedList<>();
    private Thread mThread;

    //初始化函数，开启任务等待线程
    public void initilize(String name){
        this.name = name;
        mThread = new Thread(new WorkRunnable());
        mThread.setName(name);
        mThread.start();
        Log.d(name, "worker start");
    }

    //销毁函数，关闭线程
    public void destroy() {
        Log.d(name, "worker destroy");
        //线程退出命令
        isRunning = false;
        if (mThread != null) {
            mThread.interrupt();
        }
        //添加一个任务，唤醒mCondition.await
        addHandler(new DummyHandler());
    }

    //添加一个新任务
    public void addHandler(IBaseHandler handler) {
        Log.d(name, "add handler " + handler);
        mLock.lock();
        mQueue.add(handler);
        mCondition.signal();
        mLock.unlock();
    }

    //获取下一个任务，如果任务列表为空，则堵塞
    private IBaseHandler getNextHandler(){
        mLock.lock();
        try {
            //如果任务队列为空，则堵塞等待
            while (mQueue.isEmpty()){
                mCondition.await();
            }
            //返回队列首任务，并从队列删除掉
            return mQueue.poll();
        } catch (InterruptedException e) {
            Log.w(name, "thread is interruped");
            e.printStackTrace();
        }
        finally {
            mLock.unlock();
        }
        return null;
    }

    private class WorkRunnable extends Thread{
        @Override
        public void run() {
            while (isRunning){
                IBaseHandler handler = getNextHandler();
                Log.d(name, "run handler " + handler + " start");
                try {
                    if (handler != null) {
                        handler.execute();
                    }
                } catch (Exception e) {
                    Log.w(name, "is Exception");
                    e.printStackTrace();
                }
                Log.d(name, "run handler " + handler + " end");
            }
            Log.d(name, "WorkRunnable run exist !");
            super.run();
        }
    }
}
