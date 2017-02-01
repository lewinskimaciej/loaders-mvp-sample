package com.example.mvppokemon.common.jobs.base;

import com.birbit.android.jobqueue.Params;


public class BaseSerializableJob extends BaseJob {

    public BaseSerializableJob(Params params) {
        super(params.persist());
    }

}
