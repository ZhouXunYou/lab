package lab.crud.suport

import org.springframework.data.jpa.domain.Specification
import lab.crud.domain.EntityBean
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Root
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Path
import java.lang.Comparable
import java.util.ArrayList
import java.io.Serializable

class QuerySpecification[Entity <: EntityBean]($fieldCondition: FieldCondition) extends Specification[Entity] {
  private val fieldCondition = $fieldCondition;
  def toPredicate(root: Root[Entity], criteriaQuery: CriteriaQuery[_], criteriaBuilder: CriteriaBuilder): Predicate = {
    val fieldNames = fieldCondition.getField.split("\\.")
    val operator = fieldCondition.getOperator
    val value = fieldCondition.getValue
    val toValue = fieldCondition.getToValue
    var path: Path[_] = root.get(fieldNames(0));
    for (i <- 1 until fieldNames.length) {
      path = path.get(fieldNames(i));
    }

    operator match {
      case Operator.EQ => {
        criteriaBuilder.equal(path, value)
      }
      case Operator.NOTEQ => {
        criteriaBuilder.notEqual(path, value)
      }
      case Operator.LIKE => {
        criteriaBuilder.like(path.as(classOf[String]), "%"+value.asInstanceOf[String]+"%")
      }
      case Operator.NOTLIKE => {
        criteriaBuilder.notLike(path.as(classOf[String]), "%"+value.asInstanceOf[String]+"%")
      }
      case Operator.LLIKE => {
        criteriaBuilder.like(path.as(classOf[String]), value.asInstanceOf[String]+"%")
      }
      case Operator.RLIKE => {
        criteriaBuilder.like(path.as(classOf[String]), "%"+value.asInstanceOf[String])
      }
      case Operator.GT|Operator.LT|Operator.GTE|Operator.LTE|Operator.BETWEEN => {
        getComparablePredicate(path,criteriaBuilder,value,operator,toValue)
      }
      case Operator.NULL => {
        criteriaBuilder.isNull(path.as(classOf[String]))
      }
      case Operator.NOTNULL => {
        criteriaBuilder.isNotNull(path.as(classOf[String]))
      }
      case Operator.IN => {
        criteriaBuilder.in(path.in(value.asInstanceOf[java.util.Collection[_]]))
      }
    }
  }
  private def getComparablePredicate(path:Path[_],criteriaBuilder: CriteriaBuilder,value:Any,operator:Operator.Value,toValue:Any):Predicate = {
    val className = path.getJavaType.getSimpleName.toLowerCase()
    className match {
      case "int"|"integer" => {
        operator match{
          case Operator.GT => {
            criteriaBuilder.greaterThan(path.as(classOf[java.lang.Integer]),value.asInstanceOf[java.lang.Integer])
          }
          case Operator.LT => {
            criteriaBuilder.lessThan(path.as(classOf[java.lang.Integer]),value.asInstanceOf[java.lang.Integer])
          }
          case Operator.GTE => {
            criteriaBuilder.greaterThanOrEqualTo(path.as(classOf[java.lang.Integer]),value.asInstanceOf[java.lang.Integer])
          }
          case Operator.LTE => {
            criteriaBuilder.lessThanOrEqualTo(path.as(classOf[java.lang.Integer]),value.asInstanceOf[java.lang.Integer])
          }
          case Operator.BETWEEN => {
            criteriaBuilder.between(path.as(classOf[java.lang.Integer]), value.asInstanceOf[java.lang.Integer], toValue.asInstanceOf[java.lang.Integer])
          }
        }
      }
      case "long" => {
        operator match{
          case Operator.GT => {
            criteriaBuilder.greaterThan(path.as(classOf[java.lang.Long]),value.asInstanceOf[java.lang.Long])
          }
          case Operator.LT => {
            criteriaBuilder.lessThan(path.as(classOf[java.lang.Long]),value.asInstanceOf[java.lang.Long])
          }
          case Operator.GTE => {
            criteriaBuilder.greaterThanOrEqualTo(path.as(classOf[java.lang.Long]),value.asInstanceOf[java.lang.Long])
          }
          case Operator.LTE => {
            criteriaBuilder.lessThanOrEqualTo(path.as(classOf[java.lang.Long]),value.asInstanceOf[java.lang.Long])
          }
          case Operator.BETWEEN => {
            criteriaBuilder.between(path.as(classOf[java.lang.Long]), value.asInstanceOf[java.lang.Long], toValue.asInstanceOf[java.lang.Long])
          }
        }
      }
      case "float" => {
        operator match{
          case Operator.GT => {
            criteriaBuilder.greaterThan(path.as(classOf[java.lang.Float]),value.asInstanceOf[java.lang.Float])
          }
          case Operator.LT => {
            criteriaBuilder.lessThan(path.as(classOf[java.lang.Float]),value.asInstanceOf[java.lang.Float])
          }
          case Operator.GTE => {
            criteriaBuilder.greaterThanOrEqualTo(path.as(classOf[java.lang.Float]),value.asInstanceOf[java.lang.Float])
          }
          case Operator.LTE => {
            criteriaBuilder.lessThanOrEqualTo(path.as(classOf[java.lang.Float]),value.asInstanceOf[java.lang.Float])
          }
          case Operator.BETWEEN => {
            criteriaBuilder.between(path.as(classOf[java.lang.Float]), value.asInstanceOf[java.lang.Float], toValue.asInstanceOf[java.lang.Float])
          }
        }
      }
      case "double" =>{
        operator match{
          case Operator.GT => {
            criteriaBuilder.greaterThan(path.as(classOf[java.lang.Double]),value.asInstanceOf[java.lang.Double])
          }
          case Operator.LT => {
            criteriaBuilder.lessThan(path.as(classOf[java.lang.Double]),value.asInstanceOf[java.lang.Double])
          }
          case Operator.GTE => {
            criteriaBuilder.greaterThanOrEqualTo(path.as(classOf[java.lang.Double]),value.asInstanceOf[java.lang.Double])
          }
          case Operator.LTE => {
            criteriaBuilder.lessThanOrEqualTo(path.as(classOf[java.lang.Double]),value.asInstanceOf[java.lang.Double])
          }
          case Operator.BETWEEN => {
            criteriaBuilder.between(path.as(classOf[java.lang.Double]), value.asInstanceOf[java.lang.Double], toValue.asInstanceOf[java.lang.Double])
          }
        }
      }
      case "date" => {
        operator match{
          case Operator.GT => {
            criteriaBuilder.greaterThan(path.as(classOf[java.util.Date]),value.asInstanceOf[java.util.Date])
          }
          case Operator.LT => {
            criteriaBuilder.lessThan(path.as(classOf[java.util.Date]),value.asInstanceOf[java.util.Date])
          }
          case Operator.GTE => {
            criteriaBuilder.greaterThanOrEqualTo(path.as(classOf[java.util.Date]),value.asInstanceOf[java.util.Date])
          }
          case Operator.LTE => {
            criteriaBuilder.lessThanOrEqualTo(path.as(classOf[java.util.Date]),value.asInstanceOf[java.util.Date])
          }
          case Operator.BETWEEN => {
            criteriaBuilder.between(path.as(classOf[java.util.Date]), value.asInstanceOf[java.util.Date], toValue.asInstanceOf[java.util.Date])
          }
        }
      }
    }
  }
}