package org.flowable;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;

public class ProcessEngineServices {
    private static ProcessEngine processEngine;



    private ProcessEngineServices() {
        this.createProcessEngine();
    }

    public static ProcessEngine getProcessEngine() {
        if(processEngine == null) {
            processEngine =  createProcessEngine();
        }
        return processEngine;
    }

    private static ProcessEngine createProcessEngine() {
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1")
                .setJdbcUsername("sa")
                .setJdbcPassword("")
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        return cfg.buildProcessEngine();
    }
}
