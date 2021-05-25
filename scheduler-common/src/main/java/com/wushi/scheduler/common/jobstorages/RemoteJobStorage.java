package com.wushi.scheduler.common.jobstorages;

import com.wushi.scheduler.common.entity.JobBean;
import com.wushi.scheduler.common.interfaces.JobStorage;

import java.util.List;

/**
 * @author yulianghua
 * @date 2019/12/30 4:38 PM
 * @description
 */
public class RemoteJobStorage implements JobStorage {
    @Override
    public List<JobBean> load() {
        return null;
    }
}
