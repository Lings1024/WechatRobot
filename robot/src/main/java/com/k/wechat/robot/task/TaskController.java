package com.k.wechat.robot.task;

import android.app.Activity;

import com.k.wechat.robot.LibInstance;
import com.k.wechat.robot.action.LabelSettingMem;
import com.k.wechat.robot.task.data.AddGroupFriendMem;
import com.k.wechat.robot.task.data.CommonListIteratMem;
import com.k.wechat.robot.task.data.SeniorGroupSendMessageMem;


public class TaskController {

    public static boolean startAddGroupFriendsTask(Activity context, String message) {
        return LibInstance.getInstance().run(context, () -> {

            TaskId.start(TaskId.TASK_ADD_GROUP_FRIEND);
            CommonListIteratMem.reset(TaskId.TASK_ADD_GROUP_FRIEND, null);
            AddGroupFriendMem.reset(message, "", "", 0, 0);
        });
    }

    public static boolean startSeniorMassSendFriend(Activity context, String message, String labelName) {
        return LibInstance.getInstance().run(context, () -> {

            TaskId.start(TaskId.TASK_SENIOR_MASS_SEND_FRIENDS);
            SeniorGroupSendMessageMem.reset(message, "", 0, "1", 0, "", labelName);
            CommonListIteratMem.reset(TaskId.TASK_SENIOR_MASS_SEND_FRIENDS, null);
            LabelSettingMem.rest(labelName);
        });
    }

}
