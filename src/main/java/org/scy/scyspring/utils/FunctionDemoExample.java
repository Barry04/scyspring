package org.scy.scyspring.utils;

import org.scy.scyspring.core.domain.Log;
import org.scy.scyspring.core.domain.SomeObj;
import org.scy.scyspring.core.domain.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class FunctionDemoExample {


    /**
     * 主函数入口
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {

        Data data = new Data();
        Map<String, Object> record = new HashMap<>();
        record.put("name", "ss");
        data.setRecord(record);
        testExample(data);
    }

    public static void testExample(Data data) {
        Map<String, Object> record = new HashMap<>(data.getRecord());
        System.out.println("record.get(\"name\") = " + record.get("name"));
        record.remove("name");

    }

    /**
     * 使用方法级别的泛型类型参数 <T> 的方法。此方法接收一个 BiFunction 接口实例，
     * 该接口可接受两个 String 类型的参数并返回一个泛型类型 T 的结果。
     * 方法内部通过调用该函数接口的 apply 方法，传入固定的字符串参数（目前参数未使用），并接收返回值，
     * 然后将返回值（目前返回值未使用）转换为字符串形式输出到控制台。
     *
     * @param someObj    包含需要使用的 URL 和 URL 名称的对象，目前该参数未在方法内使用。
     * @param targetList 一个泛型列表，目前该参数未在方法内使用。
     * @param func       一个 BiFunction 接口实例，用于执行特定操作并返回泛型类型 T 的结果。
     * @param <T>        方法级别的泛型类型参数，指示 func 返回的类型。
     */
    private static <T> void process(SomeObj someObj, List<T> targetList, BiFunction<String, String, T> func) {
        // 应用传入的 BiFunction，并接收返回值，但目前返回值未被使用
        targetList.add(func.apply(someObj.getUrl(), someObj.getUrlName()));
    }


    private static void addLog(SomeObj someObj, List<Log> logs) {

        process(someObj, logs, Log::new);
    }

    private static void addUserInfo(SomeObj someObj, List<UserInfo> userInfos) {
        process(someObj, userInfos, UserInfo::new);
    }

}
