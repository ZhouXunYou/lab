package lab.crud.repository.impl

import java.io.Serializable

import org.springframework.data.jpa.repository.support.SimpleJpaRepository

import lab.crud.domain.EntityBean
import javax.persistence.EntityManager
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import lab.crud.repository.EntityRepository

class 
  EntityRepositoryImpl[Entity <: EntityBean](val $entityInformation: JpaEntityInformation[Entity, _], val $entityManager: EntityManager) extends 
  SimpleJpaRepository[Entity, Serializable]($entityInformation, $entityManager) with EntityRepository[Entity] {
  var entityInformation = $entityInformation
  var entityManager = $entityManager
  override def getEntityInformation(): JpaEntityInformation[Entity, _] = {
    this.entityInformation
  }
  override def getEntityManager: EntityManager = {
    this.entityManager
  }
  override def getEntityClass: Class[Entity] = {
    this.entityInformation.getJavaType
  }
}