package com.liuj.lsf.client;

import com.liuj.lsf.exceptions.LsfTimeoutException;
import com.liuj.lsf.msg.BaseMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by cdliujian1 on 2016/11/1.
 */
public class MsgFuture<V> implements java.util.concurrent.Future<V> {

    private Logger logger = LoggerFactory.getLogger(MsgFuture.class);
    
    private int timeout;

    private volatile Object result;

    private long sendTime;

    private transient ReentrantLock reentrantLock = new ReentrantLock();

    private transient Condition condition = reentrantLock.newCondition();

    public MsgFuture(BaseMsg request, int timeout) {
        this.timeout = timeout;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return this.result != null;
    }

    public V get() throws InterruptedException {
        logger.debug("sendTime:{}",sendTime);
        return get(timeout, TimeUnit.MILLISECONDS);
    }

    public V get(long timeout, TimeUnit unit) throws InterruptedException {
        long now = System.currentTimeMillis();
        timeout = unit.toMillis(timeout); // 转为毫秒
        long past = now - sendTime;
        long left = timeout - past;
        if(left < 0){
            //已经没有剩余时间
            if(isDone()){
                return (V)this.result;
            }else{
                throw new LsfTimeoutException("返回超时，后续的响应将会被丢弃abort");
            }
        }else{

            reentrantLock.lock();
            try {
                logger.debug("await "+left+" ms");
                condition.await(left,TimeUnit.MILLISECONDS);
            }finally {
                reentrantLock.unlock();
            }
            if(isDone()){
                return (V)this.result;
            }
            //还可以等待
            try {
                //等待后重新获取
                return get(left, TimeUnit.MILLISECONDS);
            }catch (InterruptedException e){
                throw e;
            }
        }
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public synchronized void setResult(Object result) {
        reentrantLock.lock();
        try {
            this.result = result;
            condition.signalAll();
        }finally {
            reentrantLock.unlock();
        }

    }
}
