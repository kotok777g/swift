package com.facebook.swift.generator.template;

public class FieldContext
{
    private final String name;
    private final short id;
    private final String javaType;
    private final String javaName;
    private final String javaGetterName;
    private final String javaSetterName;

    FieldContext(String name,
                 short id,
                 String javaType,
                 String javaName,
                 String javaGetterName,
                 String javaSetterName)
    {
        this.name = name;
        this.id = id;
        this.javaType = javaType;
        this.javaName = javaName;
        this.javaGetterName = javaGetterName;
        this.javaSetterName = javaSetterName;
    }

    public String getName()
    {
        return name;
    }

    public short getId()
    {
        return id;
    }

    public String getJavaType()
    {
        return javaType;
    }

    public String getJavaName()
    {
        return javaName;
    }

    public String getJavaGetterName()
    {
        return javaGetterName;
    }

    public String getJavaSetterName()
    {
        return javaSetterName;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((javaGetterName == null) ? 0 : javaGetterName.hashCode());
        result = prime * result + ((javaName == null) ? 0 : javaName.hashCode());
        result = prime * result + ((javaSetterName == null) ? 0 : javaSetterName.hashCode());
        result = prime * result + ((javaType == null) ? 0 : javaType.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FieldContext other = (FieldContext) obj;
        if (id != other.id) {
            return false;
        }
        if (javaGetterName == null) {
            if (other.javaGetterName != null) {
                return false;
            }
        }
        else if (!javaGetterName.equals(other.javaGetterName)) {
            return false;
        }
        if (javaName == null) {
            if (other.javaName != null) {
                return false;
            }
        }
        else if (!javaName.equals(other.javaName)) {
            return false;
        }
        if (javaSetterName == null) {
            if (other.javaSetterName != null) {
                return false;
            }
        }
        else if (!javaSetterName.equals(other.javaSetterName)) {
            return false;
        }
        if (javaType == null) {
            if (other.javaType != null) {
                return false;
            }
        }
        else if (!javaType.equals(other.javaType)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "FieldContext [name=" + name + ", id=" + id + ", javaType=" + javaType + ", javaName=" + javaName + ", javaGetterName=" + javaGetterName + ", javaSetterName=" + javaSetterName + "]";
    }
}