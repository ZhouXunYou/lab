package lab.crud.scala.service

import lab.crud.scala.entity.Test
import lab.crud.scala.repository.TestRepository
import lab.crud.service.EntityService

trait TestService extends EntityService[Test, TestRepository[Test]] {
  
}