package com.onepiece.jucdemo;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.WebApplicationContext;

/**
 * springboot测试类
 *
 * @author meng ran
 * @date 2019-03-07 20:18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JucDemoApplication.class)
@Slf4j
public class BaseTest {

  public static final String TOKEN_KEY = "X-Bjca-Token";
  public static final String TOKEN_VALUE = "8da64f2a-ca3f-4dad-ad65-e2f59158b5a1";

  @Autowired WebApplicationContext wac;
  private MockMvc mockMvc;

  @Before
  public void setUp() {
    mockMvc = webAppContextSetup(wac).build();
  }

  @Test
  public void test() throws Exception {
    DataSourceTransactionManager transactionManager =
        (DataSourceTransactionManager) wac.getBean("transactionManager");
    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    def.setPropagationBehavior(
        TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
    TransactionStatus status = transactionManager.getTransaction(def); // 获得事务状态
    int timeout = def.getTimeout();
    // 事务单独提交一次
    System.out.println("status1.isCompleted()----->" + status.isCompleted());
    try {
      // do your business
      transactionManager.commit(status);
      System.out.println("status1.isCompleted()----->" + status.isCompleted());
    } catch (Exception e) {
      transactionManager.rollback(status);
    }
  }
}
