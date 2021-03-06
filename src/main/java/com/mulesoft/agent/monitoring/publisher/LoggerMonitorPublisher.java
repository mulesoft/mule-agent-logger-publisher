/*
* (c) 2003-2014 MuleSoft, Inc. This software is protected under international copyright
* law. All use of this software is subject to MuleSoft's Master Subscription Agreement
* (or other master license agreement) separately entered into in writing between you and
* MuleSoft. If such an agreement is not in place, you may not use the software.
*/

package com.mulesoft.agent.monitoring.publisher;

import com.mulesoft.agent.AgentEnableOperationException;
import com.mulesoft.agent.configuration.Configurable;
import com.mulesoft.agent.domain.monitoring.Metric;
import com.mulesoft.agent.handlers.InternalMessageHandler;
import com.mulesoft.agent.services.OnOffSwitch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Handler that publishes JMX information obtained from the Monitoring Service to a log.
 * </p>
 */
@Named("mule.agent.logger.jmx.internal.handler")
@Singleton
public class LoggerMonitorPublisher implements InternalMessageHandler<List<Metric>>
{
    private final transient Log logger = LogFactory.getLog(getClass());

    @Configurable("unnamed")
    String publisherName;

    /**
     * <p>
     * A switch used to enable and disable the service
     * </p>
     */
    private OnOffSwitch enabledSwitch = OnOffSwitch.newNullSwitch(true);

    /**
     * <p>
     * A formatter used for printing out dates
     * </p>
     */
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    public boolean handle(@NotNull List<Metric> metrics)
    {

        if(isEnabled())
        {
            Date date = new Date();

            logger.info("--- Begin Metrics Collected by publisher: " +  publisherName + " --- " + dateFormat.format(date));

            for (Metric metric : metrics)
            {
                logger.info("Metric: " + metric.getName() + " Value: " + metric.getValue());
            }

            logger.info("--- End Metrics Collected by publisher: " +  publisherName + " --- " + dateFormat.format(date));

            return true;
        }

        return false;
    }

    @Override
    public void enable(boolean state) throws AgentEnableOperationException
    {
        enabledSwitch.switchTo(state);
    }

    @Override
    public boolean isEnabled()
    {
        return enabledSwitch.isEnabled();
    }
}
