package japgolly.univeq

import scala.compiletime.*

private object OpsInlineHacks:
  inline def eq_Byte   (a: Byte   , b: Byte   ): Boolean = a == b
  inline def eq_Char   (a: Char   , b: Char   ): Boolean = a == b
  inline def eq_Short  (a: Short  , b: Short  ): Boolean = a == b
  inline def eq_Int    (a: Int    , b: Int    ): Boolean = a == b
  inline def eq_Long   (a: Long   , b: Long   ): Boolean = a == b
  inline def eq_Float  (a: Float  , b: Float  ): Boolean = a == b
  inline def eq_Double (a: Double , b: Double ): Boolean = a == b
  inline def eq_Boolean(a: Boolean, b: Boolean): Boolean = a == b

  transparent inline def ne_Byte   (a: Byte   , b: Byte   ): Boolean = a != b
  transparent inline def ne_Char   (a: Char   , b: Char   ): Boolean = a != b
  transparent inline def ne_Short  (a: Short  , b: Short  ): Boolean = a != b
  transparent inline def ne_Int    (a: Int    , b: Int    ): Boolean = a != b
  transparent inline def ne_Long   (a: Long   , b: Long   ): Boolean = a != b
  transparent inline def ne_Float  (a: Float  , b: Float  ): Boolean = a != b
  transparent inline def ne_Double (a: Double , b: Double ): Boolean = a != b
  transparent inline def ne_Boolean(a: Boolean, b: Boolean): Boolean = a != b
end OpsInlineHacks

extension [A](a: A)

  transparent inline def ==*[B >: A](b: B)(using ev: => UnivEq[B]): Boolean =
    inline erasedValue[B] match
      case _: Byte    => OpsInlineHacks.eq_Byte   (a.asInstanceOf[Byte   ], b.asInstanceOf[Byte   ])
      case _: Char    => OpsInlineHacks.eq_Char   (a.asInstanceOf[Char   ], b.asInstanceOf[Char   ])
      case _: Short   => OpsInlineHacks.eq_Short  (a.asInstanceOf[Short  ], b.asInstanceOf[Short  ])
      case _: Int     => OpsInlineHacks.eq_Int    (a.asInstanceOf[Int    ], b.asInstanceOf[Int    ])
      case _: Long    => OpsInlineHacks.eq_Long   (a.asInstanceOf[Long   ], b.asInstanceOf[Long   ])
      case _: Float   => OpsInlineHacks.eq_Float  (a.asInstanceOf[Float  ], b.asInstanceOf[Float  ])
      case _: Double  => OpsInlineHacks.eq_Double (a.asInstanceOf[Double ], b.asInstanceOf[Double ])
      case _: Boolean => OpsInlineHacks.eq_Boolean(a.asInstanceOf[Boolean], b.asInstanceOf[Boolean])
      case _: Unit    => true
      case _          => a == b

  transparent inline def !=*[B >: A](b: B)(using ev: => UnivEq[B]): Boolean =
    inline erasedValue[B] match
      case _: Byte    => OpsInlineHacks.ne_Byte   (a.asInstanceOf[Byte   ], b.asInstanceOf[Byte   ])
      case _: Char    => OpsInlineHacks.ne_Char   (a.asInstanceOf[Char   ], b.asInstanceOf[Char   ])
      case _: Short   => OpsInlineHacks.ne_Short  (a.asInstanceOf[Short  ], b.asInstanceOf[Short  ])
      case _: Int     => OpsInlineHacks.ne_Int    (a.asInstanceOf[Int    ], b.asInstanceOf[Int    ])
      case _: Long    => OpsInlineHacks.ne_Long   (a.asInstanceOf[Long   ], b.asInstanceOf[Long   ])
      case _: Float   => OpsInlineHacks.ne_Float  (a.asInstanceOf[Float  ], b.asInstanceOf[Float  ])
      case _: Double  => OpsInlineHacks.ne_Double (a.asInstanceOf[Double ], b.asInstanceOf[Double ])
      case _: Boolean => OpsInlineHacks.ne_Boolean(a.asInstanceOf[Boolean], b.asInstanceOf[Boolean])
      case _: Unit    => false
      case _          => a != b
