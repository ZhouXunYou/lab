package lab.crud.service

import lab.crud.domain.EntityBean
import lab.crud.repository.EntityRepository
import java.io.Serializable
import java.util.{ Set => JavaSet }
import java.util.{ List => JavaList }
import java.util.{ Map => JavaMap }
import java.lang.{ Iterable => JavaIterable }
import lab.crud.suport.FieldCondition
import lab.crud.suport.Sortor
import org.springframework.data.domain.Page
import lab.crud.suport.Pagination

trait EntityService[Entity <: EntityBean, Repository <: EntityRepository[Entity]] {
  
  /**
   * 新增或修改
   */
  def saveOrUpdate(entity: Entity): Serializable
  /**
   * 批量新增或修改
   */
  def batchSaveOrUpdate(entities: JavaIterable[Entity]): JavaSet[Serializable]
  /**
   * 用实体删除
   */
  def remove(entity: Entity): Unit
  /**
   * 用ID删除
   */
  def remove(id: Serializable): Unit
  /**
   * 通过ID批量删除
   */
  def batchRemove(ids: Array[Serializable]): Unit
  /**
   * 通过实体批量删除
   */
  def batchRemove(entities: JavaIterable[Entity]): Unit

  /**
   * 通过ID获取实体
   */
  def get(id: Serializable): Entity
  
  /**
   * 条件查询
   */
  def find(fieldConditions: JavaList[FieldCondition]): JavaList[Entity]
  /**
   * 条件查询、排序
   */
  def find(fieldConditions: JavaList[FieldCondition], sorts: Array[Sortor]): JavaList[Entity]
  /**
   * 条件查询、分页
   */
  def find(fieldConditions: JavaList[FieldCondition], pagination: Pagination): Page[Entity]
  /**
   * 条件查询、分页、排序
   */
  def find(fieldConditions: JavaList[FieldCondition], pagination: Pagination, sorts: Array[Sortor]): Page[Entity]
  
  /**
   * 利用SQL直接查询，select * from t where column = :paramName
   * sql 中通过 :<参数名> 指定参数名,
   * params中key为参数，value为参数最终执行SQL时替换的值
   */
  def nativeSQL(sql:String,params:JavaMap[String,Any]): JavaList[Entity]
}