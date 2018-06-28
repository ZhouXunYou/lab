package lab.crud.suport

object Operator extends Enumeration {
  /**
	 * EQ:等
	 * NOTEQ:不等
	 * LIKE:like
	 * NOTLIKE:not like
	 * LLIKE:left like
	 * RLIKE:right like
	 * GT:大于
	 * LT:小于
	 * GTE:大于等于
	 * LTE:小于等于
	 * NULL:为空
	 * NOTNULL:不为空
	 * BETWEEN:between
	 * IN:in
	 * UNKNOWN:用于未指定时的判断
	 */
	val EQ,NOTEQ,LIKE,NOTLIKE,LLIKE,RLIKE,GT,LT,GTE,LTE,NULL,NOTNULL,BETWEEN,IN,UNKNOWN = Value
}