# logback + Kafka + logstash 集成

我们是通过 logback 打印日志，然后将日志通过 kafka 消息队列发送到 Logstash,经过处理以后存储到 Elasticsearch 中，然后通过 Kibana 图形化界面进行分析和处理。

在 spring boot 应用程序中，默认使用 logback 来记录日志，并用 INFO 级别输出日志到控制台。

日志级别和顺序：TRACE < DEBUG < INFO < WARN < ERROR < FATAL

Spring Boot官方推荐优先使用带有-spring的文件名作为,按照如下规则组织配置文件名，就能被正确加载:

logback-spring.xml > logback-spring.groovy > logback.xml > logback.groovy

## 1. logback 与 Kafka 的集成

logback 记录日志到 Kafka 消息队列中去，主要使用的是 `com.github.danielwegener:logback-kafka-appender:0.2.0-RC2` 这个依赖.

### 1.1. KafkaAppender 配置说明

由于Logback Encoder API中的重大更改，您需要至少使用logback版本1.2。

确保项目依赖中有：

[maven pom.xml]

```maven
<dependency>
    <groupId>com.github.danielwegener</groupId>
    <artifactId>logback-kafka-appender</artifactId>
    <version>0.2.0-RC2</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.3</version>
    <scope>runtime</scope>
</dependency>
```

[maven pom.xml]

```xml
<configuration>

    <!-- This is the kafkaAppender -->
    <appender name="kafkaAppender" class="com.github.danielwegener.logback.kafka.KafkaAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
        <topic>logs</topic>
        <keyingStrategy class="com.github.danielwegener.logback.kafka.keying.NoKeyKeyingStrategy" />
        <deliveryStrategy class="com.github.danielwegener.logback.kafka.delivery.AsynchronousDeliveryStrategy" />
        
        <!-- 可选参数, 用于固定分区 -->
        <!-- <partition>0</partition> -->
        
        <!-- 可选参数，用于在kafka消息中包含日志时间戳 -->
        <!-- <appendTimestamp>true</appendTimestamp> -->

        <!-- 每个<producerConfig>转换为常规kafka-client配置（格式：key = value） -->
        <!-- 生产者配置记录在这里：https//kafka.apache.org/documentation.html#newproducerconfigs -->
        <!-- bootstrap.servers是唯一必需的 producerConfig -->
        <producerConfig>bootstrap.servers=localhost:9092</producerConfig>

        <!-- 如果kafka不可用，这是后备appender。 -->
        <appender-ref ref="STDOUT" />
    </appender>

    <!-- 标准输出 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="info">
        <appender-ref ref="kafkaAppender" />
    </root>
</configuration>
```

### 1.2. 兼容性:Compatibility

logback-kafka-appender 依赖于 `org.apache.kafka:kafka-clients:1.0.0:jar`。它可以将日志附加到版本为 `0.9.0.0` 或更高版本的 kafka 代理。

对 `kafka-clients` 的依赖性不会被遮蔽，并且可以通过依赖性覆盖升级到更高的 api 兼容版本。

### 1.3. 分发策略:Delivery strategies

直接通过网络进行日志记录并不是一件容易的事情，因为它可能不如本地文件系统可靠，并且如果传输出现问题，对应用程序性能的影响要大得多。

您需要做出一个重要的决定：是将所有日志传递到远程 Kafka 更重要，还是让应用程序保持平稳运行更为重要？这两个决定都允许您调整此 appender 以获得吞吐量。

- AsynchronousDeliveryStrategy: 

    将每个日志消息分派给Kafka生成器。如果由于某些原因传递失败，则将消息发送给 fallback appenders。
    但是，如果生产者发送的缓冲区已满，这个交付策略就会阻塞(如果到代理的连接丢失，就会发生这种情况)。
    为了避免这种阻塞，可以启用 `producerConfig` `block.buffer.full=false`。
    所有不能足够快地交付的日志消息都将立即转到 fallback appenders。

- BlockingDeliveryStrategy: 
    
    将每条日志消息分派给Kafka Producer。如果由于某些原因导致传递失败，则会将消息分派给备用追加程序(fallback appender)。
    但是，如果生成器发送缓冲区已满，则此DeliveryStrategy 阻止每个调用线程，直到实际传递日志消息。
    通常不鼓励这种策略，因为它对吞吐量有很大的负面影响。警告：此策略不应与 `producerConfig` 一起使用 `linger.ms`
    
#### 1.3.1. 关于 broker 的中断

AsynchronousDeliveryStrategy 不会阻止被Kafka元数据交换阻塞的应用程序。
这意味着：如果在日志记录上下文启动时无法访问所有代理，或者所有代理在较长时间内无法访问（> metadata.max.age.ms），
则 appender 最终将阻塞。这种行为通常是不受欢迎的，可以使用 kafka-clients 0.9 进行迁移（参见＃16）。
在此之前，您可以使用 logback 自己的 AsyncAppender 包装 KafkaAppender。

示例配置可能如下所示：

```xml
<configuration>

    <!-- This is the kafkaAppender -->
    <appender name="kafkaAppender" class="com.github.danielwegener.logback.kafka.KafkaAppender">
    <!-- Kafka Appender configuration -->
    </appender>

    <appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="kafkaAppender" />
    </appender>

    <root level="info">
        <appender-ref ref="ASYNC" />
    </root>
</configuration>
```

#### 1.3.2. 自定义分发策略(delivery strategies)

你可能使用自己的分发策略，只需继承 `com.github.danielwegener.logback.kafka.delivery.DeliveryStrategy`

#### 1.3.3. 备用追加程序:fallback appender

如果由于某种原因，kafka-producer决定它无法发布日志消息，那么该消息仍然可以记录到 fallback appender（STDOUT 或 STDERR 上的 ConsoleAppender 将是一个合理的选择）。

只需将您的后备appender作为logback appender-ref添加到logback.xml中的KafkaAppender部分。 每个无法传递给kafka的消息都将写入所有已定义的appender-ref。

1.1 章节示例：`<appender-ref ref ="STDOUT">` 中 `STDOUT` 是已定义的 appender。

请注意，AsynchronousDeliveryStrategy 将重用 kafka 生成器io线程将消息写入备用 appender。 因此，所有后备追加者应该是合理的快速，所以他们不会减慢或打破卡夫卡生产者。

#### 1.3.4. 生产者调整

这个appender使用kafka-0.8.2中引入的 [kafka生成器](https://kafka.apache.org/documentation.html#producerconfigs)。 它使用生成器默认配置。

您可以使用 `<producerConfig> Name = Value </ producerConfig>` 块覆盖任何已知的kafka生成器配置（请注意，boostrap.servers配置是必需的）。 
这允许很多微调潜力（例如，使用batch.size，compression.type 和 linger.ms）。

#### 1.3.5. 序列化

该模块支持任何 `ch.qos.logback.core.encoder.Encoder`。这允许您使用能够编码 `ILoggingEvent`或 `IAccessEvent` 的任何编码器，
如众所周知的[logback PatternLayoutEncoder](https://logback.qos.ch/manual/encoders.html#PatternLayoutEncoder)，
或者例如 [logstash-logback-encoder的LogstashEncoxer](https://github.com/logstash/logstash-logback-encoder#usage)。

##### 1.3.5.1 自定义序列化

如果要在kafka日志记录主题上编写与字符串不同的内容，可以使用编码机制。 用例将是生产或消费方面的较小消息大小和/或更好的序列化/反序列化性能。
有用的格式可以是BSON，Avro或其他。

要推出自己的实现，请参阅[logback文档](https://logback.qos.ch/xref/ch/qos/logback/core/encoder/Encoder.html)。
请注意，logback-kafka-appender永远不会调用headerBytes（）或footerBytes（）方法。

您的编码器应该针对您要支持的事件类型的任何子类型（通常是 `ILoggingEvent`）进行类型参数化，例如:

`public class MyEncoder extends ch.qos.logback.core.encoder.Encoder<ILoggingEvent> {/*..*/}`

### 1.4 键控策略/分区:Keying strategies / Partitioning

Kafka的可扩展性和排序保证严重依赖于分区的概念（[这里有更多细节](https://kafka.apache.org/082/documentation.html#introduction)）。
对于应用程序日志记录，这意味着我们需要决定如何在多个kafka主题分区上分发日志消息。
这个决定的一个含义是消息在从任意多分区消费者消费时如何排序，因为kafka仅在每个单独的分区上提供有保证的读取顺序。
另一个含义是我们的日志消息在所有可用分区中的分布均匀，因此在多个代理之间保持平衡。

日志消息的顺序可能重要，也可能不重要，具体取决于预期的消费者 - 受众（例如，logstash索引器无论如何都会按时间戳重新排序所有消息）。

您可以使用partition属性为kafka appender提供固定分区，或让生产者使用消息密钥对消息进行分区。 因此logback-kafka-appender支持以下键控策略策略：

- `NoKeyKeyingStrategy` : 

    不生成 message key。如果未提供固定分区，则导致跨分区的循环分布。

- `HostNameKeyingStrategy` : 

    此策略使用 HOSTNAME 作为 message key。 这很有用，因为它可以确保此主机发出的所有日志消息对于任何使用者都保持正确的顺序。
    但是这种策略可能导致少量主机的日志分配不均匀（与分区数量相比）。

- `ContextNameKeyingStrategy` : 

    此策略使用 logback 的 CONTEXT_NAME 作为 message key。
    这可以确保由同一日志记录上下文记录的所有日志消息将保持在任何使用者的正确顺序中。
    但是这种策略可能导致少量主机的日志分配不均匀（与分区数量相比）。
    此策略仅适用于ILoggingEvents。
    
- `ThreadNameKeyingStrategy` : 

    此策略使用调用线程名(thread name)称作为 message key。
    这可确保同一线程记录的所有消息将保持正确的顺序，供任何使用者使用。
    但是这种策略可能会导致少量线程（-names）的日志分配不均匀（与分区数量相比）。
    此策略仅适用于 ILoggingEvents。

- `LoggerNameKeyingStrategy` : 

    *此策略使用记录器名称(logger name)作为 message key。
    这可确保同一记录器记录的所有消息都将保持在任何使用者的正确顺序中。
    但是这种策略可能会导致少量不同记录器的日志分配不均匀（与分区数量相比）。
    此策略仅适用于 ILoggingEvents。
    
#### 1.4.1. 自定义键控策略 Custom keying strategies

如果上述键控策略都不满足您的要求，您可以通过实现自定义 KeyingStrategy 轻松实现自己的：

```java
package foo;
import com.github.danielwegener.logback.kafka.keying.KeyingStrategy;

/* 这是一个有效的例子，但并没有多大意义 */
public class LevelKeyingStrategy implements KeyingStrategy<ILoggingEvent> {
    @Override
    public byte[] createKey(ILoggingEvent e) {
        return ByteBuffer.allocate(4).putInt(e.getLevel()).array();
    }
}
```

作为大多数自定义 logback 组件，您的自定义分区策略还可以实现 `ch.qos.logback.core.spi.ContextAware` 和 `ch.qos.logback.core.spi.LifeCycle` 接口。

当您想要使用kafka的日志压缩工具时，自定义键控策略可能会特别方便。

### 1.5. logback-spring.xml 示例

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="60 seconds" debug="false">
    <contextName>oauth2-auth-server</contextName>
    <!--定义日志文件的存储地址 勿在 LogBack 的配置中使用相对路径-->
    <property name="LOG_HOME" value="logs"/>

    <!--输出到控制台-->
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
        <encoder>
            <pattern>%-12(%d{yyyy-MM-dd HH:mm:ss.SSS}) %contextName [%thread] %highlight(%-5level) %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!--输出到kafka-->
    <appender name="kafka" class="com.github.danielwegener.logback.kafka.KafkaAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>%-12(%d{yyyy-MM-dd HH:mm:ss.SSS}) %contextName [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
        <topic>oauth2-auth-server</topic>
        <!-- 我们不关心如何对日志消息进行分区 -->
        <keyingStrategy class="com.github.danielwegener.logback.kafka.keying.NoKeyKeyingStrategy"/>

        <!-- 使用异步传递。 日志记录不会阻止应用程序线程 -->
        <deliveryStrategy class="com.github.danielwegener.logback.kafka.delivery.AsynchronousDeliveryStrategy"/>

        <!-- 每个<producerConfig>转换为常规kafka-client配置（格式：key = value） -->
        <!-- 生产者配置记录在这里：https://kafka.apache.org/documentation.html#newproducerconfigs -->
        <!-- bootstrap.servers是唯一必需的 producerConfig -->
        <producerConfig>bootstrap.servers=192.168.213.13:9092,192.168.213.14:9092,192.168.213.21:9092</producerConfig>
        <!-- 不用等待代理对批的接收进行打包。  -->
        <producerConfig>acks=0</producerConfig>
        <!-- 等待最多1000毫秒并收集日志消息，然后再批量发送 -->
        <producerConfig>linger.ms=1000</producerConfig>
        <!-- 即使生产者缓冲区运行已满，也不要阻止应用程序而是开始丢弃消息 -->
        <producerConfig>max.block.ms=0</producerConfig>
        <!-- 定义用于标识kafka代理的客户端ID -->
        <producerConfig>client.id=${HOSTNAME}-${CONTEXT_NAME}-logback-relaxed</producerConfig>
        <!-- 如果kafka不可用，这是后备appender。 -->
        <appender-ref ref="file"/>
    </appender>
    
    <!--输出到文件-->
    <appender name="file" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <FileNamePattern>${LOG_HOME}/oauth2-auth-server.log.%d{yyyy-MM-dd}.log</FileNamePattern>
            <!--日志文件保留天数-->
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder>
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
            <pattern>%-12(%d{yyyy-MM-dd HH:mm:ss.SSS}) %contextName [%thread] %-5level %logger{36} - %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <root level="info">
        <appender-ref ref="console"/>
        <appender-ref ref="kafka"/>
    </root>
</configuration>
```

## 2. Kafka 与 Logstash 的集成

logstash 与 Kafka 的简单配置

```yaml
input {
     kafka {
        topics => "applog"    
        bootstrap_servers => "Kafka服务器IP:9092,Kafka服务器IP:9092"
        codec => "json"
    }
}
filter {
}
output {
  //控制台输入
  stdout {  codec => rubydebug }
  elasticsearch {
    hosts => [ "elasticsearch服务器IP:9200" ]
    index => "kafka"
  }

}
```

启动 logstash：

`.\bin\logstash -f .\conf\logstash-kaka.conf`

