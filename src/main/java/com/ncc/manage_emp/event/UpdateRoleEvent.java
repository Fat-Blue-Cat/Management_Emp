package com.ncc.manage_emp.event;

import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.entity.WorkTime;
import org.springframework.context.ApplicationEvent;

/*
UpdateRoleEvent phải kế thừa lớp ApplicationEvent của Spring
Như vậy nó mới được coi là một sự kiện hợp lệ.
 */
public class UpdateRoleEvent extends ApplicationEvent {
    /*
            Mọi Class kế thừa ApplicationEvent sẽ
            phải gọi Constructor tới lớp cha.
    */
    private Users users;
    private WorkTime workTime;
    public UpdateRoleEvent(Object source, Users users, WorkTime workTime){
        // Object source là object tham chiếu tới
        // nơi đã phát ra event này!
        super(source);
        this.users = users;
        this.workTime = workTime;
    }

    public Users getUsers() {
        return users;
    }

    public WorkTime getWorkTime() {
        return workTime;
    }
}
