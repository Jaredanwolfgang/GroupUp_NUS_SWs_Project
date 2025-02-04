package com.sustech.groupup.controller.grouping;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sustech.groupup.entity.api.GroupWithMemberDTO;
import com.sustech.groupup.entity.api.StatusDTO;
import com.sustech.groupup.entity.api.SurveyDTO;
import com.sustech.groupup.entity.converter.SurveyConverter;
import com.sustech.groupup.entity.db.GroupEntity;
import com.sustech.groupup.entity.db.QueryEntity;
import com.sustech.groupup.entity.db.SurveyEntity;
import com.sustech.groupup.mapper.GroupMapper;
import com.sustech.groupup.mapper.QueryMapper;
import com.sustech.groupup.services.GroupService;
import com.sustech.groupup.services.QueryService;
import com.sustech.groupup.services.SurveyService;
import com.sustech.groupup.utils.Response;
import org.springframework.web.bind.annotation.*;

import com.sustech.groupup.config.Constant;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constant.API_VERSION + "/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;
    private final QueryService queryService;
    private final GroupService groupService;
    private final SurveyConverter surveyConverter;
    private final GroupMapper groupMapper;
    private final ObjectMapper objectMapper;

    @GetMapping("/{number}")
    public Response getSurveyById (@PathVariable long number) throws JsonProcessingException {
        var resp = surveyConverter.toDTO(surveyService.getSurveyById(number));
        return Response.getSuccess("success",resp);
    }

    @PostMapping()
    public Response addSurvey (@RequestBody SurveyDTO surveyDTO) {
        SurveyEntity surveyEntity = surveyConverter.toEntity(surveyDTO);
        surveyService.insertSurvey(surveyEntity, surveyDTO.getOwners(),surveyDTO.getMembers());
        return Response.getSuccess(Map.of("survey_id", surveyEntity.getId()));
    }

    @PutMapping("/{id}")
    public Response updateSurveyById (@PathVariable long id, @RequestBody SurveyDTO surveyDTO) {
        SurveyEntity surveyEntity = surveyConverter.toEntity(surveyDTO);
        surveyEntity.setId(id);
        surveyService.updateSurvey(surveyEntity, surveyDTO.getMembers(), surveyDTO.getOwners());
        return Response.getSuccess();
    }

    @DeleteMapping("/{id}")
    public Response deleteSurveyById (@PathVariable long id) {
        surveyService.deleteSurveyById(id);
        return Response.getSuccess();
    }

    @PutMapping("/{id}/status")
    public Response updateSurveyStatusById (@PathVariable long id, @RequestBody StatusDTO dto) {
        surveyService.updateStatusBySurveyId(id,dto.getStatus());
        return Response.getSuccess();
    }

    @GetMapping("/{id}/status")
    public Response getSurveyStatusById(@PathVariable long id) {
        SurveyEntity surveyEntity = surveyService.getSurveyById(id);
        return Response.getSuccess(Map.of("status",surveyEntity.getStatus()));
    }

    @GetMapping("/{id}/allquery")
    public Response getQueryList(@PathVariable long id,
                                 @RequestParam(defaultValue = "-1") int pageSize,
                                 @RequestParam(defaultValue = "1") int pageNo,
                                 @RequestParam(defaultValue = "") String queryOwner) {
        IPage<QueryEntity> queryResult = queryService.getQueryList(id, pageSize, pageNo, queryOwner); // 调用Service层方法获取查询结果
        Map<String, Object> data = new HashMap<>();
        data.put("total_size", queryResult.getSize());
        data.put("list", queryResult.getRecords());
        return Response.getSuccess("success",data);
    }

    @DeleteMapping("/{id}/allquery")
    public Response deleteQueryBySurveyId(@PathVariable long id) {
        queryService.deletQueryBySurveyId(id);
        return Response.getSuccess("success","");
    }

    @GetMapping("/{id}/allgroup")
    public Response getGroupList(@PathVariable long id,
                                 @RequestParam(defaultValue = "-1") int pageSize,
                                 @RequestParam(defaultValue = "1") int pageNo) {
        IPage<GroupWithMemberDTO> queryResult= groupService.getGroupList(pageNo, pageSize, id);
        Map<String, Object> data = new HashMap<>();
        data.put("total_size", queryResult.getSize());
        data.put("list", queryResult.getRecords());
        return Response.getSuccess("success",data);
    }

    @PutMapping("/{id}/allgroup")
    public Response updateGroupList(@PathVariable long id, @RequestBody JsonNode jsonNode) throws JsonProcessingException {
        JsonNode list=jsonNode.get("list");
        System.out.println(list);
        List<GroupWithMemberDTO> groupList=objectMapper.readValue(list.toString(),new TypeReference<List<GroupWithMemberDTO>>(){});
        System.out.println(groupList);
        for (GroupWithMemberDTO groupWithMemberDTO : groupList) {
            long groupId=groupWithMemberDTO.getId();
            List<Long> memberIds=groupWithMemberDTO.getGroupMember();
            groupService.deleteGroupMembersByGroupId(groupId);
            for (Long memberId : memberIds) {
                groupService.addGroupMember(groupId, memberId);
            }
        }
        return Response.getSuccess("success","");
    }

    @GetMapping("/{id}/basicinfo")
    public Response getSurveyBasicInfo(@PathVariable long id) {
        Map<String, Object> data = new HashMap<>();
        data.put("members_count",surveyService.getSurveyMembersCount(id));
        data.put("groups_count",surveyService.getSurveyGroupsCount(id));
        data.put("answered_member_count",surveyService.getSurveyAnswersCount(id));
        data.put("grouped_member_count",surveyService.getSurveyGroupedMembersCount(id));
        return Response.getSuccess("success",data);
    }
}
