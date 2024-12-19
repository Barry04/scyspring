## 信创自定义数据库接入简易方案。

1、 TableManagerService 是通过数据库类型getTyep，放回特定的bean。

```java
    /**
 * Get running table manager service.
 *
 * @return Running service according to the configured database type.
 */
public static DbRunningTableManagerService getDbTableManagerService() {
    DbRunningTableManagerService dbTableManagerService = null;
    String dbType = getDbRunningType();
    List<DbRunningTableManagerService> beans = SpringUtils.getBeansValues(DbRunningTableManagerService.class);
    for (DbRunningTableManagerService bean : beans) {
        if (dbType.equalsIgnoreCase(bean.getType())) {
            dbTableManagerService = bean;
            break;
        }
    }
    return dbTableManagerService;
}

```

2、 对于内部只有一个实现类，且按照mybati的扫描路径区分，

```java
    public static <T> T getSystemRunningDataRepoService(Class<T> type) {
    String dbType = getDbRunningType();
    List<T> beans = SpringUtils.getBeansValues(type);
    //·运行端内部只有一个实现类，是按照mybatis的扫描路径区分的，没有类型区分，也不太好强加类型
    List<T> beansWithType = new ArrayList<>();
    List<T> beansWithoutType = new ArrayList<>();
    for (T bean : beans) {
        if (bean instanceof Typedservice) {
            beansWithType.add(bean);
        } else {
            beansWithoutType.add(bean);
        }
    }
    for (T bean : beansWithType) {
        if (dbType.equalsIgnoreCase(((TypedService) bean).getType())) {
            return bean;
        }
    }
    if (beansWithoutType.size() != 1) {
        throw new RuntimeException(Strings.lenientFormat(
                "the interface:%s has no implementation", type.getName()));
    }
    return beansWithoutType.get(0);
}

```