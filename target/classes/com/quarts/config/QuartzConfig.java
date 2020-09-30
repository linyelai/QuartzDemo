package com.quarts.config;

import com.quarts.job.SendFaxJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2020/9/30 15:03
 */
@Configuration
public class QuartzConfig {

    /**
     * 1.通过name+group获取唯一的jobKey;2.通过groupname来获取其下的所有jobkey
     */
    final static String GROUP_NAME = "CocofaxJobGroups";

   // @Value("${quartz.scheduler.instanceName}")
    private String quartzInstanceName;

    @Value("${org.quartz.jobStore.dataSource}")
    private String dataSourceName;
    @Value("${org.quartz.dataSource.cocofax.driver}")
    private String myDSDriver;

    @Value("${org.quartz.dataSource.cocofax.URL}")
    private String url;

    @Value("${org.quartz.dataSource.cocofax.user}")
    private String username;

    @Value("${org.quartz.dataSource.cocofax.password}")
    private String password;

    @Value("${org.quartz.dataSource.cocofax.maxConnections}")
    private int maxConnection;


    /**
     * 设置属性
     *
     * @return
     * @throws IOException
     */
    private Properties quartzProperties() throws IOException {
        Properties prop = new Properties();
        // 调度标识名 集群中每一个实例都必须使用相同的名称
        prop.put("quartz.scheduler.instanceName", dataSourceName);
        // ID设置为自动获取 每一个必须不同
        prop.put("org.quartz.scheduler.instanceId", "AUTO");
        // 禁用quartz软件更新
        prop.put("org.quartz.scheduler.skipUpdateCheck", "true");
        prop.put("org.quartz.scheduler.jmx.export", "true");


        // 数据库代理类，一般org.quartz.impl.jdbcjobstore.StdJDBCDelegate可以满足大部分数据库
        prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        // 数据保存方式为数据库持久化
        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        // 数据库别名 随便取
        prop.put("org.quartz.jobStore.dataSource", dataSourceName);
        // 表的前缀，默认QRTZ_
        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
        // 是否加入集群
        prop.put("org.quartz.jobStore.isClustered", "true");

        // 调度实例失效的检查时间间隔
        prop.put("org.quartz.jobStore.clusterCheckinInterval", "20000");
        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
        // 信息保存时间 ms 默认值60秒
        prop.put("org.quartz.jobStore.misfireThreshold", "120000");
        prop.put("org.quartz.jobStore.txIsolationLevelSerializable", "true");
        prop.put("org.quartz.jobStore.selectWithLockSQL", "SELECT * FROM {0}LOCKS WHERE LOCK_NAME = ? FOR UPDATE");

        // 程池的实现类（一般使用SimpleThreadPool即可满足几乎所有用户的需求）
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        // 定线程数，至少为1（无默认值）(一般设置为1-100之间的整数合适)
        prop.put("org.quartz.threadPool.threadCount", "10");
        // 设置线程的优先级（最大为java.lang.Thread.MAX_PRIORITY 10，最小为Thread.MIN_PRIORITY 1，默认为5）
        prop.put("org.quartz.threadPool.threadPriority", "5");
        prop.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");

        prop.put("org.quartz.plugin.triggHistory.class", "org.quartz.plugins.history.LoggingJobHistoryPlugin");
        prop.put("org.quartz.plugin.shutdownhook.class", "org.quartz.plugins.management.ShutdownHookPlugin");
        prop.put("org.quartz.plugin.shutdownhook.cleanShutdown", "true");

        prop.put("org.quartz.dataSource."+dataSourceName+".driver", myDSDriver);
        prop.put("org.quartz.dataSource."+dataSourceName+".URL", url);
        prop.put("org.quartz.dataSource."+dataSourceName+".user", username);
        prop.put("org.quartz.dataSource."+dataSourceName+".password", password);
        prop.put("org.quartz.dataSource."+dataSourceName+".maxConnections", maxConnection);

        return prop;
    }



/****************************************************以下配置需要注意******************************************************/


    /**
     * 调度工厂
     * 此处配置需要调度的触发器 例如 executeJobTrigger
     *
     *
     * @return
     * @throws IOException
     * @throws PropertyVetoException
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException,  SQLException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        factory.setAutoStartup(true);
        factory.setQuartzProperties(quartzProperties());
        factory.setApplicationContextSchedulerContextKey("applicationContext");
        return factory;
    }


    @Bean
    public Scheduler scheduler() throws IOException, SQLException {
        return schedulerFactoryBean().getScheduler();
    }

}
