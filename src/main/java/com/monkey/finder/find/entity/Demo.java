package com.monkey.finder.find.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="com.monkey.finder.find.entity.Demo")
public class Demo {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column demo.id
     *
     * @mbggenerated
     */
    @ApiModelProperty(value="id测试id")
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column demo.name
     *
     * @mbggenerated
     */
    @ApiModelProperty(value="name测试姓名")
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column demo.age
     *
     * @mbggenerated
     */
    @ApiModelProperty(value="age测试年龄")
    private Integer age;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column demo.id
     *
     * @return the value of demo.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column demo.id
     *
     * @param id the value for demo.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column demo.name
     *
     * @return the value of demo.name
     *
     * @mbggenerated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column demo.name
     *
     * @param name the value for demo.name
     *
     * @mbggenerated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column demo.age
     *
     * @return the value of demo.age
     *
     * @mbggenerated
     */
    public Integer getAge() {
        return age;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column demo.age
     *
     * @param age the value for demo.age
     *
     * @mbggenerated
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table demo
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", age=").append(age);
        sb.append("]");
        return sb.toString();
    }
}