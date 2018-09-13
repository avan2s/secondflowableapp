package org.flowable;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.runtime.Execution;

import java.util.List;

public class SendRejectionMail implements JavaDelegate {
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Send rejection Mail to " + delegateExecution.getVariable("employee"));
        RuntimeService runtimeService = ProcessEngineServices.getProcessEngine().getRuntimeService();

        List<Execution> executionsWaitingForEmailReceiveTask = runtimeService.createExecutionQuery()
                .activityId("waitForRejection")
                .list();

//        List<Execution> executionsWaitingForEmailEvent = runtimeService.createExecutionQuery()
//                .messageEventSubscriptionName("rejectionMail")
//                .list();

        executionsWaitingForEmailReceiveTask.forEach(e -> runtimeService.trigger(e.getId()));
//        executionsWaitingForEmailEvent.forEach(e -> runtimeService.messageEventReceived("rejectionEmail", e.getId()));
    }
}
