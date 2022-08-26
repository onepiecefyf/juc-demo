package com.onepiece.jucdemo.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.time.LocalDateTime;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * MybatisPlus 自动注入字段默认值
 * @author fengyafei
 */
@Component
public class MybatisPlusDataHandler implements MetaObjectHandler {

  /**
   * javabean的属性需要添加注解@TableField(fill = FieldFill.INSERT)
   */
  @Override
  public void insertFill(MetaObject metaObject) {
    this.strictInsertFill(metaObject, "createTime", () -> LocalDateTime.now(), LocalDateTime.class);
    this.strictInsertFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class);
    setFieldValByName("delFlag", "0", metaObject);
  }

  /**
   * javabean的属性需要添加注解@TableField(fill = FieldFill.UPDATE)
   */
  @Override
  public void updateFill(MetaObject metaObject) {
    this.strictInsertFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class);
  }
}
