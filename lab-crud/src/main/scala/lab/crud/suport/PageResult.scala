package lab.crud.suport
import org.springframework.data.domain.Page
import lab.crud.domain.EntityBean

trait PageResult[Entity <: EntityBean] extends Page[Entity] {
  
}