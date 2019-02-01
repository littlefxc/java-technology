package com.littlefxc.examples.goods.worker;


import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.task.WorkflowTaskCoordinator;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import com.netflix.conductor.common.metadata.tasks.TaskResult.Status;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;

/**
 * @author fengxuechao
 * @date 2019/1/8
 */
public class ComputeMoneyAndGoods implements Worker {

    private String taskDefName;

    public ComputeMoneyAndGoods(String taskDefName) {
        this.taskDefName = taskDefName;
    }

    public static void main(String[] args) {

        TaskClient taskClient = new TaskClient();
        taskClient.setRootURI("http://192.168.212.75:8080/api/");
//Point this to the server API

        int threadCount = 2;            //number of threads used to execute workers.  To avoid starvation, should be same or more than number of workers

        //Worker worker1 = new DeployWorker("deploy");
        //Worker worker2 = new EncodeWorker("encode");
        Worker worker3 = new ComputeMoneyAndGoods("computeMoneyAndGoods");//定义轮询的worker

        //Create WorkflowTaskCoordinator
        WorkflowTaskCoordinator.Builder builder = new WorkflowTaskCoordinator.Builder();
        WorkflowTaskCoordinator coordinator = builder.withWorkers(worker3).withThreadCount(threadCount).withTaskClient(taskClient).build();

        //Start for polling and execution of the tasks
        coordinator.init();

    }

    @Override
    public String getTaskDefName() {
        return taskDefName;
    }

    @Override
    public TaskResult execute(Task task) {
        TaskResult result = new TaskResult(task);
        result.setStatus(Status.COMPLETED);
        String goodsPrice = (String) task.getInputData().get("goods_price");
        Integer buyNum = (Integer) task.getInputData().get("buy_num");
        Integer warehouseCount = (Integer) task.getInputData().get("warehouse_count");
        String money = (String) task.getInputData().get("user_money");
        System.err.println("userMoney=" + money);
        BigDecimal userMoney = new BigDecimal(money);
        BigDecimal price = new BigDecimal(goodsPrice);
        BigDecimal num = new BigDecimal(buyNum);
        BigDecimal multiply = price.multiply(num);
        int compare = userMoney.compareTo(multiply);
        try {
            if (warehouseCount < buyNum) {
                result.getOutputData().put("next_task", "not_enough_goods");
                result.getOutputData().put("msg", URLEncoder.encode("We don't have that number of products", "UTF-8"));
                return result;
            }
            if (compare >= 0) {
                result.getOutputData().put("next_task", "pay");
                result.getOutputData().put("total", multiply);
                result.getOutputData().put("remain_user_money", userMoney.subtract(multiply).toString());
                result.getOutputData().put("remain_goods_count", warehouseCount - buyNum);
            } else {
                result.getOutputData().put("next_task", "not_enough_money_to_pay");
                result.getOutputData().put("msg", URLEncoder.encode("You don't have enough money", "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
