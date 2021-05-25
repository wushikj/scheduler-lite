package com.wushi.scheduler.common.interfaces;

import com.wushi.scheduler.common.entity.JobBean;
import com.wushi.scheduler.common.exceptions.JobConfigSerializationException;

import java.io.IOException;
import java.util.List;

/**
 * Job存储接口
 *
 * @author yulianghua
 * @date 2019/12/30 4:27 PM
 * @description
 */
public interface JobStorage {
    /**
     * 加载jobs.json中的任务配置
     *
     * @return List<JobBean>
     */
    List<JobBean> load() throws IOException, JobConfigSerializationException;
}
