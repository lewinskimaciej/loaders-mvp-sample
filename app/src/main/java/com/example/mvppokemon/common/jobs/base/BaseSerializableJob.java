package com.example.mvppokemon.common.jobs.base;

import com.birbit.android.jobqueue.Params;

import java.io.Serializable;

/**
 * Created on 05.01.2017.
 *
 * @author Sławomir Onyszko
 */
public class BaseSerializableJob extends BaseJob implements Serializable {

    public BaseSerializableJob(Params params) {
        super(params.persist());
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
    }
}
