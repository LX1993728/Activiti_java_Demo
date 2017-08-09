package activiti.lx.expression;

public class AcquireUser {
	public String getUserNameById(String id) {
		// 根据业务从数据库中查询
		// ......
		return "用户"+id;
	}
}
