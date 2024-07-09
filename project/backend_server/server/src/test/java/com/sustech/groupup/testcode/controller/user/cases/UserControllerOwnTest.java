package com.sustech.groupup.testcode.controller.user.cases;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sustech.groupup.config.Constant;
import com.sustech.groupup.testcode.controller.grouping.survey.SurveyAPI;
import com.sustech.groupup.testcode.controller.user.AuthAPI;
import com.sustech.groupup.testcode.controller.user.UserQueryAPI;
import com.sustech.groupup.testutils.RespChecker;
import com.sustech.groupup.testutils.annotation.ControllerTest;

@ControllerTest
public class UserControllerOwnTest {

    @Autowired
    private UserQueryAPI queryAPI;
    
    @Autowired
    private AuthAPI authAPI;

    @Autowired
    private SurveyAPI surveyAPI;
    @Test
    public void testNormalQuery() throws Exception {
        var auth = authAPI.templateUserLogin();//create user
        var uid = auth.getId();
        surveyAPI.successfulCreateTemplate(auth,List.of(uid),List.of());//创建一个survey
        var res = queryAPI.successfulQueryOwn(uid,auth,-1,1);
        assert res.size()==1;
        assert res.getFirst()==1;
    }
}

