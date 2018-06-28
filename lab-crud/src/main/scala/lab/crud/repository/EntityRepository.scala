package lab.crud.repository

import java.io.Serializable

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

import lab.crud.domain.EntityBean
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import javax.persistence.EntityManager
import org.springframework.data.jpa.repository.support.JpaEntityInformation

@NoRepositoryBean
trait EntityRepository[Entity <: EntityBean] extends JpaRepository[Entity, Serializable] with JpaSpecificationExecutor[Entity] {
  def getEntityInformation(): JpaEntityInformation[Entity, _]
  def getEntityManager: EntityManager
  def getEntityClass: Class[Entity];
}