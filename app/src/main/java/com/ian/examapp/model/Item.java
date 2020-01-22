package com.ian.examapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Item implements Serializable
{
    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("specs")
    private String specs;

    @SerializedName("height")
    private Integer height;

    @SerializedName("type")
    private String type;

    @SerializedName("age")
    private Integer age;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSpecs()
    {
        return specs;
    }

    public void setSpecs(String specs)
    {
        this.specs = specs;
    }

    public Integer getHeight()
    {
        return height;
    }

    public void setHeight(Integer height)
    {
        this.height = height;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Integer getAge()
    {
        return age;
    }

    public void setAge(Integer age)
    {
        this.age = age;
    }
}
