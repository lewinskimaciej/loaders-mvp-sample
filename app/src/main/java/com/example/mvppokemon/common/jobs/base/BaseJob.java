package com.example.mvppokemon.common.jobs.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;


public class BaseJob extends Job {

    private int retryMaxCount = JobParameters.RETRY_MAX_COUNT;
    private long delay = JobParameters.RETRY_DELAY;

    public BaseJob() {
        super(new Params(JobPriority.MEDIUM_LEVEL_1));
    }

    BaseJob(Params params) {
        super(params);
    }

    @Override
    public void onAdded() {
        // blank override
    }

    @Override
    public void onRun() throws Throwable {
        // blank override
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        // blank override
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.createExponentialBackoff(retryMaxCount, delay);
    }

    public int getRetryMaxCount() {
        return retryMaxCount;
    }

    public void setRetryMaxCount(int retryMaxCount) {
        this.retryMaxCount = retryMaxCount;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
