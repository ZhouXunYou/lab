package lab.crud.scala.test

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import lab.crud.repository.impl.EntityRepositoryImpl

@EnableAutoConfiguration
@EntityScan(Array("lab.crud.scala.entity"))
@ComponentScan(basePackages = Array("lab.crud.scala.service"))
@EnableJpaRepositories(enableDefaultTransactions = true, repositoryBaseClass = classOf[EntityRepositoryImpl[_]],basePackages = Array("lab.crud.scala.repository"))
class RunConfig {
  
}