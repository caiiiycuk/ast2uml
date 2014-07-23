package com.github.caiiiycuk.ast2uml

import org.scalatest._
import com.github.caiiiycuk.ast2uml.clang.ClangAstParser
import com.github.caiiiycuk.ast2uml._
import com.github.caiiiycuk.ast2uml.query.AstQuery

class QueryTest extends FlatSpec with Matchers {

  behavior of "Query"

  it should "Query class" in {
    implicit val root = AstRoot(List(AstNamespace(AstNode("namespace1", Seq()), List(AstClass(AstNode("class1", Seq()), List())))))

    val paths = AstQuery.findAll(classOf[AstClass])
    val nodes = paths.map(p => p.map(_.astName))

    nodes should equal(
      List(List(".", "namespace1", "class1")))
  }

  it should "Query all classes" in {
    implicit val root = AstRoot(List(
      AstNamespace(AstNode("namespace1", Seq()), List(AstClass(AstNode("class1", Seq()), List()))),
      AstNamespace(AstNode("namespace2", Seq()), List(AstNamespace(AstNode("nested", Seq()), List(AstClass(AstNode("class2", Seq()), List())))))))

    val paths = AstQuery.findAll(classOf[AstClass])
    val nodes = paths.map(p => p.map(_.astName))

    nodes should equal(
      List(List(".", "namespace1", "class1"),
        List(".", "namespace2", "nested", "class2")))
  }

}