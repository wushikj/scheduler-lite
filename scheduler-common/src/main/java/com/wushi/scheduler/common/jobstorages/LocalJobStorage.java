package com.wushi.scheduler.common.jobstorages;

import com.wushi.scheduler.common.interfaces.JobStorage;
import com.wushi.scheduler.common.utitls.PathUtils;
import com.wushi.scheduler.common.entity.JobBean;
import com.wushi.scheduler.common.exceptions.JobConfigSerializationException;
import de.beosign.snakeyamlanno.constructor.AnnotationAwareConstructor;
import de.beosign.snakeyamlanno.constructor.AnnotationAwareListConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.AbstractFileResolvingResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileUrlResource;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yulianghua
 * @date 2019/12/30 4:38 PM
 * @description
 */
public class LocalJobStorage implements JobStorage {
    private static final Logger logger = LoggerFactory.getLogger(LocalJobStorage.class);

    @Override
    public List<JobBean> load() throws IOException, JobConfigSerializationException {
        List<JobBean> jobs;
        String externalConfig = getExternalConfigPath();
        AbstractFileResolvingResource resource;
        File file = new File(externalConfig);
        if (file.exists()) {
            logger.info("LocalConfigurationSource-External:" + externalConfig);
            resource = new FileUrlResource(externalConfig);
        } else {
            logger.info("LocalConfigurationSource-Internal:/config/jobs.yml");
            resource = new ClassPathResource("/config/jobs.yml");
        }

        if (!resource.exists()) {
            throw new FileNotFoundException("jobs.yml不存在。");
        }

        jobs = getJobBeans(resource);
        return jobs;
    }

    private List<JobBean> getJobBeans(AbstractFileResolvingResource resource) throws JobConfigSerializationException {
        List<JobBean> jobs;
        try {
            String content = getResourceContent(resource);
            jobs = getResourceContentFromYml(content);
        } catch (Exception ex) {
            throw new JobConfigSerializationException("任务配置文件序列化异常。", ex);
        }
        return jobs;
    }

    /**
     * 获取resource目录下文件的字符串内容
     *
     * @param resource {@link ClassPathResource}
     * @return String
     * @throws IOException 异常
     */
    private String getResourceContent(AbstractFileResolvingResource resource) throws IOException {
        try (InputStream in = resource.getInputStream();
             Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

    private List<JobBean> getResourceContentFromYml(String content) {
        AnnotationAwareConstructor annotationAwareListConstructor = new AnnotationAwareListConstructor(JobBean.class, true);
        Yaml yaml = new Yaml(annotationAwareListConstructor);
        return yaml.loadAs(content, List.class);
    }

    /**
     * 获取外部配置
     *
     * @return String
     */
    private String getExternalConfigPath() {
        String basePath = PathUtils.getBasePath();
        return Paths.get(basePath, "config", "jobs.yml").toUri().getPath();
    }
}