package lab.crud.scala.service.impl

import lab.crud.service.impl.EntityServiceImpl
import lab.crud.scala.service.TestService
import lab.crud.scala.repository.TestRepository
import lab.crud.scala.entity.Test
import org.springframework.stereotype.Service

@Service
class TestServiceImpl extends EntityServiceImpl[Test,TestRepository[Test]] with TestService {
  
}