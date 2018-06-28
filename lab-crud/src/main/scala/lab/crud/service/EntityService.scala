package lab.crud.service

import lab.crud.domain.EntityBean
import lab.crud.repository.EntityRepository
import java.io.Serializable
import java.util.{ Set => JavaSet }
import java.util.{ List => JavaList }
import java.lang.{ Iterable => JavaIterable }
import lab.crud.suport.FieldCondition
import lab.crud.suport.Sortor
import org.springframework.data.domain.Page
import lab.crud.suport.Pagination

trait EntityService[Entity <: EntityBean, Repository <: EntityRepository[Entity]] {
  /**
   * public Serializable add(Entity entity);
   * public Set<Serializable> add(Iterable<Entity> entitys);
   * public void remove(Entity entity);
   * public void remove(String id);
   * public void remove(Iterable<Entity> entitys);
   * public void remove(Set<String> ids,Class<?> entityClass);
   * public void modify(Entity entity);
   * public Entity get(String id);
   * public Page<Entity> find(QueryPage queryPage);
   * public Repository getRepository();
   * public Object invokeMethod(String methodName,Class<?>[] paramTypes,Object...params) throws RuntimeException;
   * public Entity saveOrUpdate(Entity entity);
   *
   */
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
  
  def batchRemove(ids: Array[Serializable]): Unit

  def batchRemove(entities: JavaIterable[Entity]): Unit

  /**
   * 通过ID获取
   */
  def get(id: Serializable): Entity

  def find(fieldConditions: JavaList[FieldCondition]): JavaList[Entity]
  def find(fieldConditions: JavaList[FieldCondition], sorts: Array[Sortor]): JavaList[Entity]
  def find(fieldConditions: JavaList[FieldCondition], pagination: Pagination): Page[Entity]
  def find(fieldConditions: JavaList[FieldCondition], pagination: Pagination, sorts: Array[Sortor]): Page[Entity]
}