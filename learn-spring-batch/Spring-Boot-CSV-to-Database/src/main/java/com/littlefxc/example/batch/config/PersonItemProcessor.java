package com.littlefxc.example.batch.config;

import com.littlefxc.example.batch.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * 批处理中的一个常见范例是摄取数据，对其进行转换，然后将其传输到其他位置。在这里编写一个简单的转换器，将名称转换为大写。
 *
 * @author Administrator
 * @date 12/36/2018
 */
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    /**
     * 根据接口，将收到一个传入的Person对象，然后将其转换为一个大写的Person。
     * PersonItemProcessor实现了Spring Batch的ItemProcessor接口。
     * 这样可以轻松地将代码连接到本实例中定义的批处理作业中。
     * <p>
     * tips:不要求输入和输出类型相同。实际上，在读取一个数据源之后，有时应用程序的数据流需要不同的数据类型。
     * </p>
     *
     * @param person
     * @return person
     * @throws Exception
     */
    @Override
    public Person process(final Person person) throws Exception {
        if ("fxc".equals(person.getFirstName())) {
            log.error("lastName can't equals 'fxc'");
            return null;
        }

        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();

        final Person transformedPerson = new Person(firstName, lastName);

        log.info("Converting (" + person + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }

}