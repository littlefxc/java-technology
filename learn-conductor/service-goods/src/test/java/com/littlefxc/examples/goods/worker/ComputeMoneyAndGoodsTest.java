package com.littlefxc.examples.goods.worker;

import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.task.WorkflowTaskCoordinator;
import com.netflix.conductor.client.worker.Worker;


/**
 * @author fengxuechao
 * @date 2019/1/8
 **/
public class ComputeMoneyAndGoodsTest {


        public static void main(String[] args) {

            TaskClient taskClient = new TaskClient();
            taskClient.setRootURI("http://192.168.212.75:8080/api/");
//Point this to the server API

            int threadCount = 2;			//number of threads used to execute workers.  To avoid starvation, should be same or more than number of workers

            //Worker worker1 = new DeployWorker("deploy");
            //Worker worker2 = new EncodeWorker("encode");
            Worker worker3 = new ComputeMoneyAndGoods("computeMoneyAndGoods");//定义轮询的worker

            //Create WorkflowTaskCoordinator
            WorkflowTaskCoordinator.Builder builder = new WorkflowTaskCoordinator.Builder();
            WorkflowTaskCoordinator coordinator = builder.withWorkers(worker3).withThreadCount(threadCount).withTaskClient(taskClient).build();

            //Start for polling and execution of the tasks
            coordinator.init();

        }


}