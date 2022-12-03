package com.nutcracker.util;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * spel工具
 *
 * @author 胡桃夹子
 * @date 2022-12-03 17:09
 */
public class SpELUtil {

    /**
     * spel转换
     *
     * @param expressionString  分析表达式字符串
     * @param variables         参数
     * @param desiredResultType 所需结果类型，类引用
     * @param <T>               类引用
     * @return 解析结果
     */
    public static <T> T parse(String expressionString, Map<String, Object> variables, Class<T> desiredResultType) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(variables);
        Expression exp = parser.parseExpression(expressionString);
        return exp.getValue(context, desiredResultType);
    }
}