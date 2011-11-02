package com.gushuley.utils.jmx;


import org.jboss.system.ServiceMBean;

import com.gushuley.utils.thread2.RepeatableThreadControl;

public interface ThreadedServiceMBean extends ServiceMBean, RepeatableThreadControl {
}
