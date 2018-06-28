package lab.crud.scala.repository

import org.springframework.stereotype.Repository

import lab.crud.repository.EntityRepository
import lab.crud.scala.entity.Test

@Repository
trait TestRepository[Bean <: Test] extends EntityRepository[Bean] {
  
}