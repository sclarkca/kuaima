package com.kuaima.manage;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public class InitAndDestroySeqBean implements InitializingBean, DisposableBean {
	private static final Logger logger = LoggerFactory.getLogger(InitAndDestroySeqBean.class);
	/**@Autowired
	 private ConfigManager configManager;
*/
	public InitAndDestroySeqBean() {
		logger.info("执行InitAndDestroySeqBean: 构造方法{}","");

	}

	@PostConstruct
	public void postConstruct() {
		logger.info("执行InitAndDestroySeqBean: postConstruct{}","");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("执行InitAndDestroySeqBean: afterPropertiesSet{}","");
		

	}

	public void initMethod() {
		logger.info("执行InitAndDestroySeqBean: init-method{}","");
	}

	@PreDestroy
	public void preDestroy() {
		logger.info("执行InitAndDestroySeqBean: preDestroy{}","");
	}

	@Override
	public void destroy() throws Exception {
		logger.info("执行InitAndDestroySeqBean: destroy{}","");
	}

	public void destroyMethod() {
		logger.info("执行InitAndDestroySeqBean: destroy-method{}","");
	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com/chj/spring/bean.xml");
		context.close();
	}

}
