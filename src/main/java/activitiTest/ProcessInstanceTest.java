package activitiTest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ProcessInstanceTest {
    ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
    //部署流程定义
    @Test
    public void deploymentProcessDefinition(){
       Deployment deployment=processEngine.getRepositoryService().createDeployment().addClasspathResource("diagrams/holloword.xml")
               .addClasspathResource("diagrams/holloword.png").deploy();
       System.out.println("1"+deployment.getId());
       System.out.println("2"+deployment.getName());
       System.out.println("3"+deployment.getCategory());
       System.out.println("4"+deployment.getDeploymentTime());
       System.out.println("5"+deployment.getTenantId());
    }
    @Test
    public void startProcessInstance(){
        String processDefinitionKey="holloworld";
        ProcessInstance processInstance=processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey);
        System.out.println("流程实例的id"+processInstance.getId());
        System.out.println("流程定义的id"+processInstance.getProcessDefinitionId());
    }

}
