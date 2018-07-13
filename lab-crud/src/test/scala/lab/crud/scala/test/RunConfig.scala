package lab.crud.scala.test

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import lab.crud.repository.impl.EntityRepositoryImpl
import org.springframework.data.jpa.repository.config.EnableJpaAuditing


@EnableAutoConfiguration
//实体包扫描路径
@EntityScan(Array("lab.crud.scala.entity"))
//Service扫描路径
@ComponentScan(basePackages = Array("lab.crud.scala.service"))
//repository 相关配置
@EnableJpaRepositories(
    repositoryBaseClass = classOf[EntityRepositoryImpl[_]],
    basePackages = Array("lab.crud.scala.repository")
)
@EnableJpaAuditing
class RunConfig {
  
}