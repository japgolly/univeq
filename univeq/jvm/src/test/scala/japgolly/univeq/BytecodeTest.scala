package japgolly.univeq

import scala.sys.process._
import utest.{assert => _, _}

object BytecodeTest extends TestSuite {

  class TestSubject {
    def testByte   (a: Byte   ) = (a !=* a) || (a ==* a)
    def testChar   (a: Char   ) = (a !=* a) || (a ==* a)
    def testShort  (a: Short  ) = (a !=* a) || (a ==* a)
    def testInt    (a: Int    ) = (a !=* a) || (a ==* a)
    def testLong   (a: Long   ) = (a !=* a) || (a ==* a)
    def testFloat  (a: Float  ) = (a !=* a) || (a ==* a)
    def testDouble (a: Double ) = (a !=* a) || (a ==* a)
    def testBoolean(a: Boolean) = (a !=* a) || (a ==* a)
  }

  // Force class evaluation
  private val testSubject = new TestSubject

  private def classesDir = System.getProperty("classes.dir")

  private lazy val actualDisassembly = {
    val clsFile = s"$classesDir/japgolly/univeq/BytecodeTest$$TestSubject.class"
    Seq("javap", "-c", clsFile).!!
  }

  private def methodBytecode(name: String, disassembly: String) =
    disassembly
      .linesIterator
      .dropWhile(!_.matches(s"^  public \\S+ $name(?:\\(.*\\))?;$$"))
      .takeWhile(_.trim.nonEmpty)
      .mkString("\n")

  private def testMethodBytecode(method: String): Unit = {
    val actual = methodBytecode(method, actualDisassembly)
    val expect = methodBytecode(method, expectedDisassembly)
    // assert(actual == expect)
    assert(actual == expect, s"Bytecode mismatch for $method()\n\n$actual\n ")
  }

  // > javap -c univeq/jvm/target/scala-3*/test-classes/japgolly/univeq/'BytecodeTest$TestSubject.class' >> univeq/jvm/src/test/scala/japgolly/univeq/BytecodeTest.scala
  private def expectedDisassembly = """

  public boolean testByte(byte);
    Code:
       0: iload_1
       1: istore_2
       2: iload_1
       3: istore_3
       4: iload_2
       5: iload_3
       6: if_icmpeq     13
       9: iconst_1
      10: goto          14
      13: iconst_0
      14: ifne          38
      17: iload_1
      18: istore        4
      20: iload_1
      21: istore        5
      23: iload         4
      25: iload         5
      27: if_icmpne     34
      30: iconst_1
      31: goto          35
      34: iconst_0
      35: ifeq          42
      38: iconst_1
      39: goto          43
      42: iconst_0
      43: ireturn

  public boolean testChar(char);
    Code:
       0: iload_1
       1: istore_2
       2: iload_1
       3: istore_3
       4: iload_2
       5: iload_3
       6: if_icmpeq     13
       9: iconst_1
      10: goto          14
      13: iconst_0
      14: ifne          38
      17: iload_1
      18: istore        4
      20: iload_1
      21: istore        5
      23: iload         4
      25: iload         5
      27: if_icmpne     34
      30: iconst_1
      31: goto          35
      34: iconst_0
      35: ifeq          42
      38: iconst_1
      39: goto          43
      42: iconst_0
      43: ireturn

  public boolean testShort(short);
    Code:
       0: iload_1
       1: istore_2
       2: iload_1
       3: istore_3
       4: iload_2
       5: iload_3
       6: if_icmpeq     13
       9: iconst_1
      10: goto          14
      13: iconst_0
      14: ifne          38
      17: iload_1
      18: istore        4
      20: iload_1
      21: istore        5
      23: iload         4
      25: iload         5
      27: if_icmpne     34
      30: iconst_1
      31: goto          35
      34: iconst_0
      35: ifeq          42
      38: iconst_1
      39: goto          43
      42: iconst_0
      43: ireturn

  public boolean testInt(int);
    Code:
       0: iload_1
       1: istore_2
       2: iload_1
       3: istore_3
       4: iload_2
       5: iload_3
       6: if_icmpeq     13
       9: iconst_1
      10: goto          14
      13: iconst_0
      14: ifne          38
      17: iload_1
      18: istore        4
      20: iload_1
      21: istore        5
      23: iload         4
      25: iload         5
      27: if_icmpne     34
      30: iconst_1
      31: goto          35
      34: iconst_0
      35: ifeq          42
      38: iconst_1
      39: goto          43
      42: iconst_0
      43: ireturn

  public boolean testLong(long);
    Code:
       0: lload_1
       1: lstore_3
       2: lload_1
       3: lstore        5
       5: lload_3
       6: lload         5
       8: lcmp
       9: ifeq          16
      12: iconst_1
      13: goto          17
      16: iconst_0
      17: ifne          42
      20: lload_1
      21: lstore        7
      23: lload_1
      24: lstore        9
      26: lload         7
      28: lload         9
      30: lcmp
      31: ifne          38
      34: iconst_1
      35: goto          39
      38: iconst_0
      39: ifeq          46
      42: iconst_1
      43: goto          47
      46: iconst_0
      47: ireturn

  public boolean testFloat(float);
    Code:
       0: fload_1
       1: fstore_2
       2: fload_1
       3: fstore_3
       4: fload_2
       5: fload_3
       6: fcmpl
       7: ifeq          14
      10: iconst_1
      11: goto          15
      14: iconst_0
      15: ifne          40
      18: fload_1
      19: fstore        4
      21: fload_1
      22: fstore        5
      24: fload         4
      26: fload         5
      28: fcmpl
      29: ifne          36
      32: iconst_1
      33: goto          37
      36: iconst_0
      37: ifeq          44
      40: iconst_1
      41: goto          45
      44: iconst_0
      45: ireturn

  public boolean testDouble(double);
    Code:
       0: dload_1
       1: dstore_3
       2: dload_1
       3: dstore        5
       5: dload_3
       6: dload         5
       8: dcmpl
       9: ifeq          16
      12: iconst_1
      13: goto          17
      16: iconst_0
      17: ifne          42
      20: dload_1
      21: dstore        7
      23: dload_1
      24: dstore        9
      26: dload         7
      28: dload         9
      30: dcmpl
      31: ifne          38
      34: iconst_1
      35: goto          39
      38: iconst_0
      39: ifeq          46
      42: iconst_1
      43: goto          47
      46: iconst_0
      47: ireturn

  public boolean testBoolean(boolean);
    Code:
       0: iload_1
       1: istore_2
       2: iload_1
       3: istore_3
       4: iload_2
       5: iload_3
       6: if_icmpeq     13
       9: iconst_1
      10: goto          14
      13: iconst_0
      14: ifne          38
      17: iload_1
      18: istore        4
      20: iload_1
      21: istore        5
      23: iload         4
      25: iload         5
      27: if_icmpne     34
      30: iconst_1
      31: goto          35
      34: iconst_0
      35: ifeq          42
      38: iconst_1
      39: goto          43
      42: iconst_0
      43: ireturn
}
"""

  override def tests = Tests {
    "testByte"    - testMethodBytecode("testByte")
    "testChar"    - testMethodBytecode("testChar")
    "testShort"   - testMethodBytecode("testShort")
    "testInt"     - testMethodBytecode("testInt")
    "testLong"    - testMethodBytecode("testLong")
    "testFloat"   - testMethodBytecode("testFloat")
    "testDouble"  - testMethodBytecode("testDouble")
    "testBoolean" - testMethodBytecode("testBoolean")
  }
}
