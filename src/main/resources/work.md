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

## 自我介绍
面试官您好，我叫孙成意，我目前在恒生电子的技术研发管理部下的低码平台就职，是一名Java后端开发工程师。
我们的低码平台就阿童木低码，是面向金融领域的配置驱动的模式和图形化开发的一个低码平台。
通过配置驱动的模式和图形化开发工具完成应用系统数据库设计、前端界面开发、后端服务开发、自动化运维流程,
支持对应用的全流程，全生命周期的开发、运维等环节，帮助业务系统实现个性化、定制化的服务。

在这个项目中，我主要参与了日常的功能模块的迭代及独立主导并多次完成平台的战略及特性需求的设计。