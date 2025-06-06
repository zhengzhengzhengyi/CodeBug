package org.example.codereview.util;

/**
 * @Author zy
 * @Date 2025/6/6 11:41
 */
public class OperationExecutor {

    private OperationStrategy strategy;

    public void setStrategy(OperationStrategy s) {
        this.strategy = s;
    }

    public void execute() {
        strategy.perform();
    }

}
