import org.scalatest.FunSuite

class PrintTest extends FunSuite {

  test("testSum") {
    val a = 5
    val b = 2

    val result = Print.sum(a, b)

    assert(result == 7)
  }

}
