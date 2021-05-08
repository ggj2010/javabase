package com.ggj.java.java;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/27 17:20
 */
@Slf4j
public class FirstDdemo {

    /**
     * 常用方式
     * 代码如下：
     *
     * public Map<Long, String> getIdNameMap(List<Account> accounts) {
     *     return accounts.stream().collect(Collectors.toMap(Account::getId, Account::getUsername));
     * }
     * 收集成实体本身map
     * 代码如下：
     *
     * public Map<Long, Account> getIdAccountMap(List<Account> accounts) {
     *     return accounts.stream().collect(Collectors.toMap(Account::getId, account -> account));
     * }
     * account -> account是一个返回本身的lambda表达式，其实还可以使用Function接口中的一个默认方法代替，使整个方法更简洁优雅：
     *
     * public Map<Long, Account> getIdAccountMap(List<Account> accounts) {
     *     return accounts.stream().collect(Collectors.toMap(Account::getId, Function.identity()));
     * }
     * 重复key的情况
     * 代码如下：
     *
     * public Map<String, Account> getNameAccountMap(List<Account> accounts) {
     *     return accounts.stream().collect(Collectors.toMap(Account::getUsername, Function.identity()));
     * }
     * 这个方法可能报错（java.lang.IllegalStateException: Duplicate key），因为name是有可能重复的。toMap有个重载方法，可以传入一个合并的函数来解决key冲突问题：
     *
     * public Map<String, Account> getNameAccountMap(List<Account> accounts) {
     *     return accounts.stream().collect(Collectors.toMap(Account::getUsername, Function.identity(), (key1, key2) -> key2));
     * }
     * 这里只是简单的使用后者覆盖前者来解决key重复问题。
     *
     * 指定具体收集的map
     * toMap还有另一个重载方法，可以指定一个Map的具体实现，来收集数据：
     *
     * public Map<String, Account> getNameAccountMap(List<Account> accounts) {
     *     return accounts.stream().collect(Collectors.toMap(Account::getUsername, Function.identity(), (key1, key2) -> key2, LinkedHashMap::new));
     * }
     * @param args
     */


//    (params) -> expression
//    (params) -> statement
//    (params) -> { statements }
    public static void main(String[] args) {
        test();


        map();
        list();
        thread();
        getMax();
        sum();
        distinct();
        sortList();
    }

    private static void sortList() {
        List<TestBean> list = Arrays.asList(new TestBean(4,18, "ggj"), new TestBean(2,18, "ggj"), new TestBean(3,19, "ggj3"));

        list=list.stream().sorted((v1,v2)->Long.compare(v1.getAge(),v2.getAge())).collect(Collectors.toList());

        for (TestBean student : list) {
            log.info(student.getName());
        }


    }

    private static void test() {
        List<TestBean> list = Arrays.asList(new TestBean(1,18, "ggj"), new TestBean(2,18, "ggj"), new TestBean(3,19, "ggj3"));
        List<Integer> idList = list.stream().map(TestBean::getId).collect(Collectors.toList());
        List<String> nameList = list.stream().map(TestBean::getName).collect(Collectors.toList());

        List<TestBean> filterList = list.stream().filter(testBean -> testBean.getAge() > 18).collect(Collectors.toList());

        List<TestBean> distinctList = list.stream().filter(distinctByKey(TestBean::getName)).collect(Collectors.toList());

        Map<String, Integer> map = list.stream().collect(Collectors.groupingBy(TestBean::getName, Collectors.summingInt(TestBean::getAge)));


        Map<String, List<TestBean>> map1 = list.stream().collect(Collectors.groupingBy(TestBean::getName));
        Map<Integer, List<TestBean>> map2 = list.stream().collect(Collectors.groupingBy(TestBean::getId));

        Map<Integer, TestBean> map3 = list.stream().collect(Collectors.toMap(TestBean::getId, testBean -> testBean));
        Map<Integer, TestBean> map4 = list.stream().collect(Collectors.toMap(TestBean::getId,  Function.identity()));

        List<Integer> addAgeList = list.stream().map(s -> s.getAge() + 1).collect(Collectors.toList());

        List<String>  stringList=Arrays.asList("1","2","3");
        List<Integer> integerList = stringList.stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());





    }

    private static void sum() {
        List<TestBean> testBeanList = Arrays.asList(new TestBean(18, "ggj"), new TestBean(18, "ggj"), new TestBean(19, "ggj3"));
        Map<String, Integer> stringIntegerMap = testBeanList.stream().collect(Collectors.groupingBy(TestBean::getName, Collectors.summingInt(TestBean::getAge)));
        System.out.println("");
    }

    private static void distinct() {
        List<Integer> list=new ArrayList<>();
        list.add(1);
        list.add(1);
        list.add(2);
        List<Integer> list2 = list.stream().distinct().collect(Collectors.toList());

        List<TestBean> testBeanList = Arrays.asList(new TestBean(18, "ggj"), new TestBean(18, "ggj2"), new TestBean(19, "ggj3"));
        long count = testBeanList.parallelStream().filter(distinctByKey(TestBean::getAge)).count();
        List<TestBean> dd = testBeanList.parallelStream().filter(distinctByKey(TestBean::getAge)).collect(Collectors.toList());

        log.info("dictinct count==>{}",count);
        long count2 = testBeanList.parallelStream().distinct().count();
        log.info("dictinct count2==>{}",count2);



    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    /**
     * map 可以取出某一个字段的属性
     * 可以进行分组
     * 可以转化为map
     */
    private static void map() {
        List<TestBean> testBeanList = Arrays.asList(new TestBean(18, "ggj"), new TestBean(20, "ggj2"), new TestBean(19, "ggj3"));
        List<Integer> groupIdList = testBeanList.stream().collect(Collectors.groupingBy(TestBean::getAge)).keySet().stream().collect(Collectors.toList());
        //转换成map
        Map<Integer, List<TestBean>> map = testBeanList.stream().collect(Collectors.groupingBy(TestBean::getAge));
        Map<Integer, TestBean> dd = testBeanList.stream().collect(Collectors.toMap(TestBean::getAge, testBean -> testBean));
        map.forEach((age,testBean)->{
          log.info("age={},name={}",age,testBean.get(0).getName());
        });
    }

    private static void getMax() {
        List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        IntSummaryStatistics stats = primes.stream().mapToInt((x) -> x)
                .summaryStatistics();
        System.out.println("Highest prime number in List : " + stats.getMax());
        System.out.println("Lowest prime number in List : " + stats.getMin());
        System.out.println("Sum of all prime numbers : " + stats.getSum());
        System.out.println("Average of all prime numbers : " + stats.getAverage());

    }
    private static void thread() {
        new Thread(() -> log.info("In Java8, Lambda expression rocks !!")).start();
        new Thread(() -> log.info("test")).start();
    }

    private static void list() {
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("3");
        list.add("2");

        list.forEach(number -> log.info(number));

        list.forEach((String number) -> {log.info(number);});

        List<String> list2=list;
        final List<String> filtered = list.stream()
                .filter(s -> s.startsWith("1"))
                .map(s -> s+"ddd")
                .collect(Collectors.toList());
        log.info("输出："+filtered.get(0));

        List<TestBean> testBeanList = Arrays.asList(new TestBean(18, "ggj"), new TestBean(18, "ggj2"), new TestBean(19, "ggj3"));
        //取出集合某一个属性的值
        List<String> dd = testBeanList.stream().map(TestBean::getName).collect(Collectors.toList());
        log.info("");

    }
}
