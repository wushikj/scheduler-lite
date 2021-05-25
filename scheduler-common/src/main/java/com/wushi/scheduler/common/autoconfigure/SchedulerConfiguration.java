package com.wushi.scheduler.common.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 调度器全局配置
 *
 * @author yulianghua
 * @date 2019/12/26 4:20 PM
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "wushi.scheduler.config")
public class SchedulerConfiguration {
    private String name;
    private String description;
    private String displayName;
    private boolean shutdownImmediate = true;
    private Source source;
    private Remote remote;
    private Console console;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isShutdownImmediate() {
        return shutdownImmediate;
    }

    public void setShutdownImmediate(boolean shutdownImmediate) {
        this.shutdownImmediate = shutdownImmediate;
    }

    public Console getConsole() {
        return console;
    }

    public void setConsole(Console console) {
        this.console = console;
    }

    public Remote getRemote() {
        return remote;
    }

    public void setRemote(Remote remote) {
        this.remote = remote;
    }

    /**
     * 线程池配置
     *
     * @date 2020/6/30 5:35 PM
     * @description
     */
    private ThreadPool threadPool = new ThreadPool();

    public ThreadPool getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    /**
     * 获取运行模式
     *
     * @return 配置源
     * @author yulianghua
     * @date 2019/12/31 9:08 AM
     * @description
     */
    public Source getSource() {
        return source;
    }

    /**
     * 设置配置来源
     *
     * @param source 配置源 {@link Source}
     * @author yulianghua
     * @date 2019/12/31 9:07 AM
     * @description
     */
    public void setSource(Source source) {
        this.source = source;
    }

    public boolean isReportInfo() {
        return reportInfo;
    }

    public void setReportInfo(boolean reportInfo) {
        this.reportInfo = reportInfo;
    }

    private boolean reportInfo;

    /**
     * 配置来源
     *
     * @author yulianghua
     * @date 2019/12/31 9:09 AM
     * @description
     */
    public enum Source {
        /**
         * 本地模式
         */
        LOCAL,
        /**
         * 远程模式
         */
        REMOTE
    }

    /**
     * 远程配置信息
     */
    public static class Remote {
        /**
         * 主机IP
         */
        private String host;
        /**
         * 密钥
         */
        private String key;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    /**
     * 控制端口配置信息
     */
    public static class Console {
        /**
         * 主机IP
         */
        private String host;
        /**
         * 控制端口，默认为9001
         */
        private int port = 9001;
        /**
         * 密钥
         */
        private String key;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        private  boolean enabled;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    /**
     * 线程池配置
     */
    public static class ThreadPool {
        /**
         * 线程类型
         *
         * @author yulianghua
         * @date 2020/6/30 5:46 PM
         */
        private String threadClass;
        /**
         * 线程数，默认50
         *
         * @author yulianghua
         * @date 2020/6/30 5:51 PM
         */
        private Integer threadCount = 50;

        /**
         * 线程优先级，默认5
         *
         * @author yulianghua
         * @date 2020/6/30 5:51 PM
         */
        private Integer threadPriority = 5;

        /**
         * @author yulianghua
         * @date 2021/3/22 10:06 AM
         */
        private Integer misfireThreshold = 60000;

        public Integer getMisfireThreshold() {
            return misfireThreshold;
        }

        public void setMisfireThreshold(Integer misfireThreshold) {
            this.misfireThreshold = misfireThreshold;
        }

        public String getThreadClass() {
            return threadClass;
        }

        public void setThreadClass(String threadClass) {
            this.threadClass = threadClass;
        }

        public Integer getThreadCount() {
            return threadCount;
        }

        public void setThreadCount(Integer threadCount) {
            this.threadCount = threadCount;
        }

        public Integer getThreadPriority() {
            return threadPriority;
        }

        public void setThreadPriority(Integer threadPriority) {
            this.threadPriority = threadPriority;
        }
    }

    @Override
    public String toString() {
        return "Scheduler configuration：\n" + "name=" + name + ", description=" + description + ", displayName=" + displayName + "\n"
                + "source=" + source + "\n"
                + "shutdownImmediate=" + isShutdownImmediate() + "\n"
                + "threadPool.threadClass=" + threadPool.threadClass + ", threadPool.threadCount=" + threadPool.threadCount + ", threadPool.threadPriority=" + threadPool.threadPriority + ", threadPool.misfireThreshold=" + threadPool.misfireThreshold + "\n"
                + "console.host=" + console.host + ", console.key=" + console.key + "\n"
                + "remote.host=" + remote.host + ", remote.key=" + remote.key;

    }
}


