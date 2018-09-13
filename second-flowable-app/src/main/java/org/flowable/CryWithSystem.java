package org.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class CryWithSystem implements JavaDelegate {
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("CRYYYYY CRYYYYY CRYYYYY. I don't like you boss. You never give me holidays.");
    }
}
