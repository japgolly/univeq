package japgolly.univeq

import scala.io.Source
import scala.sys.process._
import utest.{assert => _, _}

/*
sbt +test:compile

for ver in 2.12 2.13 3; do
  rd=univeq/jvm/src/test/resources-$ver
  mkdir -p $rd
  cls='BytecodeTest$TestSubject.class'
  javap -c "univeq/jvm/target/scala-$ver"*"/test-classes/japgolly/univeq/$cls" > "$rd/${cls%class}txt"
done
*/

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

  private val className = "BytecodeTest$TestSubject"

  private lazy val actualDisassembly = {
    val clsFile = s"$classesDir/japgolly/univeq/$className.class"
    Seq("javap", "-c", clsFile).!!
  }

  private lazy val expectedDisassembly = {
    val filename = s"/$className.txt"
    val source = Source.fromInputStream(getClass.getResourceAsStream(filename))
    try source.mkString finally source.close()
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
