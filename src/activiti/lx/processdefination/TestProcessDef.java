package activiti.lx.processdefination;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
/**
 * 管理流程定义
 * @author liuxun
 *
 */
public class TestProcessDef {

	/**
	 * 1.部署流程定义
	 */
	@Test
	public void deploy() {
		// 创建流程引擎
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 创建部署环境配置对象
		DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService().createDeployment();
		// 部署流程

		// 方式一：读取单个的流程定义文件
		Deployment deployment = deploymentBuilder.name("测试") // 设置部署流程的名称
				.addClasspathResource("activiti/lx/processdefination/MyProcess.bpmn") // 设置流程文件
				.addClasspathResource("activiti/lx/processdefination/MyProcess.png") // 设置流程文件
				.deploy(); // 部署
		System.out.println("部署ID: " + deployment.getId());
	}

	/**
	 * 1.部署流程定义(部署zip格式文件)
	 */
	@Test
	public void deploy2() {
		// 创建流程引擎
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 创建部署环境配置对象
		DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService().createDeployment();
		// 部署流程

		ZipInputStream zipInputStream = new ZipInputStream(
				this.getClass().getClassLoader().getResourceAsStream("activiti/lx/processdefination/process.zip"));
		// 方式二：读取zip压缩文件
		Deployment deployment = deploymentBuilder.name("测试zip部署") // 设置部署流程的名称
				.addZipInputStream(zipInputStream).deploy();
		System.out.println("部署ID: " + deployment.getId());
	}

	/**
	 * 2. 查看流程规则的信息(流程定义)
	 */
	@Test
	public void view() {
		// 创建流程引擎对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 获取流程定义信息
		RepositoryService repositoryService = processEngine.getRepositoryService();
		ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
		// 添加过滤条件
		// definitionQuery.processDefinitionName("");
		definitionQuery.processDefinitionKey("myProcess");
		// 添加分页条件
		// definitionQuery.listPage(0, 10);
		// 添加排序条件
		// definitionQuery.orderByProcessDefinitionId();

		List<ProcessDefinition> list = definitionQuery.list();
		// 迭代效果查看流程定义
		for (ProcessDefinition pd : list) {
			System.out.print("id= " + pd.getId() + ", ");
			System.out.print("name= " + pd.getName() + ", ");
			System.out.print("key= " + pd.getKey() + ", ");
			System.out.print("version= " + pd.getVersion() + ", ");
			System.out.println("deploymentId= " + pd.getDeploymentId() + ", ");
		}
	}

	/**
	 * 3.删除流程定义
	 */
	@Test
	public void delDeploy() {
		// 创建流程引擎对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 设置要删除的部署的ID
		String deploymentId = "1";
		// 删除指定的部署信息，如果有关联信息则报错(例如启动了流程定义相关的流程实例)
		// processEngine.getRepositoryService().deleteDeployment(deploymentId );
		// 删除指定的部署信息，如果有关联信息则级联删除
		// 第二个参数cascade，代表是否级联删除
		processEngine.getRepositoryService().deleteDeployment(deploymentId, true);
	}

	/**
	 * 4.获取流程定义中的资源文件(查看流程图片)
	 * 
	 * @throws IOException
	 */
	@Test
	public void getResource() throws IOException {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		String deploymentId = "1";
		// 获取指定ID流程定义下的所有资源文件的名称列表
		List<String> names = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
		String resourceName = null;
		// 遍历资源文件名称列表
		for (String string : names) {
			// 获取'.png'结尾名称为流程图片名称
			if (string.endsWith(".png")) {
				resourceName = string;
			}
		}
		// 如果流程图片存在
		if (resourceName != null) {
			InputStream in = processEngine.getRepositoryService().getResourceAsStream(deploymentId, resourceName);
			// 指定拷贝目录
			File file = new File("/Users/liuxun/Downloads/" + resourceName);
			// 原始方式
			// OutputStream out = new FileOutputStream(file);
			// byte[] b = new byte[1024];
			// int len = 0;
			// while((len=in.read(b))!=-1) {
			// out.write(b, 0, len);
			// }
			// out.close();
			// 使用FileUtils文件操作工具类，将流程图片拷贝到指定目录下
			FileUtils.copyInputStreamToFile(in, file);
		}
	}
	/**
	 * 5.获取最新版本的流程定义
	 */
	@Test
	public void getLatestVersion() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		ProcessDefinitionQuery query = processEngine.getRepositoryService().createProcessDefinitionQuery();
		query.orderByProcessDefinitionId().asc();
		List<ProcessDefinition> list = query.list();
		HashMap<String, ProcessDefinition> map = new HashMap<String, ProcessDefinition>();
		for (ProcessDefinition pd : list) {
			map.put(pd.getKey(), pd);
		}
		ArrayList<ProcessDefinition> lastList = new ArrayList<>(map.values());
		for (ProcessDefinition processDefinition : lastList) {
			System.out.println(processDefinition.getName()+" "+processDefinition.getVersion());
		}
	}
	
}
