package lab.crud.service.impl

import java.io.Serializable
import java.lang.{ Iterable => JavaIterable }
import java.util.ArrayList
import java.util.{ HashSet => JavaHashSet }
import java.util.{ List => JavaList }
import java.util.{ Set => JavaSet }
import java.util.{ Map => JavaMap }
import java.util.{ Iterator => JavaIterator }


import org.springframework.beans.factory.annotation.Autowired

import lab.crud.domain.EntityBean
import lab.crud.repository.EntityRepository
import lab.crud.service.EntityService
import lab.crud.suport.FieldCondition
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification;
import lab.crud.suport.QuerySpecification
import lab.crud.suport.QuerySpecification
import lab.crud.suport.XOR
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.PageRequest
import lab.crud.suport.Sortor
import org.springframework.data.domain.Sort.Order
import lab.crud.suport.SortDirection
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Page
import lab.crud.suport.Pagination
import javax.persistence.Query
import org.springframework.transaction.annotation.Transactional

class EntityServiceImpl[Entity <: EntityBean, Repository <: EntityRepository[Entity]] extends EntityService[Entity, Repository] {
  @Autowired
  private var repository: Repository = _
  @Transactional
  override def saveOrUpdate(entity: Entity): Serializable = {
    this.repository.saveOrUpdate(entity)
  }
  @Transactional
  override def batchSaveOrUpdate(entities: JavaIterable[Entity]): JavaSet[Serializable] = {
    this.repository.batchSaveOrUpdate(entities)
  }
  @Transactional
  override def remove(entity: Entity): Unit = {
    this.repository.remove(entity)
  }
  @Transactional
  override def remove(id: Serializable): Unit = {
     this.repository.remove(id)
  }
  @Transactional
  override def batchRemove(entities: JavaIterable[Entity]): Unit = {
    this.repository.batchRemove(entities)
  }
  @Transactional
  override def batchRemove(ids: Array[Serializable]): Unit = {
    this.repository.batchRemove(ids)
  }

  override def get(id: Serializable): Entity = {
    this.repository.get(id)
  }

  override def find(fieldConditions: JavaList[FieldCondition]): JavaList[Entity] = {
    this.repository.find(fieldConditions)
  }
  override def find(fieldConditions: JavaList[FieldCondition],sorts: Array[Sortor]): JavaList[Entity] = {
    this.repository.find(fieldConditions,sorts)
  }
  override def find(fieldConditions: JavaList[FieldCondition], pagination: Pagination): Page[Entity] = {
    this.repository.find(fieldConditions, pagination)
  }

  override def find(fieldConditions: JavaList[FieldCondition],pagination: Pagination,sorts: Array[Sortor]): Page[Entity] = {
    this.repository.find(fieldConditions, pagination, sorts)
  }
}