package com.timwang.pattern.core.pattern.a_observer;


/**
 * 观察者：更新日志操作的观察者
 * @author wangjun
 * @date 2019/06/03
 */
public class ProjectUpdateLogObserver implements Observer {
    private Integer projectId;
    private Integer userId;
    private Integer type;
    private String note;

    public ProjectUpdateLogObserver(Integer projectId, Integer userId, Integer type, String note) {
        this.projectId = projectId;
        this.userId = userId;
        this.type = type;
        this.note = note;
    }

    @Override
    public void update(String message) {
        // TODO insert updateLog
        ProjectUpdateLog projectUpdateLog = new ProjectUpdateLog();
        projectUpdateLog.setProjectId(this.projectId);
        projectUpdateLog.setCreateUserId(this.userId);
        projectUpdateLog.setType(this.type);
        projectUpdateLog.setNote(this.note);
        System.out.println("=======this is a update log insert transaction========" + projectUpdateLog.toString());
    }

}
