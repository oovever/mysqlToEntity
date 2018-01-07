package ${global.basePackage}.${global.po};

import java.io.Serializable;
<#if pojo.hasDateType==true>
import java.util.Date;
</#if>

/**
 * ${pojo.comment!''}
 *
 * @author ${global.author} ${global.now?string("yyyy-MM-dd HH:mm")}
 */
public class ${pojo.className} implements Serializable {
    private static final long serialVersionUID = 1L;

<#list pojo.fieldList as field>
    /** ${field.comment!''} */
    private ${field.fieldType} ${field.fieldName};
</#list>


<#list pojo.fieldList as field>
    /** ${field.comment!''} */
    public ${field.fieldType} get${field.fieldName?cap_first}() {
        return ${field.fieldName};
    }
    public void set${field.fieldName?cap_first}(${field.fieldType} ${field.fieldName}) {
        this.${field.fieldName} = ${field.fieldName};
    }
</#list>

    @Override
    public String toString() {
        StringBuffer ts = new StringBuffer(this.getClass().getTableName()).append("[");

    <#list pojo.fieldList as field>
        ts.append("${field.fieldName}:").append(${field.fieldName})<#if field_has_next>.append(", ")</#if>;
    </#list>

        return ts.append("]").toString();
    }

}