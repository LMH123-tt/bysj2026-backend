package com.ruoyi.content.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SentinelRuleConfig
{
    @PostConstruct
    public void initRules()
    {
        initFlowRules();
        initDegradeRules();
    }

    private void initFlowRules()
    {
        List<FlowRule> rules = new ArrayList<>();

        FlowRule viewRule = new FlowRule();
        viewRule.setResource("contentDetail");
        viewRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        viewRule.setCount(200);
        viewRule.setLimitApp("default");
        rules.add(viewRule);

        FlowRule searchRule = new FlowRule();
        searchRule.setResource("contentSearch");
        searchRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        searchRule.setCount(100);
        searchRule.setLimitApp("default");
        rules.add(searchRule);

        FlowRule publishRule = new FlowRule();
        publishRule.setResource("contentPublish");
        publishRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        publishRule.setCount(50);
        publishRule.setLimitApp("default");
        rules.add(publishRule);

        FlowRuleManager.loadRules(rules);
    }

    private void initDegradeRules()
    {
        List<DegradeRule> rules = new ArrayList<>();

        DegradeRule viewDegrade = new DegradeRule("contentDetail");
        viewDegrade.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
        viewDegrade.setCount(0.5);
        viewDegrade.setTimeWindow(30);
        viewDegrade.setMinRequestAmount(10);
        viewDegrade.setStatIntervalMs(10000);
        rules.add(viewDegrade);

        DegradeRule searchDegrade = new DegradeRule("contentSearch");
        searchDegrade.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
        searchDegrade.setCount(0.5);
        searchDegrade.setTimeWindow(30);
        searchDegrade.setMinRequestAmount(10);
        searchDegrade.setStatIntervalMs(10000);
        rules.add(searchDegrade);

        DegradeRule slowCallDegrade = new DegradeRule("contentDetail");
        slowCallDegrade.setGrade(RuleConstant.DEGRADE_GRADE_RT);
        slowCallDegrade.setCount(1000);
        slowCallDegrade.setTimeWindow(30);
        slowCallDegrade.setMinRequestAmount(10);
        slowCallDegrade.setStatIntervalMs(10000);
        rules.add(slowCallDegrade);

        DegradeRuleManager.loadRules(rules);
    }
}
