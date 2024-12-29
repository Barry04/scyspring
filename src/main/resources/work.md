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


### 1. **偏向锁（Biased Locking）**
- **定义**：偏向锁是一种优化，目的是减少同一个线程多次进入同步块时的加锁开销。
- **工作原理**：
    - 当一个线程首次获取锁时，锁会“偏向”这个线程。
    - 偏向锁不会涉及实际的加锁操作，线程进入同步块时，只需简单地检查对象头中的线程ID是否匹配即可。
    - 如果其他线程尝试获取这个锁，偏向锁会被撤销并升级为轻量级锁。
- **优点**：偏向于单线程场景，减少CAS操作带来的开销。
- **适用场景**：同步块基本上只有一个线程访问的情况。
- **开销**：撤销偏向锁需要一定代价。

---

### 2. **轻量级锁（Lightweight Locking）**
- **定义**：轻量级锁通过**自旋**来避免线程阻塞，减少上下文切换的开销。
- **工作原理**：
    - 当一个线程尝试获取一个未被加锁的锁时，会在**对象头**中记录线程栈中的锁记录指针。
    - 如果另一个线程也尝试获取这个锁，它不会立即阻塞，而是自旋一段时间（短时间循环尝试获取锁）。
    - 如果竞争过于激烈，自旋失败，锁升级为重量级锁。
- **优点**：避免了线程阻塞和唤醒的系统调用开销。
- **适用场景**：多个线程短时间内竞争锁，且持有锁时间短的情况。
- **开销**：自旋本身需要占用CPU时间。

---

### 3. **重量级锁（Heavyweight Locking）**
- **定义**：重量级锁是传统的操作系统级别的锁，会导致线程进入**阻塞**状态。
- **工作原理**：
    - 如果锁的竞争激烈且轻量级锁无法解决，锁会升级为重量级锁。
    - 使用操作系统的Mutex机制，线程进入**等待队列**，当锁释放时，通过操作系统唤醒被阻塞的线程。
- **优点**：适用于高竞争场景，确保线程安全。
- **缺点**：
    - 线程切换开销高。
    - 阻塞和唤醒线程的操作需要系统调用，性能较差。
- **适用场景**：高竞争场景，锁被持有的时间较长。

---

### 4. **总结对比**

| 特性       | 偏向锁                   | 轻量级锁            | 重量级锁           |
|------------|--------------------------|---------------------|--------------------|
| **竞争程度** | 无竞争                  | 低竞争              | 高竞争             |
| **加锁机制** | 检查对象头线程ID        | 自旋（不阻塞线程）  | 阻塞线程           |
| **性能**   | 性能最佳                | 较好               | 性能最差           |
| **适用场景** | 单线程访问同步块        | 短时间低竞争的多线程| 高竞争的多线程     |
| **开销**   | 偏向锁撤销有一定开销    | 自旋消耗CPU         | 线程切换和系统调用 |

---

## 自我介绍
xxx您好，我叫孙成意，目前在恒生电子的技术研发管理部下的低码平台就职，是一名Java后端开发工程师。
该低码平台名阿童木低码，是面向金融领域,以配置驱动的模式和图形化开发的一个低码平台。
通过配置驱动的模式和图形化开发设计完成应用系统数据库设计、前端界面开发、后端服务开发、自动化运维流程,
支持对应用的全流程，全生命周期的开发、运维等环节，帮助业务系统实现个性化、定制化的服务。

在这个项目中，我主要参与了日常的功能模块的迭代及独立主导并多次完成平台的战略及特性需求的设计与开发


架构介绍：
低码设计器
为用户提供了一个可视化的界面，允许非技术人员通过拖拽组件、配置参数、设计界面等方式，快速创建和设计应用程序的前端和业务流程。

低码解析引擎
连接设计器与运行引擎的桥梁，主要负责将设计器中创建的应用模型进行解析、转化，并生成能够执行的代码或配置，以便运行引擎能够理解和执行。
低码运行引擎
负责执行由应用解析引擎生成的代码或配置，真正将设计的应用投入运行。它是平台的核心执行单元，确保低代码平台的应用能够在生产环境中稳定运行。
1、任务调度与执行
2、动态数据交互
3、todo
可视化服务编排引擎