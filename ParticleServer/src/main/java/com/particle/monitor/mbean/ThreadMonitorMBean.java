package com.particle.monitor.mbean;

import com.particle.executor.thread.IScheduleThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.util.List;

public class ThreadMonitorMBean implements DynamicMBean {

    private static final Logger logger = LoggerFactory.getLogger(ThreadMonitorMBean.class);

    private String threadName;
    private IScheduleThread IScheduleThread;
    private MBeanInfo mBeanInfo;

    public ThreadMonitorMBean(String threadName, IScheduleThread IScheduleThread) {
        this.threadName = threadName;
        this.IScheduleThread = IScheduleThread;

        try {
            Class clazz = this.getClass();

            // 关于 "recorder" 属性的元信息 : 名称为 UpTime，只读属性 ( 没有写方法 )。
            MBeanAttributeInfo getScheduledTaskListMethodAttribute = new MBeanAttributeInfo(
                    "ScheduledTaskListMethodAttribute",
                    "Get scheduled task list.",
                    clazz.getMethod("getScheduledTaskList", new Class[0]),
                    null);
            MBeanAttributeInfo getRepeatingTaskListMethodAttribute = new MBeanAttributeInfo(
                    "RepeatingTaskListMethodAttribute",
                    "Get scheduled task list.",
                    clazz.getMethod("getRepeatingTaskList", new Class[0]),
                    null);

            // 关于构造函数的元信息
            MBeanConstructorInfo mBeanConstructorInfo = new MBeanConstructorInfo(
                    "Constructor for ThreadMonitorMBean",
                    clazz.getConstructor(new Class[]{String.class, IScheduleThread.class}));

            //ThreadMonitorMBean 的元信息，
            mBeanInfo = new MBeanInfo(
                    clazz.getName(),
                    "ThreadMonitorMBean_" + this.threadName,
                    new MBeanAttributeInfo[]{
                            getScheduledTaskListMethodAttribute,
                            getRepeatingTaskListMethodAttribute},
                    new MBeanConstructorInfo[]{mBeanConstructorInfo},
                    null,
                    null);
        } catch (Exception e) {
            logger.error("Fail to init ThreadMonitorMBean.", e);
        }
    }

    public List<String> getScheduledTaskList() {
        return this.IScheduleThread.getScheduledTaskList();
    }

    public List<String> getRepeatingTaskList() {
        return this.IScheduleThread.getRepeatingTaskList();
    }

    @Override
    public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
        switch (attribute) {
            case "ScheduledTaskListMethodAttribute":
                return this.getScheduledTaskList();
            case "RepeatingTaskListMethodAttribute":
                return this.getRepeatingTaskList();
        }

        return null;
    }

    @Override
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {

    }

    @Override
    public AttributeList getAttributes(String[] attributes) {
        return null;
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        return null;
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        return null;
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return this.mBeanInfo;
    }
}
