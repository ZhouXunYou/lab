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
import java.util.Iterator

class EntityServiceImpl[Entity <: EntityBean, Repository <: EntityRepository[Entity]] extends EntityService[Entity, Repository] {
  @Autowired
  private var repository: Repository = _

  override def saveOrUpdate(entity: Entity): Serializable = {
    repository.save(entity).getPrimaryKey;
  }
  override def batchSaveOrUpdate(entities: JavaIterable[Entity]): JavaSet[Serializable] = {
    val primaryKeys: JavaSet[Serializable] = new JavaHashSet[Serializable]()
    val entitys = this.repository.saveAll(entities)
    for (i <- 0 until entitys.size()) {
      primaryKeys.add(entitys.get(i).getPrimaryKey)
    }
    primaryKeys
  }

  override def remove(entity: Entity): Unit = {
    repository.delete(entity);
  }

  override def remove(id: Serializable): Unit = {
    repository.deleteById(id);
  }

  override def batchRemove(entities: JavaIterable[Entity]): Unit = {
    this.repository.deleteAll(entities)
  }

  override def batchRemove(ids: Array[Serializable]): Unit = {
    val entities: JavaList[Entity] = new ArrayList
    ids.foreach(id => {
      val entity: Entity = this.repository.getEntityClass.newInstance().asInstanceOf[Entity]
      entity.setPrimaryKey(id)
      entities.add(entity);
    })
    batchRemove(entities);
  }

  override def get(id: Serializable): Entity = {
    this.repository.findById(id).orElse(this.repository.getEntityClass.newInstance().asInstanceOf[Entity])
  }

  override def find(fieldConditions: JavaList[FieldCondition]): JavaList[Entity] = {
    this.repository.findAll(buildSpecification(fieldConditions))
  }
  override def find(fieldConditions: JavaList[FieldCondition],sorts: Array[Sortor]): JavaList[Entity] = {
    this.repository.findAll(buildSpecification(fieldConditions),buildSort(sorts))
  }
  override def find(fieldConditions: JavaList[FieldCondition], pagination: Pagination): Page[Entity] = {
    this.repository.findAll(buildSpecification(fieldConditions), buildPageRequest(pagination,null))
  }

  override def find(fieldConditions: JavaList[FieldCondition],pagination: Pagination,sorts: Array[Sortor]): Page[Entity] = {
    this.repository.findAll(buildSpecification(fieldConditions), buildPageRequest(pagination,sorts))
  }
  private def buildPageRequest(pagination: Pagination,sorts: Array[Sortor]):PageRequest={
    val sort = buildSort(sorts);
    if(sort==null){
      return new PageRequest(pagination.getPageNo-1, pagination.getPageSize)
    }else{
      return new PageRequest(pagination.getPageNo-1, pagination.getPageSize,sort)
    }
  }
  private def buildSort(sorts: Array[Sortor]):Sort = {
    var sort:Sort = null
    
    if(sorts!=null && sorts.length>0){
      val orders = new ArrayList[Order];
      for (i <- 0 until sorts.length) {
        var order: Order = null
        if (sorts(i).getSortDirection == SortDirection.ASC) {
          order = new Order(Direction.ASC, sorts(i).getSortField);
        } else {
          order = new Order(Direction.DESC, sorts(i).getSortField);
        }
        orders.add(order)
      }
      sort = new Sort(orders)
    }
    return sort
  }
  private def buildSpecification(fieldConditions: JavaList[FieldCondition]):Specification[Entity] = {
    var specification: Specification[Entity] = null
    if (fieldConditions != null && fieldConditions.size() > 0) {
      specification = new QuerySpecification[Entity](fieldConditions.get(0))
      for (i <- 1 until fieldConditions.size()) {
        if (fieldConditions.get(i).getXor == XOR.And) {
          specification = specification.and(new QuerySpecification[Entity](fieldConditions.get(i)))
        } else if (fieldConditions.get(i).getXor == XOR.Or) {
          specification = specification.or(new QuerySpecification[Entity](fieldConditions.get(i)))
        }
      }
    }
    return specification
  }

  def nativeSQL(sql:String,params:JavaMap[String,Any]): JavaList[Entity] = {
    if(!sql.trim().substring(0,6).equals("select")){
      throw new RuntimeException("sql statement must be query")
    }
    val query:Query = this.repository.getEntityManager.createNativeQuery(sql,this.repository.getEntityClass);
    if(params!=null&&params.size()>0){
      val keys = params.keySet().iterator()
      while(keys.hasNext()){
        val key = keys.next();
        val param = params.get();
        query.setParameter(key, param)
      }
    }
    query.getResultList.asInstanceOf[JavaList[Entity]]
  }
  
}