package lab.crud.entity.superdomain

import java.io.Serializable
import java.util.Date

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Transient
import lab.crud.domain.EntityBean

@MappedSuperclass
@JsonIgnoreProperties(Array("PrimaryKey"))
class UUIDBaseEntity extends EntityBean {
  private var id: String = _
  /**
   * 创建时间
   */
  private var createTime: Date = _
  /**
   * 更新时间
   */
  private var updateTime: Date = _

  @Id
  @GenericGenerator(name = "uuidGenerator", strategy = "uuid")
  @GeneratedValue(generator = "uuidGenerator")
  @Column(name = "id",length=32)
  def getId(): String = {
    this.id
  }
  def setId(id: String): Unit = {
    this.id = id
  }
  @CreatedDate
	@Column(name = "create_time")
  def getCreateTime: Date = {
    this.createTime
  }
  def setCreateTime(createTime: Date): Unit = {
    this.createTime = createTime
  }
  @LastModifiedDate
	@Column(name = "update_time")
  def getUpdateTime: Date = {
    this.updateTime
  }
  def setUpdateTime(updateTime: Date): Unit = {
    this.updateTime = updateTime
  }
  def setPrimaryKey(id: Serializable) = {
    this.id = id.asInstanceOf[String]
  }
  @Transient
  override def getPrimaryKey: Serializable = {
    this.id.asInstanceOf[Serializable]
  }

}