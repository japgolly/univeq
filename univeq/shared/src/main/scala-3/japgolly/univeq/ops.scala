package japgolly.univeq

import scala.compiletime.*

private object UnivEqOpsInlineHacks:
  inline def eq_Byte   (a: Byte   , b: Byte   ): Boolean = a == b
  inline def eq_Char   (a: Char   , b: Char   ): Boolean = a == b
  inline def eq_Short  (a: Short  , b: Short  ): Boolean = a == b
  inline def eq_Int    (a: Int    , b: Int    ): Boolean = a == b
  inline def eq_Long   (a: Long   , b: Long   ): Boolean = a == b
  inline def eq_Float  (a: Float  , b: Float  ): Boolean = a == b
  inline def eq_Double (a: Double , b: Double ): Boolean = a == b
  inline def eq_Boolean(a: Boolean, b: Boolean): Boolean = a == b

  inline def ne_Byte   (a: Byte   , b: Byte   ): Boolean = a != b
  inline def ne_Char   (a: Char   , b: Char   ): Boolean = a != b
  inline def ne_Short  (a: Short  , b: Short  ): Boolean = a != b
  inline def ne_Int    (a: Int    , b: Int    ): Boolean = a != b
  inline def ne_Long   (a: Long   , b: Long   ): Boolean = a != b
  inline def ne_Float  (a: Float  , b: Float  ): Boolean = a != b
  inline def ne_Double (a: Double , b: Double ): Boolean = a != b
  inline def ne_Boolean(a: Boolean, b: Boolean): Boolean = a != b
end UnivEqOpsInlineHacks

extension [A](a: A)

  inline def ==*[B >: A](b: B)(using inline ev: UnivEq[B]): Boolean =
    inline erasedValue[B] match
      case _: Byte    => UnivEqOpsInlineHacks.eq_Byte   (a.asInstanceOf[Byte   ], b.asInstanceOf[Byte   ])
      case _: Char    => UnivEqOpsInlineHacks.eq_Char   (a.asInstanceOf[Char   ], b.asInstanceOf[Char   ])
      case _: Short   => UnivEqOpsInlineHacks.eq_Short  (a.asInstanceOf[Short  ], b.asInstanceOf[Short  ])
      case _: Int     => UnivEqOpsInlineHacks.eq_Int    (a.asInstanceOf[Int    ], b.asInstanceOf[Int    ])
      case _: Long    => UnivEqOpsInlineHacks.eq_Long   (a.asInstanceOf[Long   ], b.asInstanceOf[Long   ])
      case _: Float   => UnivEqOpsInlineHacks.eq_Float  (a.asInstanceOf[Float  ], b.asInstanceOf[Float  ])
      case _: Double  => UnivEqOpsInlineHacks.eq_Double (a.asInstanceOf[Double ], b.asInstanceOf[Double ])
      case _: Boolean => UnivEqOpsInlineHacks.eq_Boolean(a.asInstanceOf[Boolean], b.asInstanceOf[Boolean])
      case _: Unit    => true
      case _          => a == b

  inline def !=*[B >: A](b: B)(using inline ev: UnivEq[B]): Boolean =
    inline erasedValue[B] match
      case _: Byte    => UnivEqOpsInlineHacks.ne_Byte   (a.asInstanceOf[Byte   ], b.asInstanceOf[Byte   ])
      case _: Char    => UnivEqOpsInlineHacks.ne_Char   (a.asInstanceOf[Char   ], b.asInstanceOf[Char   ])
      case _: Short   => UnivEqOpsInlineHacks.ne_Short  (a.asInstanceOf[Short  ], b.asInstanceOf[Short  ])
      case _: Int     => UnivEqOpsInlineHacks.ne_Int    (a.asInstanceOf[Int    ], b.asInstanceOf[Int    ])
      case _: Long    => UnivEqOpsInlineHacks.ne_Long   (a.asInstanceOf[Long   ], b.asInstanceOf[Long   ])
      case _: Float   => UnivEqOpsInlineHacks.ne_Float  (a.asInstanceOf[Float  ], b.asInstanceOf[Float  ])
      case _: Double  => UnivEqOpsInlineHacks.ne_Double (a.asInstanceOf[Double ], b.asInstanceOf[Double ])
      case _: Boolean => UnivEqOpsInlineHacks.ne_Boolean(a.asInstanceOf[Boolean], b.asInstanceOf[Boolean])
      case _: Unit    => false
      case _          => a != b
