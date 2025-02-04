package com.sustech.groupup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustech.groupup.entity.db.GroupEntity;
import org.apache.catalina.Group;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface GroupMapper extends BaseMapper<GroupEntity> {

    @Select("select group_member.member_id from group_member where group_id=#{groupId}")
    List<Long> getMembersByGroupId(Long groupId);

    @Select("select count(*) from group_member where group_id=#{groupId}")
    int getMemberCountByGroupId(Long groupId);

    @Delete("delete from group_member where group_id=#{groupId} and member_id=#{memberId}")
    void deleteMemberByGroupIdAndMemberId(Long groupId, Long memberId);

    @Delete("delete from group_member where group_id=#{groupId}")
    void deleteAllMembersByGroupId(Long groupId);

    @Insert("insert into group_member (group_id, member_id) VALUES (#{groupId},#{memberId})")
    void insertMemberByGroupId(Long groupId, Long memberId);

    @Select("select count(*) from group_table where survey_id=#{surveyId}")
    int getGroupCountBySurveyId(Long surveyId);

    @Select("select count(*) from group_member where group_id=#{groupId}")
    int getMembersCountByGroupId(Long groupId);
}
