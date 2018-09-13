package org.flowable;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HolidayRequest {

    private ProcessEngine processEngine;
    private RuntimeService runtimeService;
    private TaskService taskService;
    private Scanner scanner;


    public HolidayRequest() {
        this.scanner = new Scanner(System.in);
        this.processEngine = ProcessEngineServices.getProcessEngine();
        this.runtimeService = this.processEngine.getRuntimeService();
        this.taskService = this.processEngine.getTaskService();
    }

    public static void main(String[] args) {
        HolidayRequest holidayRequestApp = new HolidayRequest();
        holidayRequestApp.start();
    }

    public void start() {
        deployProcessDefinitions();

        System.out.println("Who are you?");
        String employee = scanner.nextLine();

        System.out.println("How many holidays do you want to request?");
        Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());

        System.out.println("Why do you need them?");
        String description = scanner.nextLine();


        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employee", employee);
        variables.put("nrOfHolidays", nrOfHolidays);
        variables.put("description", description);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayRequest", variables);
        ProcessInstance processInstance2 = runtimeService.startProcessInstanceByKey("cryRequest", variables);

        List<Task> managerTasks = this.showManagerTasks();
        int taskIndex = Integer.valueOf(scanner.nextLine()) - 1;
        Task taskToComplete = managerTasks.get(taskIndex);
        doApproveOrRejectTask(taskToComplete);


    }

    private void doApproveOrRejectTask(Task taskToComplete) {
        Map<String, Object> processVariables = taskService.getVariables(taskToComplete.getId());
        System.out.println(processVariables.get("employee") + " wants " +
                processVariables.get("nrOfHolidays") + " of holidays. Do you approve this? (y/n)");
        Map<String, Object> variables;
        boolean approved = scanner.nextLine().toLowerCase().equals("y");
        variables = new HashMap<>();
        variables.put("approved", approved);
        taskService.complete(taskToComplete.getId(), variables);
    }

    private List<Task> showManagerTasks() {
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        System.out.println("Hey Managers, you have " + tasks.size() + " tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ") " + tasks.get(i).getName());
        }
        return tasks;
    }

    private void deployProcessDefinitions() {
        // do a deployment
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("holiday-request.bpmn20.xml")
                .deploy();

        Deployment deployment2 = repositoryService.createDeployment()
                .addClasspathResource("Cry_Process.bpmn20.xml")
                .deploy();


        // find the deployed process definition
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        System.out.println("Found process definition : " + processDefinition.getName());

        ProcessDefinition processDefinition2 = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment2.getId())
                .singleResult();
        System.out.println("Found process definition : " + processDefinition2.getName());
    }


}
