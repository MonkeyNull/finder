package com.monkey.finder.find.dao;

import com.monkey.finder.find.entity.Account;

public interface AccountMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table find_account
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table find_account
     *
     * @mbggenerated
     */
    int insert(Account record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table find_account
     *
     * @mbggenerated
     */
    int insertSelective(Account record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table find_account
     *
     * @mbggenerated
     */
    Account selectByPrimaryKey(Long userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table find_account
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Account record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table find_account
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Account record);

    Account selctByEmail(String userEmail);
}