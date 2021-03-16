package japgolly.univeq

extension [A](a: A)

  inline def ==*[B >: A](b: B)(using erased UnivEq[B]): Boolean =
    a == b

  inline def !=*[B >: A](b: B)(using erased UnivEq[B]): Boolean =
    a != b

