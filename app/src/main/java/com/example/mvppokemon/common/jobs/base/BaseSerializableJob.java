package com.example.mvppokemon.common.jobs.base;

import com.birbit.android.jobqueue.Params;

import java.io.Serializable;


public class BaseSerializableJob extends BaseJob {

    public BaseSerializableJob(Params params) {
        super(params.persist());
    }

}
