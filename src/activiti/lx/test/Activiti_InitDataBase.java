package activiti.lx.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Activiti_InitDataBase {

	/**
	 * 使用框架提供的自动建表功能(不提供配置文件)
	 */
	@Test
	public void testDb1() {
		// 创建Activiti流程引擎配置对象
		ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();

		// 配置数据库驱动
		cfg.setJdbcDriver("com.mysql.jdbc.Driver");
		// 配置数据库连接
		cfg.setJdbcUrl("jdbc:mysql://localhost:3306/activiti?createDatabaseIfNotExist=true");
		// 配置用户名
		cfg.setJdbcUsername("root");
		// 配置数据库密码
		cfg.setJdbcPassword("root");
		/**
		 * 设置自动建表
		 */
		cfg.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		// 根据引擎配置对象创建流程引擎
		ProcessEngine processEngine = cfg.buildProcessEngine();
	}

	/**
	 * 使用框架提供的自动建表功能(使用配置文件)--可以从框架提供的例子中获取
	 */
	@Test
	public void testDb2() {
		String resource = "activiti-context.xml";// 配置文件名称
		String beanName = "processEngineConfiguration";// 配置id值
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource(resource,
						beanName);
		ProcessEngine processEngine = conf.buildProcessEngine();
	}
	
	/**
	 * 使用框架提供的自动建表(使用默认的配置文件)
	 */
	@Test
	public void testDb3() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	}
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	@Test
	public void test4() {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		
		RuntimeService runtimeService = processEngine.getRuntimeService();
		TaskService taskService = processEngine.getTaskService();
	}

}
